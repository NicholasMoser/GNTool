package com.github.nicholasmoser.gnt4.seq.operands;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;

public class OperandParser {
  private static Map<String, Byte> REGISTERS = null;

  public static Byte getByte(String operand) {
    if (REGISTERS == null) {
      REGISTERS = Maps.newHashMapWithExpectedSize(71);
      REGISTERS.put("gpr0", (byte) 0x0);
      REGISTERS.put("gpr1", (byte) 0x1);
      REGISTERS.put("gpr2", (byte) 0x2);
      REGISTERS.put("gpr3", (byte) 0x3);
      REGISTERS.put("gpr4", (byte) 0x4);
      REGISTERS.put("gpr5", (byte) 0x5);
      REGISTERS.put("gpr6", (byte) 0x6);
      REGISTERS.put("gpr7", (byte) 0x7);
      REGISTERS.put("gpr8", (byte) 0x8);
      REGISTERS.put("gpr9", (byte) 0x9);
      REGISTERS.put("gpr10", (byte) 0xA);
      REGISTERS.put("gpr11", (byte) 0xB);
      REGISTERS.put("gpr12", (byte) 0xC);
      REGISTERS.put("gpr13", (byte) 0xD);
      REGISTERS.put("gpr14", (byte) 0xE);
      REGISTERS.put("gpr15", (byte) 0xF);
      REGISTERS.put("gpr16", (byte) 0x10);
      REGISTERS.put("gpr17", (byte) 0x11);
      REGISTERS.put("gpr18", (byte) 0x12);
      REGISTERS.put("gpr19", (byte) 0x13);
      REGISTERS.put("cr_companion", (byte) 0x13);
      REGISTERS.put("gpr20", (byte) 0x14);
      REGISTERS.put("ctr", (byte) 0x14);
      REGISTERS.put("gpr21", (byte) 0x15);
      REGISTERS.put("cr", (byte) 0x15);
      REGISTERS.put("gpr22", (byte) 0x16);
      REGISTERS.put("stored_pc", (byte) 0x16);
      REGISTERS.put("gpr23", (byte) 0x17);
      REGISTERS.put("sp", (byte) 0x17);
      REGISTERS.put("seq_p_sp0", (byte) 0x18);
      REGISTERS.put("seq_p_sp1", (byte) 0x19);
      REGISTERS.put("seq_p_sp2", (byte) 0x1A);
      REGISTERS.put("seq_p_sp3", (byte) 0x1B);
      REGISTERS.put("seq_p_sp4", (byte) 0x1C);
      REGISTERS.put("seq_p_sp5", (byte) 0x1D);
      REGISTERS.put("final_instruction_seq", (byte) 0x1D);
      REGISTERS.put("seq_p_sp6", (byte) 0x1E);
      REGISTERS.put("seq_p_sp7", (byte) 0x1F);
      REGISTERS.put("seq_p_sp8", (byte) 0x20);
      REGISTERS.put("mot", (byte) 0x20);
      REGISTERS.put("animation", (byte) 0x20);
      REGISTERS.put("seq_p_sp9", (byte) 0x21);
      REGISTERS.put("movement", (byte) 0x21);
      REGISTERS.put("seq_p_sp10", (byte) 0x22);
      REGISTERS.put("chr_p_field_0x4C", (byte) 0x22);
      REGISTERS.put("seq_p_sp11", (byte) 0x23);
      REGISTERS.put("seq_p_sp12", (byte) 0x24);
      REGISTERS.put("seq_p_sp13", (byte) 0x25);
      REGISTERS.put("seq_p_sp14", (byte) 0x26);
      REGISTERS.put("chr_p", (byte) 0x26);
      REGISTERS.put("seq_p_sp15", (byte) 0x27);
      REGISTERS.put("foe_chr_p", (byte) 0x27);
      REGISTERS.put("seq_p_sp16", (byte) 0x28);
      REGISTERS.put("seq_p_sp17", (byte) 0x29);
      REGISTERS.put("seq_p_sp18", (byte) 0x2A);
      REGISTERS.put("seq_p_sp19", (byte) 0x2B);
      REGISTERS.put("seq_p_sp", (byte) 0x2B);
      REGISTERS.put("seq_p_sp20", (byte) 0x2C);
      REGISTERS.put("seq_p_sp21", (byte) 0x2D);
      REGISTERS.put("seq_p_sp22", (byte) 0x2E);
      REGISTERS.put("seq_p_sp23", (byte) 0x2F);
      REGISTERS.put("seq_file", (byte) 0x2F);
      REGISTERS.put("hitbox_identity_matrix", (byte) 0x30);
      REGISTERS.put("controllers", (byte) 0x32);
      REGISTERS.put("primary_controller", (byte) 0x33);
      REGISTERS.put("display", (byte) 0x34);
      REGISTERS.put("save_data", (byte) 0x39);
      REGISTERS.put("debug_mode", (byte) 0x3A);
      REGISTERS.put("pause_game", (byte) 0x3B);
      REGISTERS.put("game_info", (byte) 0x3C);
      REGISTERS.put("unused", (byte) 0x3D);
    }
    return REGISTERS.get(operand.toLowerCase());
  }
}
