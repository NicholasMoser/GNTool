package com.github.nicholasmoser.dol;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

/**
 * A class to parse dol files into Dol objects.
 */
public class DolParser {

  private final Path dolPath;
  // Must be 32 byte aligned
  private long[] textSectionMemoryAddresses;
  private long[] dataSectionMemoryAddresses;
  // Must be 4 byte aligned
  private long[] textSectionSizes;
  private long[] dataSectionSizes;
  private long entryPoint;

  /**
   * Create a new DolParser from a given dol file path.
   *
   * @param dolPath The path to the dol file to parse.
   */
  public DolParser(Path dolPath) {
    this.dolPath = dolPath;
  }

  /**
   * Parses a dol file and returns a Dol object.
   *
   * @return The Dol object.
   * @throws IOException If an I/O error occurs.
   */
  public Dol parse() throws IOException {

    try (RandomAccessFile raf = new RandomAccessFile(dolPath.toFile(), "r")) {
      long[] textSectionOffsets = ByteUtils.readUint32s(raf, Dol.TEXT_SECTIONS_MAX);
      long[] dataSectionOffsets = ByteUtils.readUint32s(raf, Dol.DATA_SECTIONS_MAX);
      textSectionMemoryAddresses = ByteUtils.readUint32s(raf, Dol.TEXT_SECTIONS_MAX);
      dataSectionMemoryAddresses = ByteUtils.readUint32s(raf, Dol.DATA_SECTIONS_MAX);
      textSectionSizes = ByteUtils.readUint32s(raf, Dol.TEXT_SECTIONS_MAX);
      dataSectionSizes = ByteUtils.readUint32s(raf, Dol.DATA_SECTIONS_MAX);
      long bssMemoryAddress = ByteUtils.readUint32(raf);
      long bssSize = ByteUtils.readUint32(raf);
      entryPoint = ByteUtils.readUint32(raf);
      if (!checkHeaderIsValid()) {
        throw new IOException("Dol header is not valid.");
      }
      byte[][] textSections = readSections(raf, textSectionOffsets, textSectionSizes);
      byte[][] dataSections = readSections(raf, dataSectionOffsets, dataSectionSizes);
      return new Dol.Builder()
          .withTextSections(textSections)
          .withDataSections(dataSections)
          .withTextSectionMemoryAddresses(textSectionMemoryAddresses)
          .withDataSectionMemoryAddresses(dataSectionMemoryAddresses)
          .withBssMemoryAddress(bssMemoryAddress)
          .withBssSize(bssSize)
          .withEntryPoint(entryPoint)
          .build();
    }
  }

  /**
   * Reads the sections from a RandomAccessFile given the list of offsets and lengths.
   *
   * @param raf     The RandomAccessFile to read from.
   * @param offsets The list of offsets of the sections.
   * @param lengths The lengths of the sections.
   * @return The array of section byte arrays.
   * @throws IOException If an I/O error occurs.
   */
  private byte[][] readSections(RandomAccessFile raf, long[] offsets, long[] lengths)
      throws IOException {
    byte[][] sections = new byte[offsets.length][];
    for (int i = 0; i < offsets.length; i++) {
      long offset = offsets[i];
      long length = lengths[i];
      if (offset != 0 && length != 0) {
        raf.seek(offset);
        byte[] textSection = new byte[(int) length];
        if (raf.read(textSection) != length) {
          throw new IOException("Failed to read section at " + offset);
        }
        sections[i] = textSection;
      } else {
        sections[i] = new byte[0];
      }
    }
    return sections;
  }

  /**
   * Checks if the Dol header is valid. No address should intersect other addresses. The entrypoint
   * should also be between 0x80000000 and 0x817FFFFF.
   *
   * @return If the header is valid.
   */
  public boolean checkHeaderIsValid() {
    if (checkAddressIntersectsOtherAddresses()) {
      return false;
    }

    return entryPoint >= 0x80000000L && entryPoint <= 0x817FFFFFL;

    // TODO: Check each section is within valid memory bounds. (0x80000000 - 0x817FFFFF)
    // TODO: Check to make sure that the file entries don't overlap each other.
  }

  private long alignAddress(long address) {
    if ((address & 0x1F) == 0) {
      return address;
    }

    return address + (0x20 - (address & 0x1F));
  }

  private boolean checkAddressIntersectsOtherAddresses() {
    for (int i = 0; i < textSectionMemoryAddresses.length; i++) {
      long address = textSectionMemoryAddresses[i];
      long size = textSectionSizes[i];
      long endAddress = address + size;

      if (size == 0) {
        continue;
      }

      // Align end address since all sections in the DOL file must be aligned to 32 bytes.
      endAddress = alignAddress(endAddress);

      // Check against text section violations first.
      for (int x = 0; x < textSectionMemoryAddresses.length; x++) {
        if (x == i || textSectionSizes[x] == 0) {
          continue;
        }

        long otherAddress = textSectionMemoryAddresses[x];
        long otherSize = textSectionSizes[x];
        long otherEndAddress = otherAddress + otherSize;

        otherEndAddress = alignAddress(otherEndAddress);

        if ((address >= otherAddress && address < otherEndAddress) || (endAddress > otherAddress
            && endAddress < otherEndAddress)) {
          return true;
        }
      }

      // Now check against data section & text section violations.
      for (int x = 0; x < dataSectionMemoryAddresses.length; x++) {
        if (dataSectionSizes[x] == 0) {
          continue;
        }

        long otherAddress = dataSectionMemoryAddresses[x];
        long otherSize = dataSectionSizes[x];
        long otherEndAddress = otherAddress + otherSize;

        otherEndAddress = alignAddress(otherEndAddress);

        var a = address >= otherAddress && address < otherEndAddress;
        var b = endAddress > otherAddress && endAddress < otherEndAddress;
        if ((a) || (b)) {
          return true;
        }
      }
    }

    // Now check for data section violations.
    for (int i = 0; i < dataSectionMemoryAddresses.length; i++) {
      long address = dataSectionMemoryAddresses[i];
      long size = dataSectionSizes[i];
      long endAddress = address + size;

      // Align end address since all sections in the DOL file must be aligned to 32 bytes.
      endAddress = alignAddress(endAddress);

      if (dataSectionSizes[i] == 0) {
        continue;
      }

      // Check against text section violations first.
      for (int x = 0; x < dataSectionMemoryAddresses.length; x++) {
        if (x == i || dataSectionSizes[x] == 0) {
          continue;
        }

        long otherAddress = dataSectionMemoryAddresses[x];
        long otherSize = dataSectionSizes[x];
        long otherEndAddress = otherAddress + otherSize;

        otherEndAddress = alignAddress(otherEndAddress);

        if ((address >= otherAddress && address < otherEndAddress) || (endAddress > otherAddress
            && endAddress < otherEndAddress)) {
          return true;
        }
      }
    }

    return false;
  }
}
