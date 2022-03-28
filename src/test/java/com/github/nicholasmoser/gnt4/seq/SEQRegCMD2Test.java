package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.gnt4.seq.operands.GPROperand;
import com.github.nicholasmoser.gnt4.seq.operands.GlobalOperand;
import com.github.nicholasmoser.gnt4.seq.operands.GlobalOperand.Value;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.gnt4.seq.operands.Operand;
import com.github.nicholasmoser.gnt4.seq.operands.SeqOperand;
import com.github.nicholasmoser.utils.ByteStream;
import org.junit.jupiter.api.Test;

public class SEQRegCMD2Test {

  /**
   * Tests calling opcode 04020213.
   *
   * <ul>
   *   <li>First effective address is a general purpose register pointer.</li>
   *   <li>Second effective address is a general purpose register pointer.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprGpr() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x02, 0x13};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("gpr2; gpr19", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(2, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof GPROperand);
    GPROperand gprOperand2 = (GPROperand) operand2;
    assertEquals(19, gprOperand2.getIndex());
    assertTrue(gprOperand2.isPointer());
  }

  /**
   * Tests calling opcode 0402132f.
   *
   * <ul>
   *   <li>First effective address is a general purpose register pointer.</li>
   *   <li>Second effective address is a seq stored pointer.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprSeq() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x13, 0x2f};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("gpr19; seq_p_sp->field_0x17", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(19, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand2;
    assertEquals(0x17, seqOperand.getIndex());
    assertTrue(seqOperand.isPointer());
  }

  /**
   * Tests calling opcode 0616157b.
   *
   * <ul>
   *   <li>First effective address is a general purpose register pointer.</li>
   *   <li>Second effective address is a global pointer plus offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprGlobalPlusOffset() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x16, 0x15, 0x7b, 0x00, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("gpr21; PAUSE_GAME + offset 0x00000000", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(21, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof GlobalOperand);
    GlobalOperand globalOperand = (GlobalOperand) operand2;
    assertEquals("PAUSE_GAME", globalOperand.getName());
    assertEquals(Value.PAUSE_GAME, globalOperand.getValue());
    assertEquals(0x80222fb0, globalOperand.getAddress());
  }

  /**
   * Tests calling opcode 04022602.
   *
   * <ul>
   *   <li>First effective address is a seq stored pointer.</li>
   *   <li>Second effective address is a general purpose register pointer.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqGpr() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x26, 0x02};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("seq_p_sp->field_0x0e; gpr2", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0xE, seqOperand.getIndex());
    assertTrue(seqOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand2;
    assertEquals(2, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
  }

  /**
   * Tests calling opcode 22052620.
   *
   * <ul>
   *   <li>First effective address is a seq stored pointer.</li>
   *   <li>Second effective address is a seq stored pointer.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqSeq() throws Exception {
    byte[] bytes = new byte[]{0x22, 0x05, 0x26, 0x20};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("seq_p_sp->field_0x0e; seq_p_sp->field_0x08", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0xE, seqOperand.getIndex());
    assertTrue(seqOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof SeqOperand);
    SeqOperand seqOperand2 = (SeqOperand) operand2;
    assertEquals(0x8, seqOperand2.getIndex());
    assertTrue(seqOperand2.isPointer());
  }

  /**
   * Tests calling opcode 04021a3f.
   *
   * <ul>
   *   <li>First effective address is a seq stored pointer.</li>
   *   <li>Second effective address is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqImmediate_1() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x1a, 0x3f, 0x00, 0x00, 0x00, 0x01};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("seq_p_sp->field_0x02; 0x1", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0x2, seqOperand.getIndex());
    assertTrue(seqOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand2;
    assertEquals(0x4, immediateOperand.getOffset());
    assertEquals(0x1, immediateOperand.getImmediateValue());
  }

  /**
   * Tests calling opcode 09081d3f.
   *
   * <ul>
   *   <li>First effective address is a seq stored pointer.</li>
   *   <li>Second effective address is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqImmediate_2() throws Exception {
    byte[] bytes = new byte[]{0x09, 0x08, 0x1d, 0x3f, 0x00, 0x01, (byte) 0xbf, 0x24};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("seq_p_sp->field_0x05; 0x1BF24", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0x5, seqOperand.getIndex());
    assertTrue(seqOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand2;
    assertEquals(0x4, immediateOperand.getOffset());
    assertEquals(0x1bf24, immediateOperand.getImmediateValue());
  }

  /**
   * Tests calling opcode 02033f00.
   *
   *
   * <ul>
   *   <li>First effective address is an immediate value offset.</li>
   *   <li>Second effective address is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testImmediateImmediate() throws Exception {
    byte[] bytes = new byte[]{0x02, 0x03, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x0c, 0x3f, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("0xC; 0x0", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand;
    assertEquals(0x4, immediateOperand.getOffset());
    assertEquals(0xC, immediateOperand.getImmediateValue());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand2 = (ImmediateOperand) operand2;
    assertEquals(0xC, immediateOperand2.getOffset());
    assertEquals(0x0, immediateOperand2.getImmediateValue());
  }

  /**
   * Tests calling opcode 04034400.
   *
   * <ul>
   *   <li>First effective address is a general purpose register value plus offset.</li>
   *   <li>Second effective address is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprPlusOffsetImmediate() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x03, 0x44, 0x00, 0x00, 0x00, 0x00, 0x24, 0x3f, 0x00, 0x00,
        0x00, 0x00, 0x02, 0x00, 0x04};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("*gpr4 + offset 0x00000024; 0x20004", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(4, gprOperand.getIndex());
    assertFalse(gprOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand2 = (ImmediateOperand) operand2;
    assertEquals(0xC, immediateOperand2.getOffset());
    assertEquals(0x20004, immediateOperand2.getImmediateValue());
  }

  /**
   * Tests calling opcode 09015300.
   *
   * <ul>
   *   <li>First effective address is a general purpose register value plus offset.</li>
   *   <li>Second effective address is a global value plus offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprPlusOffsetGlobalPlusOffset() throws Exception {
    byte[] bytes = new byte[]{0x09, 0x01, 0x53, 0x00, 0x00, 0x00, 0x00, 0x68, 0x7c, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x22, 0x1c};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("*gpr19 + offset 0x00000068; GAME_INFO + offset 0x0000221C", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(19, gprOperand.getIndex());
    assertFalse(gprOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof GlobalOperand);
    GlobalOperand globalOperand = (GlobalOperand) operand2;
    assertEquals("GAME_INFO", globalOperand.getName());
    assertEquals(Value.GAME_INFO, globalOperand.getValue());
    assertEquals(0x802261d8, globalOperand.getAddress());
  }

  /**
   * Tests calling opcode 04116600.
   *
   * <ul>
   *   <li>First effective address is a seq stored value plus offset.</li>
   *   <li>Second effective address is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqPlusOffsetImmediate_1() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x11, 0x66, 0x00, 0x00, 0x00, 0x00, 0x18, 0x3f, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x17, 0x34};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("*seq_p_sp->field_0x98 + offset 0x00000018; 0x1734", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0x98, seqOperand.getIndex());
    assertFalse(seqOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand2;
    assertEquals(0xC, immediateOperand.getOffset());
    assertEquals(0x1734, immediateOperand.getImmediateValue());
  }

  /**
   * Tests calling opcode 04026600.
   *
   * <ul>
   *   <li>First effective address is a seq stored value plus offset.</li>
   *   <li>Second effective address is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqPlusOffsetImmediate_2() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x66, 0x00, 0x00, 0x00, 0x00, 0x5c, 0x3f, 0x00, 0x00,
        0x00, 0x3f, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("*seq_p_sp->field_0x98 + offset 0x0000005C; 0x3F000000", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0x98, seqOperand.getIndex());
    assertFalse(seqOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand2;
    assertEquals(0xC, immediateOperand.getOffset());
    assertEquals(0x3F000000, immediateOperand.getImmediateValue());
  }

  /**
   * Tests calling opcode 04037c00.
   *
   * <ul>
   *   <li>First effective address is a global value plus offset.</li>
   *   <li>Second effective address is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobalPlusOffsetImmediate() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x03, 0x7c, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3f, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x02, 0x00};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("GAME_INFO + offset 0x00000000; 0x200", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof GlobalOperand);
    GlobalOperand globalOperand = (GlobalOperand) operand;
    assertEquals("GAME_INFO", globalOperand.getName());
    assertEquals(Value.GAME_INFO, globalOperand.getValue());
    assertEquals(0x802261d8, globalOperand.getAddress());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand2;
    assertEquals(0xC, immediateOperand.getOffset());
    assertEquals(0x200, immediateOperand.getImmediateValue());
  }

  /**
   * Tests calling opcode 09010293.
   *
   * <ul>
   *   <li>First effective address is a general purpose register address.</li>
   *   <li>Second effective address is a general purpose register value summed with a general
   *   purpose register value plus offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprGprSumPlusOffset() throws Exception {
    byte[] bytes = new byte[]{0x09, 0x01, 0x02, (byte) 0x93, 0x00, 0x00, 0x00, 0x02};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("gpr2; *gpr19 + *gpr2 + 0000", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(2, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof GPROperand);
    GPROperand gprOperand2 = (GPROperand) operand2;
    assertEquals(19, gprOperand2.getIndex());
    assertFalse(gprOperand2.isPointer());
  }
}
