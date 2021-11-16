package com.github.nicholasmoser.gnt4.seq.ext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.ByteUtils;
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
  }
}
