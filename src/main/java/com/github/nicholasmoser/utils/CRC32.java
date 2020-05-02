package com.github.nicholasmoser.utils;

import com.google.common.io.Files;
import java.io.IOException;
import java.nio.file.Path;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * Common functions for CRC32 hashing.
 */
public class CRC32 {
  /**
   * Returns the CRC32 hash for a file from the given Path.
   * 
   * @param filePath The Path to the file.
   * @return The CRC32 value as an integer.
   * @throws IOException If there is an I/O exception when reading the file.
   */
  public static int getHash(Path filePath) throws IOException {
    HashFunction crc32 = Hashing.crc32();
    HashCode hashValue = Files.asByteSource(filePath.toFile()).hash(crc32);
    return hashValue.asInt();
  }

  /**
   * Returns the CRC32 hash for a given array of bytes.
   * 
   * @param bytes The bytes to hash.
   * @return The CRC32 value as an integer.
   */
  public static int getHash(byte[] bytes) {
    HashFunction crc32 = Hashing.crc32();
    HashCode hashValue = crc32.hashBytes(bytes);
    return hashValue.asInt();
  }
}
