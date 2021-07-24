package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.io.CountingInputStream;
import java.io.IOException;

public class SeqOp {

  private final int opcode;
  private final CountingInputStream cis;
  private String description;

  private SeqOp(int opcode, CountingInputStream cis) {
    this.opcode = opcode;
    this.cis = cis;
  }

  public SeqOp get(int opcode, CountingInputStream cis) throws IOException {
    SeqOp seqOp = new SeqOp(opcode, cis);
    seqOp.parse();
    return seqOp;
  }

  public int getOpcode() {
    return opcode;
  }

  private void parse() throws IOException
  {
    byte opcode_last_byte = (byte) (opcode & 0xff);
    if ((opcode & 0x40) == 0) {
      if ((opcode & 0x80) == 0) {
        if (opcode_last_byte < 0x18) {
          skipWord();
          description = "Get pointer r" + opcode_last_byte;
        }
        else {
          if (opcode_last_byte < 0x30) {
            skipWord();
            description = "Get stored pointer r" + opcode_last_byte;
          }
          else {
            zz_800c978c_(true);
            skipWord();
          }
        }
      }
      else {
        byte lastSixBits = (byte) (opcode & 0x3f);
        if (lastSixBits < 0x18) {
          description = "Get value in r" + opcode_last_byte;
        }
        else {
          if (lastSixBits < 0x30) {
            description = "Get value in stored r" + opcode_last_byte;
          }
          else {
            zz_800c978c_(false);
          }
        }
        int second = ByteUtils.readInt32(cis);
        int secondHalf = second & 0xffff;
        if (secondHalf < 0x18) {
          description += " + value in r" + secondHalf + " + " + (second >> 0x10);
        }
        else {
          description += " + value in stored r" + secondHalf + " + " + (second >> 0x10);
        }
        skipWord();
      }
    }
    else {
      byte lastSixBits = (byte) (opcode & 0x3f);
      if (lastSixBits < 0x18) {
        description = "Get value at register " + opcode_last_byte;
      }
      else {
        if (lastSixBits < 0x30) {
          description = "Get value at stored register " + opcode_last_byte;
        }
        else {
          zz_800c978c_(lastSixBits,return_val,(int *)(pc + 1));
        }
      }
      description += " + " + ByteUtils.readInt32(cis);
    }
    return new_pc;
  }

  private void skipWord() throws IOException {
    if (cis.skip(4) != 4) {
      throw new IOException("Failed to skip 4 bytes.");
    }
  }

  /**
   *
   * @param pcAtOpcode If the program counter is currently pointing to the opcode. Otherwise it is
   *                   pointing to the word after the opcode.
   * @throws IOException
   */
  private void zz_800c978c_(boolean pcAtOpcode) throws IOException
  {
    byte opcode_last_byte = (byte) (opcode & 0xff);
    switch(opcode_last_byte) {
      case 0x30:
        description = "Get value at DAT_8022342c";
        break;
      case 0x32:
        description = "Get value at DAT_80222eb0";
        break;
      case 0x33:
        description = "Get value at DAT_80222e70";
        break;
      case 0x34:
        description = "Get value at DAT_802231a8";
        break;
      case 0x39:
        description = "Get value at DAT_802231e8";
        break;
      case 0x3a:
        description = "Get value at DAT_802233a8";
        break;
      case 0x3b:
        description = "Get value at DAT_80222fb0";
        break;
      case 0x3c:
        description = "Get value at DAT_802261d8";
        break;
      case 0x3d:
        description = "Get value at DAT_80222e64";
        break;
      case 0x3e:
        long offset = cis.getCount();
        if (pcAtOpcode) {
          offset -= 4;
        }
        description = "Get pointer of offset " + offset;
        skipWord();
        break;
      case 0x3f:
        long offset2 = cis.getCount();
        if (pcAtOpcode) {
          offset2 -= 4;
        }
        description = "Get pointer of offset " + offset2;
    }
  }

  public static class Op {

    private final int distance;
    private final String description;

    public Op(int distance, String description) {
      this.distance = distance;
      this.description = description;
    }

    public int getDistance() {
      return distance;
    }

    public String getDescription() {
      return description;
    }
  }
}
