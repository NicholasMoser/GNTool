package com.github.nicholasmoser.iso;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Class to parse an ISO and extract files.
 */
public class ISOParser {

  private final Path isoPath;

  /**
   * Create a new ISOParser.
   *
   * @param isoPath The path to the ISO to parse.
   */
  public ISOParser(Path isoPath) {
    this.isoPath = isoPath;
  }

  /**
   * Returns the ISOHeader for the ISO.
   *
   * @return The ISOHeader for the ISO.
   * @throws IOException If an I/O error occurs.
   */
  public ISOHeader getISOHeader() throws IOException {
    if (!Files.exists(isoPath)) {
      throw new IOException(isoPath + " does not exist.");
    }
    ISOHeader.Builder headerBuilder = new ISOHeader.Builder();
    try (RandomAccessFile raf = new RandomAccessFile(isoPath.toFile(), "r")) {
      checkGameCubeMagicNumber(raf);
      int fstBinOffset = buildSysFiles(raf, headerBuilder);
      buildFiles(raf, headerBuilder, fstBinOffset);
    }
    return headerBuilder.createISOHeader();
  }

  /**
   * Builds the sys files. The files will be read from the provided RandomAccessFile and built into
   * the ISOHeader builder. The fst.bin position in the file will be returned.
   *
   * @param raf           The RandomAccessFile for the ISO to read from.
   * @param headerBuilder The builder for the ISOHeader.
   * @return The fst.bin position in the file.
   * @throws IOException If an I/O error occurs.
   */
  private int buildSysFiles(RandomAccessFile raf, ISOHeader.Builder headerBuilder)
      throws IOException {
    raf.seek(1024);
    int apploaderLength = ByteUtils.readInt32(raf);
    raf.skipBytes(28);
    int mainDolPosition = ByteUtils.readInt32(raf);
    int fileSystemTablePosition = ByteUtils.readInt32(raf);
    int fileSystemTableLength = ByteUtils.readInt32(raf);
    int mainDolLength = fileSystemTablePosition - mainDolPosition;
    raf.skipBytes(8);
    int dataStart = ByteUtils.readInt32(raf);

    ISODirectory sys = new ISODirectory.Builder()
        .setParent("").setName("sys").setGamePath("sys/").build();
    ISOFile bootBin = new ISOFile.Builder()
        .setParent(sys.getGamePath())
        .setPos(ISO.BOOT_BIN_POS)
        .setLen(ISO.BOOT_BIN_LEN)
        .setName("boot.bin")
        .setGamePath("sys/boot.bin")
        .build();
    ISOFile bi2Bin = new ISOFile.Builder()
        .setParent(sys.getGamePath())
        .setPos(ISO.BI_2_POS)
        .setLen(ISO.BI_2_LEN)
        .setName("bi2.bin")
        .setGamePath("sys/bi2.bin")
        .build();
    ISOFile apploaderImg = new ISOFile.Builder()
        .setParent(sys.getGamePath())
        .setPos(ISO.APPLOADER_POS)
        .setLen(apploaderLength)
        .setName("apploader.img")
        .setGamePath("sys/apploader.img")
        .build();
    ISOFile mainDol = new ISOFile.Builder()
        .setParent(sys.getGamePath())
        .setPos(mainDolPosition)
        .setLen(mainDolLength)
        .setName("main.dol")
        .setGamePath("sys/main.dol")
        .build();
    ISOFile fstBin = new ISOFile.Builder()
        .setParent(sys.getGamePath())
        .setPos(fileSystemTablePosition)
        .setLen(fileSystemTableLength)
        .setName("fst.bin")
        .setGamePath("sys/fst.bin")
        .build();
    headerBuilder.setSys(sys);
    headerBuilder.setBootBin(bootBin);
    headerBuilder.setBi2Bin(bi2Bin);
    headerBuilder.setApploaderImg(apploaderImg);
    headerBuilder.setMainDol(mainDol);
    headerBuilder.setFstBin(fstBin);

    return fileSystemTablePosition;
  }

  /**
   * Builds  the files and directories under the files directory. The files will be read from the
   * provided RandomAccessFile and built into the ISOHeader builder. The file more specifically will
   * be read from the fst.bin in the ISO.
   *
   * @param raf           The RandomAccessFile for the ISO to read from.
   * @param headerBuilder The ISOHeader builder.
   * @param fstBinOffset  The location of the fst.bin data in the ISO.
   * @throws IOException If an I/O error occurs.
   */
  private void buildFiles(RandomAccessFile raf, ISOHeader.Builder headerBuilder,
      int fstBinOffset)
      throws IOException {
    raf.seek(fstBinOffset);
    List<ISOItem> files = FileSystemTable.read(raf);
    headerBuilder.setFiles(files);
  }

  /**
   * Checks the GameCube ISO magic number at offset 28. It should be 0xC2339F3D. An IOException will
   * be thrown if this magic number is not present.
   *
   * @param raf The RandomAccessFile to read from.
   * @throws IOException If the RandomAccessFile is not a GameCube ISO.
   */
  public void checkGameCubeMagicNumber(RandomAccessFile raf) throws IOException {
    raf.seek(28);
    if (ByteUtils.readInt32(raf) != 0xC2339F3D) {
      throw new IOException("Not a GameCube ISO.");
    }
  }
}
