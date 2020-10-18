package com.github.nicholasmoser.dol;

public class DolUtil {

  /**
   * The sections for the GNT4 dol.
   * TODO: This is GNT4 code and should be moved under the GNT4 package.
   */
  public enum Section {
    INIT,
    TEXT,
    CTORS,
    DTORS,
    RODATA,
    DATA,
    BSS,
    SDATA,
    SBSS,
    SDATA2,
    SBSS2
  }

  /**
   * Converts an address in ram to an offset in the dol. Most sections you can simply subtract
   * 0x80003000 to get the offset, but since the bss, sbss, and sbss2 have a length of 0 in the dol,
   * you must then subtract by 0x80003000 plus the sizes of each skipped to get offsets for sections
   * after them.
   *
   * @param ramAddress The address in ram.
   * @return The offset in the dol.
   */
  public static long ram2dol(long ramAddress) {
    String hexAddress = String.format("%08X", ramAddress);
    return switch (getSection(ramAddress)) {
      case INIT, TEXT, CTORS, DTORS, RODATA, DATA -> ramAddress - 0x80003000L;
      case BSS -> throw new IllegalArgumentException(
          "Addresses in the bss section do not have an offset in the dol: " + hexAddress);
      case SDATA -> ramAddress - 0x80056F40L;
      case SBSS -> throw new IllegalArgumentException(
          "Addresses in the bss2 section do not have an offset in the dol: " + hexAddress);
      case SDATA2 -> ramAddress - 0x80057C00L;
      case SBSS2 -> throw new IllegalArgumentException(
          "Addresses in the sbss2 section do not have an offset in the dol: " + hexAddress);
      default -> throw new IllegalArgumentException(
          "Unexpected section for ram address: " + hexAddress);
    };
  }

  /**
   * Returns the Section enum for a given ram address.
   *
   * @param ramAddress The address in ram.
   * @return The corresponding Section in the dol.
   */
  public static Section getSection(long ramAddress) {
    if (ramAddress >= 0x8027C578L) {
      String message = "Target address of code is outside the bounds of the dol (0x8027C578+): ";
      throw new IllegalArgumentException(message + String.format("%08x", ramAddress));
    } else if (ramAddress >= 0x8027C560L) {
      return Section.SBSS2;
    } else if (ramAddress >= 0x80277CA0L) {
      return Section.SDATA2;
    } else if (ramAddress >= 0x80276FE0L) {
      return Section.SBSS;
    } else if (ramAddress >= 0x80276920L) {
      return Section.SDATA;
    } else if (ramAddress >= 0x802229E0L) {
      return Section.BSS;
    } else if (ramAddress >= 0x80205C40L) {
      return Section.DATA;
    } else if (ramAddress >= 0x801FD840L) {
      return Section.RODATA;
    } else if (ramAddress >= 0x801FD820L) {
      return Section.DTORS;
    } else if (ramAddress >= 0x801FD800L) {
      return Section.CTORS;
    } else if (ramAddress >= 0x800056C0L) {
      return Section.TEXT;
    } else if (ramAddress >= 0x80003100L) {
      return Section.INIT;
    } else {
      String message = "Target address of code is outside the bounds the of dol (0x80003100-): ";
      throw new IllegalArgumentException(message + String.format("%08x", ramAddress));
    }
  }
}
