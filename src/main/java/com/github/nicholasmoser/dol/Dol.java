package com.github.nicholasmoser.dol;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The DOL format is the name given to a section of Wii and GameCube disc partitions. This section
 * represents an object file. It contains code for the game or program on the disc as well as the
 * data needed by that code. The discs do not explicitly contain a file with the DOL format in the
 * file system, but by convention most disc viewers/editors list a file called main.dol which
 * contains the DOL section of the disc.
 * <p>
 * Like many object formats, DOLs are divided into sections, grouped by like access. For example,
 * executable code may be placed in one section, read only data in another, etc. The file begins
 * with a header and then is directly followed by the section data. The format does not support
 * relocation or symbol information and so DOLs are much simpler and much less flexible than most
 * common object formats such as ELF or PE (.exe). The REL format is used by some games including
 * Mario Kart Wii when a more complex object format is required.
 * <p>
 * The file begins with a header of 0x100 bytes. The header allows for a maximum of 18 sections with
 * contents. The first 7 sections can contain executable code and the remaining 11 sections can only
 * contain data. The header also allows for a zero initialised (bss) range. This range can overlap
 * the 18 sections, with the sections taking priority. DOL files on Wii and GameCube should only
 * affect address in the range 0x80004000 to 0x81200000. Addresses outside this range are reserved
 * for the operating system and the loader.
 * <p>
 * For more info see http://wiki.tockdom.com/wiki/DOL_(File_Format)
 * <p>
 *   <table border="1">
 *   <tr>
 *     <td>.init</td>
 *     <td>Code which will be executed when program initializes.</td>
 *   </tr>
 *   <tr>
 *     <td>.text</td>
 *     <td>User's executable code.</td>
 *   </tr>
 *   <tr>
 *     <td>extab</td>
 *     <td>Read only C++ Exception Tables </td>
 *   </tr>
 *   <tr>
 *     <td>extabindex</td>
 *     <td>Read only C++ Exception Tables.</td>
 *   </tr>
 *   <tr>
 *     <td>.ctors</td>
 *     <td>Pointers to functions which are marked as __attribute__ ((constructor)) as well as
 *     static C++ objects' constructors.</td>
 *   </tr>
 *   <tr>
 *     <td>.dtors</td>
 *     <td>Pointers to functions which are marked as __attribute__ ((destructor)) as well as
 *     static C++ objects' destructors.</td>
 *   </tr>
 *   <tr>
 *     <td>.rodata</td>
 *     <td>Read-only data.</td>
 *   </tr>
 *   <tr>
 *     <td>.data</td>
 *     <td>Initialized data.</td>
 *   </tr>
 *   <tr>
 *     <td>.sdata</td>
 *     <td>Holds small initialized modifiable data that contribute to the program memory image.</td>
 *   </tr>
 *   <tr>
 *     <td>.sdata2</td>
 *     <td>PPC embedded small initialized data.</td>
 *   </tr>
 *   <tr>
 *     <td>.bss</td>
 *     <td>Read/write initialized (zeroed) data.</td>
 *   </tr>
 *   <tr>
 *     <td>.sbss</td>
 *     <td>Small initialized (zeroed) data.</td>
 *   </tr>
 *   <tr>
 *     <td>.sbss2</td>
 *     <td>PPC embedded small initialized (zeroed) data.</td>
 *   </tr>
 * </table>
 * <p>
 * For more info see https://www.nxp.com/docs/en/application-note/PPCEABI.pdf
 */
public class Dol {

  public static final int TEXT_SECTIONS_MAX = 7;
  public static final int DATA_SECTIONS_MAX = 11;
  public static final int DOL_HEADER_END = 0x100;
  public static final int DOL_HEADER_PADDING = 0x1C;
  public static final String[] TEXT_NAMES = {
      ".init", ".text", ".text1", ".text2", ".text3", ".text4", ".text5"
  };
  public static final String[] DATA_NAMES = {
      "extab", "extabindex", ".ctors", ".dtors", ".rodata", ".data", ".sdata", ".sdata2", ".bss",
      ".sbss", ".sbss2"
  };

