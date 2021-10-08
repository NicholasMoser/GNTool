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
import java.util.List;
import java.util.Map.Entry;

public class OpcodeGroup24 {

  public static Opcode parse(ByteStream bs, byte opcodeByte) throws IOException {
    return switch (opcodeByte) {
      case 0x06 -> UnknownOpcode.of(0x24, 0x06, 0x8, bs);
      case 0x07 -> UnknownOpcode.of(0x24, 0x07, 0x4, bs);
      case 0x08 -> UnknownOpcode.of(0x24, 0x08, 0x4, bs);
      case 0x09 -> UnknownOpcode.of(0x24, 0x09, 0x8, bs);
      case 0x0A -> op_240A(bs);
      case 0x0B -> UnknownOpcode.of(0x24, 0x0B, 0x4, bs);
      case 0x0C -> UnknownOpcode.of(0x24, 0x0C, 0x8, bs);
      case 0x0D -> UnknownOpcode.of(0x24, 0x0D, 0x8, bs);
      case 0x0E -> UnknownOpcode.of(0x24, 0x0E, 0x8, bs);
      case 0x0F -> UnknownOpcode.of(0x24, 0x0F, 0x8, bs);
      case 0x10 -> UnknownOpcode.of(0x24, 0x10, 0x4, bs);
      case 0x14 -> UnknownOpcode.of(0x24, 0x14, 0x8, bs);
      case 0x15 -> UnknownOpcode.of(0x24, 0x15, 0x8, bs);
      case 0x17 -> UnknownOpcode.of(0x24, 0x17, 0x8, bs);
      case 0x18 -> UnknownOpcode.of(0x24, 0x18, 0x8, bs);
      case 0x19 -> UnknownOpcode.of(0x24, 0x19, 0x8, bs);
      case 0x1A -> op_241A(bs);
      case 0x1B -> UnknownOpcode.of(0x24, 0x1B, 0xC, bs);
      case 0x1C -> UnknownOpcode.of(0x24, 0x1C, 0x4, bs);
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
    String description;
    switch (opcode[2]) {
      case 0x0 -> description = String.format("set_af_flags \"%s\"", getAFFlags(flag));
      case 0x1 -> description = String.format("remove_af_flags \"%s\"", getAFFlags(flag));
      case 0x2 -> description = String.format("and_af_flags \"%s\"", getAFFlags(flag));
      case 0x3 -> description = String.format("add_af_flags \"%s\"", getAFFlags(flag));
      case 0x4 -> description = String.format("xor_af_flags \"%s\"", getAFFlags(flag));
      case 0x5 -> description = String.format("get_af_flags \"%s\"", getAFFlags(flag));
      case 0x6 -> description = String.format("set_nf_flags \"%s\"", getNFFlags(flag));
      case 0x7 -> description = String.format("remove_nf_flags \"%s\"", getNFFlags(flag));
      case 0x8 -> description = String.format("and_nf_flags \"%s\"", getNFFlags(flag));
      case 0x9 -> description = String.format("add_nf_flags \"%s\"", getNFFlags(flag));
      case 0xa -> description = String.format("xor_nf_flags \"%s\"", getNFFlags(flag));
      case 0xb -> description = String.format("get_nf_flags \"%s\"", getNFFlags(flag));
      case 0xc -> description = String.format("set_pf_flags \"%s\"", getPFFlags(flag));
      case 0xd -> description = String.format("remove_pf_flags \"%s\"", getPFFlags(flag));
      case 0xe -> description = String.format("and_pf_flags \"%s\"", getPFFlags(flag));
      case 0xf -> description = String.format("add_pf_flags \"%s\"", getPFFlags(flag));
      case 0x10 -> description = String.format("xor_pf_flags \"%s\"", getPFFlags(flag));
      case 0x11 -> description = String.format("get_pf_flags \"%s\"", getPFFlags(flag));
      case 0x12 -> description = String.format("set_kf_flags \"%s\"", getKFFlags(flag));
      case 0x13 -> description = String.format("remove_kf_flags \"%s\"", getKFFlags(flag));
      case 0x14 -> description = String.format("and_kf_flags \"%s\"", getKFFlags(flag));
      case 0x15 -> description = String.format("add_kf_flags \"%s\"", getKFFlags(flag));
      case 0x16 -> description = String.format("xor_kf_flags \"%s\"", getKFFlags(flag));
      case 0x17 -> description = String.format("get_kf_flags \"%s\"", getKFFlags(flag));
      case 0x18 -> description = String.format("set_df_flags \"%s\"", getDFFlags(flag));
      case 0x19 -> description = String.format("remove_df_flags \"%s\"", getDFFlags(flag));
      case 0x1a -> description = String.format("and_df_flags \"%s\"", getDFFlags(flag));
      case 0x1b -> description = String.format("add_df_flags \"%s\"", getDFFlags(flag));
      case 0x1c -> description = String.format("xor_df_flags \"%s\"", getDFFlags(flag));
      case 0x1d -> description = String.format("get_df_flags \"%s\"", getDFFlags(flag));
      case 0x1e -> description = "0x1e";
      case 0x1f -> description = "0x1f";
      case 0x20 -> description = "0x20";
      case 0x21 -> description = "0x21";
      case 0x22 -> description = "0x22";
      case 0x23 -> description = "0x23";
      case 0x24 -> description = "0x24";
      case 0x25 -> description = "0x25";
      case 0x26 -> description = "0x26";
      case 0x27 -> description = "0x27";
      case 0x28 -> description = "0x28";
      case 0x29 -> description = "0x29";
      case 0x2a -> description = "0x2a";
      case 0x2b -> description = "0x2b";
      case 0x2c -> description = "0x2c";
      case 0x2d -> description = "0x2d";
      case 0x2e -> description = "0x2e";
      case 0x2f -> description = "0x2f";
      case 0x30 -> description = "0x30";
      case 0x31 -> description = "0x31";
      case 0x32 -> description = "0x32";
      case 0x33 -> description = "0x33";
      case 0x34 -> description = "0x34";
      case 0x35 -> description = "0x35";
      case 0x36 -> description = "0x36";
      case 0x37 -> description = "0x37";
      case 0x38 -> description = "0x38";
      case 0x39 -> description = "0x39";
      case 0x3a -> description = "0x3a";
      case 0x3b -> description = "0x3b";
      case 0x3c -> description = "0x3c";
      case 0x3d -> description = "0x3d";
      case 0x3e -> description = "0x3e";
      case 0x3f -> description = "0x3f";
      case 0x40 -> description = "0x40";
      case 0x41 -> description = "0x41";
      case 0x42 -> description = "0x42";
      case 0x43 -> description = "0x43";
      case 0x44 -> description = "0x44";
      case 0x45 -> description = "0x45";
      case 0x46 -> description = "0x46";
      case 0x47 -> description = "0x47";
      case 0x48 -> description = "0x48";
      case 0x49 -> description = "0x49";
      case 0x4a -> description = "0x4a";
      case 0x4b -> description = "0x4b";
      case 0x4c -> description = "0x4c";
      case 0x4d -> description = "0x4d";
      case 0x4e -> description = "0x4e";
      case 0x4f -> description = "0x4f";
      case 0x50 -> description = "0x50";
      case 0x51 -> description = "0x51";
      case 0x52 -> description = "0x52";
      case 0x53 -> description = "0x53";
      case 0x54 -> description = "0x54";
      case 0x55 -> description = "0x55";
      case 0x56 -> description = "0x56";
      case 0x57 -> description = "0x57";
      case 0x58 -> description = "0x58";
      case 0x59 -> description = "0x59";
      default -> throw new IOException("Opcode parameter not supported: " + Arrays.toString(opcode));
    }
    return new FlagOperation(offset, Bytes.concat(opcode, flagBytes), description);
  }

  private static String getAFFlags(int flag) {
    List<String> flags = new ArrayList<>();
    for (Entry<Integer, String> entry : Seq.AF_FLAGS.entrySet()) {
      if ((entry.getKey() & flag) != 0) {
        flags.add(entry.getValue());
      }
    }
    return String.join(", ", flags);
  }

  private static String getNFFlags(int flag) {
    List<String> flags = new ArrayList<>();
    for (Entry<Integer, String> entry : Seq.NF_FLAGS.entrySet()) {
      if ((entry.getKey() & flag) != 0) {
        flags.add(entry.getValue());
      }
    }
    return String.join(", ", flags);
  }

  private static String getPFFlags(int flag) {
    List<String> flags = new ArrayList<>();
    for (Entry<Integer, String> entry : Seq.PF_FLAGS.entrySet()) {
      if ((entry.getKey() & flag) != 0) {
        flags.add(entry.getValue());
      }
    }
    return String.join(", ", flags);
  }

  private static String getKFFlags(int flag) {
    List<String> flags = new ArrayList<>();
    for (Entry<Integer, String> entry : Seq.KF_FLAGS.entrySet()) {
      if ((entry.getKey() & flag) != 0) {
        flags.add(entry.getValue());
      }
    }
    return String.join(", ", flags);
  }

  private static String getDFFlags(int flag) {
    List<String> flags = new ArrayList<>();
    for (Entry<Integer, String> entry : Seq.DF_FLAGS.entrySet()) {
      if ((entry.getKey() & flag) != 0) {
        flags.add(entry.getValue());
      }
    }
    return String.join(", ", flags);
  }
}