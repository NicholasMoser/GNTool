package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.operands.ChrOperand;
import com.github.nicholasmoser.gnt4.seq.operands.GPROperand;
import com.github.nicholasmoser.gnt4.seq.operands.GlobalOperand;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateFloatOperand;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.gnt4.seq.operands.Operand;
import com.github.nicholasmoser.gnt4.seq.operands.SeqOperand;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class that mimics the functionality of the function EFT_REG_CMD1. This function retrieves one
 * instruction operand for an opcode. There are three types of operands:
 * <ul>
 *   <li>General purpose register addresses and values</li>
 *   <li>Global addresses</li>
 *   <li>Immediate values</li>
 * </ul>
 */
public class EFT_REG_CMD1 {

  private final ByteStream bs;
  private final ByteArrayOutputStream bytes;
  private Operand operand;
  private int opcode;

  private EFT_REG_CMD1(ByteStream bs) {
    this.bs = bs;
    this.bytes = new ByteArrayOutputStream();
  }

  /**
   * Parses the current opcode in a seq byte stream and returns an object containing the one
   * instruction operand.
   *
   * @param bs The seq byte stream to read from.
   * @return The EFT_REG_CMD1 result.
   * @throws IOException If an I/O error occurs.
   */
  public static EFT_REG_CMD1 get(ByteStream bs) throws IOException {
    EFT_REG_CMD1 operand = new EFT_REG_CMD1(bs);
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

  /**
   * Pushes the given word to bytes array. This keeps track of what bytes have been read.
   *
   * @param word The word to push.
   * @throws IOException If an I/O error occurs.
   */
  private void pushFloat(float word) throws IOException {
    bytes.write(ByteUtils.fromFloat(word));
  }

  private void parse() throws IOException {
    this.opcode = bs.readWord();
    pushWord(opcode);
    // Get last 8 bits (1111_1111)
    byte cmd = (byte) (opcode & 0xff);

    if (cmd < 0x20) {
      operand = new GPROperand(cmd, true);
    } else if (cmd < 0x29) {
      operand = EFT_REG_GP(cmd);
    } else if (cmd == 0x2a) {
      int offset = bs.offset();
      int word = bs.readWord();
      operand = new ImmediateOperand(offset, word);
      pushWord(word);
    } else if (cmd == 0x2b) {
      int firstOperand = bs.readWord();
      int secondCmd = bs.peekWord();
      pushWord(firstOperand);
      if (secondCmd < 0x20) {
        operand = new GPROperand(secondCmd, true);
        operand.addInfo(String.format(" + %04x", firstOperand));
      } else if (secondCmd < 0x29) {
        operand = EFT_REG_GP(cmd);
        operand.addInfo(String.format(" + %04x", firstOperand));
      } else {
        // This part is weird, double check this logic
        EFT_REG_CMD1 eft_cmd = EFT_REG_CMD1.get(bs);
        bytes.write(eft_cmd.getBytes());
        operand = eft_cmd.getOperand();
        operand.addInfo(String.format(" + %04x", firstOperand));
      }
    } else if (cmd == 0x2c) {
      pushWord(bs.readWord());
      int offset = bs.offset();
      float value = bs.readFloat();
      // also updates 0x80223bac with this value
      operand = new ImmediateFloatOperand(offset, (float) (value * 0.1));
      pushFloat(value);
    }
  }

  /**
   * Checks the type of Operand and returns it.
   *
   * @param bitFlag  The bits to check for what to return.
   * @return The Operand.
   */
  private Operand EFT_REG_GP(byte bitFlag) {
    return switch (bitFlag) {
      case 0x21 ->
        // Appears to be a matrix identity used for matrix multiplication of attacking hitbox
        // Address: 8022342c
          new GlobalOperand(GlobalOperand.Value.HITBOX_IDENTITY_MATRIX);
      case 0x22 ->
        // Pointer to structs of controllers.
        // Address: 80222eb0
          new GlobalOperand(GlobalOperand.Value.CONTROLLERS);
      case 0x23 ->
        // Pointer to struct of primary controller information. This is the controller being used
        // to navigate menus, stages, etc.
        // Address: 80222e70
          new GlobalOperand(GlobalOperand.Value.PRIMARY_CONTROLLER);
      case 0x24 ->
        // Pointer to display information, such as resolution, buffer, gamma, z list, z sort, and
        // snapshot. This information is viewable from the debug menu under screen_menu.
        // Address: 802231a8
          new GlobalOperand(GlobalOperand.Value.DISPLAY);
      case 0x25 ->
        // Pointer to save data.
        // Address: 802231e8
          new GlobalOperand(GlobalOperand.Value.SAVE_DATA);
      case 0x26 ->
        // 0x2 is debug mode, 0x0 is normal mode
        // Address: 802233a8
          new GlobalOperand(GlobalOperand.Value.DEBUG_MODE);
      case 0x27 ->
        // 0xF is paused, 0x0 is un-paused
        // Address: 80222fb0
          new GlobalOperand(GlobalOperand.Value.PAUSE_GAME);
      case 0x28 ->
        // Pointer to game info, such as the current battle mode, scene, and game ticks.
        // Address: 802261d8
          new GlobalOperand(GlobalOperand.Value.GAME_INFO);
      default -> throw new IllegalStateException(
          String.format("Unknown lookup value: %02x", bitFlag));
    };
  }
}
