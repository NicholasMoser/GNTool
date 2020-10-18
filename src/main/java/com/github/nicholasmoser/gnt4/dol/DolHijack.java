package com.github.nicholasmoser.gnt4.dol;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gecko.GeckoCode;
import com.github.nicholasmoser.gecko.GeckoCodeGroup;
import com.github.nicholasmoser.gecko.InsertAsmCode;
import com.github.nicholasmoser.gecko.active.ActiveInsertAsmCode;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for hijacking code in the dol for GNT4. The code currently hijacked is a space of 297
 * instructions (1188 bytes) relating to the unused recording function in training mode.
 *
 * <ul>
 *   <li>80086f58 - Every frame of replay loop</li>
 *   <li>80087060 - Increase record number</li>
 *   <li>800870c0 - Saves recorded number?</li>
 *   <li>800871a0 - Does something every frame of recording</li>
 *   <li>80087364 - Helper function for 800b3790</li>
 *   <li>80087374 - Stop replay</li>
 * </ul>
 */
public class DolHijack {

  private static final Logger LOGGER = Logger.getLogger(DolHijack.class.getName());

  // Inclusive
  public final static long START_RAM_ADDRESS = 0x80086F58L;
  public final static long START_DOL_OFFSET = 0x83F58;

  // Exclusive
  public final static long END_RAM_ADDRESS = 0x800873FCL;
  public final static long END_DOL_OFFSET = 0x843FC;

  public final static long TOTAL = END_RAM_ADDRESS - START_RAM_ADDRESS;

  /**
   * Returns whether or not the given Gecko codes overflow the limit of hijacked code. Logs and
   * displays a message if so.
   *
   * @param newCodes The GeckoCode List to check.
   * @return If the Gecko codes overflow the limit of hijacked code.
   */
  public static boolean checkHijackOverflow(List<GeckoCodeGroup> existingCodeGroups,
      List<GeckoCode> newCodes) {
    long furthestEnd = getEndOfHijacking(existingCodeGroups);
    long bytesLeft = DolHijack.END_RAM_ADDRESS - furthestEnd;
    long totalBytes = 0;
    for (GeckoCode code : newCodes) {
      if (code instanceof InsertAsmCode) {
        InsertAsmCode insertCode = (InsertAsmCode) code;
        totalBytes += insertCode.getBytes().length;
      }
    }
    if (totalBytes > bytesLeft) {
      String msg = String
          .format("Codes hijack %d bytes but only %d bytes are left.", totalBytes, bytesLeft);
      LOGGER.log(Level.SEVERE, msg);
      Message.error("Not Enough Space to Insert Codes", msg);
      return true;
    }
    return false;
  }

  /**
   * Returns the end of hijacked codes in the given list of code groups.
   *
   * @param existingCodeGroups The existing code groups to find the furthest end of hijacking.
   * @return The address at the end of hijacking.
   */
  public static long getEndOfHijacking(List<GeckoCodeGroup> existingCodeGroups) {
    long furthestEnd = DolHijack.START_RAM_ADDRESS;
    for (GeckoCodeGroup group : existingCodeGroups) {
      for (GeckoCode code : group.getCodes()) {
        if (code instanceof ActiveInsertAsmCode) {
          ActiveInsertAsmCode insertCode = (ActiveInsertAsmCode) code;
          long hijackedAddress = insertCode.getHijackedAddress();
          int length = insertCode.getHijackedBytes().length;
          long end = hijackedAddress + length;
          if (end > DolHijack.END_RAM_ADDRESS) {
            throw new IllegalStateException(
                "codeGroups extends past max hijack: " + existingCodeGroups);
          } else if (end > furthestEnd) {
            furthestEnd = end;
          }
        }
      }
    }
    return furthestEnd;
  }
}
