package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.utils.ByteStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class EffectiveAddressTest {

  /**
   * Tests calling opcode 13040013.
   *
   * Effective address is a general purpose register value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGpr_1() throws Exception {
    byte[] bytes = new byte[]{0x13, 0x04, 0x00, 0x13};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 03030003.
   *
   * Effective address is a general purpose register value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGpr_2() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x03};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 13060022.
   *
   * Effective address is a seq stored value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeq_1() throws Exception {
    byte[] bytes = new byte[]{0x13, 0x06, 0x00, 0x22};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 21060026.
   *
   * Effective address is a seq stored value.
   *
   * This is called for getting this seq's chr_p +4 bytes appears to be the opponent chr_p
   * TODO: Is that true?
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeq_2() throws Exception {
    byte[] bytes = new byte[]{0x21, 0x06, 0x00, 0x26};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 06060032.
   *
   * Effective address is a global value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobal_1() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x06, 0x00, 0x32};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 06060039.
   *
   * Effective address is a global value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobal_2() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x06, 0x00, 0x39};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 0206003e.
   *
   * Effective address is an immediate value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testReadImmediate() throws Exception {
    byte[] bytes = new byte[]{(byte) 0x02, 0x06, 0x00, 0x3e, 0x00, 0x00, 0x01, 0x00, 0x7f, 0x7f,
        0x7f, 0x7f};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0xc, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 0206003f.
   *
   * Effective address is an immediate value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testPeekImmediate() throws Exception {
    byte[] bytes = new byte[]{(byte) 0x02, 0x06, 0x00, 0x3f, 0x00, 0x00, 0x01, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode
   *
   * Effective address is a general purpose register value plus general purpose register value
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  @Disabled
  public void testGprSumGpr() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x03};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode
   *
   * Effective address is a seq stored value plus general purpose register value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  @Disabled
  public void testSeqSumGpr() throws Exception {
    byte[] bytes = new byte[]{0x21, 0x06, 0x00, 0x26};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode
   *
   * Effective address is a global value plus seq stored value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  @Disabled
  public void testGlobalSumSeq() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x06, 0x00, 0x39};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode
   *
   * Effective address is a read immediate value plus seq stored value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  @Disabled
  public void testReadImmediateSumSeqPlusOffset() throws Exception {
    byte[] bytes = new byte[]{(byte) 0x02, 0x06, 0x00, 0x3e, 0x00, 0x00, 0x01, 0x00, 0x7f, 0x7f,
        0x7f, 0x7f};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0xc, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 020600bf.
   *
   * Effective address is a peek immediate value plus general purpose register value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testPeekImmediateSumGprPlusOffset() throws Exception {
    byte[] bytes = new byte[]{(byte) 0x02, 0x06, 0x00, (byte) 0xbf, 0x12, 0x34, 0x00, 0x04, 0x00, 0x00, 0x30, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0xc, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 03030042.
   *
   * Effective address is a general purpose register value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprPlusOffset_1() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x42, 0x00, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 03030053.
   *
   * Effective address is a general purpose register value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprPlusOffset_2() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x53, 0x00, 0x00, 0x00, 0x04};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 01500068.
   *
   * Effective address is a seq stored value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqPlusOffset_1() throws Exception {
    byte[] bytes = new byte[]{0x01, 0x50, 0x00, 0x68, 0x00, 0x00, 0x00, 0x5c};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 03030066.
   *
   * Effective address is a seq stored value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqPlusOffset_2() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x66, 0x00, 0x00, 0x00, 0x20};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 01500070.
   *
   * Effective address is a global value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobalPlusOffset_1() throws Exception {
    byte[] bytes = new byte[]{0x01, 0x50, 0x00, 0x70, 0x00, 0x00, 0x00, 0x60};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 3e020079.
   *
   * Effective address is a global value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobalPlusOffset_2() throws Exception {
    byte[] bytes = new byte[]{0x3e, 0x02, 0x00, 0x79, 0x00, 0x00, 0x01, (byte) 0xb6};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 0f0d007e.
   *
   * Effective address is a read immediate value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testReadImmediatePlusOffset() throws Exception {
    byte[] bytes = new byte[]{0x0f, 0x0d, 0x00, 0x7e, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00,
        0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(new byte[]{0x0f, 0x0d, 0x00, 0x7e, 0x00, 0x00, 0x10, 0x00}, ea.getBytes());
  }

  /**
   * Tests calling opcode 0f0d007f.
   *
   * Effective address is a peeked immediate value plus offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testPeekImmediatePlusOffset() throws Exception {
    byte[] bytes = new byte[]{0x0f, 0x0d, 0x00, 0x7f, 0x00, 0x00, 0x00, (byte) 0x89, 0x0f, 0x0e,
        0x00, 0x3f};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(new byte[]{0x0f, 0x0d, 0x00, 0x7f, 0x00, 0x00, 0x00, (byte) 0x89},
        ea.getBytes());
  }
}


