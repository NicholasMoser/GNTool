package com.github.nicholasmoser.gnt4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import com.github.nicholasmoser.Code;

/**
 * A singleton object representing the different GNT4 options and codes, as well as the ability
 * to execute them.
 */
public class GNT4Codes {
  private static final String MAIN_DOL = "sys/main.dol";
  private static final String TITLE_SEQ = "files/maki/m_title.seq";
  private static final String CSS_SEQ = "files/maki/char_sel.seq";
  private static final String CSS_4P_SEQ = "files/maki/charsel4.seq";
  private static GNT4Codes INSTANCE;

  // https://github.com/NicholasMoser/GNTool#audio-fix
  private GNT4Code audioFix;

  // https://github.com/NicholasMoser/GNTool#skip-cutscenes
  private GNT4Code skipCutscenes1;
  private GNT4Code skipCutscenes2;
  private GNT4Code skipCutscenes3;

  // https://github.com/NicholasMoser/GNTool#character-selection-speed
  private GNT4Code cssInitialSpeed_1v1_1p;
  private GNT4Code cssInitialSpeed_1v1_2p;
  private GNT4Code cssInitialSpeed_ffa_1p;
  private GNT4Code cssInitialSpeed_ffa_2p;
  private GNT4Code cssInitialSpeed_ffa_3p;
  private GNT4Code cssInitialSpeed_ffa_4p;
  private GNT4Code cssMaxSpeed_1v1_1p;
  private GNT4Code cssMaxSpeed_1v1_2p;
  private GNT4Code cssMaxSpeed_ffa_1p;
  private GNT4Code cssMaxSpeed_ffa_2p;
  private GNT4Code cssMaxSpeed_ffa_3p;
  private GNT4Code cssMaxSpeed_ffa_4p;

  // https://github.com/NicholasMoser/GNTool#title-timeout-to-demo
  private GNT4Code demoTimeOut;

  /**
   * Private constructor to enforce singleton pattern.
   */
  private GNT4Codes() {
    audioFix = new GNT4Code(MAIN_DOL, 0x16CC0C, new byte[]{0x41, (byte) 0x82, 0x00, 0x20}, new byte[]{0x48, 0x00, 0x00, 0x21});
    skipCutscenes1 = new GNT4Code(MAIN_DOL, 0x9B14, new byte[]{0x48, 0x0B, (byte) 0xF5, 0x41}, new byte[]{0x60, 0x00, 0x00, 0x00});
    skipCutscenes2 = new GNT4Code(MAIN_DOL, 0x9B28, new byte[]{0x48, 0x0B, (byte) 0xF5, 0x2D}, new byte[]{0x60, 0x00, 0x00, 0x00});
    skipCutscenes3 = new GNT4Code(MAIN_DOL, 0x9B3C, new byte[]{0x48, 0x0B, (byte) 0xF5, 0x19}, new byte[]{0x60, 0x00, 0x00, 0x00});
    cssInitialSpeed_1v1_1p = new GNT4Code(CSS_SEQ, 0x531C);
    cssInitialSpeed_1v1_2p = new GNT4Code(CSS_SEQ, 0x9078);
    cssInitialSpeed_ffa_1p = new GNT4Code(CSS_4P_SEQ, 0x82BC);
    cssInitialSpeed_ffa_2p = new GNT4Code(CSS_4P_SEQ, 0x8680);
    cssInitialSpeed_ffa_3p = new GNT4Code(CSS_4P_SEQ, 0x8A54);
    cssInitialSpeed_ffa_4p = new GNT4Code(CSS_4P_SEQ, 0x8E28);
    cssMaxSpeed_1v1_1p = new GNT4Code(CSS_SEQ, 0x4D80);
    cssMaxSpeed_1v1_2p = new GNT4Code(CSS_SEQ, 0x8AFC);
    cssMaxSpeed_ffa_1p = new GNT4Code(CSS_4P_SEQ, 0x5468);
    cssMaxSpeed_ffa_2p = new GNT4Code(CSS_4P_SEQ, 0x5B48);
    cssMaxSpeed_ffa_3p = new GNT4Code(CSS_4P_SEQ, 0x6468);
    cssMaxSpeed_ffa_4p = new GNT4Code(CSS_4P_SEQ, 0x6F48);
    demoTimeOut = new GNT4Code(TITLE_SEQ, 0x1B6E4);
  }

