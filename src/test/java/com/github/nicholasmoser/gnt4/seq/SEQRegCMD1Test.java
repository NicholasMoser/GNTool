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

public class SEQRegCMD1Test {

  /**
   * Tests calling opcode 13040013.
   * <p>
   * Operand is a general purpose register value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGpr_1() throws Exception {
    byte[] bytes = new byte[]{0x13, 0x04, 0x00, 0x13};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("gpr19", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(19, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
  }

  /**
   * Tests calling opcode 03030003.
   * <p>
   * Operand is a general purpose register value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGpr_2() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x03};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("gpr3", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(3, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
  }

  /**
   * Tests calling opcode 13060022.
   * <p>
   * Operand is a seq stored value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeq_1() throws Exception {
    byte[] bytes = new byte[]{0x13, 0x06, 0x00, 0x22};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("seq_p_sp->field_0x28", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0xA, seqOperand.getIndex());
    assertTrue(seqOperand.isPointer());
  }

  /**
   * Tests calling opcode 21060026.
   * <p>
   * Operand is a seq stored value.
   * <p>
   * This is called for getting this seq's chr_p +4 bytes appears to be the opponent chr_p
   * TODO: Is that true?
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeq_2() throws Exception {
    byte[] bytes = new byte[]{0x21, 0x06, 0x00, 0x26};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("seq_p_sp->field_0x38", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0xE, seqOperand.getIndex());
    assertTrue(seqOperand.isPointer());
  }

  /**
   * Tests calling opcode 06060032.
   * <p>
   * Operand is a global value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobal_1() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x06, 0x00, 0x32};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("CONTROLLERS", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof GlobalOperand);
    GlobalOperand globalOperand = (GlobalOperand) operand;
    assertEquals("CONTROLLERS", globalOperand.getName());
    assertEquals(Value.CONTROLLERS, globalOperand.getValue());
    assertEquals(0x80222eb0, globalOperand.getAddress());
  }

  /**
   * Tests calling opcode 06060039.
   * <p>
   * Operand is a global value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobal_2() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x06, 0x00, 0x39};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("SAVE_DATA", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof GlobalOperand);
    GlobalOperand globalOperand = (GlobalOperand) operand;
    assertEquals("SAVE_DATA", globalOperand.getName());
    assertEquals(Value.SAVE_DATA, globalOperand.getValue());
    assertEquals(0x802231e8, globalOperand.getAddress());
  }

  /**
   * Tests calling opcode 0206003e.
   * <p>
   * Operand is an immediate value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testReadImmediate() throws Exception {
    byte[] bytes = new byte[]{(byte) 0x02, 0x06, 0x00, 0x3e, 0x00, 0x00, 0x01, 0x00, 0x7f, 0x7f,
        0x7f, 0x7f};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("0x100", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand;
    assertEquals(0x4, immediateOperand.getOffset());
    assertEquals(0x100, immediateOperand.getImmediateValue());
  }

  /**
   * Tests calling opcode 0206003f.
   * <p>
   * Operand is an immediate value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testPeekImmediate() throws Exception {
    byte[] bytes = new byte[]{(byte) 0x02, 0x06, 0x00, 0x3f, 0x00, 0x00, 0x01, 0x00};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("0x100", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand;
    assertEquals(0x4, immediateOperand.getOffset());
    assertEquals(0x100, immediateOperand.getImmediateValue());
  }

  /**
   * Tests calling opcode 03030080.
   * <p>
   * Operand is a general purpose register value plus general purpose register value
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprSumGpr() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, (byte) 0x80, 0x00, 0x00, 0x00, 0x17};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("*gpr0 + *gpr23 + 0000", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(0, gprOperand.getIndex());
    assertFalse(gprOperand.isPointer());
  }

  /**
   * Tests calling opcode 210600af.
   * <p>
   * Operand is a seq stored value plus general purpose register value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqSumGpr() throws Exception {
    byte[] bytes = new byte[]{0x21, 0x06, 0x00, (byte) 0xaf, 0x00, 0x00, 0x00, 0x17};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("*seq_p_sp->field_0x5c + *gpr23 + 0000", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0x17, seqOperand.getIndex());
    assertFalse(seqOperand.isPointer());
  }

  /**
   * Tests calling opcode 060600b4.
   * <p>
   * Operand is a global value plus seq stored value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobalSumSeq() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x06, 0x00, (byte) 0xb4, 0x00, 0x00, 0x00, (byte) 0x24};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("DISPLAY + *seq_p_sp->field_0x90 + 0000", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof GlobalOperand);
    GlobalOperand globalOperand = (GlobalOperand) operand;
    assertEquals("DISPLAY", globalOperand.getName());
    assertEquals(Value.DISPLAY, globalOperand.getValue());
    assertEquals(0x802231a8, globalOperand.getAddress());
  }

  /**
   * Tests calling opcode 020600be.
   * <p>
   * Operand is a read immediate value plus seq stored value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testReadImmediateSumSeqPlusOffset() throws Exception {
    byte[] bytes = new byte[]{(byte) 0x02, 0x06, 0x00, (byte) 0xbe, 0x12, 0x34, 0x00, 0x04, 0x00,
        0x00, 0x30, 0x00};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(new byte[]{(byte) 0x02, 0x06, 0x00, (byte) 0xbe, 0x12, 0x34, 0x00, 0x04},
        ea.getBytes());
    assertEquals("0x3000 + *gpr4 + 1234",
        ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand;
    assertEquals(0x8, immediateOperand.getOffset());
    assertEquals(0x3000, immediateOperand.getImmediateValue());
  }

  /**
   * Tests calling opcode 020600bf.
   * <p>
   * Operand is a peek immediate value plus general purpose register value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testPeekImmediateSumGprPlusOffset() throws Exception {
    byte[] bytes = new byte[]{(byte) 0x02, 0x06, 0x00, (byte) 0xbf, 0x12, 0x34, 0x00, 0x04, 0x00,
        0x00, 0x30, 0x00};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(new byte[]{(byte) 0x02, 0x06, 0x00, (byte) 0xbf, 0x12, 0x34, 0x00, 0x04},
        ea.getBytes());
    assertEquals("0x3000 + *gpr4 + 1234",
        ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand;
    assertEquals(0x8, immediateOperand.getOffset());
    assertEquals(0x3000, immediateOperand.getImmediateValue());
  }

  /**
   * Tests calling opcode 03030042.
   * <p>
   * Operand is a general purpose register value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprPlusOffset_1() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x42, 0x00, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("*gpr2->field_0x00", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(2, gprOperand.getIndex());
    assertFalse(gprOperand.isPointer());
  }

  /**
   * Tests calling opcode 03030053.
   * <p>
   * Operand is a general purpose register value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprPlusOffset_2() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x53, 0x00, 0x00, 0x00, 0x04};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("*gpr19->field_0x04", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(19, gprOperand.getIndex());
    assertFalse(gprOperand.isPointer());
  }

  /**
   * Tests calling opcode 01500068.
   * <p>
   * Operand is a seq stored value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqPlusOffset_1() throws Exception {
    byte[] bytes = new byte[]{0x01, 0x50, 0x00, 0x68, 0x00, 0x00, 0x00, 0x5c};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("*seq_p_sp->field_0x40->field_0x5C", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0x10, seqOperand.getIndex());
    assertFalse(seqOperand.isPointer());
  }

  /**
   * Tests calling opcode 03030066.
   * <p>
   * Operand is a seq stored value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqPlusOffset_2() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x66, 0x00, 0x00, 0x00, 0x20};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("*seq_p_sp->field_0x38->field_0x20", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0xE, seqOperand.getIndex());
    assertFalse(seqOperand.isPointer());
  }

  /**
   * Tests calling opcode 01500070.
   * <p>
   * Operand is a global value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobalPlusOffset_1() throws Exception {
    byte[] bytes = new byte[]{0x01, 0x50, 0x00, 0x70, 0x00, 0x00, 0x00, 0x60};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("HITBOX_IDENTITY_MATRIX->field_0x60", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof GlobalOperand);
    GlobalOperand globalOperand = (GlobalOperand) operand;
    assertEquals("HITBOX_IDENTITY_MATRIX", globalOperand.getName());
    assertEquals(Value.HITBOX_IDENTITY_MATRIX, globalOperand.getValue());
    assertEquals(0x80223428, globalOperand.getAddress());
  }

  /**
   * Tests calling opcode 3e020079.
   * <p>
   * Operand is a global value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobalPlusOffset_2() throws Exception {
    byte[] bytes = new byte[]{0x3e, 0x02, 0x00, 0x79, 0x00, 0x00, 0x01, (byte) 0xb6};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(bytes.length, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("SAVE_DATA->field_0x1B6", ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof GlobalOperand);
    GlobalOperand globalOperand = (GlobalOperand) operand;
    assertEquals("SAVE_DATA", globalOperand.getName());
    assertEquals(Value.SAVE_DATA, globalOperand.getValue());
    assertEquals(0x802231e8, globalOperand.getAddress());
  }

  /**
   * Tests calling opcode 0f0d007e.
   * <p>
   * Operand is a read immediate value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testReadImmediatePlusOffset() throws Exception {
    byte[] bytes = new byte[]{0x0f, 0x0d, 0x00, 0x7e, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00,
        0x00};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(new byte[]{0x0f, 0x0d, 0x00, 0x7e, 0x00, 0x00, 0x10, 0x00}, ea.getBytes());
    assertEquals("0x0->field_0x1000",
        ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand;
    assertEquals(0x8, immediateOperand.getOffset());
    assertEquals(0x0, immediateOperand.getImmediateValue());
  }

  /**
   * Tests calling opcode 0f0d007f.
   * <p>
   * Operand is a peeked immediate value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testPeekImmediatePlusOffset() throws Exception {
    byte[] bytes = new byte[]{0x0f, 0x0d, 0x00, 0x7f, 0x00, 0x00, 0x00, (byte) 0x89, 0x0f, 0x0e,
        0x00, 0x3f};
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD1 ea = SEQ_RegCMD1.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(new byte[]{0x0f, 0x0d, 0x00, 0x7f, 0x00, 0x00, 0x00, (byte) 0x89},
        ea.getBytes());
    assertEquals("0xF0E003F->field_0x89",
        ea.getDescription());
    Operand operand = ea.getOperand();
    assertTrue(operand instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand;
    assertEquals(0x8, immediateOperand.getOffset());
    assertEquals(0x0f0e003f, immediateOperand.getImmediateValue());
  }
}


