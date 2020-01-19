package com.github.nicholasmoser.audio;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper class for DSPADPCM. Used for sound effects.
 */
public class DspAdpcmEncoder {
  private static final Logger LOGGER = Logger.getLogger(DspAdpcmEncoder.class.getName());

  /**
   * Runs DSPADPCM.exe on the given input file and saves the output to the given output file.
   *
   * @param input  The input file.
   * @param output The output file.
   * @return The output text of DSPADPCM.exe
   * @throws IOException If an I/O error occurs.
   */
  public static String run(Path input, Path output) throws IOException {
    try {
      Process process = new ProcessBuilder("DSPADPCM.exe", "-e", "-v", input.toString(),
          output.toString()).start();
      process.waitFor();
      return new String(process.getInputStream().readAllBytes());
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  /**
   * DSPADPCM.exe adds a Loop End Offset to the header even though the sound is not looped. This
   * causes issues in GNT4 resulting in the sound looped. This can be fixed by zero-ing the four
   * bytes for the Loop End Offset.
   *
   * @param dspPath The path to the dsp to zero the Loop End Offset.
   * @throws IOException If an I/O error occurs.
   */
  public static void zeroLoopEndOffset(Path dspPath) throws IOException {
    if (!Files.isRegularFile(dspPath)) {
      throw new IOException(dspPath + " is not accessible.");
    }
    byte[] dspBytes = Files.readAllBytes(dspPath);
    dspBytes[0x14] = 0x0;
    dspBytes[0x15] = 0x0;
    dspBytes[0x16] = 0x0;
    dspBytes[0x17] = 0x0;
    Files.write(dspPath, dspBytes);
  }

  /**
   * @return If DSPADPCM.exe, dsptool.dll, or soundfile.dll cannot be found in GNTool's directory.
   */
  public static boolean isNotAvailable() {
    Path dspadpcm = Paths.get("DSPADPCM.exe");
    Path dsptool = Paths.get("dsptool.dll");
    Path soundfile = Paths.get("soundfile.dll");
    return !Files.isRegularFile(dspadpcm) || !Files.isRegularFile(dsptool) || !Files
        .isRegularFile(soundfile);
  }

  /**
   * Asks the user to input a location to the Nintendo SDK to import DSPADPCM.exe from.
   * This will also move over the associated libraries dsptool.dll and soundfile.dll
   *
   * @return If the move was successful.
   */
  public static boolean moveDspAdpcm() throws Exception {
    Optional<Path> optionalSelection = Choosers.getDspAdpcm(GNTool.USER_HOME);
    if (optionalSelection.isPresent()) {
      Path dspadpcm = optionalSelection.get();
      Path dsptool = dspadpcm.getParent().resolve("dsptool.dll");
      Path soundfile = dspadpcm.getParent().resolve("soundfile.dll");
      if (!Files.isRegularFile(dsptool)) {
        LOGGER.log(Level.SEVERE, "Missing dsptool.dll");
        Message.error("Missing dsptool.dll", "dsptool.dll should be in same directory as DSPADPCM.exe");
        return false;
      } else if (!Files.isRegularFile(soundfile)) {
        LOGGER.log(Level.SEVERE, "Missing soundfile.dll");
        Message.error("Missing soundfile.dll", "soundfile.dll should be in same directory as DSPADPCM.exe");
        return false;
      }
      Files.copy(dspadpcm, Paths.get("DSPADPCM.exe"), StandardCopyOption.REPLACE_EXISTING);
      Files.copy(dsptool, Paths.get("dsptool.dll"), StandardCopyOption.REPLACE_EXISTING);
      Files.copy(soundfile, Paths.get("soundfile.dll"), StandardCopyOption.REPLACE_EXISTING);
      LOGGER.log(Level.INFO, "DSPADPCM.exe, dsptool.dll, and soundfile.dll successfully copied.");
      return true;
    }
    return false;
  }
}
