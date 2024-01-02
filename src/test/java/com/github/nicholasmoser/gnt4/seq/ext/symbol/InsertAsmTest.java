package com.github.nicholasmoser.gnt4.seq.ext.symbol;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.gnt4.seq.opcodes.BranchLinkReturn;
import com.github.nicholasmoser.gnt4.seq.opcodes.IntAdd;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class InsertAsmTest {
  @Test
  public void testNormal() {
    String name = "Test 1";
    int offset = 0x60;
    List<Opcode> oldOpcodes = new ArrayList<>();
    oldOpcodes.add(new IntAdd(0x10, new byte[] {0x04, 0x07, 0x07, 0x3F, 0x00, 0x00, 0x00, 0x01}, "gpr7, 0x1"));
    List<Opcode> newOpcodes = new ArrayList<>();
    newOpcodes.add(new BranchLinkReturn(0x10));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    InsertAsm insertAsm = new InsertAsm(name, offset, newOpcodes, oldOpcodes, innerLabels);
    assertThat(insertAsm.name()).isEqualTo(name);
    assertThat(insertAsm.dataOffset()).isEqualTo(0x30);
    assertThat(insertAsm.length()).isEqualTo(0x50);
    assertThat(insertAsm.oldOpcodes()).isEqualTo(oldOpcodes);
    assertThat(insertAsm.newOpcodes()).isEqualTo(newOpcodes);
    assertThat(insertAsm.innerLabels()).isEmpty();
  }

  @Test
  public void test15ByteName() {
    String name = "ABCDEFGHIJKLMNO";
    int offset = 0x60;
    List<Opcode> oldOpcodes = new ArrayList<>();
    oldOpcodes.add(new IntAdd(0x10, new byte[] {0x04, 0x07, 0x07, 0x3F, 0x00, 0x00, 0x00, 0x01}, "gpr7, 0x1"));
    List<Opcode> newOpcodes = new ArrayList<>();
    newOpcodes.add(new BranchLinkReturn(0x10));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    InsertAsm insertAsm = new InsertAsm(name, offset, newOpcodes, oldOpcodes, innerLabels);
    assertThat(insertAsm.name()).isEqualTo(name);
    assertThat(insertAsm.dataOffset()).isEqualTo(0x30);
    assertThat(insertAsm.length()).isEqualTo(0x50);
    assertThat(insertAsm.oldOpcodes()).isEqualTo(oldOpcodes);
    assertThat(insertAsm.newOpcodes()).isEqualTo(newOpcodes);
    assertThat(insertAsm.innerLabels()).isEmpty();
  }

  @Test
  public void test16ByteName() {
    String name = "ABCDEFGHIJKLMNOP";
    int offset = 0x60;
    List<Opcode> oldOpcodes = new ArrayList<>();
    oldOpcodes.add(new IntAdd(0x10, new byte[] {0x04, 0x07, 0x07, 0x3F, 0x00, 0x00, 0x00, 0x01}, "gpr7, 0x1"));
    List<Opcode> newOpcodes = new ArrayList<>();
    newOpcodes.add(new BranchLinkReturn(0x10));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    InsertAsm insertAsm = new InsertAsm(name, offset, newOpcodes, oldOpcodes, innerLabels);
    assertThat(insertAsm.name()).isEqualTo(name);
    assertThat(insertAsm.dataOffset()).isEqualTo(0x40);
    assertThat(insertAsm.length()).isEqualTo(0x60);
    assertThat(insertAsm.oldOpcodes()).isEqualTo(oldOpcodes);
    assertThat(insertAsm.newOpcodes()).isEqualTo(newOpcodes);
    assertThat(insertAsm.innerLabels()).isEmpty();
  }

  @Test
  public void testMultipleOpcodes() {
    String name = "Test 1";
    int offset = 0x60;
    List<Opcode> oldOpcodes = new ArrayList<>();
    oldOpcodes.add(new IntAdd(0x10, new byte[] {0x04, 0x07, 0x07, 0x3F, 0x00, 0x00, 0x00, 0x01}, "gpr7, 0x1"));
    oldOpcodes.add(new UnknownOpcode(0x18, new byte[] {0x24, 0x14, 0x01, 0x0B, 0x00, 0x00, (byte) 0xF8, 0x00}));
    oldOpcodes.add(new BranchLinkReturn(0x20));
    List<Opcode> newOpcodes = new ArrayList<>();
    newOpcodes.add(new IntAdd(0x10, new byte[] {0x04, 0x07, 0x07, 0x3F, 0x00, 0x00, 0x00, 0x02}, "gpr7, 0x2"));
    newOpcodes.add(new BranchLinkReturn(0x10));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    InsertAsm insertAsm = new InsertAsm(name, offset, newOpcodes, oldOpcodes, innerLabels);
    assertThat(insertAsm.name()).isEqualTo(name);
    assertThat(insertAsm.dataOffset()).isEqualTo(0x30);
    assertThat(insertAsm.length()).isEqualTo(0x60);
    assertThat(insertAsm.oldOpcodes()).isEqualTo(oldOpcodes);
    assertThat(insertAsm.newOpcodes()).isEqualTo(newOpcodes);
    assertThat(insertAsm.innerLabels()).isEmpty();
  }

  @Test
  public void testOldBytesAreNotValidOpcodes() {
    String name = "Test 1";
    int offset = 0x60;
    byte[] oldBytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
    List<Opcode> newOpcodes = new ArrayList<>();
    newOpcodes.add(new BranchLinkReturn(0x10));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    InsertAsm insertAsm = new InsertAsm(name, offset, newOpcodes, oldBytes, innerLabels);
    assertThat(insertAsm.name()).isEqualTo(name);
    assertThat(insertAsm.dataOffset()).isEqualTo(0x30);
    assertThat(insertAsm.length()).isEqualTo(0x50);
    assertThat(insertAsm.oldOpcodes()).isNull();
    assertThat(insertAsm.newOpcodes()).isEqualTo(newOpcodes);
    assertThat(insertAsm.innerLabels()).isEmpty();
  }

  @Test
  public void testInnerLabels() {
    String name = "Test 1";
    int offset = 0x60;
    List<Opcode> oldOpcodes = new ArrayList<>();
    oldOpcodes.add(new IntAdd(0x10, new byte[] {0x04, 0x07, 0x07, 0x3F, 0x00, 0x00, 0x00, 0x01}, "gpr7, 0x1"));
    List<Opcode> newOpcodes = new ArrayList<>();
    newOpcodes.add(new BranchLinkReturn(0x10));
    Map<String, Integer> innerLabels = new LinkedHashMap<>();
    innerLabels.put("ABCDEFGHIJK", 0x10);
    innerLabels.put("ABCDEFGHIJKL", 0x100);
    innerLabels.put("ABCDEFGHIJKLM", 0x4);
    innerLabels.put("ABCDEFGHIJKLMNOPQRSTUVWXYZ!?.", 0x80000);
    InsertAsm insertAsm = new InsertAsm(name, offset, newOpcodes, oldOpcodes, innerLabels);
    assertThat(insertAsm.name()).isEqualTo(name);
    assertThat(insertAsm.dataOffset()).isEqualTo(0x30);
    assertThat(insertAsm.length()).isEqualTo(0xD0);
    assertThat(insertAsm.oldOpcodes()).isEqualTo(oldOpcodes);
    assertThat(insertAsm.newOpcodes()).isEqualTo(newOpcodes);
    assertThat(insertAsm.innerLabels()).isEqualTo(innerLabels);
  }
}
