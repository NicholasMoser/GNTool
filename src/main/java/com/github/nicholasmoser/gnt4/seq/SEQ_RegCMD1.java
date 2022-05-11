package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.operands.ChrOperand;
import com.github.nicholasmoser.gnt4.seq.operands.GPROperand;
import com.github.nicholasmoser.gnt4.seq.operands.GlobalOperand;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.gnt4.seq.operands.Operand;
import com.github.nicholasmoser.gnt4.seq.operands.SeqOperand;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class that mimics the functionality of the function SEQ_RegCMD1. This function retrieves one
 * instruction operand for an opcode. There are four types of operands:
 * <ul>
 *   <li>General purpose register addresses and values</li>
 *   <li>Seq stored addresses and values</li>
 *   <li>Global addresses</li>
 *   <li>Immediate values</li>
 * </ul>
 */
public class SEQ_RegCMD1 {

  private final ByteStream bs;
  private final ByteArrayOutputStream bytes;
  private Operand operand;
  private int opcode;

  private SEQ_RegCMD1(ByteStream bs) {
    this.bs = bs;
    this.bytes = new ByteArrayOutputStream();
  }

  /**
   * Parses the current opcode in a seq byte stream and returns an object containing the one
   * instruction operand.
   *
   * @param bs The seq byte stream to read from.
   * @return The SEQ_RegCMD1 result.
   * @throws IOException If an I/O error occurs.
   */
  public static SEQ_RegCMD1 get(ByteStream bs) throws IOException {
    SEQ_RegCMD1 operand = new SEQ_RegCMD1(bs);
    operand.parse();
    return operand;
  }

  /**
   * @return The opcode.
   */
  public int getOpcode() {
    return opcode;
  }

  /**
   * @return The opcode bytes plus any bytes read by SEQ_RegCMD1.
   */
  public byte[] getBytes() {
    return bytes.toByteArray();
  }

  /**
   * @return A description of the result of SEQ_RegCMD1.
   */
  public String getDescription() {
    return operand.toString();
  }

  public Operand getOperand() {
    return operand;
  }

  /**
   * Pushes the given word to bytes array. This keeps track of what bytes have been read.
   *
   * @param word The word to push.
   * @throws IOException If an I/O error occurs.
   */
  private void pushWord(int word) throws IOException {
    bytes.write(ByteUtils.fromInt32(word));
  }

  private void parse() throws IOException {
    this.opcode = bs.peekWord();
    // Get last 8 bits (1111_1111)
    byte opcode_last_byte = (byte) (opcode & 0xff);
    // If second bit set (0100_0000) of last byte
    if ((opcode & 0x40) == 0) {
      // If first bit set (1000_0000) of last byte
      if ((opcode & 0x80) == 0) {
        // Load affective address
        if (opcode_last_byte < 0x18) {
          pushWord(bs.readWord());
          operand = new GPROperand(opcode_last_byte, true);
        } else if (opcode_last_byte < 0x30) {
          pushWord(bs.readWord());
          operand = getSeqOperand(opcode_last_byte - 0x18, true);
        } else {
          operand = SEQ_RegGP(opcode_last_byte, true);
          pushWord(bs.readWord());
        }
      } else {
        // Load effective address sum with offset
        byte lastSixBits = (byte) (opcode & 0x3f);
        if (lastSixBits < 0x18) {
          operand = new GPROperand(lastSixBits, false);
        } else if (lastSixBits < 0x30) {
          operand = getSeqOperand(lastSixBits - 0x18, false);
        } else {
          operand = SEQ_RegGP(lastSixBits, false);
        }
        pushWord(bs.readWord());
        int word = bs.readWord();
        pushWord(word);
        int bottomTwoBytes = word & 0xffff;
        int topTwoBytes = word >> 0x10;
        if (bottomTwoBytes < 0x18) {
          operand.addInfo(String.format(" + *gpr%d", bottomTwoBytes));
        } else {
          operand.addInfo(String.format(" + *seq_p_sp->field_0x%02x", bottomTwoBytes * 4));
        }
        operand.addInfo(String.format(" + %04x", topTwoBytes));
      }
    } else {
      // Load effective address with offset
      byte lastSixBits = (byte) (opcode & 0x3f);
      if (lastSixBits < 0x18) {
        operand = new GPROperand(lastSixBits, false);
      } else if (lastSixBits < 0x30) {
        operand = getSeqOperand(lastSixBits - 0x18, false);
      } else {
        operand = SEQ_RegGP(lastSixBits, false);
      }
      pushWord(bs.readWord());
      int word = bs.readWord();
      operand.withField(word);
      pushWord(word);
    }
  }

