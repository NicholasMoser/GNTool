package com.github.nicholasmoser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import com.github.nicholasmoser.utils.FPKUtils;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class ProtoBufCreatorTest {

  // Input directory and output protobuf binary
  private static final String isoRoot = "C:/root/";
  private static final String output = "C:/files.dat";

  @Test
  @Disabled("This is only kept for reference, it is only used to create a new protobuf binary file.")
  public void test() throws Exception {
    Path modReadyPath = Paths.get(isoRoot);
    List<String> filePaths = getFilePaths(new ArrayList<String>(), modReadyPath);
    GNTFileProtos.GNTFiles.Builder filesBuilder = GNTFileProtos.GNTFiles.newBuilder();
    for (String filePath : filePaths) {
      GNTFileProtos.GNTFile.Builder fileBuilder = GNTFileProtos.GNTFile.newBuilder();
      String relativeFilePath = filePath.replace(isoRoot, "");
      int hash = getHash(Paths.get(filePath));
      fileBuilder.setFilePath(relativeFilePath);
      fileBuilder.setHash(hash);
      if (relativeFilePath.endsWith(".fpk")) {
        List<GNTFileProtos.GNTChildFile> children = getChildren(Paths.get(filePath));
        fileBuilder.addAllGntChildFile(children);
      }
      GNTFileProtos.GNTFile gntFile = fileBuilder.build();
      filesBuilder.addGntFile(gntFile);
    }
    GNTFileProtos.GNTFiles gntFiles = filesBuilder.build();
    try (OutputStream os = Files.newOutputStream(Paths.get(output))) {
      gntFiles.writeTo(os);
    }
  }


  /**
   * Opens the given FPK file and extracts it contents. This includes uncompressing them from
   * Eighting PRS compression.
   * 
   * @param filePath The FPK file to extract.
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  private List<GNTFileProtos.GNTChildFile> getChildren(Path filePath) throws IOException {
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

        int hash = getHash(fileBytes);
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

  /**
   * Returns the CRC32 hash for a file from the given Path.
   * 
   * @param filePath The Path to the file.
   * @return The CRC32 value as an integer.
   * @throws IOException If there is an I/O exception when reading the file.
   */
  private int getHash(Path filePath) throws IOException {
    HashFunction crc32 = Hashing.crc32();
    byte[] bytes = Files.readAllBytes(filePath);
    HashCode hashValue = crc32.hashBytes(bytes);
    return hashValue.asInt();
  }

  /**
   * Returns the CRC32 hash for an array of bytes.
   * 
   * @param bytes The bytes to hash.
   * @return The CRC32 value as an integer.
   * @throws IOException If there is an I/O exception when reading the file.
   */
  private int getHash(byte[] bytes) throws IOException {
    HashFunction crc32 = Hashing.crc32();
    HashCode hashValue = crc32.hashBytes(bytes);
    return hashValue.asInt();
  }

  /**
   * Recursively searches a directory for file paths and returns them.
   * 
   * @param fileNames The list to add the file paths to.
   * @param dir The directory to search.
   * @return The list of file paths.
   * @throws IOException If there is an I/O exception when searching the directory.
   */
  private List<String> getFilePaths(List<String> fileNames, Path dir) throws IOException {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
      for (Path path : stream) {
        if (path.toFile().isDirectory()) {
          getFilePaths(fileNames, path);
        } else {
          fileNames.add(path.toAbsolutePath().toString().replace('\\', '/'));
        }
      }
    }
    return fileNames;
  }
}
