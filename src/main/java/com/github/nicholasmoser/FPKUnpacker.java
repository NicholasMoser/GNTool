package com.github.nicholasmoser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import com.github.nicholasmoser.utils.FPKUtils;

/**
 * Unpacks FPK files. This includes uncompressing them with the Eighting PRS algorithm.
 */
public class FPKUnpacker {

  private static final Logger LOGGER = Logger.getLogger(FPKUnpacker.class.getName());
  
  private Path inputDirectory;
  
  public FPKUnpacker(Path inputDirectory) {
    this.inputDirectory = inputDirectory;
  }
  
  /**
   * Unpacks and uncompresses FPK files. First will prompt the user for an input and output
   * directory. The input directory will be copied to the output directory and then each FPK file
   * will have the contained files inside of it unpacked to their relative directories. This will
   * uncompress the files from their Eighting PRS compressed format.
   * 
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  public void unpack() throws IOException {
    LOGGER.info("Unpacking FPKs...");
    extractDirectory(inputDirectory.toFile());
    moveAltVersusTextures();
    moveNonFpksToFiles();
    fixSysFiles();
    moveFpackFolder();
    LOGGER.info("Finished unpacking FPKs.");
  }
  
  /**
   * Moves the fpack folder to the main workspace directory.
   * @throws IOException If an I/O error occurs.
   */
  private void moveFpackFolder() throws IOException {
    move(inputDirectory.resolve("fpack"), inputDirectory.getParent().resolve("fpack"));
  }

  /**
   * Moves the alternative versus textures from the files directory to the vs/files directory.
   * It does this for Gaara, Ino, Kankuro, Sakura, Shikamaru, and Temari.
   * @throws IOException If an I/O error occurs.
   */
  private void moveAltVersusTextures() throws IOException {
    moveAltVersusTexture("gar", "旧");
    moveAltVersusTexture("ino", "長");
    moveAltVersusTexture("kan", "旧");
    moveAltVersusTexture("sak", "長");
    moveAltVersusTexture("sik", "旧");
    moveAltVersusTexture("tem", "旧");
  }

  /**
   * Moves the audio folder, the movie folder, and the opening.bnr files from the
   * workspace directory to the files folder. This is because the game expects them
   * in the files folder in order to run uncompressed.
   * @throws IOException If an I/O error occurs.
   */
  private void moveNonFpksToFiles() throws IOException {
    moveToFiles(inputDirectory.resolve("audio"));
    moveToFiles(inputDirectory.resolve("movie"));
    moveToFiles(inputDirectory.resolve("opening.bnr"));
  }
  
  
  /**
   * Fixes the system folder and its respective files. When extracted from the ISO using GCR,
   * the folder and its files do not have the names that the game expects when running the
   * game from the workspace uncompressed.
   * @throws IOException If an I/O error occurs.
   */
  private void fixSysFiles() throws IOException {
    Path oldSys = inputDirectory.resolve("&&SystemData");
    Path newSys = inputDirectory.resolve("sys");
    move(oldSys, newSys);
    Path oldAppLoader = newSys.resolve("AppLoader.ldr");
    Path newAppLoader = newSys.resolve("apploader.img");
    move(oldAppLoader, newAppLoader);
    Path oldDol = newSys.resolve("Start.dol");
    Path newDol = newSys.resolve("main.dol");
    move(oldDol, newDol);
    Path oldToc = newSys.resolve("Game.toc");
    Path newToc = newSys.resolve("fst.bin");
    move(oldToc, newToc);
    splitISOHeader(newSys);
    
  }
  
  /**
   * Splits the ISO.hdr file into the boot.bin and bi2.bin files.
   * The data needs to be in these two files in order for the game to run from
   * the workspace directory uncompressed.
   * @param sysPath The path to the system folder.
   * @throws IOException If an I/O error occurs.
   */
  private void splitISOHeader(Path sysPath) throws IOException {
    Path isoHeader = sysPath.resolve("ISO.hdr");
    byte[] bytes = Files.readAllBytes(isoHeader);
    byte[] bootBytes = Arrays.copyOfRange(bytes, 0x0, 0x440);
    Files.write(sysPath.resolve("boot.bin"), bootBytes);
    byte[] bi2bytes = Arrays.copyOfRange(bytes, 0x440, 0x2440);
    Files.write(sysPath.resolve("bi2.bin"), bi2bytes);
  }
  
  /**
   * Moves a file from the workspace directory to the files folder.
   * @param source The file to move.
   * @throws IOException If an I/O error occurs.
   */
  private void moveToFiles(Path source) throws IOException {
    Path name = source.getFileName();
    move(source, inputDirectory.resolve("files").resolve(name));
  }
  
