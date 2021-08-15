package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.utils.ByteStream;
import org.junit.jupiter.api.Test;

public class EffectiveAddressesTest {

  /**
   * Tests calling opcode 04020213.
   *
   * <ul>
   *   <li>First effective address is a general purpose register value.</li>
   *   <li>Second effective address is a general purpose register value.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprGpr() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x02, 0x13};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 0616157b.
   *
   * <ul>
   *   <li>First effective address is a general purpose register value.</li>
   *   <li>Second effective address is a global value plus offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprGlobal() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x16, 0x15, 0x7b, 0x00, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 04022602.
   *
   * <ul>
   *   <li>First effective address is a seq stored value.</li>
   *   <li>Second effective address is a general purpose register value.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqGpr() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x26, 0x02};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 22052620.
   *
   * <ul>
   *   <li>First effective address is a seq stored value.</li>
   *   <li>Second effective address is a seq stored value.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqSeq() throws Exception {
    byte[] bytes = new byte[]{0x22, 0x05, 0x26, 0x20};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 04021a3f.
   *
   * <ul>
   *   <li>First effective address is a seq stored value.</li>
   *   <li>Second effective address is an opcode offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqOpcode_1() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x1a, 0x3f, 0x00, 0x00, 0x00, 0x01};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 09081d3f.
   *
   * <ul>
   *   <li>First effective address is a seq stored value.</li>
   *   <li>Second effective address is an opcode offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqOpcode_2() throws Exception {
    byte[] bytes = new byte[]{0x09, 0x08, 0x1d, 0x3f, 0x00, 0x01, (byte) 0xbf, 0x24};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 02033f00.
   *
   *
   * <ul>
   *   <li>First effective address is an opcode offset.</li>
   *   <li>Second effective address is an opcode offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testOpcodeOpcode() throws Exception {
    byte[] bytes = new byte[]{0x02, 0x03, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x0c, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 04034400.
   *
   * <ul>
   *   <li>First effective address is a general purpose register value plus offset.</li>
   *   <li>Second effective address is an opcode offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGprPlusOffsetOpcode() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x03, 0x44, 0x00, 0x00, 0x00, 0x00, 0x24, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x04};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
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
    byte[] bytes = new byte[]{0x09, 0x01, 0x53, 0x00, 0x00, 0x00, 0x00, 0x68, 0x7c, 0x00, 0x00, 0x00, 0x00, 0x00, 0x22, 0x1c};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 04116600.
   *
   * <ul>
   *   <li>First effective address is a seq stored value plus offset.</li>
   *   <li>Second effective address is an opcode offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqPlusOffsetOpcode_1() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x11, 0x66, 0x00, 0x00, 0x00, 0x00, 0x18, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x17, 0x34};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 04026600.
   *
   * <ul>
   *   <li>First effective address is a seq stored value plus offset.</li>
   *   <li>Second effective address is an opcode offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testSeqPlusOffsetOpcode_2() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x66, 0x00, 0x00, 0x00, 0x00, 0x5c, 0x3f, 0x00, 0x00, 0x00, 0x3f, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 04037c00.
   *
   * <ul>
   *   <li>First effective address is a global value plus offset.</li>
   *   <li>Second effective address is an opcode offset.</li>
   * </ul>
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void testGlobalPlusOffsetOpcode() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x03, 0x7c, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * Tests calling opcode 09010293.
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test12() throws Exception {
    byte[] bytes = new byte[]{0x09, 0x01, 0x02, (byte) 0x93, 0x00, 0x00, 0x00, 0x02};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }
}
