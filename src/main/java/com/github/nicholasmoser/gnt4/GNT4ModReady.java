package com.github.nicholasmoser.gnt4;

import com.google.common.base.Verify;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

/**
 * Class to handle making the GNT4 files mod ready. Mod ready in this case means that it can be run
 * as-is in Dolphin. GNT4 was programmed to be able to be run both with the files compressed into
 * FPK files AND not compressed in FPK files. The issue though is that when extracted from the FPK
 * files the structure is not the structure that the game expects to be able to run them
 * uncompressed. Therefore this class contains operations to make that the case. This mostly
 * involves changing the location of files and directory names.
 */
public class GNT4ModReady {
  private Path inputDirectory;

  public GNT4ModReady(Path inputDirectory) {
    this.inputDirectory = inputDirectory;
  }

  public void prepare() throws IOException {
    moveAltVersusTextures();
    moveNonFpksToFiles();
    renameSysFiles();
  }

  /**
   * Checks if the given file path is one of the two split files. boot.bin and bi2.bin in the system
   * folder both are combined to form the ISO.hdr file.
   * 
   * @param filePath The file path to check.
   * @return If the file path is one of the two split files (boot.bin or bi2.bin).
   */
  public static boolean isSplitFiles(String filePath) {
    Verify.verifyNotNull(filePath);
    return filePath.endsWith("boot.bin") || filePath.endsWith("bi2.bin");
  }

  /**
   * Converts a mod ready expected file path to the extracted GNT4 file path. Mod ready paths are
   * the file paths expected when running the game with files extracted from their respective FPK
   * files.
   * 
   * @param filePath The file path to get the extracted file path for.
   * @return The extracted file path.
   */
  public static String fromModReadyPath(String filePath) {
    Verify.verifyNotNull(filePath);
    filePath = filePath.replace("files", "fpack");
    filePath = filePath.replace("fpack/audio", "audio");
    filePath = filePath.replace("fpack/movie", "movie");
    filePath = filePath.replace("fpack/opening.bnr", "opening.bnr");
    switch (filePath) {
      case "sys/apploader.img":
        filePath = "&&systemdata/AppLoader.ldr";
        break;
      case "sys/main.dol":
        filePath = "&&systemdata/Start.dol";
        break;
      case "sys/fst.bin":
        filePath = "&&systemdata/Game.toc";
        break;
      case "sys/ISO.hdr":
        filePath = "&&systemdata/ISO.hdr";
        break;
      case "fpack/vs/gar/旧/4201.txg":
        filePath = "fpack/gar/旧/4201.txg";
        break;
      case "fpack/vs/ino/長/4201.txg":
        filePath = "fpack/ino/長/4201.txg";
        break;
      case "fpack/vs/kan/旧/4201.txg":
        filePath = "fpack/kan/旧/4201.txg";
        break;
      case "fpack/vs/sak/長/4201.txg":
        filePath = "fpack/sak/長/4201.txg";
        break;
      case "fpack/vs/sik/旧/4201.txg":
        filePath = "fpack/sik/旧/4201.txg";
        break;
      case "fpack/vs/tem/旧/4201.txg":
        filePath = "fpack/tem/旧/4201.txg";
        break;
      default:
        break;
    }
    return filePath;
  }

