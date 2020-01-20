package com.github.nicholasmoser.audio;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper class for dtkmake.exe. Used for sound effects. dtkmake.exe is part of the official
 * Nintendo GameCube SDK. It can be found under: NINTENDO GameCube SDK 1.0/X86/bin/dtkmake.exe
 */
public class DtkMake {

  private static final Logger LOGGER = Logger.getLogger(DtkMake.class.getName());

  /**
   * Runs dtkmake.exe on the given input file and saves the output to the given output file.
   *
   * @param input  The input file.
   * @param output The output file.
   * @return The output text of dtkmake.exe
   * @throws IOException If an I/O error occurs.
   */
  public static String run(Path input, Path output) throws IOException {
    try {
      Process process = new ProcessBuilder("dtkmake.exe", input.toString(),
          output.toString()).start();
      process.waitFor();
      return new String(process.getInputStream().readAllBytes());
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  /**
   * @return If dtkmake.exe cannot be found in GNTool's directory.
   */
  public static boolean isNotAvailable() {
    Path dspadpcm = Paths.get("dtkmake.exe");
    return !Files.isRegularFile(dspadpcm);
  }

  /**
   * Asks the user to input a location to the Nintendo SDK to import dtkmake.exe from.
   *
   * @return If the move was successful.
   */
  public static boolean copyDtkMake() throws Exception {
    Optional<Path> optionalSelection = Choosers.getDtkMake(GNTool.USER_HOME);
    if (optionalSelection.isPresent()) {
      Path dspadpcm = optionalSelection.get();
      Files.copy(dspadpcm, Paths.get("dtkmake.exe"), StandardCopyOption.REPLACE_EXISTING);
      LOGGER.log(Level.INFO, "dtkmake.exe successfully copied.");
      return true;
    }
    return false;
  }

  /**
   * Fixes a .wav file to ensure that its data subchunk is located at 0x24. This is necessary
   * because dtkmake.exe expects it to be at that location. It is fixed by remnoving all other
   * subchunks that may be present, such as LIST INFO metadata.
   *
   * @param wavFile The .wav file to fix.
   * @throws IOException If an I/O error occurs.
   */
  public static void fixWavHeader(Path wavFile) throws IOException {
    if (!Files.isRegularFile(wavFile)) {
      throw new IOException(wavFile + " cannot be found.");
    }
    byte[] newBytes;
    try (InputStream is = Files.newInputStream(wavFile)) {
      byte[] header = new byte[0x24];
      if (is.read(header) != 0x24) {
        throw new IOException("Failed to read wav file header: " + wavFile);
      }
      byte[] subchunk = new byte[0x4];
      if (is.read(subchunk) != 0x4) {
        throw new IOException("Failed to read first subchunk of wav file: " + wavFile);
      }
      if (isDataSubchunk(subchunk)) {
        // Header is correct size, no further action needed.
        return;
      }
      while (!isDataSubchunk(subchunk)) {
        if (is.read(subchunk) != 0x4) {
          throw new IOException("Failed to read subchunk of wav file: " + wavFile);
        }
        int size = ByteBuffer.wrap(subchunk).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
        if (is.skip(size) != size) {
          throw new IOException("Failed to skip unneccesary subchunk of wav file: " + wavFile);
        }
        if (is.read(subchunk) != 0x4) {
          throw new IOException("Failed to read subchunk of wav file: " + wavFile);
        }
      }
      byte[] audioData = is.readAllBytes();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      outputStream.write(header);
      outputStream.write(subchunk);
      outputStream.write(audioData);
      newBytes = outputStream.toByteArray();
    }
    Files.write(wavFile, newBytes);
  }

  /**
   * Checks if the given subchunk id bytes reference the data subchunk.
   *
   * @param bytes The subchunk id bytes to check.
   * @return If the subchunk id bytes reference the data subchunk.
   */
  private static boolean isDataSubchunk(byte[] bytes) {
    return "data".equals(new String(bytes));
  }
}
