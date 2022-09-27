package com.github.nicholasmoser.ppc;

import static com.github.nicholasmoser.utils.ByteUtils.hexStringToBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CompareWithImmediateTest {

  @Test
  public void canHandleZeroImmediate() {
    assertThat(hexStringToBytes("2C010000")).isEqualTo(CompareWithImmediate.getBytes(1, 0));
  }

  @Test
  public void canHandleZeroRegister() {
    assertThat(hexStringToBytes("2C000001")).isEqualTo(CompareWithImmediate.getBytes(0, 1));
  }

  @Test
  public void canHandleMaxPositiveImmediate() {
    assertThat(hexStringToBytes("2C017FFF")).isEqualTo(CompareWithImmediate.getBytes(1, 0x7FFF));
  }

  @Test
  public void canHandleMinNegativeImmediate() {
    assertThat(hexStringToBytes("2C018000")).isEqualTo(CompareWithImmediate.getBytes(1, -0x8000));
  }

  @Test
  public void canHandleMaxRegister() {
    assertThat(hexStringToBytes("2C1F0001")).isEqualTo(CompareWithImmediate.getBytes(31, 1));
  }

  @Test
  public void canHandlePositiveImmediate() {
    assertThat(hexStringToBytes("2C120010")).isEqualTo(CompareWithImmediate.getBytes(18, 0x10));
    assertThat(hexStringToBytes("2C130100")).isEqualTo(CompareWithImmediate.getBytes(19, 0x100));
    assertThat(hexStringToBytes("2C140104")).isEqualTo(CompareWithImmediate.getBytes(20, 0x104));
    assertThat(hexStringToBytes("2C150108")).isEqualTo(CompareWithImmediate.getBytes(21, 0x108));
    assertThat(hexStringToBytes("2C16010C")).isEqualTo(CompareWithImmediate.getBytes(22, 0x10C));
    assertThat(hexStringToBytes("2C170110")).isEqualTo(CompareWithImmediate.getBytes(23, 0x110));
    assertThat(hexStringToBytes("2C180A7C")).isEqualTo(CompareWithImmediate.getBytes(24, 0xA7C));
    assertThat(hexStringToBytes("2C197000")).isEqualTo(CompareWithImmediate.getBytes(25, 0x7000));
  }

  @Test
  public void canHandleNegativeImmediate() {
    assertThat(hexStringToBytes("2C09FFF0")).isEqualTo(CompareWithImmediate.getBytes(9, -0x10));
    assertThat(hexStringToBytes("2C0AFF00")).isEqualTo(CompareWithImmediate.getBytes(10, -0x100));
    assertThat(hexStringToBytes("2C0BFEFC")).isEqualTo(CompareWithImmediate.getBytes(11, -0x104));
    assertThat(hexStringToBytes("2C0CFEF8")).isEqualTo(CompareWithImmediate.getBytes(12, -0x108));
    assertThat(hexStringToBytes("2C0DFEF4")).isEqualTo(CompareWithImmediate.getBytes(13, -0x10C));
    assertThat(hexStringToBytes("2C0EFEF0")).isEqualTo(CompareWithImmediate.getBytes(14, -0x110));
    assertThat(hexStringToBytes("2C0FF584")).isEqualTo(CompareWithImmediate.getBytes(15, -0xA7C));
    assertThat(hexStringToBytes("2C109000")).isEqualTo(CompareWithImmediate.getBytes(16, -0x7000));
  }

  @Test
  public void throwExceptionOnTooLargePositiveImmediate() {
    assertThrows(IllegalArgumentException.class, () -> CompareWithImmediate.getBytes(1, 0x8000));
    assertThrows(IllegalArgumentException.class, () -> CompareWithImmediate.getBytes(2, 0x8001));
    assertThrows(IllegalArgumentException.class, () -> CompareWithImmediate.getBytes(3, 0x10000));
    assertThrows(IllegalArgumentException.class, () -> CompareWithImmediate.getBytes(4, 0x7FFFFFFF));
  }

  @Test
  public void throwExceptionOnTooSmallNegativeImmediate() {
    assertThrows(IllegalArgumentException.class, () -> CompareWithImmediate.getBytes(5, -0x8001));
    assertThrows(IllegalArgumentException.class, () -> CompareWithImmediate.getBytes(6, -0x8002));
    assertThrows(IllegalArgumentException.class, () -> CompareWithImmediate.getBytes(7, -0x10000));
    assertThrows(IllegalArgumentException.class, () -> CompareWithImmediate.getBytes(8, -0x7FFFFFFF));
  }
}
