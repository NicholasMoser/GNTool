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
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
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
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
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
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
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
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
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
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
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
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
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
    byte[] bytes = new byte[]{0x09, 0x01, 0x53, 0x00, 0x00, 0x00, 0x00, 0x68, 0x7c, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x22, 0x1c};
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
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
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
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }
}
