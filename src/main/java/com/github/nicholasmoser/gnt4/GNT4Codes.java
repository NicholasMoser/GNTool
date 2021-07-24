package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.Code;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A singleton object representing the different GNT4 options and codes, as well as the ability to
 * execute them.
 */
public class GNT4Codes {

  // Game files to apply codes to
  public static final String MAIN_DOL = "sys/main.dol";
  public static final String TITLE_SEQ = "files/maki/m_title.seq";
  public static final String CSS_SEQ = "files/maki/char_sel.seq";
  public static final String CSS_4P_SEQ = "files/maki/charsel4.seq";

  // https://github.com/NicholasMoser/GNTool#audio-fix
  public static final GNT4Code AUDIO_FIX = new GNT4Code(MAIN_DOL, 0x16CC0C,
      new byte[]{0x41, (byte) 0x82, 0x00, 0x20},
      new byte[]{0x48, 0x00, 0x00, 0x21});

  // https://github.com/NicholasMoser/GNTool#skip-cutscenes
  public static final GNT4Code SKIP_CUTSCENES_1 = new GNT4Code(MAIN_DOL, 0x9B14,
      new byte[]{0x48, 0x0B, (byte) 0xF5, 0x41},
      new byte[]{0x60, 0x00, 0x00, 0x00});
  public static final GNT4Code SKIP_CUTSCENES_2 = new GNT4Code(MAIN_DOL, 0x9B28,
      new byte[]{0x48, 0x0B, (byte) 0xF5, 0x2D},
      new byte[]{0x60, 0x00, 0x00, 0x00});
  public static final GNT4Code SKIP_CUTSCENES_3 = new GNT4Code(MAIN_DOL, 0x9B3C,
      new byte[]{0x48, 0x0B, (byte) 0xF5, 0x19},
      new byte[]{0x60, 0x00, 0x00, 0x00});
  public static final List<GNT4Code> SKIP_CUTSCENES = Arrays
      .asList(SKIP_CUTSCENES_1, SKIP_CUTSCENES_2, SKIP_CUTSCENES_3);

  // https://github.com/NicholasMoser/GNTool#character-selection-speed
  public static final GNT4Code CSS_INITIAL_SPEED_1V1_1P = new GNT4Code(CSS_SEQ, 0x531C);
  public static final GNT4Code CSS_INITIAL_SPEED_1V1_2P = new GNT4Code(CSS_SEQ, 0x9078);
  public static final GNT4Code CSS_INITIAL_SPEED_FFA_1P = new GNT4Code(CSS_4P_SEQ, 0x82BC);
  public static final GNT4Code CSS_INITIAL_SPEED_FFA_2P = new GNT4Code(CSS_4P_SEQ, 0x8680);
  public static final GNT4Code CSS_INITIAL_SPEED_FFA_3P = new GNT4Code(CSS_4P_SEQ, 0x8A54);
  public static final GNT4Code CSS_INITIAL_SPEED_FFA_4P = new GNT4Code(CSS_4P_SEQ, 0x8E28);
  public static final List<GNT4Code> INITIAL_SPEEDS_1V1 = Arrays
      .asList(CSS_INITIAL_SPEED_1V1_1P, CSS_INITIAL_SPEED_1V1_2P);
  public static final List<GNT4Code> INITIAL_SPEEDS_FFA = Arrays
      .asList(CSS_INITIAL_SPEED_FFA_1P, CSS_INITIAL_SPEED_FFA_2P, CSS_INITIAL_SPEED_FFA_3P,
          CSS_INITIAL_SPEED_FFA_4P);
  public static final GNT4Code CSS_MAX_SPEED_1V1_1P = new GNT4Code(CSS_SEQ, 0x4D80);
  public static final GNT4Code CSS_MAX_SPEED_1V1_2P = new GNT4Code(CSS_SEQ, 0x8AFC);
  public static final GNT4Code CSS_MAX_SPEED_FFA_1P = new GNT4Code(CSS_4P_SEQ, 0x5468);
  public static final GNT4Code CSS_MAX_SPEED_FFA_2P = new GNT4Code(CSS_4P_SEQ, 0x5B48);
  public static final GNT4Code CSS_MAX_SPEED_FFA_3P = new GNT4Code(CSS_4P_SEQ, 0x6468);
  public static final GNT4Code CSS_MAX_SPEED_FFA_4P = new GNT4Code(CSS_4P_SEQ, 0x6F48);
  public static final List<GNT4Code> MAX_SPEEDS_1V1 = Arrays
      .asList(CSS_MAX_SPEED_1V1_1P, CSS_MAX_SPEED_1V1_2P);
  public static final List<GNT4Code> MAX_SPEEDS_FFA = Arrays
      .asList(CSS_MAX_SPEED_FFA_1P, CSS_MAX_SPEED_FFA_2P, CSS_MAX_SPEED_FFA_3P,
          CSS_MAX_SPEED_FFA_4P);

