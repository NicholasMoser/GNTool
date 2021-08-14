package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class that mimics the functionality of the function in GNT4 at address 0x800c95e4,
 * get_effective_address.
 */
public class EffectiveAddress {

  private final ByteStream bs;
  private final ByteArrayOutputStream bytes;
  private StringBuilder description;
  private int opcode;

  /**
   * Private constructor of EffectiveAddress. To create one you must call {@link #get(ByteStream)}.
   *
   * @param bs The byte stream of the seq.
   */
  private EffectiveAddress(ByteStream bs) {
    this.bs = bs;
    this.bytes = new ByteArrayOutputStream();
    this.description = new StringBuilder();
  }

  /**
   * Parses the current opcode in a seq byte stream and calls <code>get_effective_address</code>.
   * This will iterate the byte stream and return an object with the results of calling
   * <code>get_effective_address</code>. This will not close the byte stream.
   *
   * @param bs The seq byte stream to read from.
   * @return The get_effective_address result.
   * @throws IOException If an I/O error occurs.
   */
  public static EffectiveAddress get(ByteStream bs) throws IOException {
    EffectiveAddress ea = new EffectiveAddress(bs);
    ea.parse();
    return ea;
  }

  /**
   * @return The opcode.
   */
  public int getOpcode() {
    return opcode;
  }

  /**
   * @return The opcode bytes plus any bytes read by get_effective_address.
   */
  public byte[] getBytes() {
    return bytes.toByteArray();
  }

  /**
   * @return A description of the result of get_effective_address.
   */
  public String getDescription() {
    return description.toString();
  }

  /**
   * Pushes the given word to bytes array. This keeps track of what bytes have been read by
   * <code>get_effective_address</code>.
   *
   * @param word The word to push.
   * @throws IOException If an I/O error occurs.
   */
  private void pushWord(int word) throws IOException {
    bytes.write(ByteUtils.fromInt32(word));
  }

  /**
   * Parses an opcode that calls <code>get_effective_address</code> and returns the result of that
   * method.
   *
   * @throws IOException If an I/O error occurs.
   */
  private void parse() throws IOException {
    this.opcode = bs.peekWord();
    // Get last 8 bits (1111_1111)
    byte opcode_last_byte = (byte) (opcode & 0xff);
    // If second bit set (0100_0000) of last byte
    if ((opcode & 0x40) == 0) {
      // If first bit set (1000_0000) of last byte
      if ((opcode & 0x80) == 0) {
        if (opcode_last_byte < 0x18) {
          pushWord(bs.readWord());
          description.append(String.format("Get pointer of gpr%02x", opcode_last_byte));
        } else if (opcode_last_byte < 0x30) {
          pushWord(bs.readWord());
          description.append(String.format("Get pointer of seq_p_sp->field_0x%02x",
              opcode_last_byte - 0x18));
        } else {
          load_value(opcode_last_byte, true);
          pushWord(bs.readWord());
        }
      } else {
        // Doesn't appear to be used much by GNT4 (if at all)
        throw new IllegalStateException("This mode is not yet supported.");
      }
    } else {
      byte lastSixBits = (byte) (opcode & 0x3f);
      if (lastSixBits < 0x18) {
        description.append(String.format("Get value of gpr%02x", lastSixBits));
      } else if (lastSixBits < 0x30) {
        description.append(String.format("Get value of seq_p_sp->field_0x%02x", lastSixBits * 4));
      } else {
        load_value(lastSixBits, false);
      }
      pushWord(bs.readWord());
      int word = bs.readWord();
      description.append(String.format(" and add %08x", word));
      pushWord(word);
    }
  }

  /**
   * @param bitFlag  The bits to check for what to return.
   * @param returnPc If the program counter should be returned.
   * @throws IOException If an I/O error occurs.
   */
  private void load_value(byte bitFlag, boolean returnPc) throws IOException {
    switch (bitFlag) {
      case 0x30 ->
          // Appears to be a matrix identity used for matrix multiplication of attacking hitbox
          // Address: 80223428
          description.append("Get pointer of HITBOX_IDENTITY_MATRIX");
      case 0x32 ->
          // Pointer to structs of controllers.
          // Address: 80222eb0
          description.append("Get pointer of CONTROLLERS");
      case 0x33 ->
          // Pointer to struct of primary controller information. This is the controller being used
          // to navigate menus, stages, etc.
          // Address: 80222e70
          description.append("Get pointer of PRIMARY_CONTROLLER");
      case 0x34 ->
          // Pointer to display information, such as resolution, buffer, gamma, z list, z sort, and
          // snapshot. This information is viewable from the debug menu under screen_menu.
          // Address: 802231a8
          description.append("Get pointer of DISPLAY");
      case 0x39 ->
          // Pointer to save data.
          // Address: 802231e8
          description.append("Get pointer of SAVE_DATA");
      case 0x3a ->
          // 0x2 is debug mode, 0x0 is normal mode
          // Address: 802233a8
          description.append("Get pointer of DEBUG_MODE");
      case 0x3b ->
          // 0xF is paused, 0x0 is un-paused
          // Address: 80222fb0
          description.append("Get pointer of PAUSE_GAME");
      case 0x3c ->
          // Pointer to game info, such as the current battle mode, scene, and game ticks.
          // Address: 802261d8
          description.append("Get pointer of GAME_INFO");
      case 0x3d ->
          // Unknown pointer, appears to be unused.
          // Address: 80222e64
          description.append("Get pointer of UNUSED");
      case 0x3e -> {
        // Read immediate value
        // Doesn't appear to be used much by GNT4 (if at all)
        int offset;
        int word;
        if (returnPc) {
          pushWord(bs.readWord());
          offset = bs.offset();
          word = bs.readWord();
          pushWord(word);
        } else {
          bs.mark();
          bs.skipWord();
          bs.skipWord();
          offset = bs.offset();
          word = bs.readWord();
          bs.reset();
        }
        description.append(
            String.format("Get pointer at opcode offset 0x%x: 0x%08x", offset, word));
      }
      case 0x3f -> {
        // Peek immediate value
        int offset2;
        int word2;
        if (returnPc) {
          pushWord(bs.readWord());
          offset2 = bs.offset();
          word2 = bs.peekWord();
        } else {
          bs.mark();
          bs.skipWord();
          bs.skipWord();
          offset2 = bs.offset();
          word2 = bs.readWord();
          bs.reset();
        }
        description.append(
            String.format("Get pointer at opcode offset 0x%x: 0x%08x", offset2, word2));
      }
      default -> throw new IllegalStateException(
          String.format("Unknown lookup value: %02x", bitFlag));
    }
  }
}
