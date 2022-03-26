package com.github.nicholasmoser.gnt4.seq.groups;

import com.github.nicholasmoser.gnt4.seq.Seq;
import com.github.nicholasmoser.gnt4.seq.opcodes.FlagOperation;
import com.github.nicholasmoser.gnt4.seq.opcodes.Opcode;
import com.github.nicholasmoser.gnt4.seq.opcodes.UnknownOpcode;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
      case 0x17 -> UnknownOpcode.of(0x8, bs);
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

  private static Opcode op_241A(ByteStream bs) throws IOException {
    int offset = bs.offset();
    byte[] opcode = bs.readBytes(4);
    byte[] flagBytes = bs.readBytes(4);
    int flag = ByteUtils.toInt32(flagBytes);
    String text;
    switch (opcode[2]) {
      case 0x0 -> text = String.format("set_af_flags \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x1 -> text = String.format("remove_af_flags \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x2 -> text = String.format("and_af_flags \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x3 -> text = String.format("add_af_flags \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x4 -> text = String.format("xor_af_flags \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x5 -> text = String.format("get_af_flags \"%s\"", getFlags(flag, Seq.AF_FLAGS));
      case 0x6 -> text = String.format("set_nf_flags \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0x7 -> text = String.format("remove_nf_flags \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0x8 -> text = String.format("and_nf_flags \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0x9 -> text = String.format("add_nf_flags \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0xa -> text = String.format("xor_nf_flags \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0xb -> text = String.format("get_nf_flags \"%s\"", getFlags(flag, Seq.NF_FLAGS));
      case 0xc -> text = String.format("set_pf_flags \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0xd -> text = String.format("remove_pf_flags \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0xe -> text = String.format("and_pf_flags \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0xf -> text = String.format("add_pf_flags \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0x10 -> text = String.format("xor_pf_flags \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0x11 -> text = String.format("get_pf_flags \"%s\"", getFlags(flag, Seq.PF_FLAGS));
      case 0x12 -> text = String.format("set_kf_flags \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x13 -> text = String.format("remove_kf_flags \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x14 -> text = String.format("and_kf_flags \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x15 -> text = String.format("add_kf_flags \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x16 -> text = String.format("xor_kf_flags \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x17 -> text = String.format("get_kf_flags \"%s\"", getFlags(flag, Seq.KF_FLAGS));
      case 0x18 -> text = String.format("set_df_flags \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x19 -> text = String.format("remove_df_flags \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x1a -> text = String.format("and_df_flags \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x1b -> text = String.format("add_df_flags \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x1c -> text = String.format("xor_df_flags \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x1d -> text = String.format("get_df_flags \"%s\"", getFlags(flag, Seq.DF_FLAGS));
      case 0x1e -> text = String.format("set_ef_flags \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x1f -> text = String.format("remove_ef_flags \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x20 -> text = String.format("and_ef_flags \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x21 -> text = String.format("add_ef_flags \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x22 -> text = String.format("xor_ef_flags \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x23 -> text = String.format("get_ef_flags \"%s\"", getFlags(flag, Seq.EF_FLAGS));
      case 0x24 -> text = String.format("set_mf_flags \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x25 -> text = String.format("remove_mf_flags \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x26 -> text = String.format("and_mf_flags \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x27 -> text = String.format("add_mf_flags \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x28 -> text = String.format("xor_mf_flags \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x29 -> text = String.format("get_mf_flags \"%s\"", getFlags(flag, Seq.MF_FLAGS));
      case 0x2a -> text = String.format("set_rf_flags \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x2b -> text = String.format("remove_rf_flags \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x2c -> text = String.format("and_rf_flags \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x2d -> text = String.format("add_rf_flags \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x2e -> text = String.format("xor_rf_flags \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x2f -> text = String.format("get_rf_flags \"%s\"", getFlags(flag, Seq.RF_FLAGS));
      case 0x30 -> text = String.format("set_sf_flags \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x31 -> text = String.format("remove_sf_flags \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x32 -> text = String.format("and_sf_flags \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x33 -> text = String.format("add_sf_flags \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x34 -> text = String.format("xor_sf_flags \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x35 -> text = String.format("get_sf_flags \"%s\"", getFlags(flag, Seq.SF_FLAGS));
      case 0x36 -> text = String.format("set_unknown_flags 0x%08X", flag);
      case 0x37 -> text = String.format("remove_unknown_flags 0x%08X", flag);
      case 0x38 -> text = String.format("and_unknown_flags 0x%08X", flag);
      case 0x39 -> text = String.format("add_unknown_flags 0x%08X", flag);
      case 0x3a -> text = String.format("xor_unknown_flags 0x%08X", flag);
      case 0x3b -> text = String.format("get_unknown_flags 0x%08X", flag);
      case 0x3c -> text = String.format("set_cf_flags \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x3d -> text = String.format("remove_cf_flags \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x3e -> text = String.format("and_cf_flags \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x3f -> text = String.format("add_cf_flags \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x40 -> text = String.format("xor_cf_flags \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x41 -> text = String.format("get_cf_flags \"%s\"", getFlags(flag, Seq.CF_FLAGS));
      case 0x42 -> text = String.format("set_chr_mod_flags \"%s\"",
          getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x43 -> text = String.format("remove_chr_mod_flags \"%s\"",
          getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x44 -> text = String.format("and_chr_mod_flags \"%s\"",
          getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x45 -> text = String.format("add_chr_mod_flags \"%s\"",
          getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x46 -> text = String.format("xor_chr_mod_flags \"%s\"",
          getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x47 -> text = String.format("get_chr_mod_flags \"%s\"",
          getFlags(flag, Seq.CHR_MOD_FLAGS));
      case 0x48 -> text = String.format("set_k2f_flags \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x49 -> text = String.format("remove_k2f_flags \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x4a -> text = String.format("and_k2f_flags \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x4b -> text = String.format("add_k2f_flags \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x4c -> text = String.format("xor_k2f_flags \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x4d -> text = String.format("get_k2f_flags \"%s\"", getFlags(flag, Seq.K2F_FLAGS));
      case 0x4e -> text = String.format("set_d2f_flags \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x4f -> text = String.format("remove_d2f_flags \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x50 -> text = String.format("and_d2f_flags \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x51 -> text = String.format("add_d2f_flags \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x52 -> text = String.format("xor_d2f_flags \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x53 -> text = String.format("get_d2f_flags \"%s\"", getFlags(flag, Seq.D2F_FLAGS));
      case 0x54 -> text = String.format("set_n2f_flags \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
      case 0x55 -> text = String.format("remove_n2f_flags \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
      case 0x56 -> text = String.format("and_n2f_flags \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
      case 0x57 -> text = String.format("add_n2f_flags \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
      case 0x58 -> text = String.format("xor_n2f_flags \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
      case 0x59 -> text = String.format("get_n2f_flags \"%s\"", getFlags(flag, Seq.N2F_FLAGS));
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