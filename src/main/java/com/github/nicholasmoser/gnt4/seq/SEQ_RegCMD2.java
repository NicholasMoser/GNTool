package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.gnt4.seq.operands.ChrOperand;
import com.github.nicholasmoser.gnt4.seq.operands.GPROperand;
import com.github.nicholasmoser.gnt4.seq.operands.GlobalOperand;
import com.github.nicholasmoser.gnt4.seq.operands.ImmediateOperand;
import com.github.nicholasmoser.gnt4.seq.operands.Operand;
import com.github.nicholasmoser.gnt4.seq.operands.OperandParser;
import com.github.nicholasmoser.gnt4.seq.operands.SeqOperand;
import com.github.nicholasmoser.gnt4.seq.structs.Chr;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class that mimics the functionality of the function SEQ_RegCMD2. This function retrieves two
 * instruction operands for an opcode. There are four types of operands:
 * <ul>
 *   <li>General purpose register addresses and values</li>
 *   <li>Seq stored addresses and values</li>
 *   <li>Global addresses</li>
 *   <li>Immediate values</li>
 * </ul>
 */
public class SEQ_RegCMD2 implements SEQ_RegCMD {

  private final ByteStream bs;
  private final ByteArrayOutputStream bytes;
  private final List<Operand> operands;
  private int opcode;

  private SEQ_RegCMD2(ByteStream bs) {
    this.bs = bs;
    this.bytes = new ByteArrayOutputStream();
    this.operands = new ArrayList<>(2);
  }

  /**
   * Parses the current opcode in a seq byte stream and returns an object containing the two
   * instruction operands.
   *
   * @param bs The seq byte stream to read from.
   * @return The SEQ_RegCMD2 result.
   * @throws IOException If an I/O error occurs.
   */
  public static SEQ_RegCMD2 get(ByteStream bs) throws IOException {
    SEQ_RegCMD2 operands = new SEQ_RegCMD2(bs);
    operands.parse();
    return operands;
  }

