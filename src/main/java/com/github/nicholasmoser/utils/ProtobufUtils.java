package com.github.nicholasmoser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.github.nicholasmoser.FPKFileHeader;
import com.github.nicholasmoser.GNTFileProtos;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.PRSUncompressor;
import com.github.nicholasmoser.gnt4.GNT4ModReady;

public class ProtobufUtils {

  /**
   * Create a GNTFiles object containing all GNTFiles and respective hashes. Ignores FPK files.
   *
   * @param compressed The compressed directory.
   * @param allowedFiles The set of files that are allowed to be stored in binary.
   * @return The GNTFiles mapping of filePaths to hashes.
   * @throws IOException If an I/O error occurs.
   */
  public static GNTFiles createBinary(Path compressed, Set<String> allowedFiles) throws IOException {
    return createBinary(compressed, allowedFiles, false);
  }

  /**
   * Create a GNTFiles object containing all GNTFiles and respective hashes. Optionally allows
   * consideration for FPK files and logic to handle the relationships to their children.
   *
   * @param compressed  The compressed directory.
   * @param allowedFiles The set of files that are allowed to be stored in binary.
   * @param checkForFpk Whether or not to check for FPK files and process their children.
   * @return The GNTFiles mapping of filePaths to hashes.
   * @throws IOException If an I/O error occurs.
   */
  public static GNTFiles createBinary(Path compressed, Set<String> allowedFiles, boolean checkForFpk) throws IOException {
    List<Path> files = Files.walk(compressed).filter(Files::isRegularFile)
        .collect(Collectors.toList());
    GNTFiles.Builder filesBuilder = GNTFiles.newBuilder();
    for (Path filePath : files) {
      String relativePath = relativizePath(compressed, filePath);
      if (allowedFiles.contains(relativePath)) {
        GNTFileProtos.GNTFile.Builder fileBuilder = GNTFileProtos.GNTFile.newBuilder();
        int hash = CRC32.getHash(filePath);
        fileBuilder.setFilePath(relativePath);
        fileBuilder.setHash(hash);
        if (checkForFpk && relativePath.endsWith(".fpk")) {
          List<GNTFileProtos.GNTChildFile> children = getChildren(filePath);
          fileBuilder.addAllGntChildFile(children);
        }
        GNTFileProtos.GNTFile gntFile = fileBuilder.build();
        filesBuilder.addGntFile(gntFile);
      }
    }
    return filesBuilder.build();
  }

  /**
   * Removes the compressed of a path from the file path, as well as the first slash. All
   * backslashes are replaced with forward slashes. So in the below example the file path goes from
   * 1. to 3. and 3. is returned. 1. C:\\compressed\\files\\a.trk 2. /files/a.trk 3. files/a.trk
   *
   * @param compressed The compressed folder.
   * @param filePath   The file path.
   * @return The relativized file path.
   */
  private static String relativizePath(Path compressed, Path filePath) {
    return filePath.toString().replace(compressed.toString(), "").replace('\\', '/').substring(1);
  }

  /**
   * Opens the given FPK file and extracts it contents. This includes uncompressing them from
   * Eighting PRS compression.
   *
   * @param filePath The FPK file to extract.
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  private static List<GNTFileProtos.GNTChildFile> getChildren(Path filePath) throws IOException {
    int bytesRead = 0;
    List<GNTFileProtos.GNTChildFile> children = new ArrayList<>();
    try (InputStream is = Files.newInputStream(filePath)) {
      int fileCount = FPKUtils.readFPKHeader(is);
      bytesRead += 16;

      List<FPKFileHeader> fpkHeaders = new ArrayList<>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readGCFPKFileHeader(is));
        bytesRead += 32;
      }

      for (FPKFileHeader header : fpkHeaders) {
        GNTFileProtos.GNTChildFile.Builder builder = GNTFileProtos.GNTChildFile.newBuilder();
        String compressedFilePath = header.getFileName();
        String childPath = GNT4ModReady.fixBrokenFileName(compressedFilePath);
        int offset = header.getOffset();
        int compressedSize = header.getCompressedSize();
        int uncompressedSize = header.getUncompressedSize();

        // Skip to the next offset if we are not already there
        if (bytesRead < offset) {
          int bytesToMove = offset - bytesRead;
          if (is.skip(bytesToMove) != bytesToMove) {
            String message = String.format("Unable to skip %d bytes from %s at offset %d",
                bytesToMove, filePath, bytesRead);
            throw new IOException(message);
          }
          bytesRead += bytesToMove;
        }

        byte[] fileBytes = new byte[compressedSize];
        if (is.read(fileBytes) != compressedSize) {
          String message = String.format("Unable to read %d bytes from %s at offset %d",
              compressedSize, filePath, bytesRead);
          throw new IOException(message);
        }
        bytesRead += compressedSize;

        // Files with the same compressed and uncompressed size are not compressed
        if (compressedSize != uncompressedSize) {
          PRSUncompressor uncompressor = new PRSUncompressor(fileBytes, uncompressedSize);
          fileBytes = uncompressor.uncompress();
          builder.setCompressed(true);
        } else {
          builder.setCompressed(false);
        }

        int hash = CRC32.getHash(fileBytes);

        builder.setFilePath("files/" + childPath);
        builder.setHash(hash);
        builder.setCompressedPath(compressedFilePath);
        children.add(builder.build());
      }
    }
    return children;
  }
}
