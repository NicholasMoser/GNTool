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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that mimics the functionality of the function EFT_REG_CMD2. This function retrieves two
 * instruction operands for an opcode. There are three types of operands:
 * <ul>
 *   <li>General purpose register addresses and values</li>
 *   <li>Global addresses</li>
 *   <li>Immediate values</li>
 * </ul>
 */
public class EFT_REG_CMD2 {

  private final ByteStream bs;
  private final ByteArrayOutputStream bytes;
  private final List<Operand> operands;
  private int opcode;

  private EFT_REG_CMD2(ByteStream bs) {
    this.bs = bs;
    this.bytes = new ByteArrayOutputStream();
    this.operands = new ArrayList<>(2);
  }

  /**
   * Parses the current opcode in a seq byte stream and returns an object containing the one
   * instruction operand.
   *
   * @param bs The seq byte stream to read from.
   * @return The EFT_REG_CMD1 result.
   * @throws IOException If an I/O error occurs.
   */
  public static EFT_REG_CMD2 get(ByteStream bs) throws IOException {
    EFT_REG_CMD2 operand = new EFT_REG_CMD2(bs);
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
   * @return The opcode bytes plus any bytes read by SEQ_RegCMD2.
   */
  public byte[] getBytes() {
    return bytes.toByteArray();
  }

  /**
   * @return A description of the result of SEQ_RegCMD2.
   */
  public String getDescription() {
    return operands.stream()
        .map(Object::toString)
        .collect(Collectors.joining(", "));
  }

  public List<Operand> getOperands() {
    return List.copyOf(operands);
  }

  public Operand getFirstOperand() {
    return operands.get(0);
  }

  public Operand getSecondOperand() {
    return operands.get(1);
  }

  /**
   * Pushes the given word to bytes array. Keeps track of what bytes have been read.
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
    byte cmd = (byte) ((opcode >> 0x8) & 0xff);

    Operand firstOperand;
    if (cmd < 0x20) {
      firstOperand = new GPROperand(cmd, true);
      cmd = (byte) (opcode & 0xff);
    } else if (cmd < 0x29) {
      firstOperand = EFT_REG_GP(cmd);
      cmd = (byte) (opcode & 0xff);
    } else if (cmd == 0x2a) {
      int offset = bs.offset();
      int word = bs.readWord();
      firstOperand = new ImmediateOperand(offset, word, 4);
      pushWord(word);
      cmd = (byte) ((bs.peekWord() >> 0x18) & 0xff);
    } else if (cmd == 0x2b) {
      int addedOffset = bs.readWord();
      int secondCmd = bs.peekWord();
      pushWord(addedOffset);
      if (secondCmd < 0x20) {
        firstOperand = new GPROperand(secondCmd, true);
        firstOperand.addInfo(String.format(" + %04x", addedOffset));
        pushWord(secondCmd);
        bs.skipWord();
      } else if (secondCmd < 0x29) {
        firstOperand = EFT_REG_GP((byte) secondCmd);
        firstOperand.addInfo(String.format(" + %04x", addedOffset));
        pushWord(secondCmd);
        bs.skipWord();
      } else {
        // This part is weird, double check this logic
        EFT_REG_CMD1 eft_cmd = EFT_REG_CMD1.get(bs);
        bytes.write(eft_cmd.getBytes());
        firstOperand = eft_cmd.getOperand();
        firstOperand.addInfo(String.format(" + %04x", addedOffset));
      }
      cmd = (byte) ((bs.peekWord() >> 0x18) & 0xff);
    } else if (cmd == 0x2c) {
      pushWord(bs.readWord());
      int offset = bs.offset();
      float value = bs.readFloat();
      // also updates 0x80223bac with this value
      firstOperand = new ImmediateFloatOperand(offset, (float) (value * 0.1));
      pushFloat(value);
      cmd = (byte) ((bs.peekWord() >> 0x18) & 0xff);
    } else {
      throw new IllegalArgumentException(String.format("Unknown EFT_REG_CMD2 cmd: 0x%X", cmd));
    }
    operands.add(firstOperand);

    Operand secondOperand;
    if (cmd < 0x20) {
      secondOperand = new GPROperand(cmd, true);
    } else if (cmd < 0x29) {
      secondOperand = EFT_REG_GP(cmd);
    } else if (cmd == 0x2a) {
      int offset = bs.offset();
      int word = bs.readWord();
      secondOperand = new ImmediateOperand(offset, word, 4);
      pushWord(word);
    } else if (cmd == 0x2b) {
      int addedOffset = bs.readWord();
      int secondCmd = bs.peekWord();
      pushWord(addedOffset);
      if (secondCmd < 0x20) {
        secondOperand = new GPROperand(secondCmd, true);
        secondOperand.addInfo(String.format(" + %04x", addedOffset));
        pushWord(secondCmd);
        bs.skipWord();
      } else if (secondCmd < 0x29) {
        secondOperand = EFT_REG_GP((byte) secondCmd);
        secondOperand.addInfo(String.format(" + %04x", addedOffset));
        pushWord(secondCmd);
        bs.skipWord();
      } else {
        // This part is weird, double check this logic
        EFT_REG_CMD1 eft_cmd = EFT_REG_CMD1.get(bs);
        bytes.write(eft_cmd.getBytes());
        secondOperand = eft_cmd.getOperand();
        secondOperand.addInfo(String.format(" + %04x", addedOffset));
      }
    } else if (cmd == 0x2c) {
      pushWord(bs.readWord());
      int offset = bs.offset();
      float value = bs.readFloat();
      // also updates 0x80223bac with this value
      secondOperand = new ImmediateFloatOperand(offset, (float) (value * 0.1));
      pushFloat(value);
    } else {
      throw new IllegalArgumentException(String.format("Unknown EFT_REG_CMD2 cmd: 0x%X", cmd));
    }
    operands.add(secondOperand);

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