  // https://github.com/NicholasMoser/GNTool#title-timeout-to-demo
  public static final GNT4Code DEMO_TIME_OUT = new GNT4Code(TITLE_SEQ, 0x1B6E4);

  // https://github.com/NicholasMoser/GNTool#main-menu-character
  public static final GNT4Code MAIN_MENU_CHARACTER = new GNT4Code(TITLE_SEQ, 0x1612C);
  public static final GNT4Code MAIN_MENU_CHARACTER_HEIGHT = new GNT4Code(TITLE_SEQ, 0x161C3);
  public static final GNT4Code MAIN_MENU_CHARACTER_SOUND = new GNT4Code(TITLE_SEQ, 0x1BE67);

  // https://github.com/NicholasMoser/GNTool#play-audio-while-paused
  public static final GNT4Code PLAY_AUDIO_WHILE_PAUSED_MISSION_MODE = new GNT4Code(MAIN_DOL,
      0x4412C,
      new byte[]{0x38, 0x60, 0x00, 0x00},
      new byte[]{0x38, 0x60, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code PLAY_AUDIO_WHILE_PAUSED_TRAINING_MODE = new GNT4Code(MAIN_DOL,
      0x41D28,
      new byte[]{0x38, 0x60, 0x00, 0x00},
      new byte[]{0x38, 0x60, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code PLAY_AUDIO_WHILE_PAUSED_OTHER_MODES = new GNT4Code(MAIN_DOL, 0x447FC,
      new byte[]{0x38, 0x60, 0x00, 0x00},
      new byte[]{0x38, 0x60, (byte) 0xFF, (byte) 0xFF});
  public static final List<GNT4Code> PLAY_AUDIO_WHILE_PAUSED = Arrays
      .asList(PLAY_AUDIO_WHILE_PAUSED_MISSION_MODE, PLAY_AUDIO_WHILE_PAUSED_TRAINING_MODE,
          PLAY_AUDIO_WHILE_PAUSED_OTHER_MODES);

  // https://github.com/NicholasMoser/GNTool#no-slow-down-on-kill
  public static final GNT4Code NO_SLOWDOWN_ON_KILL = new GNT4Code(MAIN_DOL, 0x11868,
      new byte[]{(byte) 0xB0, 0x65, 0x00, 0x00},
      new byte[]{0x60, 0x00, 0x00, 0x00});

  // https://github.com/NicholasMoser/GNTool#unlock-all
  public static final GNT4Code UNLOCK_ALL_1 = new GNT4Code(MAIN_DOL, 0x7BB0,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x60},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_2 = new GNT4Code(MAIN_DOL, 0x7BDC,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x5c},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_3 = new GNT4Code(MAIN_DOL, 0x7BF0,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x58},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_4 = new GNT4Code(MAIN_DOL, 0x7C34,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x54},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_5 = new GNT4Code(MAIN_DOL, 0x7C48,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x50},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_6 = new GNT4Code(MAIN_DOL, 0x7C8C,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x4c},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_7 = new GNT4Code(MAIN_DOL, 0x7CA0,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x48},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_8 = new GNT4Code(MAIN_DOL, 0x7CE4,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x44},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_9 = new GNT4Code(MAIN_DOL, 0x7CF8,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x40},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_10 = new GNT4Code(MAIN_DOL, 0x7D0C,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x3c},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_11 = new GNT4Code(MAIN_DOL, 0x7D68,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x38},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_STAGES = new GNT4Code(MAIN_DOL, 0x7D7C,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x34},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_CHARACTERS_1 = new GNT4Code(MAIN_DOL, 0x7DC0,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x30},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_CHARACTERS_2 = new GNT4Code(MAIN_DOL, 0x7DD4,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x2c},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_15 = new GNT4Code(MAIN_DOL, 0x7E18,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x28},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_OPTIONS = new GNT4Code(MAIN_DOL, 0x7E2C,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x24},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_17 = new GNT4Code(MAIN_DOL, 0x7E70,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x20},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_18 = new GNT4Code(MAIN_DOL, 0x7E84,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x1c},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_19 = new GNT4Code(MAIN_DOL, 0x7EC8,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x18},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final GNT4Code UNLOCK_ALL_20 = new GNT4Code(MAIN_DOL, 0x7EDC,
      new byte[]{(byte) 0x80, 0x04, 0x01, 0x14},
      new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
  public static final List<GNT4Code> UNLOCK_ALL_CODES = Arrays
      .asList(UNLOCK_ALL_1, UNLOCK_ALL_2, UNLOCK_ALL_3, UNLOCK_ALL_4, UNLOCK_ALL_5, UNLOCK_ALL_6,
          UNLOCK_ALL_7, UNLOCK_ALL_8, UNLOCK_ALL_9, UNLOCK_ALL_10, UNLOCK_ALL_11, UNLOCK_STAGES,
          UNLOCK_CHARACTERS_1, UNLOCK_CHARACTERS_2, UNLOCK_ALL_15, UNLOCK_OPTIONS, UNLOCK_ALL_17,
          UNLOCK_ALL_18, UNLOCK_ALL_19, UNLOCK_ALL_20);

  // https://github.com/NicholasMoser/GNTool#ZTK-Damage-Taken-Multiplier
  public static final GNT4Code ZTK_DAMAGE_TAKEN_MULTIPLIER = new GNT4Code(MAIN_DOL, 0x220C90);
  // https://github.com/NicholasMoser/GNTool#Ukon-Damage-Taken-Multiplier
  public static final GNT4Code UKON_DAMAGE_TAKEN_MULTIPLIER = new GNT4Code(MAIN_DOL, 0x220C94);

  // https://github.com/NicholasMoser/GNTool#Widescreen
  public static final GNT4Code PERSPECTIVE_INSTRUCTION = new GNT4Code(MAIN_DOL, 0x16B15C,
      new byte[]{(byte) 0xff, (byte) 0xa0, 0x10, (byte) 0x90},
      new byte[]{(byte) 0xc3, (byte) 0xa2, (byte) 0xa0, 0x24});
  public static final GNT4Code WIDESCREEN_VALUE = new GNT4Code(MAIN_DOL, 0x2220C4,
      new byte[]{0x3f, (byte) 0xa2, (byte) 0x8f, 0x5c},
      new byte[]{0x3f, (byte) 0xe3, (byte) 0x8e, 0x39});
  public static final List<GNT4Code> WIDESCREEN_CODES = Arrays
      .asList(PERSPECTIVE_INSTRUCTION, WIDESCREEN_VALUE);

  // https://github.com/NicholasMoser/GNTool#X-Does-Not-Break-Throws
  public static final GNT4Code X_DOES_NOT_BREAK_THROWS = new GNT4Code(MAIN_DOL, 0x602F0,
      new byte[]{0x70, 0x00, 0x22, 0x30},
      new byte[]{0x70, 0x00, 0x02, 0x30});

  // https://github.com/NicholasMoser/GNTool#Load-Character-Models-in-Character-Select-Screen
  public static final GNT4Code CSS_LOAD_CHR_MODELS_P1 = new GNT4Code(CSS_SEQ, 0x4DC8);
  public static final GNT4Code CSS_LOAD_CHR_MODELS_P2 = new GNT4Code(CSS_SEQ, 0x8B4C);
  public static final GNT4Code CSS_FFA_LOAD_CHR_MODELS_P1 = new GNT4Code(CSS_4P_SEQ, 0x54C8);
  public static final GNT4Code CSS_FFA_LOAD_CHR_MODELS_P2 = new GNT4Code(CSS_4P_SEQ, 0x5CE4);
  public static final GNT4Code CSS_FFA_LOAD_CHR_MODELS_P3 = new GNT4Code(CSS_4P_SEQ, 0x65FC);
  public static final GNT4Code CSS_FFA_LOAD_CHR_MODELS_P4 = new GNT4Code(CSS_4P_SEQ, 0x70D4);
  public static final List<GNT4Code> CSS_LOAD_CHR_MODELS = Arrays.asList(CSS_LOAD_CHR_MODELS_P1, CSS_LOAD_CHR_MODELS_P2);
  public static final List<GNT4Code> CSS_FFA_LOAD_CHR_MODELS = Arrays.asList(CSS_FFA_LOAD_CHR_MODELS_P1, CSS_FFA_LOAD_CHR_MODELS_P2, CSS_FFA_LOAD_CHR_MODELS_P3, CSS_FFA_LOAD_CHR_MODELS_P4);

  private final Path uncompressedDirectory;

  public GNT4Codes(Path uncompressedDirectory) {
    this.uncompressedDirectory = uncompressedDirectory;
  }

  /**
   * Reads the bytes from the file at the offset the code references.
   *
   * @param code The code to get bytes for.
   * @return The bytes from the file at the offset the code references.
   * @throws IOException If an I/O error occurs.
   */
  public byte[] getCodeBytes(GNT4Code code) throws IOException {
    Path filePath = uncompressedDirectory.resolve(code.getFilePath());
    return readBytes(filePath, code.getOffset(), code.getLength());
  }

  /**
   * Reads the int from the file at the offset the code references.
   *
   * @param code The code to get the int for.
   * @return The int from the file at the offset the code references.
   * @throws IOException If an I/O error occurs.
   */
  public int getCodeInt(GNT4Code code) throws IOException {
    Path filePath = uncompressedDirectory.resolve(code.getFilePath());
    byte[] bytes = readBytes(filePath, code.getOffset(), code.getLength());
    return ByteUtils.toInt32(bytes);
  }

  /**
   * Sets the given bytes to the file at the offset the code references.
   *
   * @param code  The code to set bytes for.
   * @param bytes The bytes to write to file.
   * @throws IOException If an I/O error occurs.
   */
  public void setCodeBytes(GNT4Code code, byte[] bytes) throws IOException {
    Path filePath = uncompressedDirectory.resolve(code.getFilePath());
    int offset = code.getOffset();
    Code.getBuilder().withOverwrite(offset, bytes).execute(filePath);
  }

  /**
   * Sets the given bytes to the files at the offsets the codes references.
   *
   * @param codes The codes to set bytes for.
   * @param bytes The bytes to write to files.
   * @throws IOException If an I/O error occurs.
   */
  public void setCodeBytes(List<GNT4Code> codes, byte[] bytes) throws IOException {
    Path filePath = uncompressedDirectory.resolve(codes.get(0).getFilePath());
    Code builder = Code.getBuilder();
    for (GNT4Code code : codes) {
      int offset = code.getOffset();
      builder.withOverwrite(offset, bytes);
    }
    builder.execute(filePath);
  }

  /**
   * Sets the given int to the file at the offset the code references.
   *
   * @param code  The code to set the int for.
   * @param value The int to write to file.
   * @throws IOException If an I/O error occurs.
   */
  public void setCodeInt(GNT4Code code, int value) throws IOException {
    Path filePath = uncompressedDirectory.resolve(code.getFilePath());
    int offset = code.getOffset();
    byte[] bytes = ByteUtils.fromInt32(value);
    Code.getBuilder().withOverwrite(offset, bytes).execute(filePath);
  }

  /**
   * Sets the given int to the files at the offsets the codes references.
   *
   * @param codes The codes to set the int for.
   * @param value The int to write to files.
   * @throws IOException If an I/O error occurs.
   */
  public void setCodeInt(List<GNT4Code> codes, int value) throws IOException {
    Path filePath = uncompressedDirectory.resolve(codes.get(0).getFilePath());
    Code builder = Code.getBuilder();
    for (GNT4Code code : codes) {
      int offset = code.getOffset();
      byte[] bytes = ByteUtils.fromInt32(value);
      builder.withOverwrite(offset, bytes);
    }
    builder.execute(filePath);
  }

  /**
   * Returns whether or not a code is activated. This means checking if the bytes at the offset of
   * the file the code references matched the patched bytes of the code.
   *
   * @param code The code to see whether or not it is activated.
   * @return If the code is activated.
   * @throws IOException If an I/O error occurs.
   */
  public boolean isCodeActivated(GNT4Code code) throws IOException {
    Path filePath = uncompressedDirectory.resolve(code.getFilePath());
    int offset = code.getOffset();
    int length = code.getLength();
    byte[] actualBytes = readBytes(filePath, offset, length);
    byte[] patchedBytes = code.getNewInstruction();
    return Arrays.equals(actualBytes, patchedBytes);
  }

  /**
   * Returns whether or not a list codes are activated. This means checking if the bytes at each
   * offset of each file the codes reference match the patched bytes of each code. All codes must
   * match for the code to be activated.
   *
   * @param codes The codes to see whether or not are all activated.
   * @return If the code is activated.
   * @throws IOException If an I/O error occurs.
   */
  public boolean isCodeActivated(List<GNT4Code> codes) throws IOException {
    Path filePath = uncompressedDirectory.resolve(codes.get(0).getFilePath());
    Map<GNT4Code, byte[]> codeToWord = readWords(filePath, codes);
    for (Map.Entry<GNT4Code, byte[]> entry : codeToWord.entrySet()) {
      byte[] actualBytes = entry.getValue();
      byte[] patchedBytes = entry.getKey().getNewInstruction();
      if (!Arrays.equals(actualBytes, patchedBytes)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Activates a code. This means writing the patched bytes to the offset of the file referenced by
   * the code.
   *
   * @param code The code to activate.
   * @throws IOException If an I/O error occurs.
   */
  public void activateCode(GNT4Code code) throws IOException {
    Path filePath = uncompressedDirectory.resolve(code.getFilePath());
    byte[] patchedBytes = code.getNewInstruction();
    int offset = code.getOffset();
    Code.getBuilder().withOverwrite(offset, patchedBytes).execute(filePath);
  }

  /**
   * Activates a List of codes. This means writing the patched bytes to each offset of each file
   * referenced by each code.
   *
   * @param codes The codes to activate.
   * @throws IOException If an I/O error occurs.
   */
  public void activateCode(List<GNT4Code> codes) throws IOException {
    Path filePath = uncompressedDirectory.resolve(codes.get(0).getFilePath());
    Code builder = Code.getBuilder();
    for (GNT4Code code : codes) {
      byte[] patchedBytes = code.getNewInstruction();
      int offset = code.getOffset();
      builder.withOverwrite(offset, patchedBytes);
    }
    builder.execute(filePath);
  }

  /**
   * Inactivates a code. This means writing the original bytes to the offset of the file referenced
   * by the code.
   *
   * @param code The code to inactivate.
   * @throws IOException If an I/O error occurs.
   */
  public void inactivateCode(GNT4Code code) throws IOException {
    Path filePath = uncompressedDirectory.resolve(code.getFilePath());
    byte[] oldBytes = code.getOldInstruction();
    int offset = code.getOffset();
    Code.getBuilder().withOverwrite(offset, oldBytes).execute(filePath);
  }

  /**
   * Inactivates a List of codes. This means writing the original bytes to each offset of each file
   * referenced by each code.
   *
   * @param codes The codes to inactivate.
   * @throws IOException If an I/O error occurs.
   */
  public void inactivateCode(List<GNT4Code> codes) throws IOException {
    Path filePath = uncompressedDirectory.resolve(codes.get(0).getFilePath());
    Code builder = Code.getBuilder();
    for (GNT4Code code : codes) {
      byte[] oldBytes = code.getOldInstruction();
      int offset = code.getOffset();
      builder.withOverwrite(offset, oldBytes);
    }
    builder.execute(filePath);
  }

  /**
   * Reads <code>length</code> number of bytes from <code>offset</code> of <code>filePath</code>.
   *
   * @param filePath The file to read bytes from.
   * @param offset   The offset of the file to read bytes from.
   * @param length   The number of bytes to read.
   * @return The bytes read.
   * @throws IOException If an I/O error occurs.
   */
  private byte[] readBytes(Path filePath, int offset, int length) throws IOException {
    try (InputStream is = Files.newInputStream(filePath)) {
      if (is.skip(offset) != offset) {
        throw new IOException(String.format("Failed to skip to offset %d of %s", offset, filePath));
      }
      byte[] bytes = new byte[length];
      if (is.read(bytes) != length) {
        throw new IOException("Failed to read bytes from " + filePath);
      }
      return bytes;
    }
  }

  /**
   * Reads 4-byte words from the given filePath from the given codes. The words will be returned as
   * a mapping of the code to its word. The codes will be sorted by offset. Please ensure that all
   * codes correspond to the same file path or undefined behavior may occur.
   *
   * @param filePath The file path.
   * @param codes    The list of GNT4Codes.
   * @return The mapping of a GNT4Code to its current word in the file.
   * @throws IOException If an I/O error occurs.
   */
  private Map<GNT4Code, byte[]> readWords(Path filePath, List<GNT4Code> codes) throws IOException {
    codes.sort(Comparator.comparingInt(GNT4Code::getOffset));
    Map<GNT4Code, byte[]> offsetToWord = Maps.newHashMapWithExpectedSize(codes.size());
    try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
      for (GNT4Code code : codes) {
        raf.seek(code.getOffset());
        byte[] bytes = new byte[4];
        if (raf.read(bytes) != 4) {
          throw new IOException("Failed to read from " + filePath);
        }
        offsetToWord.put(code, bytes);
      }
      return offsetToWord;
    }
  }

  /**
   * Sets the main menu character. A separate method is needed for this due to some more complicated
   * logic involving reading from {@link GNT4Characters} for values.
   *
   * @param character The name of the main menu character.
   * @throws IOException If an I/O error occurs.
   */
  public void setMainMenuCharacter(String character) throws IOException {
    // Main Menu Character
    int characterId = GNT4Characters.INTERNAL_CHAR_ORDER.get(character);
    byte[] charBytes = ByteUtils.fromInt32(characterId);
    Path dolPath = uncompressedDirectory.resolve(MAIN_MENU_CHARACTER.getFilePath());
    int mainMenuCharacterOffset = MAIN_MENU_CHARACTER.getOffset();

    // Main Menu Character Sound
    byte soundEffect = GNT4Characters.CHAR_SEL_SOUND.get(character);
    int mainMenuCharacterSoundOffset = MAIN_MENU_CHARACTER_SOUND.getOffset();
    byte[] soundByte = new byte[]{soundEffect};

    // Main Menu Character Height
    byte height = GNT4Characters.CHAR_HEIGHT_ADJUST.get(character);
    int mainMenuCharacterHeightOffset = MAIN_MENU_CHARACTER_HEIGHT.getOffset();
    byte[] heightByte = new byte[]{height};

    Code.getBuilder()
        .withOverwrite(mainMenuCharacterOffset, charBytes)
        .withOverwrite(mainMenuCharacterSoundOffset, soundByte)
        .withOverwrite(mainMenuCharacterHeightOffset, heightByte)
        .execute(dolPath);
  }
}
