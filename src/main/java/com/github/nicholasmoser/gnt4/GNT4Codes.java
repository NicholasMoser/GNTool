package com.github.nicholasmoser.gnt4;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.nicholasmoser.Code;

public class GNT4Codes {
  private static final Logger LOGGER = Logger.getLogger(GNT4Codes.class.getName());

  private static final String MAIN_DOL = "sys/main.dol";

  /**
   * Activates the code to fix audio for different ISO file offsets.
   * 
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   */
  public static void activateAudioFixCode(Path uncompressedDirectory) {
    Path dolPath = uncompressedDirectory.resolve(MAIN_DOL);
    byte[] newInstruction = {0x48, 0x00, 0x00, 0x21};
    int soundCheckOffset = 0x16CC0C;
    try {
      Code.getBuilder().withOverwrite(soundCheckOffset, newInstruction).execute(dolPath);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error Activating Audio Fix Code", e);
    }
  }

  /**
   * Inactivates the code to fix audio for different ISO file offsets.
   * 
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   */
  public static void inactivateAudioFixCode(Path uncompressedDirectory) {
    Path dolPath = uncompressedDirectory.resolve(MAIN_DOL);
    byte[] newInstruction = {0x41, (byte) 0x82, 0x00, 0x20};
    int soundCheckOffset = 0x16CC0C;
    try {
      Code.getBuilder().withOverwrite(soundCheckOffset, newInstruction).execute(dolPath);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error Inactivating Audio Fix Code", e);
    }
  }

  /**
   * Activates the code to skip cutscenes.
   * 
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   */
  public static void activateSkipCutscenesCode(Path uncompressedDirectory) {
    Path dolPath = uncompressedDirectory.resolve(MAIN_DOL);
    int firstCutsceneOffset = 0x9B14;
    int secondCutsceneOffset = 0x9B28;
    int thirdCutsceneOffset = 0x9B3C;
    byte[] instruction = {0x60, 0x00, 0x00, 0x00};
    try {
      Code.getBuilder().withOverwrite(firstCutsceneOffset, instruction)
      .withOverwrite(secondCutsceneOffset, instruction)
      .withOverwrite(thirdCutsceneOffset, instruction).execute(dolPath);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error Activating Skip Cutscenes Code", e);
    }
  }
  
  /**
   * Inactivates the code to skip cutscenes.
   * 
   * @param uncompressedDirectory The directory of uncompressed files for the workspace.
   */
  public static void inactivateSkipCutscenesCode(Path uncompressedDirectory) {
    Path dolPath = uncompressedDirectory.resolve(MAIN_DOL);
    int firstCutsceneOffset = 0x9B14;
    int secondCutsceneOffset = 0x9B28;
    int thirdCutsceneOffset = 0x9B3C;
    byte[] firstInstruction = {0x48, 0x0B, (byte) 0xF5, 0x41};
    byte[] secondInstruction = {0x48, 0x0B, (byte) 0xF5, 0x2D};
    byte[] thirdInstruction = {0x48, 0x0B, (byte) 0xF5, 0x19};
    try {
      Code.getBuilder().withOverwrite(firstCutsceneOffset, firstInstruction)
      .withOverwrite(secondCutsceneOffset, secondInstruction)
      .withOverwrite(thirdCutsceneOffset, thirdInstruction).execute(dolPath);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error Inactivating Skip Cutscenes Code", e);
    }
  }
}
