package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.gnt4.seq.ext.parser.SymbolParser;
import com.github.nicholasmoser.utils.ByteStream;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ExistingBinaryTest {
  @Test
  public void testNormal() throws Exception {
    String name = "Test 1";
    int offset = 0x100;
    int length = 0x70;
    ExistingBinary binary = new ExistingBinary(name, offset, length);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.length()).isEqualTo(0x20);
    assertThat(binary.dataOffset()).isEqualTo(offset);
    assertThat(binary.binaryLength()).isEqualTo(length);

    // Now parse the bytes and assert it is the same symbol
    ByteStream bs = new ByteStream(binary.bytes());
    List<Symbol> symbols = SymbolParser.parse(bs, 1);
    assertThat(symbols.size()).isEqualTo(1);
    Symbol symbol = symbols.get(0);
    assertThat(symbol).isOfAnyClassIn(ExistingBinary.class);
    ExistingBinary actualExistingBinary = (ExistingBinary) symbol;
    assertThat(actualExistingBinary.length()).isEqualTo(binary.length());
    assertThat(actualExistingBinary.name()).isEqualTo(binary.name());
    assertThat(actualExistingBinary.bytes()).isEqualTo(binary.bytes());
    assertThat(actualExistingBinary.dataOffset()).isEqualTo(binary.dataOffset());
    assertThat(actualExistingBinary.binaryLength()).isEqualTo(binary.binaryLength());
  }

  @Test
  public void test15ByteName() throws Exception {
    String name = "ABCDEFGHIJKLMNO";
    int offset = 0x100;
    int length = 0x70;
    ExistingBinary binary = new ExistingBinary(name, offset, length);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.length()).isEqualTo(0x20);
    assertThat(binary.dataOffset()).isEqualTo(offset);
    assertThat(binary.binaryLength()).isEqualTo(length);

    // Now parse the bytes and assert it is the same symbol
    ByteStream bs = new ByteStream(binary.bytes());
    List<Symbol> symbols = SymbolParser.parse(bs, 1);
    assertThat(symbols.size()).isEqualTo(1);
    Symbol symbol = symbols.get(0);
    assertThat(symbol).isOfAnyClassIn(ExistingBinary.class);
    ExistingBinary actualExistingBinary = (ExistingBinary) symbol;
    assertThat(actualExistingBinary.length()).isEqualTo(binary.length());
    assertThat(actualExistingBinary.name()).isEqualTo(binary.name());
    assertThat(actualExistingBinary.bytes()).isEqualTo(binary.bytes());
    assertThat(actualExistingBinary.dataOffset()).isEqualTo(binary.dataOffset());
    assertThat(actualExistingBinary.binaryLength()).isEqualTo(binary.binaryLength());
  }

  @Test
  public void test16ByteName() throws Exception {
    String name = "ABCDEFGHIJKLMNOP";
    int offset = 0x100;
    int length = 0x70;
    ExistingBinary binary = new ExistingBinary(name, offset, length);
    assertThat(binary.name()).isEqualTo(name);
    assertThat(binary.length()).isEqualTo(0x30);
    assertThat(binary.dataOffset()).isEqualTo(offset);
    assertThat(binary.binaryLength()).isEqualTo(length);

    // Now parse the bytes and assert it is the same symbol
    ByteStream bs = new ByteStream(binary.bytes());
    List<Symbol> symbols = SymbolParser.parse(bs, 1);
    assertThat(symbols.size()).isEqualTo(1);
    Symbol symbol = symbols.get(0);
    assertThat(symbol).isOfAnyClassIn(ExistingBinary.class);
    ExistingBinary actualExistingBinary = (ExistingBinary) symbol;
    assertThat(actualExistingBinary.length()).isEqualTo(binary.length());
    assertThat(actualExistingBinary.name()).isEqualTo(binary.name());
    assertThat(actualExistingBinary.bytes()).isEqualTo(binary.bytes());
    assertThat(actualExistingBinary.dataOffset()).isEqualTo(binary.dataOffset());
    assertThat(actualExistingBinary.binaryLength()).isEqualTo(binary.binaryLength());
  }
}
