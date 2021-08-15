package com.github.nicholasmoser.gnt4.seq;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.utils.ByteStream;
import org.junit.jupiter.api.Test;

public class EffectiveAddressesTest {

  /**
   * 04037c00
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test1() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x03, 0x7c, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 04021a3f
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test2() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x1a, 0x3f, 0x00, 0x00, 0x00, 0x01};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 09081d3f
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test3() throws Exception {
    byte[] bytes = new byte[]{0x09, 0x08, 0x1d, 0x3f, 0x00, 0x01, (byte) 0xbf, 0x24};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 02033f00
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test4() throws Exception {
    byte[] bytes = new byte[]{0x02, 0x03, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x0c, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 04020213
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test5() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x02, 0x13};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 09015300
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test6() throws Exception {
    byte[] bytes = new byte[]{0x09, 0x01, 0x53, 0x00, 0x00, 0x00, 0x00, 0x68, 0x7c, 0x00, 0x00, 0x00, 0x00, 0x00, 0x22, 0x1c};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 04022602
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test7() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x26, 0x02};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 04034400
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test8() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x03, 0x44, 0x00, 0x00, 0x00, 0x00, 0x24, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x04};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 0616157b
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test9() throws Exception {
    byte[] bytes = new byte[]{0x06, 0x16, 0x15, 0x7b, 0x00, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x8, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 04116600
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test10() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x11, 0x66, 0x00, 0x00, 0x00, 0x00, 0x18, 0x3f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x17, 0x34};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 22052620
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test11() throws Exception {
    byte[] bytes = new byte[]{0x22, 0x05, 0x26, 0x20};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x4, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }

  /**
   * 09010293
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

  /**
   * 04026600
   *
   * @throws Exception If any Exception occurs
   */
  @Test
  public void test13() throws Exception {
    byte[] bytes = new byte[]{0x04, 0x02, 0x66, 0x00, 0x00, 0x00, 0x00, 0x5c, 0x3f, 0x00, 0x00, 0x00, 0x3f, 0x00, 0x00, 0x00};
    ByteStream bs = new ByteStream(bytes);
    EffectiveAddresses ea = EffectiveAddresses.get(bs);
    System.out.println(ea.getDescription());
    assertEquals(0x10, bs.offset());
    assertArrayEquals(bytes, ea.getBytes());
  }
}