  /**
   * Checks a {@link SeqOperand} to see if it is a known Operand type, and instead returns that
   * type if so. For example, {@link ChrOperand} is one such known type at a specific index.
   *
   * @param index The index.
   * @param isPointer If this operand is a pointer.
   * @return The operand.
   */
  public Operand getSeqOperand(int index, boolean isPointer) {
    if (index == 0xE) {
      return new ChrOperand(false, isPointer);
    } else if (index == 0xF) {
      return new ChrOperand(true, isPointer);
    }
    return new SeqOperand(index, isPointer);
  }

  /**
   * Checks the type of Operand and returns it.
   *
   * @param bitFlag  The bits to check for what to return.
   * @param returnPc If the program counter should be returned.
   * @return The Operand.
   * @throws IOException If an I/O error occurs.
   */
  private Operand SEQ_RegGP(byte bitFlag, boolean returnPc) throws IOException {
    return switch (bitFlag) {
      case 0x30 ->
          // Appears to be a matrix identity used for matrix multiplication of attacking hitbox
          // Address: 80223428
          new GlobalOperand(GlobalOperand.Value.HITBOX_IDENTITY_MATRIX);
      case 0x32 ->
          // Pointer to structs of controllers.
          // Address: 80222eb0
          new GlobalOperand(GlobalOperand.Value.CONTROLLERS);
      case 0x33 ->
          // Pointer to struct of primary controller information. This is the controller being used
          // to navigate menus, stages, etc.
          // Address: 80222e70
          new GlobalOperand(GlobalOperand.Value.PRIMARY_CONTROLLER);
      case 0x34 ->
          // Pointer to display information, such as resolution, buffer, gamma, z list, z sort, and
          // snapshot. This information is viewable from the debug menu under screen_menu.
          // Address: 802231a8
          new GlobalOperand(GlobalOperand.Value.DISPLAY);
      case 0x39 ->
          // Pointer to save data.
          // Address: 802231e8
          new GlobalOperand(GlobalOperand.Value.SAVE_DATA);
      case 0x3a ->
          // 0x2 is debug mode, 0x0 is normal mode
          // Address: 802233a8
          new GlobalOperand(GlobalOperand.Value.DEBUG_MODE);
      case 0x3b ->
          // 0xF is paused, 0x0 is un-paused
          // Address: 80222fb0
          new GlobalOperand(GlobalOperand.Value.PAUSE_GAME);
      case 0x3c ->
          // Pointer to game info, such as the current battle mode, scene, and game ticks.
          // Address: 802261d8
          new GlobalOperand(GlobalOperand.Value.GAME_INFO);
      case 0x3d ->
          // Unknown pointer, appears to be unused.
          // Address: 80222e64
          new GlobalOperand(GlobalOperand.Value.UNUSED);
      case 0x3e -> readImmediate(returnPc);
      case 0x3f -> peekImmediate(returnPc);
      default -> throw new IllegalStateException(
          String.format("Unknown lookup value: %02x", bitFlag));
    };
  }

  private Operand readImmediate(boolean returnPc) throws IOException {
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
    return new ImmediateOperand(offset, word);
  }

  private Operand peekImmediate(boolean returnPc) throws IOException {
    int offset;
    int word;
    if (returnPc) {
      pushWord(bs.readWord());
      offset = bs.offset();
      word = bs.peekWord();
    } else {
      bs.mark();
      bs.skipWord();
      bs.skipWord();
      offset = bs.offset();
      word = bs.readWord();
      bs.reset();
    }
    return new ImmediateOperand(offset, word);
  }
}
