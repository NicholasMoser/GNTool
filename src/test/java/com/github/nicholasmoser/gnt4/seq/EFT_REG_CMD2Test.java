package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.nicholasmoser.gnt4.seq.operands.GPROperand;
import com.github.nicholasmoser.gnt4.seq.operands.GlobalOperand;
import com.github.nicholasmoser.gnt4.seq.operands.GlobalOperand.Value;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.gnt4.seq.operands.Operand;
import com.github.nicholasmoser.utils.ByteStream;
import org.junit.jupiter.api.Test;

public class EFT_REG_CMD2Test {
  /**
   * Tests calling opcode 0301002A.
   *
   * <ul>
   *   <li>First operand is a general purpose register pointer.</li>
   *   <li>Second operand is an immediate value.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprImmediate() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x01, 0x00, 0x2A, 0x00, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("gpr0, 0x0", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(0, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof ImmediateOperand);
    ImmediateOperand immediateOperand2 = (ImmediateOperand) operand2;
    assertEquals(0, immediateOperand2.getImmediateValue());
  }

  /**
   * Tests calling opcode 0301002A.
   *
   * <ul>
   *   <li>First operand is a general purpose register pointer.</li>
   *   <li>Second operand is a global operand.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprGlobal() throws Exception {
    byte[] bytes = new byte[]{0x07, 0x01, 0x01, 0x2B, 0x00, 0x00, 0x08, 0x60, 0x00, 0x00, 0x00, 0x21};
    ByteStream bs = new ByteStream(bytes);
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    assertEquals(0xC, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
    assertEquals("gpr1, HITBOX_IDENTITY_MATRIX + 0860", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(1, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof GlobalOperand);
    GlobalOperand globalOperand2 = (GlobalOperand) operand2;
    assertEquals(Value.HITBOX_IDENTITY_MATRIX, globalOperand2.getValue());
  }

  /**
   * Tests calling opcode 08160C11 0000002B 00000AE0 00000021.
   *
   * <ul>
   *   <li>The operand is a global operand.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testCMD1AndCMD2() throws Exception {
    byte[] bytes = new byte[] {0x08, 0x16, 0x0C, 0x11, 0x00, 0x00, 0x00, 0x2B, 0x00, 0x00, 0x0A,
        (byte) 0xE0, 0x00, 0x00, 0x00, 0x21};
    // EFT_REG_CMD2
    ByteStream bs = new ByteStream(bytes);
    EFT_REG_CMD2 ea = EFT_REG_CMD2.get(bs);
    assertEquals(0x4, bs.offset());
    assertEquals("gpr12, gpr17", ea.getDescription());
    Operand operand = ea.getFirstOperand();
    assertTrue(operand instanceof GPROperand);
    GPROperand gprOperand = (GPROperand) operand;
    assertEquals(12, gprOperand.getIndex());
    assertTrue(gprOperand.isPointer());
    Operand operand2 = ea.getSecondOperand();
    assertTrue(operand2 instanceof GPROperand);
    GPROperand gprOperand2 = (GPROperand) operand2;
    assertEquals(17, gprOperand2.getIndex());

    // EFT_REG_CMD1
    EFT_REG_CMD1 ea2 = EFT_REG_CMD1.get(bs);
    assertEquals(0x10, bs.offset());
    assertEquals("HITBOX_IDENTITY_MATRIX + 0ae0", ea2.getDescription());
    operand = ea2.getOperand();
    assertTrue(operand instanceof GlobalOperand);
    GlobalOperand globalOperand = (GlobalOperand) operand;
    assertEquals(Value.HITBOX_IDENTITY_MATRIX, globalOperand.getValue());
    assertTrue(gprOperand.isPointer());
  }
}
