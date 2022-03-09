package com.github.nicholasmoser.gnt4.seq.ext;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class SeqEditBuilderTest {

  private static final int BRANCH_BACK_LEN = 8;

  @Test
  void testSeqBytes() throws Exception {
    String name = "Test Edit";
    int offset = 0x4;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    SeqEdit seqEdit = SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(0xC)
        .name(name)
        .newBytes(newBytes)
        .create();
    assertEquals(name, seqEdit.getName());
    assertEquals(offset, seqEdit.getOffset());
    assertArrayEquals(newBytes, seqEdit.getNewBytes());
    assertArrayEquals(new byte[]{4, 5, 6, 7, 8, 9, 10, 11}, seqEdit.getOldBytes());
    assertEquals(newBytes.length + BRANCH_BACK_LEN, seqEdit.getNewBytesWithBranchBack().length);
  }

  @Test
  void testSeqPath() throws Exception {
    Path seqPath = Paths.get("src/test/resources/gnt4/seq/ext/small.seq");
    String name = "Test Edit";
    int offset = 0x4;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    SeqEdit seqEdit = SeqEditBuilder.getBuilder()
        .seqPath(seqPath)
        .startOffset(offset)
        .endOffset(0xC)
        .name(name)
        .newBytes(newBytes)
        .create();
    assertEquals(name, seqEdit.getName());
    assertEquals(offset, seqEdit.getOffset());
    assertArrayEquals(newBytes, seqEdit.getNewBytes());
    assertArrayEquals(new byte[]{4, 5, 6, 7, 8, 9, 10, 11}, seqEdit.getOldBytes());
    assertEquals(newBytes.length + BRANCH_BACK_LEN, seqEdit.getNewBytesWithBranchBack().length);
  }

  @Test
  void testFirstFourBytes() throws Exception {
    String name = "Test Edit";
    int offset = 0;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    SeqEdit seqEdit = SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(0x8)
        .name(name)
        .newBytes(newBytes)
        .create();
    assertEquals(name, seqEdit.getName());
    assertEquals(offset, seqEdit.getOffset());
    assertArrayEquals(newBytes, seqEdit.getNewBytes());
    assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7}, seqEdit.getOldBytes());
    assertEquals(newBytes.length + BRANCH_BACK_LEN, seqEdit.getNewBytesWithBranchBack().length);
  }

  @Test
  void testLastFourBytes() throws Exception {
    String name = "Test Edit";
    int offset = 0x8;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    SeqEdit seqEdit = SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15})
        .startOffset(offset)
        .endOffset(0x10)
        .name(name)
        .newBytes(newBytes)
        .create();
    assertEquals(name, seqEdit.getName());
    assertEquals(offset, seqEdit.getOffset());
    assertArrayEquals(newBytes, seqEdit.getNewBytes());
    assertArrayEquals(new byte[]{8, 9, 10, 11, 12, 13, 14, 15}, seqEdit.getOldBytes());
    assertEquals(newBytes.length + BRANCH_BACK_LEN, seqEdit.getNewBytesWithBranchBack().length);
  }

  @Test
  void testLargeNewBytes() throws Exception {
    String name = "Test Edit";
    int offset = 0x4;
    byte[] newBytes = new byte[0x1000];
    SeqEdit seqEdit = SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(0xC)
        .name(name)
        .newBytes(newBytes)
        .create();
    assertEquals(name, seqEdit.getName());
    assertEquals(offset, seqEdit.getOffset());
    assertArrayEquals(newBytes, seqEdit.getNewBytes());
    assertArrayEquals(new byte[]{4, 5, 6, 7, 8, 9, 10, 11}, seqEdit.getOldBytes());
    assertEquals(newBytes.length + BRANCH_BACK_LEN, seqEdit.getNewBytesWithBranchBack().length);
  }

  @Test
  void testLargeOldBytes() throws Exception {
    String name = "Test Edit";
    int offset = 0x444;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    SeqEdit seqEdit = SeqEditBuilder.getBuilder()
        .seqBytes(new byte[0x1000])
        .startOffset(offset)
        .endOffset(0x644)
        .name(name)
        .newBytes(newBytes)
        .create();
    assertEquals(name, seqEdit.getName());
    assertEquals(offset, seqEdit.getOffset());
    assertArrayEquals(newBytes, seqEdit.getNewBytes());
    assertArrayEquals(new byte[0x200], seqEdit.getOldBytes());
    assertEquals(newBytes.length + BRANCH_BACK_LEN, seqEdit.getNewBytesWithBranchBack().length);
  }

  @Test
  void testNullStartOffsetErrors() {
    String name = "Test Edit";
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .endOffset(0x8)
        .name(name)
        .newBytes(newBytes)
        .create());
  }

  @Test
  void testNullEndOffsetErrors() {
    String name = "Test Edit";
    int offset = 0x4;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .name(name)
        .newBytes(newBytes)
        .create());
  }

  @Test
  void testNullNewBytesErrors() {
    String name = "Test Edit";
    int offset = 0x4;
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(0x8)
        .name(name)
        .create());
  }

  @Test
  void testNullNameErrors() {
    int offset = 0x4;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(0x8)
        .newBytes(newBytes)
        .create());
  }

  @Test
  void testNoSeqBytesOrPathErrors() {
    String name = "Test Edit";
    int offset = 0x4;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .startOffset(offset)
        .endOffset(0x8)
        .name(name)
        .newBytes(newBytes)
        .create());
  }

  @Test
  void testInvalidPathErrors() {
    Path invalidPath = Paths.get("doesnotexist");
    String name = "Test Edit";
    int offset = 0x4;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    assertThrows(IOException.class, () -> SeqEditBuilder.getBuilder()
        .seqPath(invalidPath)
        .startOffset(offset)
        .endOffset(0x8)
        .name(name)
        .newBytes(newBytes)
        .create());
  }

  @Test
  void testStartOffsetNot4ByteAligned() {
    String name = "Test Edit";
    int offset = 0x5;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(0x8)
        .name(name)
        .newBytes(newBytes)
        .create());
  }

  @Test
  void testEndOffsetNot4ByteAligned() {
    String name = "Test Edit";
    int offset = 0x4;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78};
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(0x6)
        .name(name)
        .newBytes(newBytes)
        .create());
  }

  @Test
  void testNewBytesNot4ByteAligned() {
    String name = "Test Edit";
    int offset = 0x4;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78, 0x11, 0x22, 0x33};
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(0x8)
        .name(name)
        .newBytes(newBytes)
        .create());
  }

  @Test
  void testOldBytesMustBeEightOrGreater() throws Exception {
    String name = "Test Edit";
    int offset = 4;
    byte[] newBytes = new byte[]{0x12, 0x34, 0x56, 0x78, 0x11, 0x22, 0x33, 0x44};
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(5)
        .name(name)
        .newBytes(newBytes)
        .create());
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(6)
        .name(name)
        .newBytes(newBytes)
        .create());
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(7)
        .name(name)
        .newBytes(newBytes)
        .create());
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(8)
        .name(name)
        .newBytes(newBytes)
        .create());
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(9)
        .name(name)
        .newBytes(newBytes)
        .create());
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(10)
        .name(name)
        .newBytes(newBytes)
        .create());
    assertThrows(IllegalArgumentException.class, () -> SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(11)
        .name(name)
        .newBytes(newBytes)
        .create());
    SeqEditBuilder.getBuilder()
        .seqBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
        .startOffset(offset)
        .endOffset(12)
        .name(name)
        .newBytes(newBytes)
        .create();
  }
}