  /**
   * Get the operand bytes from an operand text description.
   *
   * @param description The description to parse.
   * @return The operand bytes.
   * @throws IOException If an I/O error occurs.
   */
  public static byte[] parseDescription(String description) throws IOException {
    String[] operands = description.split(", ");
    OperandBytes first;
    OperandBytes second;
    if (operands[0].contains(" + ")) {
      // Load effective address sum with offset
      first =  parseEASumPlusOffsetDescription(operands[0]);
    } else if (operands[0].contains("->")) {
      // Load effective address with offset
      first = parseEAPlusOffsetDescription(operands[0]);
    } else {
      // Load affective address
      first = parseEADescription(operands[0]);
    }
    if (operands[1].contains(" + ")) {
      // Load effective address sum with offset
      second =  parseEASumPlusOffsetDescription(operands[1]);
    } else if (operands[1].contains("->")) {
      // Load effective address with offset
      second = parseEAPlusOffsetDescription(operands[1]);
    } else {
      // Load affective address
      second = parseEADescription(operands[1]);
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(first.flag);
    if (first.bytes.length > 0) {
      // First operand has extra bytes so flag of second operand is pushed
      baos.write(0);
      baos.write(first.bytes);
      baos.write(second.flag);
      baos.write(new byte[3]);
      baos.write(second.bytes);
    } else {
      // First operand and second operand flag together at third and fourth byte
      baos.write(second.flag);
      baos.write(second.bytes);
    }
    return baos.toByteArray();
  }

  private void parse() throws IOException {
    this.opcode = bs.peekWord();
    // Get third 8 bits (1111_1111)
    byte first_address_byte = (byte) ((opcode >> 0x8) & 0xff);
    byte second_address_byte;
    // If second bit set (0100_0000) of third byte
    if ((first_address_byte & 0x40) == 0) {
      // If first bit set (1000_0000) of third byte
      if ((first_address_byte & 0x80) == 0) {
        // Load affective address
        if (first_address_byte < 0x18) {
          operands.add(new GPROperand(first_address_byte, true));
          second_address_byte = (byte) (opcode & 0xff);
        } else if (first_address_byte < 0x30) {
          operands.add(getSeqOperand(first_address_byte - 0x18, true));
          second_address_byte = (byte) (opcode & 0xff);
        } else {
          operands.add(SEQ_RegGP(first_address_byte, true));
          pushWord(bs.readWord());
          second_address_byte = (byte) ((bs.peekWord() >> 0x18) & 0xff);
        }
      } else {
        // Load effective address sum with offset
        throw new IllegalStateException("This mode is not yet supported.");
      }
    } else {
      // Load effective address with offset
      Operand firstOperand;
      byte lastSixBits = (byte) (first_address_byte & 0x3f);
      if (lastSixBits < 0x18) {
        firstOperand = new GPROperand(lastSixBits, false);
      } else if (lastSixBits < 0x30) {
        firstOperand = getSeqOperand(lastSixBits - 0x18, false);
      } else {
        firstOperand = SEQ_RegGP(lastSixBits, false);
      }
      pushWord(bs.readWord());
      int word = bs.readWord();
      firstOperand.withField(word);
      operands.add(firstOperand);
      pushWord(word);
      second_address_byte = (byte) ((bs.peekWord() >> 0x18) & 0xff);
    }

    // Part 2
    // If second bit set (0100_0000) of last byte
    if ((second_address_byte & 0x40) == 0) {
      // If first bit set (1000_0000) of last byte
      if ((second_address_byte & 0x80) == 0) {
        // Load effective address
        if (second_address_byte < 0x18) {
          pushWord(bs.readWord());
          operands.add(new GPROperand(second_address_byte, true));
        } else if (second_address_byte < 0x30) {
          pushWord(bs.readWord());
          operands.add(getSeqOperand(second_address_byte - 0x18, true));
        } else {
          operands.add(SEQ_RegGP(second_address_byte, true));
          pushWord(bs.readWord());
        }
      } else {
        // Load effective address sum with offset
        Operand secondOperand;
        byte lastSixBits2 = (byte) (second_address_byte & 0x3f);
        if (lastSixBits2 < 0x18) {
          secondOperand = new GPROperand(lastSixBits2, false);
        } else if (lastSixBits2 < 0x30) {
          secondOperand = getSeqOperand(lastSixBits2 - 0x18, false);
        } else {
          secondOperand = SEQ_RegGP(lastSixBits2, false);
        }
        pushWord(bs.readWord());
        int word = bs.readWord();
        pushWord(word);
        int bottomTwoBytes = word & 0xffff;
        int topTwoBytes = word >> 0x10;
        String offset = OperandParser.GetOperand((byte) bottomTwoBytes);
        if (offset == null) {
          throw new IOException("Unable to find operand for offset " + bottomTwoBytes);
        }
        secondOperand.addInfo(String.format(" + *%s", offset));
        secondOperand.addInfo(String.format(" + %04x", topTwoBytes));
        operands.add(secondOperand);
      }
    } else {
      // Load effective address with offset
      Operand secondOperand;
      byte lastSixBits2 = (byte) (second_address_byte & 0x3f);
      if (lastSixBits2 < 0x18) {
        secondOperand = new GPROperand(lastSixBits2, false);
      } else if (lastSixBits2 < 0x30) {
        secondOperand = getSeqOperand(lastSixBits2 - 0x18, false);
      } else {
        secondOperand = SEQ_RegGP(lastSixBits2, false);
      }
      pushWord(bs.readWord());
      int word2 = bs.readWord();
      secondOperand.withField(word2);
      operands.add(secondOperand);
      pushWord(word2);
    }
    if (operands.size() != 2) {
      throw new IllegalStateException("Failed to parse operands, only found " + operands.size());
    }
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

  record OperandBytes(byte flag, byte[] bytes) {

  }

  /**
   * Load affective address
   *
   * @param description The description to parse.
   * @return The bytes of the operands.
   * @throws IOException If any I/O exception occurs.
   */
  private static OperandBytes parseEADescription(String description) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte flag = 0;
    if (description.startsWith("*")) {
      throw new IOException("Unexpected * for load effective address: " + description);
    }
    Byte registerVal = OperandParser.getByte(description);
    if (registerVal == null) {
      int immediate = Long.decode(description).intValue();
      flag = (byte) (flag | (byte)0x3f);
      baos.write(ByteUtils.fromInt32(immediate));
    } else {
      flag = (byte) (flag | registerVal);
    }
    return new OperandBytes(flag, baos.toByteArray());
  }

  /**
   * Load effective address with offset
   *
   * @param description The description to parse.
   * @return The bytes of the operands.
   * @throws IOException If any I/O exception occurs.
   */
  private static OperandBytes parseEAPlusOffsetDescription(String description) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte flag = 0x40;
    // Remove dereference
    if (description.startsWith("*")) {
      description = description.substring(1);
    }
    String[] parts = description.split("->");
    Byte registerVal = OperandParser.getByte(parts[0]);
    if (registerVal == null) {
      flag = (byte) (flag | (byte)0x3f); // immediate value
    } else {
      flag = (byte) (flag | registerVal);
    }
    if (parts[1].startsWith("field_")) {
      baos.write(ByteUtils.fromUint32(Long.decode(parts[1].replace("field_", "")).intValue()));
    } else {
      // Known struct
      if (registerVal == null) {
        throw new IOException("Referencing known struct but no register value found for: " + parts[0]);
      }
      Optional<Integer> chrField = switch (registerVal & 0x3f) {
        case 0x26, 0x27 -> Chr.getOffset(parts[1]);
        default -> throw new IOException("Unknown struct for register val " + registerVal);
      };
      if (chrField.isEmpty()) {
        throw new IOException(String.format("Unknown field for %s", description));
      }
      baos.write(ByteUtils.fromInt32(chrField.get()));
    }
    if (registerVal == null) {
      int immediate = Long.decode(parts[0]).intValue(); // write immediate value
      baos.write(ByteUtils.fromInt32(immediate));
    }
    return new OperandBytes(flag, baos.toByteArray());
  }

