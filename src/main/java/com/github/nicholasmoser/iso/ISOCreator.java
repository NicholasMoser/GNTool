package com.github.nicholasmoser.iso;

import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.base.VerifyException;
import com.google.common.io.CountingOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
    //ISOParser parser = new ISOParser(isoPath);
    //List<ISOItem> isoItems = parser.getISOItems();
    //create(isoItems);
  }

  /**
   * Create an ISO using a specific table of contents.
   *
   * @param isoItems The ISO items.
   * @throws IOException If an I/O error occurs.
   */
  public void create(List<ISOItem> isoItems)
      throws IOException {
    // Get values for files in the sys folder
    Path apploaderImg = resolve("sys/apploader.img");
    Path bi2Bin = resolve("sys/bi2.bin");
    Path bootBin = resolve("sys/boot.bin");
    Path fstBin = resolve("sys/fst.bin");
    Path mainDol = resolve("sys/main.dol");
    ISOItem apploader = isoItems.get(4);
    ISOItem dol = isoItems.get(5);
    ISOItem fst = isoItems.get(6);
    int apploaderOffset = apploader.getPos();
    int dolOffset = dol.getPos();
    int fstOffset = fst.getPos();
    int fstSize = fst.getLen();
    int maxFstSize = fst.getLen(); // This is only different for multi-disk games

    // Write out the ISO
    try (CountingOutputStream os = new CountingOutputStream(Files.newOutputStream(isoPath))) {
      byte[] bootBinBytes = Files.readAllBytes(bootBin);

      // Updates the bytes for the boot.bin to account for new values.
      System.arraycopy(ByteUtils.fromUint32(dolOffset), 0, bootBinBytes, 0x420, 4);
      System.arraycopy(ByteUtils.fromUint32(fstOffset), 0, bootBinBytes, 0x424, 4);
      System.arraycopy(ByteUtils.fromUint32(fstSize), 0, bootBinBytes, 0x428, 4);
      System.arraycopy(ByteUtils.fromUint32(maxFstSize), 0, bootBinBytes, 0x42C, 4);

      writeAndPad(os, bootBinBytes, ISO.BOOT_BIN_LEN);
      writeAndPad(os, bi2Bin, ISO.BI_2_LEN);
      writeAndPad(os, apploaderImg, dolOffset - apploaderOffset);
      writeAndPad(os, mainDol, fstOffset - dolOffset);
      writeAndPad(os, fstBin, fstSize);
      writeFiles(os, isoItems);
    }
  }

  /**
   * Writes the files from a list of ISOItems to a CountingOutputStream.
   *
   * @param cos      The CountingOutputStream to write to.
   * @param isoItems The list of ISOItems to retrieve the files from.
   * @throws IOException If an I/O error occurs or the fst offset is before the current offset.
   */
  private void writeFiles(CountingOutputStream cos, List<ISOItem> isoItems) throws IOException {
    int firstFileIndex = 7;
    for (int i = firstFileIndex; i < isoItems.size(); i++) {
      ISOItem currentItem = isoItems.get(i);
      if (!currentItem.isDirectory()) {
        int dataPos = currentItem.getPos();
        long currentPos = cos.getCount();
        if (currentPos > dataPos) {
          String name = currentItem.getName();
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
    writeAndPad(os, Files.readAllBytes(filePath), total);
  }

  /**
   * Writes bytes to an output stream. If less than total bytes are written, zeroes will be padded
   * until total is reached.
   *
   * @param os    The OutputStream to write to.
   * @param bytes The bytes to write.
   * @param total The total number of bytes to write including padding of zeroes.
   * @throws IOException If an I/O error occurs or more bytes are written than total.
   */
  private void writeAndPad(OutputStream os, byte[] bytes, int total) throws IOException {
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
