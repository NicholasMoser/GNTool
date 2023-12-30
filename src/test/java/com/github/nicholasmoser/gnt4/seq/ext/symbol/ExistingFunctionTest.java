package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ExistingFunctionTest {
  @Test
  public void testNormal() {
    String name = "Test 1";
    int offset = 0x100;
    int length = 0x70;
    ExistingFunction function = new ExistingFunction(name, offset, length);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.length()).isEqualTo(0x20);
    assertThat(function.dataOffset()).isEqualTo(offset);
    assertThat(function.functionLength()).isEqualTo(length);
  }

  @Test
  public void test15ByteName() {
    String name = "ABCDEFGHIJKLMNO";
    int offset = 0x100;
    int length = 0x70;
    ExistingFunction function = new ExistingFunction(name, offset, length);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.length()).isEqualTo(0x20);
    assertThat(function.dataOffset()).isEqualTo(offset);
    assertThat(function.functionLength()).isEqualTo(length);
  }

  @Test
  public void test16ByteName() {
    String name = "ABCDEFGHIJKLMNOP";
    int offset = 0x100;
    int length = 0x70;
    ExistingFunction function = new ExistingFunction(name, offset, length);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.length()).isEqualTo(0x30);
    assertThat(function.dataOffset()).isEqualTo(offset);
    assertThat(function.functionLength()).isEqualTo(length);
  }
}
