package com.github.nicholasmoser.gnt4.seq.operands;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.HashMap;
import java.util.Map;

public class OperandParser {

  private static BiMap<String, Byte> REGISTERS = null;
  private static Map<String, String> ALT_NAMES = null;

  public static Byte getByte(String operand) {
    if (REGISTERS == null) {
      init();
    }
    String altName = ALT_NAMES.get(operand);
    if (altName != null) {
      operand = altName;
    }
    return REGISTERS.get(operand.toLowerCase());
  }

  public static String GetOperand(Byte lookupByte) {
    if (REGISTERS == null) {
      init();
    }
    return REGISTERS.inverse().get(lookupByte);
  }

  private static void init() {
    ALT_NAMES = new HashMap<>();
    ALT_NAMES.put("gpr19", "cr_companion");
    ALT_NAMES.put("gpr20", "ctr");
    ALT_NAMES.put("gpr21", "cr");
    ALT_NAMES.put("gpr22", "stored_pc");
    ALT_NAMES.put("gpr23", "sp");
    ALT_NAMES.put("seq_p_sp5", "final_instruction_seq");
    ALT_NAMES.put("seq_p_sp8", "mot");
    ALT_NAMES.put("animation", "mot");
    ALT_NAMES.put("seq_p_sp9", "movement");
    ALT_NAMES.put("chr_p_field_0x4C", "seq_p_sp10");
    ALT_NAMES.put("seq_p_sp14", "chr_p");
    ALT_NAMES.put("seq_p_sp15", "foe_chr_p");
    ALT_NAMES.put("seq_p_sp19", "seq_p_sp");
    ALT_NAMES.put("seq_p_sp23", "seq_file");
    REGISTERS = new ImmutableBiMap.Builder<String, Byte>()
        .put("gpr0", (byte) 0x0)
        .put("gpr1", (byte) 0x1)
        .put("gpr2", (byte) 0x2)
        .put("gpr3", (byte) 0x3)
        .put("gpr4", (byte) 0x4)
        .put("gpr5", (byte) 0x5)
        .put("gpr6", (byte) 0x6)
        .put("gpr7", (byte) 0x7)
        .put("gpr8", (byte) 0x8)
        .put("gpr9", (byte) 0x9)
        .put("gpr10", (byte) 0xA)
        .put("gpr11", (byte) 0xB)
        .put("gpr12", (byte) 0xC)
        .put("gpr13", (byte) 0xD)
        .put("gpr14", (byte) 0xE)
        .put("gpr15", (byte) 0xF)
        .put("gpr16", (byte) 0x10)
        .put("gpr17", (byte) 0x11)
        .put("gpr18", (byte) 0x12)
        //.put("gpr19", (byte) 0x13)
        .put("cr_companion", (byte) 0x13)
        //.put("gpr20", (byte) 0x14)
        .put("ctr", (byte) 0x14)
        //.put("gpr21", (byte) 0x15)
        .put("cr", (byte) 0x15)
        //.put("gpr22", (byte) 0x16)
        .put("stored_pc", (byte) 0x16)
        //.put("gpr23", (byte) 0x17)
        .put("sp", (byte) 0x17)
        .put("seq_p_sp0", (byte) 0x18)
        .put("seq_p_sp1", (byte) 0x19)
        .put("seq_p_sp2", (byte) 0x1A)
        .put("seq_p_sp3", (byte) 0x1B)
        .put("seq_p_sp4", (byte) 0x1C)
        //.put("seq_p_sp5", (byte) 0x1D)
        .put("final_instruction_seq", (byte) 0x1D)
        .put("seq_p_sp6", (byte) 0x1E)
        .put("seq_p_sp7", (byte) 0x1F)
        //.put("seq_p_sp8", (byte) 0x20)
        .put("mot", (byte) 0x20)
        //.put("animation", (byte) 0x20)
        //.put("seq_p_sp9", (byte) 0x21)
        .put("movement", (byte) 0x21)
        .put("seq_p_sp10", (byte) 0x22)
        //.put("chr_p_field_0x4C", (byte) 0x22)
        .put("seq_p_sp11", (byte) 0x23)
        .put("seq_p_sp12", (byte) 0x24)
        .put("seq_p_sp13", (byte) 0x25)
        //.put("seq_p_sp14", (byte) 0x26)
        .put("chr_p", (byte) 0x26)
        //.put("seq_p_sp15", (byte) 0x27)
        .put("foe_chr_p", (byte) 0x27)
        .put("seq_p_sp16", (byte) 0x28)
        .put("seq_p_sp17", (byte) 0x29)
        .put("seq_p_sp18", (byte) 0x2A)
        //.put("seq_p_sp19", (byte) 0x2B)
        .put("seq_p_sp", (byte) 0x2B)
        .put("seq_p_sp20", (byte) 0x2C)
        .put("seq_p_sp21", (byte) 0x2D)
        .put("seq_p_sp22", (byte) 0x2E)
        //.put("seq_p_sp23", (byte) 0x2F)
        .put("seq_file", (byte) 0x2F)
        .put("hitbox_identity_matrix", (byte) 0x30)
        .put("controllers", (byte) 0x32)
        .put("primary_controller", (byte) 0x33)
        .put("display", (byte) 0x34)
        .put("save_data", (byte) 0x39)
        .put("debug_mode", (byte) 0x3A)
        .put("pause_game", (byte) 0x3B)
        .put("game_info", (byte) 0x3C)
        .put("unused", (byte) 0x3D)
        .build();
  }
}
