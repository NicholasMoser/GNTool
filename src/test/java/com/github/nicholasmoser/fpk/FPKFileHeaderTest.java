package com.github.nicholasmoser.fpk;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FPKFileHeaderTest {

  /**
   * Tests the file header of a file from GNT4, which uses short paths and big endian.
   */
  @Test
  public void testGNT4FileHeader() {
    String fileName = "cmn/3000.poo";
    int offset = 0x130;
    int compressedSize = 0x70;
    int uncompressedSize = 0x1C4;
    byte[] expectedBytes = new byte[] {
        0x63, 0x6D, 0x6E, 0x2F, 0x33, 0x30, 0x30, 0x30, 0x2E, 0x70, 0x6F, 0x6F, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x30, 0x00, 0x00, 0x00, 0x70, 0x00, 0x00,
        0x01, (byte) 0xC4
    };
    FPKFileHeader header = new FPKFileHeader(fileName, offset, compressedSize, uncompressedSize,
        false, true);
    assertEquals(fileName, header.getFileName());
    assertEquals(offset, header.getOffset());
    assertEquals(compressedSize, header.getCompressedSize());
    assertEquals(uncompressedSize, header.getUncompressedSize());
    assertArrayEquals(expectedBytes, header.getBytes());
  }

  /**
   * Tests the file header of a file from Rev3, which uses long paths and big endian.
   */
  @Test
  public void testRev3FileHeader() {
    String fileName = "game/game00_fra.seq";
    int offset = 0xD00;
    int compressedSize = 0xDDC8;
    int uncompressedSize = 0x47FE8;
    byte[] expectedBytes = new byte[] {
        0x67, 0x61, 0x6D, 0x65, 0x2F, 0x67, 0x61, 0x6D, 0x65, 0x30, 0x30, 0x5F, 0x66, 0x72, 0x61,
        0x2E, 0x73, 0x65, 0x71, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0D, 0x00, 0x00, 0x00, (byte) 0xDD,
        (byte) 0xC8, 0x00, 0x04, 0x7F, (byte) 0xE8
    };
    FPKFileHeader header = new FPKFileHeader(fileName, offset, compressedSize, uncompressedSize,
        true, true);
    assertEquals(fileName, header.getFileName());
    assertEquals(offset, header.getOffset());
    assertEquals(compressedSize, header.getCompressedSize());
    assertEquals(uncompressedSize, header.getUncompressedSize());
    assertArrayEquals(expectedBytes, header.getBytes());
  }

  /**
   * Tests the file header of a file from Bloody Roar 4, which uses long paths and little endian.
   */
  @Test
  public void testBR4FileHeader() {
    String fileName = "chr/cmn/1000_ps2.dff";
    int offset = 0x17E0;
    int compressedSize = 0x8DA;
    int uncompressedSize = 0x3695;
    byte[] expectedBytes = new byte[] {
        0x63, 0x68, 0x72, 0x2F, 0x63, 0x6D, 0x6E, 0x2F, 0x31, 0x30, 0x30, 0x30, 0x5F, 0x70, 0x73,
        0x32, 0x2E, 0x64, 0x66, 0x66, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xE0, 0x17, 0x00, 0x00, (byte) 0xDA, 0x08, 0x00,
        0x00, (byte) 0x95, 0x36, 0x00, 0x00
    };
    FPKFileHeader header = new FPKFileHeader(fileName, offset, compressedSize, uncompressedSize,
        true, false);
    assertEquals(fileName, header.getFileName());
    assertEquals(offset, header.getOffset());
    assertEquals(compressedSize, header.getCompressedSize());
    assertEquals(uncompressedSize, header.getUncompressedSize());
    assertArrayEquals(expectedBytes, header.getBytes());
  }
}
