package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeqKage {

  private static final Logger LOGGER = Logger.getLogger(SeqKage.class.getName());

  /**
   * Runs SEQKage.exe on the given input file.
   *
   * @param input  The input file.
   * @return The output text of SEQKage.exe
   * @throws IOException If an I/O error occurs.
   */
  public static String run(Path input) throws IOException {
    try {
      Process process = new ProcessBuilder("SEQKage.exe", input.toString()).start();
      process.waitFor();
      return new String(process.getInputStream().readAllBytes());
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }

  /**
   * @return If SEQKage.exe cannot be found in GNTool's directory.
   */
  public static boolean isNotAvailable() {
    Path seqkage = Paths.get("SEQKage.exe");
    return !Files.isRegularFile(seqkage);
  }

  public static boolean copySeqKage() throws IOException {
    Optional<Path> optionalSelection = Choosers.getSeqKage(GNTool.USER_HOME);
    if (optionalSelection.isPresent()) {
      Path seqkage = optionalSelection.get();
      Files.copy(seqkage, Paths.get("SEQKage.exe"), StandardCopyOption.REPLACE_EXISTING);
      LOGGER.log(Level.INFO, "SEQKage.exe successfully copied.");
      return true;
    }
    return false;
  }
}