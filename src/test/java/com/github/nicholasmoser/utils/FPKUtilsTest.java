package com.github.nicholasmoser.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.nicholasmoser.fpk.FPKFileHeader;
import com.github.nicholasmoser.testing.Prereqs;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class FPKUtilsTest {
  @Test
  public void readGNT4FileHeader() throws Exception {
    Path compressedDir = Prereqs.getCompressedGNT4();
    Path fpkPath = compressedDir.resolve("files/fpack/cmn0000.fpk");
    try(InputStream is = Files.newInputStream(fpkPath)) {
      int numOfFiles = FPKUtils.readFPKHeader(is, true);
      assertEquals(9, numOfFiles);
      // File 1
      FPKFileHeader header = FPKUtils.readFPKFileHeader(is, false, true);
      assertEquals("cmn/3000.poo", header.getFileName());
      assertEquals(0x130, header.getOffset());
      assertEquals(0x70, header.getCompressedSize());
      assertEquals(0x1C4, header.getUncompressedSize());
      // File 2
      header = FPKUtils.readFPKFileHeader(is, false, true);
      assertEquals("cmn/3000.pro", header.getFileName());
      assertEquals(0x1A0, header.getOffset());
      assertEquals(0x94, header.getCompressedSize());
      assertEquals(0x11C, header.getUncompressedSize());
      // File 3
      header = FPKUtils.readFPKFileHeader(is, false, true);
      assertEquals("cmn/3000.sam", header.getFileName());
      assertEquals(0x240, header.getOffset());
      assertEquals(0xB580, header.getCompressedSize());
      assertEquals(0xB580, header.getUncompressedSize());
      // File 4
      header = FPKUtils.readFPKFileHeader(is, false, true);
      assertEquals("cmn/3000.sdi", header.getFileName());
      assertEquals(0xB7C0, header.getOffset());
      assertEquals(0x1DB, header.getCompressedSize());
      assertEquals(0x28C, header.getUncompressedSize());
      // File 5
      header = FPKUtils.readFPKFileHeader(is, false, true);
      assertEquals("cmn/bannerj.tpl", header.getFileName());
      assertEquals(0xB9A0, header.getOffset());
      assertEquals(0xD1C, header.getCompressedSize());
      assertEquals(0xE60, header.getUncompressedSize());
      // File 6
      header = FPKUtils.readFPKFileHeader(is, false, true);
      assertEquals("cmn/icon.tpl", header.getFileName());
      assertEquals(0xC6C0, header.getOffset());
      assertEquals(0x519, header.getCompressedSize());
      assertEquals(0x660, header.getUncompressedSize());
      // File 7
      header = FPKUtils.readFPKFileHeader(is, false, true);
      assertEquals("cmn/0000.txg", header.getFileName());
      assertEquals(0xCBE0, header.getOffset());
      assertEquals(0xE0A9, header.getCompressedSize());
      assertEquals(0x20240, header.getUncompressedSize());
      // File 8
      header = FPKUtils.readFPKFileHeader(is, false, true);
      assertEquals("cmn/0002.txg", header.getFileName());
      assertEquals(0x1AC90, header.getOffset());
      assertEquals(0x810, header.getCompressedSize());
      assertEquals(0x100C0, header.getUncompressedSize());
      // File 9
      header = FPKUtils.readFPKFileHeader(is, false, true);
      assertEquals("cmn/0003.txg", header.getFileName());
      assertEquals(0x1B4A0, header.getOffset());
      assertEquals(0x21D7, header.getCompressedSize());
      assertEquals(0x59E0, header.getUncompressedSize());

      // Error if you read any further (in most cases)
      assertThrows(IOException.class, () -> FPKUtils.readFPKFileHeader(is, false, true));
    }
  }
}
