package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class BinaryTest {
  @Test
  public void testNormal() {
    String name = "Test 1";
    byte[] bytes = new byte[] {0x01, 0x02, 0x03, 0x04};
    Binary binary = new Binary(name, bytes);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.dataOffset()).isEqualTo(0x20);
    assertThat(binary.length()).isEqualTo(0x30);
    byte[] actualBytes = Arrays.copyOfRange(binary.bytes(), binary.dataOffset(), binary.dataOffset() + binary.binaryLength());
    assertThat(actualBytes).isEqualTo(bytes);
    assertThat(actualBytes).isEqualTo(binary.binaryBytes());
  }

  @Test
  public void test15ByteName() {
    String name = "ABCDEFGHIJKLMNO";
    byte[] bytes = new byte[] {0x01, 0x02, 0x03, 0x04};
    Binary binary = new Binary(name, bytes);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.dataOffset()).isEqualTo(0x20);
    assertThat(binary.length()).isEqualTo(0x30);
    byte[] actualBytes = Arrays.copyOfRange(binary.bytes(), binary.dataOffset(), binary.dataOffset() + binary.binaryLength());
    assertThat(actualBytes).isEqualTo(bytes);
    assertThat(actualBytes).isEqualTo(binary.binaryBytes());
  }

  @Test
  public void test16ByteName() {
    String name = "ABCDEFGHIJKLMNOP";
    byte[] bytes = new byte[] {0x01, 0x02, 0x03, 0x04};
    Binary binary = new Binary(name, bytes);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.dataOffset()).isEqualTo(0x30);
    assertThat(binary.length()).isEqualTo(0x40);
    byte[] actualBytes = Arrays.copyOfRange(binary.bytes(), binary.dataOffset(), binary.dataOffset() + binary.binaryLength());
    assertThat(actualBytes).isEqualTo(bytes);
    assertThat(actualBytes).isEqualTo(binary.binaryBytes());
  }

  @Test
  public void test17ByteBinary() {
    String name = "Test 1";
    byte[] bytes = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
    Binary binary = new Binary(name, bytes);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.dataOffset()).isEqualTo(0x20);
    assertThat(binary.length()).isEqualTo(0x40);
    byte[] actualBytes = Arrays.copyOfRange(binary.bytes(), binary.dataOffset(), binary.dataOffset() + binary.binaryLength());
    assertThat(actualBytes).isEqualTo(bytes);
    assertThat(actualBytes).isEqualTo(binary.binaryBytes());
  }
}