  /**
   * Moves the alternative versus textures from the files directory to the files/vs directory
   * for a given character and its respective input folder name.
   * @param character The three letter character name folder in the files directory.
   * @param inputFolder The folder name in the character name folder in the files directory.
   * @throws IOException If an I/O error occurs.
   */
  private void moveAltVersusTexture(String character, String inputFolder) throws IOException {
    Path files = inputDirectory.resolve("files");
    Path sourceAltTextureFolder = files.resolve(character).resolve(inputFolder);
    Path targetAltTextureFolder = files.resolve("vs").resolve(character).resolve(inputFolder);
    move(sourceAltTextureFolder, targetAltTextureFolder);
    Files.delete(files.resolve(character));
  }
  
  /**
   * A recursive method to extract and uncompress the files inside an FPK from a given directory.
   * This method will call itself recursively for each directory it encounters.
   * @param directory The directory to search and extract from.
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  private void extractDirectory(File directory) throws IOException {
    for (File fileEntry : directory.listFiles()) {
      if (fileEntry.isDirectory()) {
        extractDirectory(fileEntry);
      } else {
        String fileName = fileEntry.getName();
        if (fileName.endsWith(".fpk")) {
          extractFPK(Paths.get(directory.getAbsolutePath()).resolve(fileName));
        }
      }
    }
  }

  /**
   * Opens the given FPK file and extracts it contents. This includes uncompressing them from
   * Eighting PRS compression.
   * @param filePath The FPK file to extract.
   * @throws IOException If there is an IO error with the FPK file or its extracted children.
   */
  private void extractFPK(Path filePath) throws IOException {
    int bytesRead = 0;
    try (InputStream is = Files.newInputStream(filePath)) {
      int fileCount = FPKUtils.readFPKHeader(is);
      bytesRead += 16;

      List<FPKFileHeader> fpkHeaders = new ArrayList<FPKFileHeader>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readFPKFileHeader(is));
        bytesRead += 32;
      }

      for (FPKFileHeader header : fpkHeaders) {
        String fileName = fixBrokenFileName(header.getFileName());
        int offset = header.getOffset();
        int compressedSize = header.getCompressedSize();
        int uncompressedSize = header.getUncompressedSize();

        // Skip to the next offset if we are not already there
        if (bytesRead < offset) {
          int bytesToMove = offset - bytesRead;
          is.skip(bytesToMove);
          bytesRead += bytesToMove;
        }

        byte[] fileBytes = new byte[compressedSize];
        is.read(fileBytes);
        bytesRead += compressedSize;
        
        // Create directories from fileName and get output directory
        Path filesPath = inputDirectory.resolve("files");
        Path outputFilePath = filesPath.resolve(fileName);
        Files.createDirectories(outputFilePath.getParent());

        // Files with the same compressed and uncompressed size are not compressed
        if (compressedSize == uncompressedSize) {
          Files.write(outputFilePath, fileBytes);
        } else {
          PRSUncompressor uncompressor = new PRSUncompressor(fileBytes, uncompressedSize);
          byte[] output = uncompressor.uncompress();
          Files.write(outputFilePath, output);
        }
      }
    }
  }
  
  /**
   * Fixes and returns filenames for compressed files that are cut off.
   * When compressed into an FPK, certain files will have their paths cut off.
   * One such example can be found in seq0000.fpk which has a compressed file with
   * the name hr/ank/0000.seq that is not correct. The first directory should be
   * chr, not hr. Therefore this method returns the filename with this fixed.
   * @param fileName The file name to fix.
   * @return The fixed file name or original if no fix is required.
   */
  private String fixBrokenFileName(String fileName) {
    if (fileName.startsWith("aki/")) {
      fileName = fileName.replace("aki/", "maki/");
    } else if (fileName.startsWith("ame/")) {
      fileName = fileName.replace("ame/", "game/");
    } else if (fileName.startsWith("g/")) {
      fileName = fileName.replace("g/", "stg/");
    } else if (fileName.startsWith("hr/")) {
      fileName = fileName.replace("hr/", "chr/");
    } else if (fileName.startsWith("ki/")) {
      fileName = fileName.replace("ki/", "maki/");
    } else if (fileName.startsWith("me/")) {
      fileName = fileName.replace("me/", "game/");
    } else if (fileName.startsWith("ru/")) {
      fileName = fileName.replace("ru/", "furu/");
    } else if (fileName.startsWith("te/")) {
      fileName = fileName.replace("te/", "unite/");
    } else if (fileName.startsWith("tg/")) {
      fileName = fileName.replace("tg/", "stg/");
    } else if (fileName.startsWith("uro/")) {
      fileName = fileName.replace("uro/", "kuro/");
    }
    return fileName;
  }
  
  /**
   * Shortcut method for Files.move with StandardCopyOption.REPLACE_EXISTING
   * @param source The path to the file to move.
   * @param target The path to the target file.
   * @throws IOException If an I/O error occurs.
   */
  private void move(Path source, Path target) throws IOException {
    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
  }
}
