package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.utils.ByteStream;
import org.junit.jupiter.api.Test;

public class EffectiveAddressTest {

  /**
   * Tests calling opcode 13040013. This will load a general purpose register value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadGpr_1() throws Exception {
    byte[] bytes = new byte[]{0x13, 0x04, 0x00, 0x13};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 03030003. This will load a general purpose register value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadGpr_2() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x03};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 13060022. This will load a seq stored value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadSeq_1() throws Exception {
    byte[] bytes = new byte[]{0x13, 0x06, 0x00, 0x22};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 21060026. This will load an seq stored value. This is called for getting
   * this seq's chr_p +4 bytes appears to be the opponent chr_p TODO: Is that true?
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadSeq_2() throws Exception {
    byte[] bytes = new byte[]{0x21, 0x06, 0x00, 0x26};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 06060032. This will load a global value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadGlobal_1() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x06, 0x00, 0x32};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 06060039. This will load a global value.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadGlobal_2() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x06, 0x00, 0x39};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 0206003e. This will read an immediate value with an offset.
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
   * Tests calling opcode 0206003f. This will peek an immediate value with an offset.
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
   * Tests calling opcode 03030042. This will load a general purpose register value with an offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadGprWithOffset_1() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x42, 0x00, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 03030053. This will load a general purpose register value with an offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadGprWithOffset_2() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x53, 0x00, 0x00, 0x00, 0x04};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 01500068. This will load a seq stored value with an offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadSeqWithOffset_1() throws Exception {
    byte[] bytes = new byte[]{0x01, 0x50, 0x00, 0x68, 0x00, 0x00, 0x00, 0x5c};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 03030066. This will load a seq stored value with an offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadSeqWithOffset_2() throws Exception {
    byte[] bytes = new byte[]{0x03, 0x03, 0x00, 0x66, 0x00, 0x00, 0x00, 0x20};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 01500070. This will load a global value with an offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadGlobalWithOffset_1() throws Exception {
    byte[] bytes = new byte[]{0x01, 0x50, 0x00, 0x70, 0x00, 0x00, 0x00, 0x60};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 3e020079. This will load a global value with an offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testLoadGlobalWithOffset_2() throws Exception {
    byte[] bytes = new byte[]{0x3e, 0x02, 0x00, 0x79, 0x00, 0x00, 0x01, (byte) 0xb6};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 0f0d007e. This will read an immediate value with an offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testReadImmediateWithOffset() throws Exception {
    byte[] bytes = new byte[]{0x0f, 0x0d, 0x00, 0x7e, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x00,
        0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddress ea = EffectiveAddress.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(new byte[]{0x0f, 0x0d, 0x00, 0x7e, 0x00, 0x00, 0x10, 0x00}, ea.getBytes());
  }

  /**
   * Tests calling opcode 0f0d007f. This will peek an immediate value with an offset.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testPeekImmediateWithOffset() throws Exception {
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


