package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ExistingBinaryTest {
  @Test
  public void testNormal() {
    String name = "Test 1";
    int offset = 0x100;
    int length = 0x70;
    ExistingBinary binary = new ExistingBinary(name, offset, length);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.length()).isEqualTo(0x20);
    assertThat(binary.dataOffset()).isEqualTo(offset);
    assertThat(binary.binaryLength()).isEqualTo(length);
  }

  @Test
  public void test15ByteName() {
    String name = "ABCDEFGHIJKLMNO";
    int offset = 0x100;
    int length = 0x70;
    ExistingBinary binary = new ExistingBinary(name, offset, length);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.length()).isEqualTo(0x20);
    assertThat(binary.dataOffset()).isEqualTo(offset);
    assertThat(binary.binaryLength()).isEqualTo(length);
  }

  @Test
  public void test16ByteName() {
    String name = "ABCDEFGHIJKLMNOP";
    int offset = 0x100;
    int length = 0x70;
    ExistingBinary binary = new ExistingBinary(name, offset, length);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.length()).isEqualTo(0x30);
    assertThat(binary.dataOffset()).isEqualTo(offset);
    assertThat(binary.binaryLength()).isEqualTo(length);
  }
}
