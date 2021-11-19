package com.github.nicholasmoser.gnt4.seq.ext;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.testing.Prereqs;
import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SeqExtTest {

  @Test
  public void testEmptyBytes() throws Exception {
    List<SeqEdit> seqEdits = SeqExt.getEdits(new byte[0]);
    assertTrue(seqEdits.isEmpty());
  }

  @Test
  public void testEightZeroes() throws Exception {
    List<SeqEdit> seqEdits = SeqExt.getEdits(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
    assertTrue(seqEdits.isEmpty());
  }

  @Test
  public void testAllSeqsNoEdits() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    for (String seq : Seqs.ALL) {
      Path seqPath = uncompressed.resolve(seq);
      List<SeqEdit> seqEdits = SeqExt.getEdits(seqPath);
      assertTrue(seqEdits.isEmpty());
    }
  }

  @Test
  public void testOnlySeqEndErrors() {
    assertThrows(IOException.class, () -> SeqExt.getEdits(SeqExt.SEQ_END));
  }

  @Test
  public void testSeqExtAndSeqEnd() throws Exception {
    byte[] bytes = Bytes.concat(SeqExt.SEQ_EXT, SeqExt.SEQ_END);
    List<SeqEdit> seqEdits = SeqExt.getEdits(bytes);
    assertTrue(seqEdits.isEmpty());
  }

  @Test
  public void testInvalidNameErrors() {
    byte[] filler = new byte[]{0, 1, 2, 3};
    byte[] bytes = Bytes.concat(SeqExt.SEQ_EXT, filler, SeqExt.SEQ_END);
    assertThrows(IOException.class, () -> SeqExt.getEdits(bytes));
  }

  @Test
  public void testNoOffsetErrors() {
    byte[] name = new byte[]{0x73, 0x65, 0x71, 0x00};
    byte[] bytes = Bytes.concat(SeqExt.SEQ_EXT, name, SeqExt.SEQ_END);
    assertThrows(IOException.class, () -> SeqExt.getEdits(bytes));
  }

  @Test
  public void testNoOldBytesErrors() {
    byte[] nameAndOffset = new byte[]{0x73, 0x65, 0x71, 0x00, 0x00, 0x00, 0x01, 0x00};
    byte[] bytes = Bytes.concat(SeqExt.SEQ_EXT, nameAndOffset, SeqExt.SEQ_END);
    assertThrows(IOException.class, () -> SeqExt.getEdits(bytes));
  }

  @Test
  public void testNoNewBytesErrors() {
    byte[] nameAndOffset = new byte[]{0x73, 0x65, 0x71, 0x00, 0x00, 0x00, 0x01, 0x00};
    byte[] oldBytes = new byte[]{0x12, 0x23, 0x45, 0x67};
    byte[] bytes = Bytes.concat(SeqExt.SEQ_EXT, nameAndOffset, oldBytes, SeqEdit.STOP,
        SeqExt.SEQ_END);
    assertThrows(IOException.class, () -> SeqExt.getEdits(bytes));
  }

  @Test
  public void testInvalidStopInBytes() {
    byte[] nullWord = new byte[]{0x0, 0x0, 0x0, 0x0};
    byte[] invalidBytes = Bytes.concat(nullWord, SeqEdit.STOP, nullWord);
    byte[] validBytes = new byte[]{0x67, 0x45, 0x23, 0x12};
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, invalidBytes, validBytes));
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, validBytes, invalidBytes));
  }

  @Test
  public void testInvalidBytesSize() {
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[0], new byte[1]));
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[0], new byte[2]));
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[0], new byte[3]));
    new SeqEdit("Name", 0x8, new byte[0], new byte[4]);
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[0], new byte[5]));
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[1], new byte[0]));
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[2], new byte[0]));
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[3], new byte[0]));
    new SeqEdit("Name", 0x8, new byte[4], new byte[0]);
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[5], new byte[0]));
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[1], new byte[1]));
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[2], new byte[2]));
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[3], new byte[3]));
    new SeqEdit("Name", 0x8, new byte[4], new byte[4]);
    new SeqEdit("Name", 0x8, new byte[8], new byte[8]);
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[8], new byte[9]));
    assertThrows(IllegalArgumentException.class,
        () -> new SeqEdit("Name", 0x8, new byte[9], new byte[8]));
  }

  @Test
  public void testOneEdit() throws Exception {
    byte[] nameAndOffset = new byte[]{0x73, 0x65, 0x71, 0x00, 0x00, 0x00, 0x01, 0x00};
    byte[] oldBytes = new byte[]{0x12, 0x23, 0x45, 0x67};
    byte[] newBytes = new byte[]{0x67, 0x45, 0x23, 0x12};
    byte[] edit = Bytes.concat(nameAndOffset, oldBytes, SeqEdit.STOP, newBytes, SeqEdit.STOP);
    byte[] bytes = Bytes.concat(SeqExt.SEQ_EXT, edit, SeqExt.SEQ_END);
    List<SeqEdit> seqEdits = SeqExt.getEdits(bytes);
    assertEquals(1, seqEdits.size());
    SeqEdit seqEdit = seqEdits.get(0);
    assertEquals("seq", seqEdit.getName());
    assertEquals(0x100, seqEdit.getOffset());
    assertArrayEquals(oldBytes, seqEdit.getOldBytes());
    assertArrayEquals(newBytes, seqEdit.getNewBytes());
    assertArrayEquals(edit, seqEdit.getFullBytes());
  }

  @Test
  public void testThreeEdits() throws Exception {
    byte[] nameAndOffset1 = new byte[]{0x73, 0x65, 0x71, 0x00, 0x00, 0x00, 0x01, 0x00};
    byte[] nameAndOffset2 = new byte[]{0x77, 0x65, 0x71, 0x00, 0x7F, (byte) 0xFF, 0x00, 0x01};
    byte[] nameAndOffset3 = new byte[]{0x77, 0x6F, 0x77, 0x00, 0x10, 0x00, 0x00, 0x27};
    byte[] bytes1 = new byte[]{0x12, 0x23, 0x45, 0x67};
    byte[] bytes2 = new byte[]{0x67, 0x45, 0x23, 0x12};
    byte[] bytes3 = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };
    byte[] bytes4 = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
    byte[] bytes5 = new byte[0x1000];
    byte[] bytes6 = new byte[0];
    byte[] edit1 = Bytes.concat(nameAndOffset1, bytes1, SeqEdit.STOP, bytes2, SeqEdit.STOP);
    byte[] edit2 = Bytes.concat(nameAndOffset2, bytes3, SeqEdit.STOP, bytes4, SeqEdit.STOP);
    byte[] edit3 = Bytes.concat(nameAndOffset3, bytes5, SeqEdit.STOP, bytes6, SeqEdit.STOP);
    byte[] bytes = Bytes.concat(SeqExt.SEQ_EXT, edit1, edit2, edit3, SeqExt.SEQ_END);
    List<SeqEdit> seqEdits = SeqExt.getEdits(bytes);
    assertEquals(3, seqEdits.size());
    SeqEdit seqEdit = seqEdits.get(0);
    assertEquals("seq", seqEdit.getName());
    assertEquals(0x100, seqEdit.getOffset());
    assertArrayEquals(bytes1, seqEdit.getOldBytes());
    assertArrayEquals(bytes2, seqEdit.getNewBytes());
    assertArrayEquals(edit1, seqEdit.getFullBytes());
    SeqEdit seqEdit2 = seqEdits.get(1);
    assertEquals("weq", seqEdit2.getName());
    assertEquals(0x7FFF0001, seqEdit2.getOffset());
    assertArrayEquals(bytes3, seqEdit2.getOldBytes());
    assertArrayEquals(bytes4, seqEdit2.getNewBytes());
    assertArrayEquals(edit2, seqEdit2.getFullBytes());
    SeqEdit seqEdit3 = seqEdits.get(2);
    assertEquals("wow", seqEdit3.getName());
    assertEquals(0x10000027, seqEdit3.getOffset());
    assertArrayEquals(bytes5, seqEdit3.getOldBytes());
    assertArrayEquals(bytes6, seqEdit3.getNewBytes());
    assertArrayEquals(edit3, seqEdit3.getFullBytes());
  }
}