  private final byte[][] textSections;
  private final byte[][] dataSections;
  private final long[] textSectionMemoryAddresses;
  private final long[] dataSectionMemoryAddresses;
  private final long bssMemoryAddress;
  private final long bssSize;
  private final long entryPoint;

  private Dol(byte[][] textSections, byte[][] dataSections, long[] textSectionMemoryAddresses,
      long[] dataSectionMemoryAddresses, long bssMemoryAddress, long bssSize, long entryPoint) {
    this.textSections = textSections;
    this.dataSections = dataSections;
    this.textSectionMemoryAddresses = textSectionMemoryAddresses;
    this.dataSectionMemoryAddresses = dataSectionMemoryAddresses;
    this.bssMemoryAddress = bssMemoryAddress;
    this.bssSize = bssSize;
    this.entryPoint = entryPoint;
  }

  /**
   * Write the dol to an output file.
   *
   * @param filePath The path to the file to create or overwrite with the dol data.
   * @throws IOException If an I/O error occurs.
   */
  public void writeToFile(Path filePath) throws IOException {
    if (!Files.exists(filePath)) {
      Files.createFile(filePath);
    }
    try (OutputStream os = Files.newOutputStream(filePath)) {
      writeSectionOffsets(os);
      writeSectionMemoryAddresses(os);
      writeSectionLengths(os);
      os.write(ByteUtils.fromUint32(bssMemoryAddress));
      os.write(ByteUtils.fromUint32(bssSize));
      os.write(ByteUtils.fromUint32(entryPoint));
      os.write(new byte[DOL_HEADER_PADDING]);
      writeSections(os);
    }
  }

  /**
   * Writes the sections  to an OutputStream.
   *
   * @param os The OutputStream to write the sections to.
   * @throws IOException If an I/O error occurs.
   */
  private void writeSections(OutputStream os) throws IOException {
    int currentOffset = DOL_HEADER_END;
    for (byte[] textSection : textSections) {
      if (textSection.length != 0) {
        os.write(textSection);
        currentOffset += textSection.length;
        int byteAlignedOffset = getNextByteAlignedPosition(currentOffset, 32);
        if (currentOffset != byteAlignedOffset) {
          os.write(new byte[byteAlignedOffset - currentOffset]);
          currentOffset = byteAlignedOffset;
        }
      }
    }
    for (byte[] dataSection : dataSections) {
      if (dataSection.length != 0) {
        os.write(dataSection);
        currentOffset += dataSection.length;
        int byteAlignedOffset = getNextByteAlignedPosition(currentOffset, 32);
        if (currentOffset != byteAlignedOffset) {
          os.write(new byte[byteAlignedOffset - currentOffset]);
          currentOffset = byteAlignedOffset;
        }
      }
    }
  }

  /**
   * Writes the section offsets to an OutputStream.
   *
   * @param os The OutputStream to write the section offsets to.
   * @throws IOException If an I/O error occurs.
   */
  private void writeSectionOffsets(OutputStream os) throws IOException {
    int currentOffset = DOL_HEADER_END;
    for (byte[] textSection : textSections) {
      if (textSection.length != 0) {
        os.write(ByteUtils.fromInt32(currentOffset));
      } else {
        os.write(ByteUtils.fromInt32(0x0));
      }
      currentOffset += textSection.length;
      currentOffset = getNextByteAlignedPosition(currentOffset, 32);
    }
    for (byte[] dataSection : dataSections) {
      if (dataSection.length != 0) {
        os.write(ByteUtils.fromInt32(currentOffset));
      } else {
        os.write(ByteUtils.fromInt32(0x0));
      }
      currentOffset += dataSection.length;
      currentOffset = getNextByteAlignedPosition(currentOffset, 32);
    }
  }

  /**
   * Writes the section memory addresses to an OutputStream.
   *
   * @param os The OutputStream to write the section memory addresses to.
   * @throws IOException If an I/O error occurs.
   */
  private void writeSectionMemoryAddresses(OutputStream os) throws IOException {
    for (long textSectionMemoryAddress : textSectionMemoryAddresses) {
      os.write(ByteUtils.fromUint32(textSectionMemoryAddress));
    }
    for (long dataSectionMemoryAddress : dataSectionMemoryAddresses) {
      os.write(ByteUtils.fromUint32(dataSectionMemoryAddress));
    }
  }