  /**
   * Converts an extracted GNT4 file path to the mod ready expected file path. Mod ready paths are
   * the file paths expected when running the game with files extracted from their respective FPK
   * files.
   * 
   * @param filePath The file path to get the mod ready path for.
   * @return The mod ready path.
   */
  public static String toModReadyPath(String filePath) {
    Verify.verifyNotNull(filePath);
    filePath = filePath.replace("fpack", "files");
    filePath = filePath.replace("audio", "files/audio");
    filePath = filePath.replace("movie", "files/movie");
    filePath = filePath.replace("opening.bnr", "files/opening.bnr");
    switch (filePath) {
      case "&&systemdata/AppLoader.ldr":
        filePath = "sys/apploader.img";
        break;
      case "&&systemdata/Start.dol":
        filePath = "sys/main.dol";
        break;
      case "&&systemdata/Game.toc":
        filePath = "sys/fst.bin";
        break;
      case "&&systemdata/ISO.hdr":
        filePath = "sys/ISO.hdr";
        break;
      case "files/gar/旧/4201.txg":
        filePath = "files/vs/gar/旧/4201.txg";
        break;
      case "files/ino/長/4201.txg":
        filePath = "files/vs/ino/長/4201.txg";
        break;
      case "files/kan/旧/4201.txg":
        filePath = "files/vs/kan/旧/4201.txg";
        break;
      case "files/sak/長/4201.txg":
        filePath = "files/vs/sak/長/4201.txg";
        break;
      case "files/sik/旧/4201.txg":
        filePath = "files/vs/sik/旧/4201.txg";
        break;
      case "files/tem/旧/4201.txg":
        filePath = "files/vs/tem/旧/4201.txg";
        break;
      default:
        break;
    }
    return filePath;
  }

  /**
   * Fixes and returns filenames for compressed files that are cut off. When compressed into an FPK,
   * certain files will have their paths cut off. One such example can be found in seq0000.fpk which
   * has a compressed file with the name hr/ank/0000.seq that is not correct. The first directory
   * should be chr, not hr. Therefore this method returns the filename with this fixed.
   * 
   * @param fileName The file name to fix.
   * @return The fixed file name or original if no fix is required.
   */
  public static String fixBrokenFileName(String fileName) {
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
   * Moves the alternative versus textures from the files directory to the vs/files directory. It
   * does this for Gaara, Ino, Kankuro, Sakura, Shikamaru, and Temari.
   * 
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
   * Moves the audio folder, the movie folder, and the opening.bnr files from the workspace
   * directory to the files folder. This is because the game expects them in the files folder in
   * order to run uncompressed.
   * 
   * @throws IOException If an I/O error occurs.
   */
  private void moveNonFpksToFiles() throws IOException {
    moveToFiles(inputDirectory.resolve("audio"));
    moveToFiles(inputDirectory.resolve("movie"));
    moveToFiles(inputDirectory.resolve("opening.bnr"));
  }

  /**
   * Renames the system folder and its respective files to be mod ready. When extracted from the ISO
   * using GCR, the folder and its files do not have the names that the game expects when running
   * the game from the workspace uncompressed.
   * 
   * @throws IOException If an I/O error occurs.
   */
  private void renameSysFiles() throws IOException {
    Path oldSys = inputDirectory.resolve("&&systemdata");
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
   * Splits the ISO.hdr file into the boot.bin and bi2.bin files. The data needs to be in these two
   * files in order for the game to run from the workspace directory uncompressed.
   * 
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
   * 
   * @param source The file to move.
   * @throws IOException If an I/O error occurs.
   */
  private void moveToFiles(Path source) throws IOException {
    Path name = source.getFileName();
    move(source, inputDirectory.resolve("files").resolve(name));
  }

  /**
   * Moves the alternative versus textures from the files directory to the files/vs directory for a
   * given character and its respective input folder name.
   * 
   * @param character The three letter character name folder in the files directory.
   * @param inputFolder The folder name in the character name folder in the files directory.
   * @throws IOException If an I/O error occurs.
   */
  private void moveAltVersusTexture(String character, String inputFolder) throws IOException {
    Path files = inputDirectory.resolve("files");
    Path sourceAltTextureFolder = files.resolve(character).resolve(inputFolder);
    Path targetAltTextureFolder = files.resolve("vs").resolve(character).resolve(inputFolder);
    move(sourceAltTextureFolder, targetAltTextureFolder);
    MoreFiles.deleteRecursively(files.resolve(character), RecursiveDeleteOption.ALLOW_INSECURE);
  }

  /**
   * Shortcut method for Files.move with StandardCopyOption.REPLACE_EXISTING
   * 
   * @param source The path to the file to move.
   * @param target The path to the target file.
   * @throws IOException If an I/O error occurs.
   */
  private void move(Path source, Path target) throws IOException {
    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
  }
}
