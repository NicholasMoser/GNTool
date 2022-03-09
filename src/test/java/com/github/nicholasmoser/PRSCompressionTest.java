package com.github.nicholasmoser;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test class for PRS compression and uncompression algorithms.
 */
public class PRSCompressionTest {

  /**
   * Tests compressing and uncompressing ten zeroes. The ten bytes of zeroes and output of
   * compressing and uncompressing should be the same.
   *
   */
  @Test
  public void testTenZeroes() {
    byte[] originalBytes = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    int originalSize = originalBytes.length;
    PRSCompressor compressor = new PRSCompressor(originalBytes);
    byte[] compressedBytes = compressor.compress();
    PRSUncompressor uncompressor = new PRSUncompressor(compressedBytes, originalSize);
    byte[] outputBytes = uncompressor.uncompress();
    assertArrayEquals(originalBytes, outputBytes);
  }

  // Create test that compresses and uncompresses all GNT files

  @Test
  public void testRandomBytes() {
    SecureRandom random = new SecureRandom();
    for (int i = 10; i < 1000; i += 50) {
      byte[] originalBytes = new byte[i];
      random.nextBytes(originalBytes);
      int originalSize = originalBytes.length;
      PRSCompressor compressor = new PRSCompressor(originalBytes);
      byte[] compressedBytes = compressor.compress();
      PRSUncompressor uncompressor = new PRSUncompressor(compressedBytes, originalSize);
      byte[] outputBytes = uncompressor.uncompress();
      assertArrayEquals(originalBytes, outputBytes);
    }
  }

  /**
   * Tests compressing and uncompressing a file with random hex values. The bytes of the original
   * file and output of compressing and uncompressing should be the same.
   * 
   * @throws Exception If any exception occurs.
   */
  @Test
  public void testRandomHex() throws Exception {
    Path randomHexPath = Paths.get("src/test/resources/random_hex.seq");
    byte[] originalBytes = Files.readAllBytes(randomHexPath);
    int originalSize = originalBytes.length;
    PRSCompressor compressor = new PRSCompressor(originalBytes);
    byte[] compressedBytes = compressor.compress();
    PRSUncompressor uncompressor = new PRSUncompressor(compressedBytes, originalSize);
    byte[] outputBytes = uncompressor.uncompress();
    assertArrayEquals(originalBytes, outputBytes);
  }

  /**
   * Tests the PRS compression of a single file that is notorious for having issues.
   * 
   * @throws Exception If any exception occurs.
   */
  @Test
  @Disabled("Not ready yet.")
  public void testSingleFileCompression() throws Exception {
    byte[] originalBytes = Files.readAllBytes(Paths.get("src/test/resources/0000_txg.dat"));
    int originalSize = originalBytes.length;
    System.out.println(String.format("Original Compressed Size: %d", originalSize));

    byte[] uncompressedBytes = Files.readAllBytes(Paths.get("src/test/resources/0000.txg"));
    PRSCompressor compressor = new PRSCompressor(uncompressedBytes);
    byte[] compressedBytes = compressor.compress();
    int compressedSize = compressedBytes.length;
    System.out.println(String.format("Compressed Size: %d", compressedSize));

    PRSUncompressor uncompressor = new PRSUncompressor(compressedBytes, uncompressedBytes.length);
    byte[] new_uncompressed = uncompressor.uncompress();
    if (!Arrays.equals(uncompressedBytes, new_uncompressed)) {
      for (int i = 0; i < uncompressedBytes.length; i++) {
        if (uncompressedBytes[i] != new_uncompressed[i]) {
          System.out.println(String.format("Index at %02x", i));
          break;
        }
      }
      Files.write(Paths.get("src/test/resources/0000_new.txg"), new_uncompressed);
      Files.write(Paths.get("src/test/resources/0000_txg_new.dat"), compressedBytes);
      fail("The decompression has changed the output.");
    }

    if (!Arrays.equals(originalBytes, compressedBytes)) {
      Files.write(Paths.get("src/test/resources/0000_txg_new.dat"), compressedBytes);
      fail("Compression does not match original compression.");
    }
  }
}
