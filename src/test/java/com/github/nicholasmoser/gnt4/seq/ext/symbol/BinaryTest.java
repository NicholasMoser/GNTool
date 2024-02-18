package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.gnt4.seq.ext.parser.SymbolParser;
import com.github.nicholasmoser.utils.ByteStream;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BinaryTest {
  @Test
  public void testNormal() throws Exception {
    String name = "Test 1";
    byte[] bytes = new byte[] {0x01, 0x02, 0x03, 0x04};
    Binary binary = new Binary(name, bytes);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.dataOffset()).isEqualTo(0x20);
    assertThat(binary.length()).isEqualTo(0x30);
    byte[] actualBytes = Arrays.copyOfRange(binary.bytes(), binary.dataOffset(), binary.dataOffset() + binary.binaryLength());
    assertThat(actualBytes).isEqualTo(bytes);
    assertThat(actualBytes).isEqualTo(binary.binaryBytes());

    // Now parse the bytes and assert it is the same symbol
    ByteStream bs = new ByteStream(binary.bytes());
    List<Symbol> symbols = SymbolParser.parse(bs, 1);
    assertThat(symbols.size()).isEqualTo(1);
    Symbol symbol = symbols.get(0);
    assertThat(symbol).isOfAnyClassIn(Binary.class);
    Binary actualBinary = (Binary) symbol;
    assertThat(actualBinary.length()).isEqualTo(binary.length());
    assertThat(actualBinary.name()).isEqualTo(binary.name());
    assertThat(actualBinary.bytes()).isEqualTo(binary.bytes());
    assertThat(actualBinary.dataOffset()).isEqualTo(binary.dataOffset());
    assertThat(actualBinary.binaryLength()).isEqualTo(binary.binaryLength());
    assertThat(actualBinary.binaryBytes()).isEqualTo(binary.binaryBytes());
  }

  @Test
  public void test15ByteName() throws Exception {
    String name = "ABCDEFGHIJKLMNO";
    byte[] bytes = new byte[] {0x01, 0x02, 0x03, 0x04};
    Binary binary = new Binary(name, bytes);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.dataOffset()).isEqualTo(0x20);
    assertThat(binary.length()).isEqualTo(0x30);
    byte[] actualBytes = Arrays.copyOfRange(binary.bytes(), binary.dataOffset(), binary.dataOffset() + binary.binaryLength());
    assertThat(actualBytes).isEqualTo(bytes);
    assertThat(actualBytes).isEqualTo(binary.binaryBytes());

    // Now parse the bytes and assert it is the same symbol
    ByteStream bs = new ByteStream(binary.bytes());
    List<Symbol> symbols = SymbolParser.parse(bs, 1);
    assertThat(symbols.size()).isEqualTo(1);
    Symbol symbol = symbols.get(0);
    assertThat(symbol).isOfAnyClassIn(Binary.class);
    Binary actualBinary = (Binary) symbol;
    assertThat(actualBinary.length()).isEqualTo(binary.length());
    assertThat(actualBinary.name()).isEqualTo(binary.name());
    assertThat(actualBinary.bytes()).isEqualTo(binary.bytes());
    assertThat(actualBinary.dataOffset()).isEqualTo(binary.dataOffset());
    assertThat(actualBinary.binaryLength()).isEqualTo(binary.binaryLength());
    assertThat(actualBinary.binaryBytes()).isEqualTo(binary.binaryBytes());
  }

  @Test
  public void test16ByteName() throws Exception {
    String name = "ABCDEFGHIJKLMNOP";
    byte[] bytes = new byte[] {0x01, 0x02, 0x03, 0x04};
    Binary binary = new Binary(name, bytes);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.dataOffset()).isEqualTo(0x30);
    assertThat(binary.length()).isEqualTo(0x40);
    byte[] actualBytes = Arrays.copyOfRange(binary.bytes(), binary.dataOffset(), binary.dataOffset() + binary.binaryLength());
    assertThat(actualBytes).isEqualTo(bytes);
    assertThat(actualBytes).isEqualTo(binary.binaryBytes());

    // Now parse the bytes and assert it is the same symbol
    ByteStream bs = new ByteStream(binary.bytes());
    List<Symbol> symbols = SymbolParser.parse(bs, 1);
    assertThat(symbols.size()).isEqualTo(1);
    Symbol symbol = symbols.get(0);
    assertThat(symbol).isOfAnyClassIn(Binary.class);
    Binary actualBinary = (Binary) symbol;
    assertThat(actualBinary.length()).isEqualTo(binary.length());
    assertThat(actualBinary.name()).isEqualTo(binary.name());
    assertThat(actualBinary.bytes()).isEqualTo(binary.bytes());
    assertThat(actualBinary.dataOffset()).isEqualTo(binary.dataOffset());
    assertThat(actualBinary.binaryLength()).isEqualTo(binary.binaryLength());
    assertThat(actualBinary.binaryBytes()).isEqualTo(binary.binaryBytes());
  }

  @Test
  public void test17ByteBinary() throws Exception {
    String name = "Test 1";
    byte[] bytes = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
    Binary binary = new Binary(name, bytes);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.dataOffset()).isEqualTo(0x20);
    assertThat(binary.length()).isEqualTo(0x40);
    byte[] actualBytes = Arrays.copyOfRange(binary.bytes(), binary.dataOffset(), binary.dataOffset() + binary.binaryLength());
    assertThat(actualBytes).isEqualTo(bytes);
    assertThat(actualBytes).isEqualTo(binary.binaryBytes());

    // Now parse the bytes and assert it is the same symbol
    ByteStream bs = new ByteStream(binary.bytes());
    List<Symbol> symbols = SymbolParser.parse(bs, 1);
    assertThat(symbols.size()).isEqualTo(1);
    Symbol symbol = symbols.get(0);
    assertThat(symbol).isOfAnyClassIn(Binary.class);
    Binary actualBinary = (Binary) symbol;
    assertThat(actualBinary.length()).isEqualTo(binary.length());
    assertThat(actualBinary.name()).isEqualTo(binary.name());
    assertThat(actualBinary.bytes()).isEqualTo(binary.bytes());
    assertThat(actualBinary.dataOffset()).isEqualTo(binary.dataOffset());
    assertThat(actualBinary.binaryLength()).isEqualTo(binary.binaryLength());
    assertThat(actualBinary.binaryBytes()).isEqualTo(binary.binaryBytes());
  }
}
