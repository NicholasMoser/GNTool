package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.io.CountingInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class that mimics the functionality of the function in GNT4 at address 0x800c95e4, seq_get_op.
 */
public class SeqOp {

  private final ByteStream bs;
  private final ByteArrayOutputStream bytes;
  private boolean parsed;
  private String description;
  private int opcode;

  private SeqOp(ByteStream bs) {
    this.bs = bs;
    this.bytes = new ByteArrayOutputStream();
    this.parsed = false;
  }

  public static SeqOp get(ByteStream bs) throws IOException {
    SeqOp seqOp = new SeqOp(bs);
    seqOp.parse();
    return seqOp;
  }

  public int getOpcode() {
    return opcode;
  }

  /**
   * @return The bytes after the opcode. Returns an empty byte array if there are none.
   */
  public byte[] getBytes() {
    return bytes.toByteArray();
  }

  public String getDescription() {
    return description;
  }

  private void pushWord(int word) throws IOException {
    bytes.write(ByteUtils.fromInt32(word));
  }

  private void parse() throws IOException {
    if (parsed) {
      throw new IllegalStateException("Opcode has already been parsed.");
    }
    this.opcode = bs.peekWord();
    // Get last 8 bits (1111_1111)
    byte opcode_last_byte = (byte) (opcode & 0xff);
    // If second bit set (0100_0000) of last byte
    if ((opcode & 0x40) == 0) {
      // If first bit set (1000_0000) of last byte
      if ((opcode & 0x80) == 0) {
        // This block of code returns a pointer.
        if (opcode_last_byte < 0x18) {
          bs.skipWord();
          description = String.format("Get pointer of gpr%02x", opcode_last_byte);
        } else if (opcode_last_byte < 0x30) {
          bs.skipWord();
          description = String.format("Get pointer of seq_p_sp->field_0x%02x",
              opcode_last_byte - 0x18);
        } else {
          zz_800c978c_(true, opcode_last_byte);
          bs.skipWord();
        }
      } else {
        // This block of code updates and returns a memory value.
        // Doesn't appear to be used much by GNT4 (if at all)
        byte lastSixBits = (byte) (opcode & 0x3f);
        if (lastSixBits < 0x18) {
          description = String.format("Get value of gpr%02x", opcode_last_byte);
        } else if (lastSixBits < 0x30) {
          description = String.format("Get value of seq_p_sp->field_0x%02x",
              opcode_last_byte - 0x18);
        } else {
          zz_800c978c_(false, lastSixBits);
        }
        int second = bs.readWord();
        int secondHalf = second & 0xffff;
        if (secondHalf < 0x18) {
          description += String.format(" + value in gpr%02x + %x", secondHalf, (second >> 0x10));
        } else {
          description += " + value in stored r" + secondHalf + " + " + (second >> 0x10);
        }
        bs.skipWord();
      }
    } else {
      // This block of code updates and returns a memory value.
      byte lastSixBits = (byte) (opcode & 0x3f);
      if (lastSixBits < 0x18) {
        description = String.format("Get value of gpr%02x", opcode_last_byte);
      } else if (lastSixBits < 0x30) {
        description = String.format("Get value of seq_p_sp->field_0x%02x", lastSixBits * 4);
      } else {
        zz_800c978c_(false, lastSixBits);
      }
      bs.skipWord();
      int word = bs.readWord();
      description += String.format(" and add %08x", word);
      pushWord(word);
    }
    parsed = true;
  }

  /**
   * This function behaves differently based on whether it is called to store a pointer or called to
   * store a value.
   * If called to store a pointer it will:
   * <ul>
   *   <li>Not increment the program counter passed in</li>
   *   <li>Return and use the new program counter</li>
   * </ul>
   * If called to store a value it will:
   * <ul>
   *   <li>Increment the program counter passed in by 4 bytes.</li>
   *   <li>Not return and use the new program counter.</li>
   * </ul>
   *
   * @param storePointer If a pointer should be returned, otherwise a value is returned.
   * @throws IOException
   */
  private void zz_800c978c_(boolean storePointer, byte opcode_last_byte) throws IOException {
    String noun = storePointer ? "pointer" : "value";
    switch (opcode_last_byte) {
      case 0x30:
        // Appears to be a matrix identity used for matrix multiplication of attacking hitbox
        // Address: 80223428
        description = String.format("Get %s of HITBOX_IDENTITY_MATRIX", noun);
        break;
      case 0x32:
        // Pointer to structs of controllers.
        // Address: 80222eb0
        description = String.format("Get %s of CONTROLLERS", noun);
        break;
      case 0x33:
        // Pointer to struct of primary controller information. This is the controller being used
        // to navigate menus, stages, etc.
        // Address: 80222e70
        description = String.format("Get %s of PRIMARY_CONTROLLER", noun);
        break;
      case 0x34:
        // Pointer to display information, such as resolution, buffer, gamma, z list, z sort, and
        // snapshot. This information is viewable from the debug menu under screen_menu.
        // Address: 802231a8
        description = String.format("Get %s of DISPLAY", noun);
        break;
      case 0x39:
        // Pointer to save data.
        // Address: 802231e8
        description = String.format("Get %s of SAVE_DATA", noun);
        break;
      case 0x3a:
        // 0x2 is debug mode, 0x0 is normal mode
        // Address: 802233a8
        description = String.format("Get %s of DEBUG_MODE", noun);
        break;
      case 0x3b:
        // 0xF is paused, 0x0 is un-paused
        // Address: 80222fb0
        description = String.format("Get %s of PAUSE_GAME", noun);
        break;
      case 0x3c:
        // Pointer to game info, such as the current battle mode, scene, and game ticks.
        // Address: 802261d8
        description = String.format("Get %s of GAME_INFO", noun);
        break;
      case 0x3d:
        // Unknown pointer, appears to be unused.
        // Address: 80222e64
        description = String.format("Get %s of UNUSED", noun);
        break;
      case 0x3e:
        // Skip a word and read a word
        // Doesn't appear to be used much by GNT4 (if at all)
        int word;
        int offset;
        if (storePointer) {
          bs.skipWord();
          offset = bs.offset();
          word = bs.readWord();
          pushWord(word);
          pushWord(bs.peekWord());
        } else {
          bs.mark(0xc);
          bs.skipWord();
          bs.skipWord();
          offset = bs.offset();
          word = bs.readWord();
          bs.reset();
          pushWord(word);
        }
        description = String.format("Get %s at opcode offset 0x%x: 0x%08x", noun, offset, word);
        break;
      case 0x3f:
        // Skip a word and peek a word
        int word2;
        int offset2;
        if (storePointer) {
          bs.skipWord();
          offset2 = bs.offset();
          word2 = bs.peekWord();
        } else {
          bs.mark(0xc);
          bs.skipWord();
          bs.skipWord();
          offset2 = bs.offset();
          word2 = bs.readWord();
          bs.reset();
        }
        pushWord(word2);
        description = String.format("Get %s at opcode offset 0x%x: 0x%08x", noun, offset2, word2);
        break;
    }
  }
}