  /**
   * Load effective address sum with offset
   *
   * @param description The description to parse.
   * @return The bytes of the operands.
   * @throws IOException If any I/O exception occurs.
   */
  private static OperandBytes parseEASumPlusOffsetDescription(String description) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte flag = (byte) 0x80;
    String[] parts = description.split(" \\+ ");
    if (parts.length != 3) {
      throw new IOException("Missing three parts for EA sum plus offset: " + description);
    }
    String first = parts[0].startsWith("*") ? parts[0].substring(1) : parts[0];
    String second = parts[1].startsWith("*") ? parts[1].substring(1) : parts[1];
    String third = parts[2].startsWith("*") ? parts[2].substring(1) : parts[2];

    // Handle first part
    Byte registerVal = OperandParser.getByte(first);
    if (registerVal == null) {
      flag = (byte) (flag | (byte)0x3f); // immediate value
    } else {
      flag = (byte) (flag | registerVal);
    }

    // Handle second part
    ByteArrayOutputStream secondBytes = new ByteArrayOutputStream();
    Byte secondRegisterVal = OperandParser.getByte(second);
    if (secondRegisterVal == null) {
      throw new IOException("Failed to find register for operand " + second);
    }
    secondBytes.write(0);
    secondBytes.write(secondRegisterVal);

    // Handle third part
    ByteArrayOutputStream thirdBytes = new ByteArrayOutputStream();
    int value = Long.decode(third).intValue();
    thirdBytes.write(ByteUtils.fromUint16(value));

    // Parts are written out in reverse order
    baos.write(thirdBytes.toByteArray());
    baos.write(secondBytes.toByteArray());
    if (registerVal == null) {
      int immediate = Long.decode(first).intValue(); // write immediate value
      baos.write(ByteUtils.fromInt32(immediate));
    }
    return new OperandBytes(flag, baos.toByteArray());
  }
}
