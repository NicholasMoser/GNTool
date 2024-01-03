package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.gnt4.seq.ext.parser.SymbolParser;
import com.github.nicholasmoser.utils.ByteStream;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ExistingFunctionTest {
  @Test
  public void testNormal() throws Exception {
    String name = "Test 1";
    int offset = 0x100;
    int length = 0x70;
    ExistingFunction function = new ExistingFunction(name, offset, length);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.length()).isEqualTo(0x20);
    assertThat(function.dataOffset()).isEqualTo(offset);
    assertThat(function.functionLength()).isEqualTo(length);

    // Now parse the bytes and assert it is the same symbol
    ByteStream bs = new ByteStream(function.bytes());
    List<Symbol> symbols = SymbolParser.parse(bs, 1);
    assertThat(symbols.size()).isEqualTo(1);
    Symbol symbol = symbols.get(0);
    assertThat(symbol).isOfAnyClassIn(ExistingFunction.class);
    ExistingFunction actualExistingFunction = (ExistingFunction) symbol;
    assertThat(actualExistingFunction.length()).isEqualTo(function.length());
    assertThat(actualExistingFunction.name()).isEqualTo(function.name());
    assertThat(actualExistingFunction.bytes()).isEqualTo(function.bytes());
    assertThat(actualExistingFunction.dataOffset()).isEqualTo(function.dataOffset());
    assertThat(actualExistingFunction.functionLength()).isEqualTo(function.functionLength());
  }

  @Test
  public void test15ByteName() throws Exception {
    String name = "ABCDEFGHIJKLMNO";
    int offset = 0x100;
    int length = 0x70;
    ExistingFunction function = new ExistingFunction(name, offset, length);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.length()).isEqualTo(0x20);
    assertThat(function.dataOffset()).isEqualTo(offset);
    assertThat(function.functionLength()).isEqualTo(length);

    // Now parse the bytes and assert it is the same symbol
    ByteStream bs = new ByteStream(function.bytes());
    List<Symbol> symbols = SymbolParser.parse(bs, 1);
    assertThat(symbols.size()).isEqualTo(1);
    Symbol symbol = symbols.get(0);
    assertThat(symbol).isOfAnyClassIn(ExistingFunction.class);
    ExistingFunction actualExistingFunction = (ExistingFunction) symbol;
    assertThat(actualExistingFunction.length()).isEqualTo(function.length());
    assertThat(actualExistingFunction.name()).isEqualTo(function.name());
    assertThat(actualExistingFunction.bytes()).isEqualTo(function.bytes());
    assertThat(actualExistingFunction.dataOffset()).isEqualTo(function.dataOffset());
    assertThat(actualExistingFunction.functionLength()).isEqualTo(function.functionLength());
  }

  @Test
  public void test16ByteName() throws Exception {
    String name = "ABCDEFGHIJKLMNOP";
    int offset = 0x100;
    int length = 0x70;
    ExistingFunction function = new ExistingFunction(name, offset, length);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.length()).isEqualTo(0x30);
    assertThat(function.dataOffset()).isEqualTo(offset);
    assertThat(function.functionLength()).isEqualTo(length);

    // Now parse the bytes and assert it is the same symbol
    ByteStream bs = new ByteStream(function.bytes());
    List<Symbol> symbols = SymbolParser.parse(bs, 1);
    assertThat(symbols.size()).isEqualTo(1);
    Symbol symbol = symbols.get(0);
    assertThat(symbol).isOfAnyClassIn(ExistingFunction.class);
    ExistingFunction actualExistingFunction = (ExistingFunction) symbol;
    assertThat(actualExistingFunction.length()).isEqualTo(function.length());
    assertThat(actualExistingFunction.name()).isEqualTo(function.name());
    assertThat(actualExistingFunction.bytes()).isEqualTo(function.bytes());
    assertThat(actualExistingFunction.dataOffset()).isEqualTo(function.dataOffset());
    assertThat(actualExistingFunction.functionLength()).isEqualTo(function.functionLength());
  }
}