  /**
   * @return The singleton instance of GNT4Codes.
   */
  public static GNT4Codes getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new GNT4Codes();
    }
    return INSTANCE;
  }

  /**
   * Returns the initial character select speed. Technically it only returns it from
   * the 2-player character select menu, but there should not be a difference between
   * the speed of the 2-player character select menu and the 4-player one.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @return The initial character select speed.
   * @throws IOException If an I/O error occurs.
   */
  public int getCssInitialSpeed(Path uncompressedDirectory) throws IOException {
    Path filePath = uncompressedDirectory.resolve(cssInitialSpeed_1v1_1p.getFilePath());
    int offset = cssInitialSpeed_1v1_1p.getOffset();
    byte[] bytes = readWord(filePath, offset);
    ByteBuffer wrapped = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    return wrapped.getInt();
  }

  /**
   * Returns the max character select speed. Technically it only returns it from
   * the 2-player character select menu, but there should not be a difference between
   * the speed of the 2-player character select menu and the 4-player one.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @return The max character select speed.
   * @throws IOException If an I/O error occurs.
   */
  public int getCssMaxSpeed(Path uncompressedDirectory) throws IOException {
    Path filePath = uncompressedDirectory.resolve(cssMaxSpeed_1v1_1p.getFilePath());
    int offset = cssMaxSpeed_1v1_1p.getOffset();
    byte[] bytes = readWord(filePath, offset);
    ByteBuffer wrapped = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    return wrapped.getInt();
  }

  /**
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @return The amount of seconds in the title screen before it transitions to the demo screen.
   * @throws IOException If an I/O error occurs.
   */
  public int getTitleDemoTimeout(Path uncompressedDirectory) throws IOException {
    Path filePath = uncompressedDirectory.resolve(demoTimeOut.getFilePath());
    int offset = demoTimeOut.getOffset();
    byte[] bytes = readWord(filePath, offset);
    ByteBuffer wrapped = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    int frames = wrapped.getInt();
    return framesToSeconds(frames);
  }

  /**
   * Sets the Initial speed of the character select screen.
   * 1 is extremely fast and 15 is extremely slow.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @param value The initial speed of the character select screen.
   * @throws IOException If an I/O error occurs.
   */
  public void setCssInitialSpeed(Path uncompressedDirectory, int value) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
    byte[] bytes = buffer.putInt(value).array();
    Path dolPath2p = uncompressedDirectory.resolve(cssInitialSpeed_1v1_1p.getFilePath());
    int cssInitialSpeedOffset_1v1_1p = cssInitialSpeed_1v1_1p.getOffset();
    int cssInitialSpeedOffset_1v1_2p = cssInitialSpeed_1v1_2p.getOffset();
    Code.getBuilder()
        .withOverwrite(cssInitialSpeedOffset_1v1_1p, bytes)
        .withOverwrite(cssInitialSpeedOffset_1v1_2p, bytes)
        .execute(dolPath2p);
    Path dolPath4p = uncompressedDirectory.resolve(cssInitialSpeed_ffa_1p.getFilePath());
    int cssInitialSpeedOffset_ffa_1p = cssInitialSpeed_ffa_1p.getOffset();
    int cssInitialSpeedOffset_ffa_2p = cssInitialSpeed_ffa_2p.getOffset();
    int cssInitialSpeedOffset_ffa_3p = cssInitialSpeed_ffa_3p.getOffset();
    int cssInitialSpeedOffset_ffa_4p = cssInitialSpeed_ffa_4p.getOffset();
    Code.getBuilder()
        .withOverwrite(cssInitialSpeedOffset_ffa_1p, bytes)
        .withOverwrite(cssInitialSpeedOffset_ffa_2p, bytes)
        .withOverwrite(cssInitialSpeedOffset_ffa_3p, bytes)
        .withOverwrite(cssInitialSpeedOffset_ffa_4p, bytes)
        .execute(dolPath4p);
  }

  /**
   * Sets the maximum speed of the character select screen.
   * 1 is extremely fast and 15 is extremely slow.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @param value The maximum speed of the character select screen.
   * @throws IOException If an I/O error occurs.
   */
  public void setCssMaxSpeed(Path uncompressedDirectory, int value) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
    byte[] bytes = buffer.putInt(value).array();
    Path dolPath2p = uncompressedDirectory.resolve(cssMaxSpeed_1v1_1p.getFilePath());
    int cssMaxSpeedOffset_1v1_1p = cssMaxSpeed_1v1_1p.getOffset();
    int cssMaxSpeedOffset_1v1_2p = cssMaxSpeed_1v1_2p.getOffset();
    Code.getBuilder()
        .withOverwrite(cssMaxSpeedOffset_1v1_1p, bytes)
        .withOverwrite(cssMaxSpeedOffset_1v1_2p, bytes)
        .execute(dolPath2p);
    Path dolPath4p = uncompressedDirectory.resolve(cssMaxSpeed_ffa_1p.getFilePath());
    int cssMaxSpeedOffset_ffa_1p = cssMaxSpeed_ffa_1p.getOffset();
    int cssMaxSpeedOffset_ffa_2p = cssMaxSpeed_ffa_2p.getOffset();
    int cssMaxSpeedOffset_ffa_3p = cssMaxSpeed_ffa_3p.getOffset();
    int cssMaxSpeedOffset_ffa_4p = cssMaxSpeed_ffa_4p.getOffset();
    Code.getBuilder()
        .withOverwrite(cssMaxSpeedOffset_ffa_1p, bytes)
        .withOverwrite(cssMaxSpeedOffset_ffa_2p, bytes)
        .withOverwrite(cssMaxSpeedOffset_ffa_3p, bytes)
        .withOverwrite(cssMaxSpeedOffset_ffa_4p, bytes)
        .execute(dolPath4p);
  }

  /**
   * Sets the title demo timeout in seconds.
   * The seconds will be converted to frames when inserted into the game.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @param value The title demo timeout in seconds.
   * @throws IOException If an I/O error occurs.
   */
  public void setTitleDemoTimeout(Path uncompressedDirectory, int value) throws IOException {
    int frames = secondsToFrames(value);
    ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
    byte[] bytes = buffer.putInt(frames).array();
    Path dolPath = uncompressedDirectory.resolve(demoTimeOut.getFilePath());
    int demoTimeOffset = demoTimeOut.getOffset();
    Code.getBuilder().withOverwrite(demoTimeOffset, bytes).execute(dolPath);
  }

  /**
   * Returns whether or not the code to fix audio for different ISO file offsets is activated.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public boolean isAudioFixCodeActivated(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(audioFix.getFilePath());
    int soundCheckOffset = audioFix.getOffset();
    byte[] bytes = readWord(dolPath, soundCheckOffset);
    byte[] patchedInstruction = audioFix.getNewInstruction();
    return Arrays.equals(bytes, patchedInstruction);
  }

  /**
   * Activates the code to fix audio for different ISO file offsets.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public void activateAudioFixCode(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(audioFix.getFilePath());
    byte[] newInstruction = audioFix.getNewInstruction();
    int soundCheckOffset = audioFix.getOffset();
    Code.getBuilder().withOverwrite(soundCheckOffset, newInstruction).execute(dolPath);
  }

  /**
   * Inactivates the code to fix audio for different ISO file offsets.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public void inactivateAudioFixCode(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(audioFix.getFilePath());
    byte[] oldInstruction = audioFix.getOldInstruction();
    int soundCheckOffset = audioFix.getOffset();
    Code.getBuilder().withOverwrite(soundCheckOffset, oldInstruction).execute(dolPath);
  }

  /**
   * Returns whether or not the code to skip cutscenes is activated. The code
   * is in three locations so this only returns true if the code is in all three locations.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public boolean isSkipCutscenesCodeActivated(Path uncompressedDirectory) throws IOException {
    Path dolPath1 = uncompressedDirectory.resolve(skipCutscenes1.getFilePath());
    int soundCheckOffset1 = skipCutscenes1.getOffset();
    byte[] bytes1 = readWord(dolPath1, soundCheckOffset1);
    byte[] patchedInstruction1 = skipCutscenes1.getNewInstruction();
    if (Arrays.equals(bytes1, patchedInstruction1)) {
      Path dolPath2 = uncompressedDirectory.resolve(skipCutscenes2.getFilePath());
      int soundCheckOffset2 = skipCutscenes2.getOffset();
      byte[] bytes2 = readWord(dolPath2, soundCheckOffset2);
      byte[] patchedInstruction2 = skipCutscenes2.getNewInstruction();
      if (Arrays.equals(bytes2, patchedInstruction2)) {
        Path dolPath3 = uncompressedDirectory.resolve(skipCutscenes3.getFilePath());
        int soundCheckOffset3 = skipCutscenes3.getOffset();
        byte[] bytes3 = readWord(dolPath3, soundCheckOffset3);
        byte[] patchedInstruction3 = skipCutscenes3.getNewInstruction();
        return Arrays.equals(bytes3, patchedInstruction3);
      }
    }
    return false;
  }

  /**
   * Activates the code to skip cutscenes.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public void activateSkipCutscenesCode(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(skipCutscenes1.getFilePath());
    int firstCutsceneOffset = skipCutscenes1.getOffset();
    int secondCutsceneOffset = skipCutscenes2.getOffset();
    int thirdCutsceneOffset = skipCutscenes3.getOffset();
    byte[] instruction = skipCutscenes1.getNewInstruction();
    Code.getBuilder().withOverwrite(firstCutsceneOffset, instruction)
        .withOverwrite(secondCutsceneOffset, instruction)
        .withOverwrite(thirdCutsceneOffset, instruction).execute(dolPath);
  }

  /**
   * Inactivates the code to skip cutscenes.
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public void inactivateSkipCutscenesCode(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(MAIN_DOL);
    int firstCutsceneOffset = skipCutscenes1.getOffset();
    int secondCutsceneOffset = skipCutscenes2.getOffset();
    int thirdCutsceneOffset = skipCutscenes3.getOffset();
    byte[] firstInstruction = skipCutscenes1.getOldInstruction();
    byte[] secondInstruction = skipCutscenes2.getOldInstruction();
    byte[] thirdInstruction = skipCutscenes3.getOldInstruction();
    Code.getBuilder().withOverwrite(firstCutsceneOffset, firstInstruction)
        .withOverwrite(secondCutsceneOffset, secondInstruction)
        .withOverwrite(thirdCutsceneOffset, thirdInstruction).execute(dolPath);
  }

  /**
   * Reads a 4-byte word from the given filePath starting from the given offset.
   * @param filePath The file path.
   * @param offset The offset to begin reading at.
   * @return The 4-byte word.
   * @throws IOException If an I/O error occurs.
   */
  private byte[] readWord(Path filePath, int offset) throws IOException {
    try(InputStream is = Files.newInputStream(filePath)) {
      if (is.skip(offset) != offset) {
        String message = String.format("Failed to read to %d of %s", offset, filePath);
        throw new IOException(message);
      }
      byte[] bytes = new byte[4];
      if (is.read(bytes) != 4) {
        String message = String.format("Failed to read TitleDemoTimeout of %s", filePath);
        throw new IOException(message);
      }
      return bytes;
    }
  }

  /**
   * Converts a number of frames to a number of seconds.
   * GNT4 runs at 60 frames per second, therefore there are 60 frames in a second.
   * @param frames The number of frames to convert.
   * @return The number of seconds.
   */
  private int framesToSeconds(int frames) {
    return frames / 60;
  }

  /**
   * Converts a number of seconds to a number of frames.
   * GNT4 runs at 60 frames per second, therefore there are 60 frames in a second.
   * @param seconds The number of seconds to convert.
   * @return The number of frames.
   */
  private int secondsToFrames(int seconds) {
    return seconds * 60;
  }
}
