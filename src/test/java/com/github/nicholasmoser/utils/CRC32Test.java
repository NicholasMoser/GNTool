package com.github.nicholasmoser.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class CRC32Test {

  @Test
  public void test32BytesOfZeroes() {
    byte[] bytes = new byte[32];
    for (int i = 0; i < 32; i++) {
      bytes[i] = 0;
    }
    assertEquals(0x190A55AD, CRC32.getHash(bytes));
    assertEquals(0x190A55AD, CRC32.getHash(bytes));
  }

  @Test
  public void test32BytesOfOnes() {
    byte[] bytes = new byte[32];
    for (int i = 0; i < 32; i++) {
      bytes[i] = (byte) 0xFF;
    }
    assertEquals(0xFF6CAB0B, CRC32.getHash(bytes));
    assertEquals(0xFF6CAB0B, CRC32.getHash(bytes));
  }

  @Test
  public void testHelloWorld() {
    assertEquals(0x1C291CA3, CRC32.getHash("Hello World!".getBytes(StandardCharsets.UTF_8)));
    assertEquals(0x1C291CA3, CRC32.getHash("Hello World!".getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  public void testFileSameAsBytes() throws Exception {
    String text = "HOW ARE YOU GENTLEMEN !!";
    Path tempDir = FileUtils.getTempDirectory();
    Path tempFile = tempDir.resolve(UUID.randomUUID().toString());
    try {
      Files.writeString(tempFile, text);
      int first = CRC32.getHash(tempFile);
      int second = CRC32.getHash(text.getBytes(StandardCharsets.UTF_8));
      assertEquals(first, second);
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }
}
