package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.utils.ByteStream;
import org.junit.jupiter.api.Test;

public class SeqOpTest {

  /**
   * Tests calling opcode 13040013. This enters the first group in seq_get_op().
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGetGprPointer1() throws Exception {
    byte[] bytes = new byte[] { 0x13, 0x04, 0x00, 0x13 };
    ByteStream bs = new ByteStream(bytes);
    SeqOp seqOp = SeqOp.get(bs);
    System.out.println(seqOp.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(new byte[0], seqOp.getBytes());
  }

  /**
   * Tests calling opcode 03030003. This enters the first group in seq_get_op().
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGetGprPointer2() throws Exception {
    byte[] bytes = new byte[] { 0x03, 0x03, 0x00, 0x03 };
    ByteStream bs = new ByteStream(bytes);
    SeqOp seqOp = SeqOp.get(bs);
    System.out.println(seqOp.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(new byte[0], seqOp.getBytes());
  }

  /**
   * Tests calling opcode 13060022. This enters the first group in seq_get_op().
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGetSeqPointer1() throws Exception {
    byte[] bytes = new byte[] { 0x13, 0x06, 0x00, 0x22 };
    ByteStream bs = new ByteStream(bytes);
    SeqOp seqOp = SeqOp.get(bs);
    System.out.println(seqOp.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(new byte[0], seqOp.getBytes());
  }

  /**
   * Tests calling opcode 21060026. This enters the first group in seq_get_op().
   * This is called for getting this seq's chr_p
   * +4 bytes appears to be the opponent chr_p TODO: Is that true?
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGetSeqPointer2() throws Exception {
    byte[] bytes = new byte[] { 0x21, 0x06, 0x00, 0x26 };
    ByteStream bs = new ByteStream(bytes);
    SeqOp seqOp = SeqOp.get(bs);
    System.out.println(seqOp.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(new byte[0], seqOp.getBytes());
  }

  /**
   * Tests calling opcode 06060032. This enters the first group in seq_get_op().
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testOpLookupGlobalPointer1() throws Exception {
    byte[] bytes = new byte[] { 0x06, 0x06, 0x00, 0x32 };
    ByteStream bs = new ByteStream(bytes);
    SeqOp seqOp = SeqOp.get(bs);
    System.out.println(seqOp.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(new byte[0], seqOp.getBytes());
  }

  /**
   * Tests calling opcode 06060039. This enters the first group in seq_get_op().
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testOpLookupGlobalPointer2() throws Exception {
    byte[] bytes = new byte[] { 0x06, 0x06, 0x00, 0x39 };
    ByteStream bs = new ByteStream(bytes);
    SeqOp seqOp = SeqOp.get(bs);
    System.out.println(seqOp.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(new byte[0], seqOp.getBytes());
  }

  /**
   * Tests calling opcode 0206003e. This enters the first group in seq_get_op().
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testOpLookupPointer3e() throws Exception {
    byte[] bytes = new byte[] {(byte) 0x02, 0x06, 0x00, 0x3e, 0x00, 0x00, 0x01, 0x00, 0x7f, 0x7f, 0x7f, 0x7f };
    ByteStream bs = new ByteStream(bytes);
    SeqOp seqOp = SeqOp.get(bs);
    System.out.println(seqOp.getDescription());
    assertEquals(0xc, bs.offset());
    assertArrayEquals(new byte[] {0x00, 0x00, 0x01, 0x00, 0x7f, 0x7f, 0x7f, 0x7f}, seqOp.getBytes());
  }

  /**
   * Tests calling opcode 0206003f. This enters the first group in seq_get_op().
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testOpLookupPointer3f() throws Exception {
    byte[] bytes = new byte[] {(byte) 0x02, 0x06, 0x00, 0x3f, 0x00, 0x00, 0x01, 0x00 };
    ByteStream bs = new ByteStream(bytes);
    SeqOp seqOp = SeqOp.get(bs);
    System.out.println(seqOp.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(new byte[] {0x00, 0x00, 0x01, 0x00}, seqOp.getBytes());
  }

  /**
   * Tests calling opcode 01500068. This enters the third group in seq_get_op().
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGetSeqValue1() throws Exception {
    byte[] bytes = new byte[] { 0x01, 0x50, 0x00, 0x68, 0x00, 0x00, 0x00, 0x5c };
    ByteStream bs = new ByteStream(bytes);
    SeqOp seqOp = SeqOp.get(bs);
    System.out.println(seqOp.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(new byte[] {0x00, 0x00, 0x00, 0x5c}, seqOp.getBytes());
  }

  /**
   * Tests calling opcode 01500070. This enters the third group in seq_get_op().
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testOpLookupGlobalValue1() throws Exception {
    byte[] bytes = new byte[] { 0x01, 0x50, 0x00, 0x70, 0x00, 0x00, 0x00, 0x60 };
    ByteStream bs = new ByteStream(bytes);
    SeqOp seqOp = SeqOp.get(bs);
    System.out.println(seqOp.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(new byte[] {0x00, 0x00, 0x00, 0x60}, seqOp.getBytes());
  }
}


