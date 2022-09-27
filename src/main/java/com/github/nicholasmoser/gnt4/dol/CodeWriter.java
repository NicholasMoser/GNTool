package com.github.nicholasmoser.gnt4.dol;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gecko.GeckoCode;
import com.github.nicholasmoser.gecko.GeckoCodeGroup;
import com.github.nicholasmoser.gecko.GeckoCodeJSON;
import com.github.nicholasmoser.gecko.GeckoWriter;
import com.github.nicholasmoser.gnt4.dol.CodeCaves.CodeCave;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A writer for Gecko Codes.
 */
public class CodeWriter {
  private static final Logger LOGGER = Logger.getLogger(CodeWriter.class.getName());
  private final Path dolPath;
  private final Path codesPath;
  private final List<GeckoCodeGroup> codeGroups;

  /**
   * @param dolPath The path to main.dol
   * @param codesPath The path to codes.json
   * @param codeGroups The existing Gecko code groups
   */
  public CodeWriter(Path dolPath, Path codesPath, List<GeckoCodeGroup> codeGroups) {
    this.dolPath = dolPath;
    this.codesPath = codesPath;
    this.codeGroups = codeGroups;
  }

  /**
   * Adds new codes to the dol.
   *
   * @param codesName The name of the code group.
   * @param codes The list of Gecko codes.
   * @throws IOException If any I/O exception occurs.
   */
  public void addCodes(String codesName, List<GeckoCode> codes) throws IOException {
    if (DolHijack.checkHijackOverflow(codeGroups, codes, CodeCave.EXI2)) {
      return;
    }
    if (targetAddressOverlap(codes, codeGroups)) {
      return;
    }
    if (!checkNameValid(codesName)) {
      return;
    }
    long hijackStartAddress = DolHijack.getEndOfHijacking(codeGroups, CodeCave.EXI2);
    GeckoWriter writer = new GeckoWriter(dolPath);
    GeckoCodeGroup group = writer.writeCodes(codes, codesName, hijackStartAddress);
    codeGroups.add(group);
    GeckoCodeJSON.writeFile(codeGroups, codesPath);
  }

  /**
   * Checks if the target addresses of the given codes overlap with any existing codes. Logs and
   * displays a message if so.
   *
   * @param codes The codes to check.
   * @param codeGroups The existing Gecko code groups.
   * @return If any of the target addresses of the codes overlap any of the existing codes.
   */
  public static boolean targetAddressOverlap(List<GeckoCode> codes, List<GeckoCodeGroup> codeGroups) {
    for (GeckoCode code : codes) {
      long targetAddress = code.getTargetAddress();
      for (GeckoCodeGroup codeGroup : codeGroups) {
        for (GeckoCode existingCode : codeGroup.getCodes()) {
          if (targetAddress == existingCode.getTargetAddress()) {
            String message = "This code overlaps with: " + codeGroup.getName();
            LOGGER.log(Level.SEVERE, message);
            Message.error("Code Error", message);
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Returns whether or not the given Code Name is valid. Logs and displays a message if not.
   *
   * @param name The Code Name.
   * @return If the Code Name is valid.
   */
  private boolean checkNameValid(String name) {
    if (name == null || name.isBlank()) {
      String message = "Code Name required for new codes.";
      LOGGER.log(Level.SEVERE, message);
      Message.error("Code Error", message);
      return false;
    }
    for (GeckoCodeGroup codeGroup : codeGroups) {
      if (name.equals(codeGroup.getName())) {
        String message = "This Code Name already exists.";
        LOGGER.log(Level.SEVERE, message);
        Message.error("Code Error", message);
        return false;
      }
    }
    return true;
  }
}
