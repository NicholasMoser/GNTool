package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.gnt4.seq.operands.ChrOperand;
import com.github.nicholasmoser.gnt4.seq.operands.GPROperand;
import com.github.nicholasmoser.gnt4.seq.operands.GlobalOperand;
import com.github.nicholasmoser.gnt4.seq.operands.GlobalOperand.Value;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.gnt4.seq.operands.Operand;
import com.github.nicholasmoser.gnt4.seq.operands.SeqOperand;
import com.github.nicholasmoser.utils.ByteStream;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class SEQRegCMD2Test {

  /**
   * Tests calling opcode 04020213.
   *
   * <ul>
   *   <li>First operand is a general purpose register pointer.</li>
   *   <li>Second operand is a general purpose register pointer.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprGpr() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x02, 0x13};
    String expected = "gpr2, gpr19";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
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
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 0402132f.
   *
   * <ul>
   *   <li>First operand is a general purpose register pointer.</li>
   *   <li>Second operand is a seq stored pointer.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprSeq() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x13, 0x2f};
    String expected = "gpr19, seq_p_sp23";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
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
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 0616157b.
   *
   * <ul>
   *   <li>First operand is a general purpose register pointer.</li>
   *   <li>Second operand is a global pointer plus offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprGlobalPlusOffset() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x16, 0x15, 0x7b, 0x00, 0x00, 0x00, 0x00};
    String expected = "gpr21, PAUSE_GAME->field_0x00";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
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
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 04022502.
   *
   * <ul>
   *   <li>First operand is a seq stored pointer.</li>
   *   <li>Second operand is a general purpose register pointer.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqGpr() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x25, 0x02};
    String expected = "seq_p_sp13, gpr2";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0xD, seqOperand.getIndex());
    assertTrue(seqOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand2;
    assertEquals(2, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 22052820.
   *
   * <ul>
   *   <li>First operand is a seq stored pointer.</li>
   *   <li>Second operand is a seq stored pointer.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqSeq() throws Exception {
    byte[] bytes = new byte[]{0x22, 0x05, 0x28, 0x20};
    String expected = "seq_p_sp16, seq_p_sp8";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0x10, seqOperand.getIndex());
    assertTrue(seqOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof SeqOperand);
    SeqOperand seqOperand2 = (SeqOperand) operand2;
    assertEquals(0x8, seqOperand2.getIndex());
    assertTrue(seqOperand2.isPointer());
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 04021a3f.
   *
   * <ul>
   *   <li>First operand is a seq stored pointer.</li>
   *   <li>Second operand is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqImmediate_1() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x1a, 0x3f, 0x00, 0x00, 0x00, 0x01};
    String expected = "seq_p_sp2, 0x1";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
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
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 09081d3f.
   *
   * <ul>
   *   <li>First operand is a seq stored pointer.</li>
   *   <li>Second operand is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqImmediate_2() throws Exception {
    byte[] bytes = new byte[]{0x09, 0x08, 0x1d, 0x3f, 0x00, 0x01, (byte) 0xbf, 0x24};
    String expected = "seq_p_sp5, 0x1BF24";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
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
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 02033f00.
   *
   *
   * <ul>
   *   <li>First operand is an immediate value offset.</li>
   *   <li>Second operand is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testImmediateImmediate() throws Exception {
    byte[] bytes = new byte[]{0x02, 0x03, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x0c, 0x3f, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x00};
    String expected = "0xC, 0x0";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
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
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 04034400.
   *
   * <ul>
   *   <li>First operand is a general purpose register value plus offset.</li>
   *   <li>Second operand is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprPlusOffsetImmediate() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x03, 0x44, 0x00, 0x00, 0x00, 0x00, 0x24, 0x3f, 0x00, 0x00,
        0x00, 0x00, 0x02, 0x00, 0x04};
    String expected = "*gpr4->field_0x24, 0x20004";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
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
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 09015300.
   *
   * <ul>
   *   <li>First operand is a general purpose register value plus offset.</li>
   *   <li>Second operand is a global value plus offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprPlusOffsetGlobalPlusOffset() throws Exception {
    byte[] bytes = new byte[]{0x09, 0x01, 0x53, 0x00, 0x00, 0x00, 0x00, 0x68, 0x7c, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x22, 0x1c};
    String expected = "*gpr19->field_0x68, GAME_INFO->field_0x221C";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
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
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 04116500.
   *
   * <ul>
   *   <li>First operand is a seq stored value plus offset.</li>
   *   <li>Second operand is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqPlusOffsetImmediate_1() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x11, 0x65, 0x00, 0x00, 0x00, 0x00, 0x18, 0x3f, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x17, 0x34};
    String expected = "*seq_p_sp13->field_0x18, 0x1734";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0xD, seqOperand.getIndex());
    assertFalse(seqOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand2;
    assertEquals(0xC, immediateOperand.getOffset());
    assertEquals(0x1734, immediateOperand.getImmediateValue());
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 04026500.
   *
   * <ul>
   *   <li>First operand is a seq stored value plus offset.</li>
   *   <li>Second operand is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqPlusOffsetImmediate_2() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x65, 0x00, 0x00, 0x00, 0x00, 0x5c, 0x3f, 0x00, 0x00,
        0x00, 0x3f, 0x00, 0x00, 0x00};
    String expected = "*seq_p_sp13->field_0x5C, 0x3F000000";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof SeqOperand);
    SeqOperand seqOperand = (SeqOperand) operand;
    assertEquals(0xD, seqOperand.getIndex());
    assertFalse(seqOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand2;
    assertEquals(0xC, immediateOperand.getOffset());
    assertEquals(0x3F000000, immediateOperand.getImmediateValue());
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 04037c00.
   *
   * <ul>
   *   <li>First operand is a global value plus offset.</li>
   *   <li>Second operand is an immediate value offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobalPlusOffsetImmediate() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x03, 0x7c, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3f, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x02, 0x00};
    String expected = "GAME_INFO->field_0x00, 0x200";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
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
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Tests calling opcode 09010293.
   *
   * <ul>
   *   <li>First operand is a general purpose register address.</li>
   *   <li>Second operand is a general purpose register value summed with a general
   *   purpose register value plus offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprGprSumPlusOffset() throws Exception {
    byte[] bytes = new byte[]{0x09, 0x01, 0x02, (byte) 0x93, 0x00, 0x00, 0x00, 0x02};
    String expected = "gpr2, *gpr19 + *gpr2 + 0000";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
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
    assertConvertingFromString(expected, bytes);
  }

  @Test
  public void testChrPActionId() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x66, 0x00, 0x00, 0x00, 0x02, 0x3C, 0x3F, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x01};
    String expected = "*chr_p->act_id, 0x1";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof ChrOperand);
    ChrOperand chrOperand = (ChrOperand) operand;
    assertEquals(0x23C, chrOperand.get());
    assertFalse(chrOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand2;
    assertEquals(0x1, immediateOperand.getImmediateValue());
    assertConvertingFromString(expected, bytes);
  }

  @Test
  public void testChrPUnknownField() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x66, 0x00, 0x00, 0x00, 0x7F, 0x7F, 0x3F, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00, 0x01};
    String expected = "*chr_p->field_0x7F7F, 0x1";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof ChrOperand);
    ChrOperand chrOperand = (ChrOperand) operand;
    assertEquals(0x7F7F, chrOperand.get());
    assertFalse(chrOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand = (ImmediateOperand) operand2;
    assertEquals(0x1, immediateOperand.getImmediateValue());
    assertConvertingFromString(expected, bytes);
  }

  @Test
  public void testFoeChrP() throws Exception {
    byte[] bytes = new byte[]{0x22, 0x05, 0x27, 0x20};
    String expected = "foe_chr_p, seq_p_sp8";
    ByteStream bs = new ByteStream(bytes);
    SEQ_RegCMD2 ea = SEQ_RegCMD2.get(bs);
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals(expected, ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof ChrOperand);
    ChrOperand chrOperand = (ChrOperand) operand;
    assertEquals(-1, chrOperand.get());
    assertTrue(chrOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof SeqOperand);
    SeqOperand seqOperand2 = (SeqOperand) operand2;
    assertEquals(0x8, seqOperand2.getIndex());
    assertTrue(seqOperand2.isPointer());
    assertConvertingFromString(expected, bytes);
  }

  /**
   * Assert that convering the String representation of an operand returns the correct bytes when
   * parsed.
   *
   * @param operand The operand to parse.
   * @param bytes The expected bytes.
   * @throws IOException If any I/O exception occurs.
   */
  private void assertConvertingFromString(String operand, byte[] bytes) throws IOException {
    byte[] actualBytes = SEQ_RegCMD2.fromDescription(operand);
    assertArrayEquals(Arrays.copyOfRange(bytes, 2, bytes.length), actualBytes);
  }
}