  /**
   * Writes the section lengths to an OutputStream.
   *
   * @param os The OutputStream to write the section lengths to.
   * @throws IOException If an I/O error occurs.
   */
  private void writeSectionLengths(OutputStream os) throws IOException {
    for (byte[] textSection : textSections) {
      os.write(ByteUtils.fromInt32(textSection.length));
    }
    for (byte[] dataSection : dataSections) {
      os.write(ByteUtils.fromInt32(dataSection.length));
    }
  }

  /**
   * Gets the next byte aligned position using a given modulo.
   *
   * @param currentPosition The current position to check against.
   * @param modulo          The modulo to use.
   * @return The next byte aligned position.
   */
  private int getNextByteAlignedPosition(int currentPosition, int modulo) {
    int remainder = currentPosition % modulo;
    if (remainder == 0) {
      return currentPosition;
    }
    return currentPosition + (modulo - remainder);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Dol{\n");
    builder.append("Text Sections:\n");
    for (int i = 0; i < textSections.length; i++) {
      byte[] textSection = textSections[i];
      String name = TEXT_NAMES[i];
      builder.append(String.format("%-6s ", name));
      if (textSection.length > 0) {
        builder.append("length = ");
        builder.append(String.format("%08x ", textSection.length));
        builder.append("address = ");
        builder.append(String.format("%08x\n", textSectionMemoryAddresses[i]));
      } else {
        builder.append("is unused\n");
      }
    }
    builder.append("Data Sections:\n");
    for (int i = 0; i < dataSections.length; i++) {
      byte[] dataSection = dataSections[i];
      String name = DATA_NAMES[i];
      builder.append(String.format("%-10s ", name));
      if (dataSection.length > 0) {
        builder.append("length = ");
        builder.append(String.format("%08x ", dataSection.length));
        builder.append("address = ");
        builder.append(String.format("%08x\n", dataSectionMemoryAddresses[i]));
      } else {
        builder.append("is unused\n");
      }
    }
    builder.append("bssMemoryAddress = ");
    builder.append(String.format("%08x\n", bssMemoryAddress));
    builder.append("bssSize = ");
    builder.append(String.format("%08x\n", bssSize));
    builder.append("entryPoint = ");
    builder.append(String.format("%08x\n", entryPoint));
    builder.append('}');
    return builder.toString();
  }

  public static class Builder {

    private byte[][] textSections;
    private byte[][] dataSections;
    private long[] textSectionMemoryAddresses;
    private long[] dataSectionMemoryAddresses;
    private long bssMemoryAddress;
    private long bssSize;
    private long entryPoint;

    public Builder withTextSections(byte[][] textSections) {
      this.textSections = textSections;
      return this;
    }

    public Builder withDataSections(byte[][] dataSections) {
      this.dataSections = dataSections;
      return this;
    }

    public Builder withTextSectionMemoryAddresses(long[] textSectionMemoryAddresses) {
      this.textSectionMemoryAddresses = textSectionMemoryAddresses;
      return this;
    }

    public Builder withDataSectionMemoryAddresses(long[] dataSectionMemoryAddresses) {
      this.dataSectionMemoryAddresses = dataSectionMemoryAddresses;
      return this;
    }

    public Builder withBssMemoryAddress(long bssMemoryAddress) {
      this.bssMemoryAddress = bssMemoryAddress;
      return this;
    }

    public Builder withBssSize(long bssSize) {
      this.bssSize = bssSize;
      return this;
    }

    public Builder withEntryPoint(long entryPoint) {
      this.entryPoint = entryPoint;
      return this;
    }

    public Dol build() {
      return new Dol(textSections, dataSections, textSectionMemoryAddresses,
          dataSectionMemoryAddresses, bssMemoryAddress,
          bssSize, entryPoint);
    }
  }
}
