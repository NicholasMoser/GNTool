package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.Seq;
import com.github.nicholasmoser.gnt4.seq.opcodes.FlagOperation;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.PlaySound;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class OpcodeGroup24 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x06 -> UnknownOpcode.of(0x8, bs);
      case 0x07 -> UnknownOpcode.of(0x4, bs);
      case 0x08 -> UnknownOpcode.of(0x4, bs);
      case 0x09 -> UnknownOpcode.of(0x8, bs);
      case 0x0A -> op_240A(bs);
      case 0x0B -> UnknownOpcode.of(0x4, bs);
      case 0x0C -> UnknownOpcode.of(0x8, bs);
      case 0x0D -> UnknownOpcode.of(0x8, bs);
      case 0x0E -> UnknownOpcode.of(0x8, bs);
      case 0x0F -> UnknownOpcode.of(0x8, bs);
      case 0x10 -> UnknownOpcode.of(0x4, bs);
      case 0x14 -> UnknownOpcode.of(0x8, bs);
      case 0x15 -> UnknownOpcode.of(0x8, bs);
      case 0x16 -> UnknownOpcode.of(0xC, bs);
      case 0x17 -> play_sound(bs);
      case 0x18 -> UnknownOpcode.of(0x8, bs);
      case 0x19 -> UnknownOpcode.of(0x8, bs);
      case 0x1A -> op_241A(bs);
      case 0x1B -> UnknownOpcode.of(0xC, bs);
      case 0x1C -> UnknownOpcode.of(0x4, bs);
      default -> throw new IOException(String.format("Unimplemented: %02X", opcodeByte));
    };
  }

  private static Opcode op_240A(ByteStream bs) throws IOException {
    int offset = bs.offset();
    int word = bs.peekWord();
    byte flag = (byte) (word >> 8 & 0xff);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    if (flag == 0x11) {
      byte[] bytes = bs.readBytes(8);
      baos.write(bytes);
    }
    byte[] bytes = bs.readBytes(8);
    baos.write(bytes);
    return new UnknownOpcode(offset, baos.toByteArray());
  }

  private static Opcode play_sound(ByteStream bs) throws IOException {
    int offset = bs.offset();
    int word = bs.readWord();
    int thirdByte = (word >> 0x8) & 0xFF;
    int lastByte = word & 0xFF;
    short operandOne = bs.readShort();
    short operandTwo = bs.readShort();
    boolean thisChr = lastByte == 0; // Otherwise, foe_chr_p
    if (!thisChr) {
      throw new IOException("Not yet supported");
    }
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.putInt(word).putShort(operandOne).putShort(operandTwo);
    switch(thirdByte) {
      case 0x0:
        String sound = String.format(" 0x%X", operandOne);
        if ((operandOne & 0xf00) == 0xb00) {
          sound += " plus offset";
        }
        return new PlaySound(offset, buffer.array(), "general" + sound);
      case 0x1:
        return new PlaySound(offset, buffer.array(), "hit");
      case 0x2:
      case 0x4:
      case 0x7:
      case 0xA:
      case 0xE:
      default:
        throw new IOException("Unknown, not yet supported: " + thirdByte);
      case 0x3:
        return new PlaySound(offset, buffer.array(), "unknown");
      case 0x5:
        return new PlaySound(offset, buffer.array(), "running");
      case 0x6:
        return new PlaySound(offset, buffer.array(), "unused");
      case 0x8:
        return new PlaySound(offset, buffer.array(), "soft_land");
      case 0x9:
        return new PlaySound(offset, buffer.array(), "hard_land");
      case 0xB:
        return new PlaySound(offset, buffer.array(), "grunt");
      case 0xC:
        return new PlaySound(offset, buffer.array(), "hiki/hiki2");
      case 0xD:
        return new PlaySound(offset, buffer.array(), "walking");
    }
  }

  private static Opcode op_241A(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] opcode = bs.readBytes(4);
    byte[] flagBytes = bs.readBytes(4);
    int flag = ByteUtils.toInt32(flagBytes);
    String text;
    switch (opcode[2]) {
      case 0x0 -> text = String.format("flags_set_af \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x1 -> text = String.format("flags_remove_af \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x2 -> text = String.format("flags_and_af \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x3 -> text = String.format("flags_add_af \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x4 -> text = String.format("flags_xor_af \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x5 -> text = String.format("flags_get_af \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x6 -> text = String.format("flags_set_nf \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0x7 -> text = String.format("flags_remove_nf \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0x8 -> text = String.format("flags_and_nf \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0x9 -> text = String.format("flags_add_nf \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0xa -> text = String.format("flags_xor_nf \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0xb -> text = String.format("flags_get_nf \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0xc -> text = String.format("flags_set_pf \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0xd -> text = String.format("flags_remove_pf \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0xe -> text = String.format("flags_and_pf \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0xf -> text = String.format("flags_add_pf \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0x10 -> text = String.format("flags_xor_pf \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0x11 -> text = String.format("flags_get_pf \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0x12 -> text = String.format("flags_set_kf \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x13 -> text = String.format("flags_remove_kf \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x14 -> text = String.format("flags_and_kf \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x15 -> text = String.format("flags_add_kf \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x16 -> text = String.format("flags_xor_kf \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x17 -> text = String.format("flags_get_kf \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x18 -> text = String.format("flags_set_df \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x19 -> text = String.format("flags_remove_df \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x1a -> text = String.format("flags_and_df \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x1b -> text = String.format("flags_add_df \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x1c -> text = String.format("flags_xor_df \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x1d -> text = String.format("flags_get_df \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x1e -> text = String.format("flags_set_ef \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x1f -> text = String.format("flags_remove_ef \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x20 -> text = String.format("flags_and_ef \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x21 -> text = String.format("flags_add_ef \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x22 -> text = String.format("flags_xor_ef \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x23 -> text = String.format("flags_get_ef \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x24 -> text = String.format("flags_set_mf \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x25 -> text = String.format("flags_remove_mf \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x26 -> text = String.format("flags_and_mf \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x27 -> text = String.format("flags_add_mf \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x28 -> text = String.format("flags_xor_mf \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x29 -> text = String.format("flags_get_mf \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x2a -> text = String.format("flags_set_rf \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x2b -> text = String.format("flags_remove_rf \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x2c -> text = String.format("flags_and_rf \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x2d -> text = String.format("flags_add_rf \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x2e -> text = String.format("flags_xor_rf \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x2f -> text = String.format("flags_get_rf \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x30 -> text = String.format("flags_set_sf \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x31 -> text = String.format("flags_remove_sf \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x32 -> text = String.format("flags_and_sf \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x33 -> text = String.format("flags_add_sf \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x34 -> text = String.format("flags_xor_sf \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x35 -> text = String.format("flags_get_sf \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x36 -> text = String.format("flags_set_unknown 0x%08X", flag);
      case 0x37 -> text = String.format("flags_remove_unknown 0x%08X", flag);
      case 0x38 -> text = String.format("flags_and_unknown 0x%08X", flag);
      case 0x39 -> text = String.format("flags_add_unknown 0x%08X", flag);
      case 0x3a -> text = String.format("flags_xor_unknown 0x%08X", flag);
      case 0x3b -> text = String.format("flags_get_unknown 0x%08X", flag);
      case 0x3c -> text = String.format("flags_set_cf \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x3d -> text = String.format("flags_remove_cf \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x3e -> text = String.format("flags_and_cf \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x3f -> text = String.format("flags_add_cf \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x40 -> text = String.format("flags_xor_cf \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x41 -> text = String.format("flags_get_cf \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x42 -> text = String.format("flags_set_chr_mod \"%s\"",
              getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x43 -> text = String.format("flags_remove_chr_mod \"%s\"",
              getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x44 -> text = String.format("flags_and_chr_mod \"%s\"",
              getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x45 -> text = String.format("flags_add_chr_mod \"%s\"",
              getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x46 -> text = String.format("flags_xor_chr_mod \"%s\"",
              getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x47 -> text = String.format("flags_get_chr_mod \"%s\"",
              getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x48 -> text = String.format("flags_set_k2f \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x49 -> text = String.format("flags_remove_k2f \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x4a -> text = String.format("flags_and_k2f \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x4b -> text = String.format("flags_add_k2f \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x4c -> text = String.format("flags_xor_k2f \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x4d -> text = String.format("flags_get_k2f \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x4e -> text = String.format("flags_set_d2f \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x4f -> text = String.format("flags_remove_d2f \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x50 -> text = String.format("flags_and_d2f \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x51 -> text = String.format("flags_add_d2f \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x52 -> text = String.format("flags_xor_d2f \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x53 -> text = String.format("flags_get_d2f \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x54 -> text = String.format("flags_set_n2f \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
      case 0x55 -> text = String.format("flags_remove_n2f \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
      case 0x56 -> text = String.format("flags_and_n2f \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
      case 0x57 -> text = String.format("flags_add_n2f \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
      case 0x58 -> text = String.format("flags_xor_n2f \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
      case 0x59 -> text = String.format("flags_get_n2f \"%s\"", getFlags(flag, Seq.N2F_FLAGS));

      default -> throw new IOException(
          "Opcode parameter not supported: " + Arrays.toString(opcode));
    }
    return new FlagOperation(offset, Bytes.concat(opcode, flagBytes), text);
  }

  private static String getFlags(int flag, Map<Integer, String> flagValues) {
    List<String> flags = new ArrayList<>();
    for (Entry<Integer, String> entry : flagValues.entrySet()) {
      if ((entry.getKey() & flag) != 0) {
        flags.add(entry.getValue());
      }
    }
    Collections.sort(flags);
    return String.join(", ", flags);
  }
}