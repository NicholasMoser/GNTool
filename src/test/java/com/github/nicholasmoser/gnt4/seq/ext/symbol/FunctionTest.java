package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntAdd;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class FunctionTest {
  @Test
  public void testNormal() {
    String name = "Test 1";
    List<Opcode> opcodes = new ArrayList<>();
    opcodes.add(new BranchLinkReturn(0x10));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    Function function = new Function(name, opcodes, innerLabels);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.dataOffset()).isEqualTo(0x20);
    assertThat(function.length()).isEqualTo(0x30);
    assertThat(function.innerLabels()).isEmpty();
  }

  @Test
  public void test15ByteName() {
    String name = "ABCDEFGHIJKLMNO";
    List<Opcode> opcodes = new ArrayList<>();
    opcodes.add(new BranchLinkReturn(0x10));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    Function function = new Function(name, opcodes, innerLabels);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.dataOffset()).isEqualTo(0x20);
    assertThat(function.length()).isEqualTo(0x30);
    assertThat(function.innerLabels()).isEmpty();
  }

  @Test
  public void test16ByteName() {
    String name = "ABCDEFGHIJKLMNOP";
    List<Opcode> opcodes = new ArrayList<>();
    opcodes.add(new BranchLinkReturn(0x10));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    Function function = new Function(name, opcodes, innerLabels);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.dataOffset()).isEqualTo(0x30);
    assertThat(function.length()).isEqualTo(0x40);
    assertThat(function.innerLabels()).isEmpty();
  }

  @Test
  public void testMultipleOpcodes() {
    String name = "Test 1";
    List<Opcode> opcodes = new ArrayList<>();
    opcodes.add(new IntAdd(0x10, new byte[] {0x04, 0x07, 0x07, 0x3F, 0x00, 0x00, 0x00, 0x01}, "gpr7, 0x1"));
    opcodes.add(new UnknownOpcode(0x18, new byte[] {0x24, 0x14, 0x01, 0x0B, 0x00, 0x00, (byte) 0xF8, 0x00}));
    opcodes.add(new BranchLinkReturn(0x20));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    Function function = new Function(name, opcodes, innerLabels);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.dataOffset()).isEqualTo(0x20);
    assertThat(function.length()).isEqualTo(0x40);
    assertThat(function.opcodesByteLength()).isEqualTo(0x14);
    assertThat(function.innerLabels()).isEmpty();
  }

  @Test
  public void testInnerLabels() {
    String name = "Test 1";
    List<Opcode> opcodes = new ArrayList<>();
    opcodes.add(new BranchLinkReturn(0x10));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    innerLabels.put("ABCDEFGHIJK", 0x10);
    innerLabels.put("ABCDEFGHIJKL", 0x100);
    innerLabels.put("ABCDEFGHIJKLM", 0x4);
    innerLabels.put("ABCDEFGHIJKLMNOPQRSTUVWXYZ!?.", 0x80000);
    Function function = new Function(name, opcodes, innerLabels);
    assertThat(function.name()).isEqualTo(name);
    assertThat(function.dataOffset()).isEqualTo(0x20);
    assertThat(function.length()).isEqualTo(0xB0);
    assertThat(function.innerLabels()).isEqualTo(innerLabels);
  }
}
