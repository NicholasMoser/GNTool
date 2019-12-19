package com.github.nicholasmoser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.github.nicholasmoser.FPKFileHeader;
import com.github.nicholasmoser.GNTFileProtos;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.PRSUncompressor;

public class ProtobufUtils {

  /**
   * Create a GNTFiles object containing all GNTFiles and respective hashes. Ignores FPK files.
   * 
   * @param root The root directory.
   * @return The GNTFiles mapping of filePaths to hashes.
   * @throws IOException If an I/O error occurs.
   */
  public static GNTFiles createBinary(Path root) throws IOException {
    return createBinary(root, false);
  }

  /**
   * Create a GNTFiles object containing all GNTFiles and respective hashes.
   * Optionally allows consideration for FPK files and logic to handle the relationships
   * to their children.
   * 
   * @param root The root directory.
   * @param checkForFpk Whether or not to check for FPK files and process their children.
   * @return The GNTFiles mapping of filePaths to hashes.
   * @throws IOException If an I/O error occurs.
   */
  public static GNTFiles createBinary(Path root, boolean checkForFpk)
      throws IOException {
    List<Path> files = Files.walk(root).filter(Files::isRegularFile).collect(Collectors.toList());
    GNTFiles.Builder filesBuilder = GNTFiles.newBuilder();
    for (Path filePath : files) {
      GNTFileProtos.GNTFile.Builder fileBuilder = GNTFileProtos.GNTFile.newBuilder();
      int hash = CRC32.getHash(filePath);
      String relativePath = relativizePath(root, filePath);
      fileBuilder.setFilePath(relativePath);
      fileBuilder.setHash(hash);
      if (checkForFpk && relativePath.endsWith(".fpk")) {
        List<GNTFileProtos.GNTChildFile> children = getChildren(filePath);
        fileBuilder.addAllGntChildFile(children);
      }
      GNTFileProtos.GNTFile gntFile = fileBuilder.build();
      filesBuilder.addGntFile(gntFile);
    }
    GNTFiles gntFiles = filesBuilder.build();
    return gntFiles;
  }

  /**
   * Remotes the root of a path from the file path, as well as the first slash. All backslashes are
   * replaced with forward slashes. So in the below example the file path goes from 1. to 3. and 3.
   * is returned. 1. C:\\root\\files\\a.trk 2. /files/a.trk 3. files/a.trk
   * 
   * @param root The root folder.
   * @param filePath The file path.
   * @return The relativized file path.
   */
  private static String relativizePath(Path root, Path filePath) {
    return filePath.toString().replace(root.toString(), "").replace('\\', '/').substring(1);
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
    String compressedFilePath = "Unknown";
    List<GNTFileProtos.GNTChildFile> children = new ArrayList<GNTFileProtos.GNTChildFile>();
    try (InputStream is = Files.newInputStream(filePath)) {
      int fileCount = FPKUtils.readFPKHeader(is);
      bytesRead += 16;

      List<FPKFileHeader> fpkHeaders = new ArrayList<FPKFileHeader>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readFPKFileHeader(is));
        bytesRead += 32;
      }

      for (FPKFileHeader header : fpkHeaders) {
        GNTFileProtos.GNTChildFile.Builder builder = GNTFileProtos.GNTChildFile.newBuilder();
        compressedFilePath = header.getFileName();
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
        String afterRoot = filePath.getParent().toString().replace('\\', '/').split("/root/")[1];

        int index = compressedFilePath.indexOf('/');
        String fixedFilePath = compressedFilePath.substring(index);

        builder.setFilePath(afterRoot + fixedFilePath); // ?
        builder.setHash(hash);
        builder.setCompressedPath(compressedFilePath);
        children.add(builder.build());
      }
    }
    return children;
  }
}
