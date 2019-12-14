package com.github.nicholasmoser.gnt4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.github.nicholasmoser.FPKFileHeader;
import com.github.nicholasmoser.GNTFileProtos;
import com.github.nicholasmoser.PRSUncompressor;
import com.github.nicholasmoser.utils.CRC32;
import com.github.nicholasmoser.utils.FPKUtils;

public class GNT4DiffChecker {

  public static void createDiffBinary(Path root, Path output) throws IOException {
    createDiffBinary(root, output, false);
  }

  public static void createDiffBinary(Path root, Path output, boolean checkForFpk)
      throws IOException {
    List<Path> files = Files.walk(root).filter(Files::isRegularFile).collect(Collectors.toList());

    GNTFileProtos.GNTFiles.Builder filesBuilder = GNTFileProtos.GNTFiles.newBuilder();
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
    GNTFileProtos.GNTFiles gntFiles = filesBuilder.build();
    try (OutputStream os = Files.newOutputStream(output)) {
      gntFiles.writeTo(os);
    }
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
    return filePath.toString().replace(root.toString(), "").replace("\\", "/").substring(1);
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
          is.skip(bytesToMove);
          bytesRead += bytesToMove;
        }

        byte[] fileBytes = new byte[compressedSize];
        is.read(fileBytes);
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
