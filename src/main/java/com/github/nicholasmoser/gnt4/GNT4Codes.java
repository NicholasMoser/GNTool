package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.Code;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

  private static final String MAIN_DOL = "sys/main.dol";
  private static final String TITLE_SEQ = "files/maki/m_title.seq";
  private static final String CSS_SEQ = "files/maki/char_sel.seq";
  private static final String CSS_4P_SEQ = "files/maki/charsel4.seq";
  private static GNT4Codes INSTANCE;

  // https://github.com/NicholasMoser/GNTool#audio-fix
  private final GNT4Code audioFix;

  // https://github.com/NicholasMoser/GNTool#skip-cutscenes
  private final GNT4Code skipCutscenes1;
  private final GNT4Code skipCutscenes2;
  private final GNT4Code skipCutscenes3;

  // https://github.com/NicholasMoser/GNTool#character-selection-speed
  private final GNT4Code cssInitialSpeed_1v1_1p;
  private final GNT4Code cssInitialSpeed_1v1_2p;
  private final GNT4Code cssInitialSpeed_ffa_1p;
  private final GNT4Code cssInitialSpeed_ffa_2p;
  private final GNT4Code cssInitialSpeed_ffa_3p;
  private final GNT4Code cssInitialSpeed_ffa_4p;
  private final GNT4Code cssMaxSpeed_1v1_1p;
  private final GNT4Code cssMaxSpeed_1v1_2p;
  private final GNT4Code cssMaxSpeed_ffa_1p;
  private final GNT4Code cssMaxSpeed_ffa_2p;
  private final GNT4Code cssMaxSpeed_ffa_3p;
  private final GNT4Code cssMaxSpeed_ffa_4p;

  // https://github.com/NicholasMoser/GNTool#title-timeout-to-demo
  private final GNT4Code demoTimeOut;

  // https://github.com/NicholasMoser/GNTool#main-menu-character
  private final GNT4Code mainMenuCharacter;
  private final GNT4Code mainMenuCharacterHeight;
  private final GNT4Code mainMenuCharacterSound;

  // https://github.com/NicholasMoser/GNTool#play-audio-while-paused
  private final GNT4Code playAudioWhilePausedMissionMode;
  private final GNT4Code playAudioWhilePausedTrainingMode;
  private final GNT4Code playAudioWhilePausedOtherModes;

  // https://github.com/NicholasMoser/GNTool#no-slow-down-on-kill
  private final GNT4Code noSlowDownOnKill;

  // https://github.com/NicholasMoser/GNTool#unlock-all
  private final List<GNT4Code> unlockAllCodes;
  private final GNT4Code unlockAll1;
  private final GNT4Code unlockAll2;
  private final GNT4Code unlockAll3;
  private final GNT4Code unlockAll4;
  private final GNT4Code unlockAll5;
  private final GNT4Code unlockAll6;
  private final GNT4Code unlockAll7;
  private final GNT4Code unlockAll8;
  private final GNT4Code unlockAll9;
  private final GNT4Code unlockAll10;
  private final GNT4Code unlockAll11;
  private final GNT4Code unlockStages;
  private final GNT4Code unlockCharacters1;
  private final GNT4Code unlockCharacters2;
  private final GNT4Code unlockAll15;
  private final GNT4Code unlockOptions;
  private final GNT4Code unlockAll17;
  private final GNT4Code unlockAll18;
  private final GNT4Code unlockAll19;
  private final GNT4Code unlockAll20;

  /**
   * Private constructor to enforce singleton pattern.
   */
  private GNT4Codes() {
    audioFix = new GNT4Code(MAIN_DOL, 0x16CC0C, new byte[]{0x41, (byte) 0x82, 0x00, 0x20},
        new byte[]{0x48, 0x00, 0x00, 0x21});
    skipCutscenes1 = new GNT4Code(MAIN_DOL, 0x9B14, new byte[]{0x48, 0x0B, (byte) 0xF5, 0x41},
        new byte[]{0x60, 0x00, 0x00, 0x00});
    skipCutscenes2 = new GNT4Code(MAIN_DOL, 0x9B28, new byte[]{0x48, 0x0B, (byte) 0xF5, 0x2D},
        new byte[]{0x60, 0x00, 0x00, 0x00});
    skipCutscenes3 = new GNT4Code(MAIN_DOL, 0x9B3C, new byte[]{0x48, 0x0B, (byte) 0xF5, 0x19},
        new byte[]{0x60, 0x00, 0x00, 0x00});
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
    mainMenuCharacter = new GNT4Code(TITLE_SEQ, 0x1612C);
    mainMenuCharacterHeight = new GNT4Code(TITLE_SEQ, 0x161C3);
    mainMenuCharacterSound = new GNT4Code(TITLE_SEQ, 0x1BE67);
    playAudioWhilePausedMissionMode = new GNT4Code(MAIN_DOL, 0x4412C,
        new byte[]{0x38, 0x60, 0x00, 0x00},
        new byte[]{0x38, 0x60, (byte) 0xFF, (byte) 0xFF});
    playAudioWhilePausedTrainingMode = new GNT4Code(MAIN_DOL, 0x41D28,
        new byte[]{0x38, 0x60, 0x00, 0x00},
        new byte[]{0x38, 0x60, (byte) 0xFF, (byte) 0xFF});
    playAudioWhilePausedOtherModes = new GNT4Code(MAIN_DOL, 0x447FC,
        new byte[]{0x38, 0x60, 0x00, 0x00},
        new byte[]{0x38, 0x60, (byte) 0xFF, (byte) 0xFF});
    noSlowDownOnKill = new GNT4Code(MAIN_DOL, 0x11868, new byte[]{(byte) 0xB0, 0x65, 0x00, 0x00},
        new byte[]{0x60, 0x00, 0x00, 0x00});

    // Unlock All
    unlockAll1 = new GNT4Code(MAIN_DOL, 0x7BB0, new byte[]{(byte) 0x80, 0x04, 0x01, 0x60},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll2 = new GNT4Code(MAIN_DOL, 0x7BDC, new byte[]{(byte) 0x80, 0x04, 0x01, 0x5c},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll3 = new GNT4Code(MAIN_DOL, 0x7BF0, new byte[]{(byte) 0x80, 0x04, 0x01, 0x58},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll4 = new GNT4Code(MAIN_DOL, 0x7C34, new byte[]{(byte) 0x80, 0x04, 0x01, 0x54},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll5 = new GNT4Code(MAIN_DOL, 0x7C48, new byte[]{(byte) 0x80, 0x04, 0x01, 0x50},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll6 = new GNT4Code(MAIN_DOL, 0x7C8C, new byte[]{(byte) 0x80, 0x04, 0x01, 0x4c},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll7 = new GNT4Code(MAIN_DOL, 0x7CA0, new byte[]{(byte) 0x80, 0x04, 0x01, 0x48},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll8 = new GNT4Code(MAIN_DOL, 0x7CE4, new byte[]{(byte) 0x80, 0x04, 0x01, 0x44},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll9 = new GNT4Code(MAIN_DOL, 0x7CF8, new byte[]{(byte) 0x80, 0x04, 0x01, 0x40},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll10 = new GNT4Code(MAIN_DOL, 0x7D0C, new byte[]{(byte) 0x80, 0x04, 0x01, 0x3c},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll11 = new GNT4Code(MAIN_DOL, 0x7D68, new byte[]{(byte) 0x80, 0x04, 0x01, 0x38},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockStages = new GNT4Code(MAIN_DOL, 0x7D7C, new byte[]{(byte) 0x80, 0x04, 0x01, 0x34},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockCharacters1 = new GNT4Code(MAIN_DOL, 0x7DC0, new byte[]{(byte) 0x80, 0x04, 0x01, 0x30},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockCharacters2 = new GNT4Code(MAIN_DOL, 0x7DD4, new byte[]{(byte) 0x80, 0x04, 0x01, 0x2c},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll15 = new GNT4Code(MAIN_DOL, 0x7E18, new byte[]{(byte) 0x80, 0x04, 0x01, 0x28},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockOptions = new GNT4Code(MAIN_DOL, 0x7E2C, new byte[]{(byte) 0x80, 0x04, 0x01, 0x24},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll17 = new GNT4Code(MAIN_DOL, 0x7E70, new byte[]{(byte) 0x80, 0x04, 0x01, 0x20},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll18 = new GNT4Code(MAIN_DOL, 0x7E84, new byte[]{(byte) 0x80, 0x04, 0x01, 0x1c},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll19 = new GNT4Code(MAIN_DOL, 0x7EC8, new byte[]{(byte) 0x80, 0x04, 0x01, 0x18},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAll20 = new GNT4Code(MAIN_DOL, 0x7EDC, new byte[]{(byte) 0x80, 0x04, 0x01, 0x14},
        new byte[]{0x38, 0x00, (byte) 0xFF, (byte) 0xFF});
    unlockAllCodes = Arrays
        .asList(unlockAll1, unlockAll2, unlockAll3, unlockAll4, unlockAll5, unlockAll6, unlockAll7,
            unlockAll8, unlockAll9, unlockAll10, unlockAll11, unlockStages, unlockCharacters1,
            unlockCharacters2, unlockAll15, unlockOptions, unlockAll17, unlockAll18, unlockAll19,
            unlockAll20);
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
   * Returns the initial character select speed. Technically it only returns it from the 2-player
   * character select menu, but there should not be a difference between the speed of the 2-player
   * character select menu and the 4-player one.
   *
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
   * Returns the max character select speed. Technically it only returns it from the 2-player
   * character select menu, but there should not be a difference between the speed of the 2-player
   * character select menu and the 4-player one.
   *
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
   * Returns the title demo timeout value.
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
   * Sets the Initial speed of the character select screen. 1 is extremely fast and 15 is extremely
   * slow.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @param value                 The initial speed of the character select screen.
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
   * Sets the maximum speed of the character select screen. 1 is extremely fast and 15 is extremely
   * slow.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @param value                 The maximum speed of the character select screen.
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
   * Sets the title demo timeout in seconds. The seconds will be converted to frames when inserted
   * into the game.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @param value                 The title demo timeout in seconds.
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
   *
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
   *
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
   *
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
   * Returns whether or not the code to skip cutscenes is activated. The code is in three locations
   * so this only returns true if the code is in all three locations.
   *
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
   *
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
   *
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
   * Returns the current main menu character.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @return the main menu character.
   * @throws IOException If an I/O error occurs.
   */
  public String getMainMenuCharacter(Path uncompressedDirectory) throws IOException {
    Path filePath = uncompressedDirectory.resolve(mainMenuCharacter.getFilePath());
    int offset = mainMenuCharacter.getOffset();
    byte[] bytes = readWord(filePath, offset);
    ByteBuffer wrapped = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    int internalCharNumber = wrapped.getInt();
    for (Map.Entry<String, Integer> entry : GNT4Characters.INTERNAL_CHAR_ORDER.entrySet()) {
      if (internalCharNumber == entry.getValue()) {
        return entry.getKey();
      }
    }
    throw new IOException(internalCharNumber + " is not a valid main menu character id.");
  }

  /**
   * Sets main menu character.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @param chr                   The name of the main menu character.
   * @throws IOException If an I/O error occurs.
   */
  public void setMainMenuCharacter(Path uncompressedDirectory, String chr) throws IOException {
    // Main Menu Character
    int characterId = GNT4Characters.INTERNAL_CHAR_ORDER.get(chr);
    ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
    byte[] charBytes = buffer.putInt(characterId).array();
    Path dolPath = uncompressedDirectory.resolve(mainMenuCharacter.getFilePath());
    int mainMenuCharacterOffset = mainMenuCharacter.getOffset();

    // Main Menu Character Sound
    byte soundEffect = GNT4Characters.CHAR_SEL_SOUND.get(chr);
    int mainMenuCharacterSoundOffset = mainMenuCharacterSound.getOffset();
    byte[] soundByte = new byte[]{soundEffect};

    // Main Menu Character Height
    byte height = GNT4Characters.CHAR_HEIGHT_ADJUST.get(chr);
    int mainMenuCharacterHeightOffset = mainMenuCharacterHeight.getOffset();
    byte[] heightByte = new byte[]{height};

    Code.getBuilder()
        .withOverwrite(mainMenuCharacterOffset, charBytes)
        .withOverwrite(mainMenuCharacterSoundOffset, soundByte)
        .withOverwrite(mainMenuCharacterHeightOffset, heightByte)
        .execute(dolPath);
  }

  /**
   * Returns whether or not the code to play audio while paused is activated.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public boolean isAudioPlaysWhilePausedActivated(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(MAIN_DOL);
    int soundCheckOffset1 = playAudioWhilePausedMissionMode.getOffset();
    byte[] bytes1 = readWord(dolPath, soundCheckOffset1);
    byte[] patchedInstruction1 = playAudioWhilePausedMissionMode.getNewInstruction();
    if (Arrays.equals(bytes1, patchedInstruction1)) {
      int soundCheckOffset2 = playAudioWhilePausedOtherModes.getOffset();
      byte[] bytes2 = readWord(dolPath, soundCheckOffset2);
      byte[] patchedInstruction2 = playAudioWhilePausedOtherModes.getNewInstruction();
      if (Arrays.equals(bytes2, patchedInstruction2)) {
        int soundCheckOffset3 = playAudioWhilePausedTrainingMode.getOffset();
        byte[] bytes3 = readWord(dolPath, soundCheckOffset3);
        byte[] patchedInstruction3 = playAudioWhilePausedTrainingMode.getNewInstruction();
        return Arrays.equals(bytes3, patchedInstruction3);
      }
    }
    return false;
  }

  /**
   * Activates the code to play audio while paused.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public void activateAudioPlaysWhilePaused(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(MAIN_DOL);
    int firstCodeOffset = playAudioWhilePausedMissionMode.getOffset();
    int secondCodeOffset = playAudioWhilePausedOtherModes.getOffset();
    int thirdCodeOffset = playAudioWhilePausedTrainingMode.getOffset();
    byte[] instruction = playAudioWhilePausedMissionMode.getNewInstruction();
    Code.getBuilder().withOverwrite(firstCodeOffset, instruction)
        .withOverwrite(secondCodeOffset, instruction)
        .withOverwrite(thirdCodeOffset, instruction).execute(dolPath);
  }

  /**
   * Inactivates the code to play audio while paused.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public void inactivateAudioPlaysWhilePaused(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(MAIN_DOL);
    int firstCodeOffset = playAudioWhilePausedMissionMode.getOffset();
    int secondCodeOffset = playAudioWhilePausedOtherModes.getOffset();
    int thirdCodeOffset = playAudioWhilePausedTrainingMode.getOffset();
    byte[] firstInstruction = playAudioWhilePausedMissionMode.getOldInstruction();
    byte[] secondInstruction = playAudioWhilePausedOtherModes.getOldInstruction();
    byte[] thirdInstruction = playAudioWhilePausedTrainingMode.getOldInstruction();
    Code.getBuilder().withOverwrite(firstCodeOffset, firstInstruction)
        .withOverwrite(secondCodeOffset, secondInstruction)
        .withOverwrite(thirdCodeOffset, thirdInstruction).execute(dolPath);
  }

  /**
   * Returns whether or not the code for no slowdown on kill is activated.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public boolean isNoSlowdownOnKillActivated(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(noSlowDownOnKill.getFilePath());
    int offset = noSlowDownOnKill.getOffset();
    byte[] bytes = readWord(dolPath, offset);
    byte[] patchedInstruction = noSlowDownOnKill.getNewInstruction();
    return Arrays.equals(bytes, patchedInstruction);
  }

  /**
   * Activates the code for no slowdown on kill.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public void activateNoSlowdownOnKillCode(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(noSlowDownOnKill.getFilePath());
    byte[] newInstruction = noSlowDownOnKill.getNewInstruction();
    int offset = noSlowDownOnKill.getOffset();
    Code.getBuilder().withOverwrite(offset, newInstruction).execute(dolPath);
  }

  /**
   * Inactivates the code for no slowdown on kill.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public void inactivateNoSlowdownOnKillCode(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(noSlowDownOnKill.getFilePath());
    byte[] oldInstruction = noSlowDownOnKill.getOldInstruction();
    int offset = noSlowDownOnKill.getOffset();
    Code.getBuilder().withOverwrite(offset, oldInstruction).execute(dolPath);
  }

  /**
   * Returns whether or not the code to unlock everything is activated. The code is in 20 locations
   * so this only returns true if the code is in all three locations.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public boolean isUnlockAllActivated(Path uncompressedDirectory) throws IOException {
    Path dolPath = uncompressedDirectory.resolve(unlockAll1.getFilePath());
    Map<GNT4Code, byte[]> codeToWord = readWords(dolPath, unlockAllCodes);
    boolean isActivated = true;
    for (GNT4Code code : unlockAllCodes) {
      byte[] currentWord = codeToWord.get(code);
      if (!Arrays.equals(currentWord, code.getNewInstruction())) {
        isActivated = false;
        break;
      }
    }
    return isActivated;
  }

  /**
   * Activates the code to unlock everything.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public void activateUnlockAll(Path uncompressedDirectory) throws IOException {
    Code builder = Code.getBuilder();
    for (GNT4Code code : unlockAllCodes) {
      builder.withOverwrite(code.getOffset(), code.getNewInstruction());
    }
    Path dolPath = uncompressedDirectory.resolve(unlockAll1.getFilePath());
    builder.execute(dolPath);
  }

  /**
   * Inactivates the code to unlock everything.
   *
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   * @throws IOException If an I/O error occurs.
   */
  public void inactivateUnlockAll(Path uncompressedDirectory) throws IOException {
    Code builder = Code.getBuilder();
    for (GNT4Code code : unlockAllCodes) {
      builder.withOverwrite(code.getOffset(), code.getOldInstruction());
    }
    Path dolPath = uncompressedDirectory.resolve(unlockAll1.getFilePath());
    builder.execute(dolPath);
  }

  /**
   * Reads a 4-byte word from the given filePath starting from the given offset.
   *
   * @param filePath The file path.
   * @param offset   The offset to begin reading at.
   * @return The 4-byte word.
   * @throws IOException If an I/O error occurs.
   */
  private byte[] readWord(Path filePath, int offset) throws IOException {
    try (InputStream is = Files.newInputStream(filePath)) {
      if (is.skip(offset) != offset) {
        throw new IOException(String.format("Failed to read to offset %d of %s", offset, filePath));
      }
      byte[] bytes = new byte[4];
      if (is.read(bytes) != 4) {
        throw new IOException("Failed to read from " + filePath);
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
   * Converts a number of frames to a number of seconds. GNT4 runs at 60 frames per second,
   * therefore there are 60 frames in a second.
   *
   * @param frames The number of frames to convert.
   * @return The number of seconds.
   */
  private int framesToSeconds(int frames) {
    return frames / 60;
  }

  /**
   * Converts a number of seconds to a number of frames. GNT4 runs at 60 frames per second,
   * therefore there are 60 frames in a second.
   *
   * @param seconds The number of seconds to convert.
   * @return The number of frames.
   */
  private int secondsToFrames(int seconds) {
    return seconds * 60;
  }
}
