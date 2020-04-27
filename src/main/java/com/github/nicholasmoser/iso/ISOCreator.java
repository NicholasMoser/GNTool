package com.github.nicholasmoser.iso;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.base.VerifyException;
import com.google.common.io.CountingOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Creates ISOs from a list of files and directories.
 */
public class ISOCreator {

  private final Path inputPath;
  private final Path isoPath;
  private final Path filesPath;

  /**
   * Create an ISOCreator using the path to the input files and the path to the output ISO file.
   *
   * @param inputPath The path to the input files
   * @param isoPath   The path to the output ISO file.
   */
  public ISOCreator(Path inputPath, Path isoPath) {
    this.inputPath = inputPath;
    this.isoPath = isoPath;
    filesPath = inputPath.resolve("files");
  }

  /**
   * Create an ISO.
   *
   * @throws IOException If an I/O error occurs.
   */
  public void create() throws IOException {
    ISOParser parser = new ISOParser(isoPath);
    ISOHeader isoHeader = parser.getISOHeader();
    create(isoHeader);
  }

  /**
   * Create an ISO using an ISOHeader.
   *
   * @param isoHeader The ISOHeader for the ISO.
   * @throws IOException If an I/O error occurs.
   */
  public void create(ISOHeader isoHeader)
      throws IOException {
    // Get values for files in the sys folder
    Path apploaderImgPath = resolve("sys/apploader.img");
    Path bi2BinPath = resolve("sys/bi2.bin");
    Path bootBinPath = resolve("sys/boot.bin");
    Path fstBinPath = resolve("sys/fst.bin");
    Path mainDolPath = resolve("sys/main.dol");
    ISOFile apploaderImg = isoHeader.getApploaderImg();
    ISOFile mainDol = isoHeader.getMainDol();
    ISOFile fstBin = isoHeader.getFstBin();
    int apploaderOffset = apploaderImg.getPos();
    int dolOffset = mainDol.getPos();
    int fstOffset = fstBin.getPos();
    int fstSize = fstBin.getLen();

    // Rewrite the fst.bin and offsets/sizes in the boot.bin
    FileSystemTable.rewrite(inputPath, isoHeader);
    bootBinRewrite(bootBinPath, dolOffset, fstOffset, fstSize);

    // Write out the ISO
    try (CountingOutputStream os = new CountingOutputStream(Files.newOutputStream(isoPath))) {
      writeAndPad(os, bootBinPath, ISO.BOOT_BIN_LEN);
      writeAndPad(os, bi2BinPath, ISO.BI_2_LEN);
      writeAndPad(os, apploaderImgPath, dolOffset - apploaderOffset);
      writeAndPad(os, mainDolPath, fstOffset - dolOffset);
      writeAndPad(os, fstBinPath, fstSize);
      writeFiles(os, isoHeader.getFiles());
    }
  }

  /**
   * Rewrites the main.dol offset, fst.bin offset, fst.bin size, and the max fst.bin size to the
   * boot.bin. The value at 0x42C is the max fst.bin size. This value is only used for multi-disk
   * games, and will therefore be set as the same as the fst.bin size since this is not currently
   * supported in GNTool.
   *
   * @param bootBinPath The path to the boot.bin to read and write to.
   * @param dolOffset   The offset of the main.dol.
   * @param fstOffset   The offset of the fst.bin.
   * @param fstSize     The size of the fst.bin.
   * @throws IOException f an I/O error occurs.
   */
  private void bootBinRewrite(Path bootBinPath, int dolOffset, int fstOffset, int fstSize)
      throws IOException {
    byte[] bootBinBytes = Files.readAllBytes(bootBinPath);
    System.arraycopy(ByteUtils.fromUint32(dolOffset), 0, bootBinBytes, 0x420, 4);
    System.arraycopy(ByteUtils.fromUint32(fstOffset), 0, bootBinBytes, 0x424, 4);
    System.arraycopy(ByteUtils.fromUint32(fstSize), 0, bootBinBytes, 0x428, 4);
    System.arraycopy(ByteUtils.fromUint32(fstSize), 0, bootBinBytes, 0x42C, 4);
    Files.write(bootBinPath, bootBinBytes);
  }

  /**
   * Writes the files from a list of ISOItems to a CountingOutputStream.
   *
   * @param cos      The CountingOutputStream to write to.
   * @param isoItems The list of ISOItems to retrieve the files from.
   * @throws IOException If an I/O error occurs or the fst offset is before the current offset.
   */
  private void writeFiles(CountingOutputStream cos, List<ISOItem> isoItems) throws IOException {
    for (ISOItem currentItem : isoItems) {
      if (!currentItem.isDirectory()) {
        ISOFile file = (ISOFile) currentItem;
        int dataPos = file.getPos();
        long currentPos = cos.getCount();
        if (currentPos > dataPos) {
          String name = file.getName();
          String message = String
              .format("Cannot write %s to ISO, fst offset of %d before %d", name, dataPos,
                  currentPos);
          throw new IOException(message);
        } else if (currentPos < dataPos) {
          // Pad with zeroes to reach data position
          int padding = (int) (dataPos - currentPos);
          cos.write(new byte[padding]);
        }
        Path gamePath = filesPath.resolve(currentItem.getGamePath());
        byte[] gameBytes = Files.readAllBytes(gamePath);
        cos.write(gameBytes);
      }
    }
  }

  /**
   * Writes bytes to an output stream. If less than total bytes are written, zeroes will be padded
   * until total is reached.
   *
   * @param os       The OutputStream to write to.
   * @param filePath The path to the file bytes to write.
   * @param total    The total number of bytes to write including padding of zeroes.
   * @throws IOException If an I/O error occurs or more bytes are written than total.
   */
  private void writeAndPad(OutputStream os, Path filePath, int total) throws IOException {
    byte[] bytes = Files.readAllBytes(filePath);
    if (bytes.length > total) {
      throw new IOException(String.format("%d bytes greater than %d", bytes.length, total));
    }
    os.write(bytes);
    if (bytes.length < total) {
      os.write(new byte[total - bytes.length]);
    }
  }

  /**
   * Converts a given path string to a {@code Path} and resolves it against this {@code Path}. A
   * VerifyException will be thrown if the file does not exist.
   *
   * @param filePathText The file path text.
   * @return The file path.
   */
  private Path resolve(String filePathText) {
    Path filePath = inputPath.resolve(filePathText);
    if (!Files.exists(filePath)) {
      throw new VerifyException("Cannot find " + filePath);
    }
    return filePath;
  }
}
