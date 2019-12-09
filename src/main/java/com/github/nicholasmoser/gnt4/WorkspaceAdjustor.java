package com.github.nicholasmoser.gnt4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class WorkspaceAdjustor {
  private Path inputDirectory;

  public WorkspaceAdjustor(Path inputDirectory) {
    this.inputDirectory = inputDirectory;
  }

  public void makeModReady() throws IOException {
    moveAltVersusTextures();
    moveNonFpksToFiles();
    renameSysFiles();
    moveFpackFolder();
  }

  public void undoModReady() throws IOException {
    moveBackAltVersusTextures();
    moveBackNonFpksToFiles();
    renameBackSysFiles();
    moveBackFpackFolder();
  }

  /**
   * Moves the fpack folder to the main workspace directory.
   * 
   * @throws IOException If an I/O error occurs.
   */
  private void moveFpackFolder() throws IOException {
    move(inputDirectory.resolve("fpack"), inputDirectory.getParent().resolve("fpack"));
  }

  /**
   * Moves the fpack folder to the main workspace directory.
   * 
   * @throws IOException If an I/O error occurs.
   */
  private void moveBackFpackFolder() throws IOException {
    move(inputDirectory.getParent().resolve("fpack"), inputDirectory.resolve("fpack"));
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
   * Moves the alternative versus textures from the files directory to the vs/files directory. It
   * does this for Gaara, Ino, Kankuro, Sakura, Shikamaru, and Temari.
   * 
   * @throws IOException If an I/O error occurs.
   */
  private void moveBackAltVersusTextures() throws IOException {
    moveBackAltVersusTexture("gar", "旧");
    moveBackAltVersusTexture("ino", "長");
    moveBackAltVersusTexture("kan", "旧");
    moveBackAltVersusTexture("sak", "長");
    moveBackAltVersusTexture("sik", "旧");
    moveBackAltVersusTexture("tem", "旧");
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
   * Moves the audio folder, the movie folder, and the opening.bnr files from the workspace
   * directory to the files folder. This is because the game expects them in the files folder in
   * order to run uncompressed.
   * 
   * @throws IOException If an I/O error occurs.
   */
  private void moveBackNonFpksToFiles() throws IOException {
    moveFromFiles(inputDirectory.resolve("audio"));
    moveFromFiles(inputDirectory.resolve("movie"));
    moveFromFiles(inputDirectory.resolve("opening.bnr"));
  }

  /**
   * Renames the system folder and its respective files to be mod ready.
   * When extracted from the ISO using GCR, the folder and its files do not have the 
   * names that the game expects when running the game from the workspace uncompressed.
   * 
   * @throws IOException If an I/O error occurs.
   */
  private void renameSysFiles() throws IOException {
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
   * Renames the system folder and its respective files back from being mod ready.
   * When extracted from the ISO using GCR, the folder and its files do not have the 
   * names that the game expects when running the game from the workspace uncompressed.
   * 
   * @throws IOException If an I/O error occurs.
   */
  private void renameBackSysFiles() throws IOException {
    Path oldSys = inputDirectory.resolve("sys");
    Path newSys = inputDirectory.resolve("&&SystemData");
    move(oldSys, newSys);
    Path oldAppLoader = newSys.resolve("apploader.img");
    Path newAppLoader = newSys.resolve("AppLoader.ldr");
    move(oldAppLoader, newAppLoader);
    Path oldDol = newSys.resolve("main.dol");
    Path newDol = newSys.resolve("Start.dol");
    move(oldDol, newDol);
    Path oldToc = newSys.resolve("fst.bin");
    Path newToc = newSys.resolve("Game.toc");
    move(oldToc, newToc);
    mergeISOHeader(newSys);
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
   * Merges the boot.bin and bi2.bin files into the ISO.hdr file. The data needed to be in the two existing
   * files in order for the game to run from the workspace directory uncompressed.
   * 
   * @param sysPath The path to the system folder.
   * @throws IOException If an I/O error occurs.
   */
  private void mergeISOHeader(Path sysPath) throws IOException {

    Path bootBin = sysPath.resolve("boot.bin");
    Path bi2Bin = sysPath.resolve("bi2.bin");
    ByteArrayOutputStream baos = new ByteArrayOutputStream(0x2440);
    baos.write(Files.readAllBytes(bootBin));
    baos.write(Files.readAllBytes(bi2Bin));
    Files.write(sysPath.resolve("ISO.hdr"), baos.toByteArray());
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
   * Moves a file from files folder to the workspace directory.
   * 
   * @param source The file to move.
   * @throws IOException If an I/O error occurs.
   */
  private void moveFromFiles(Path source) throws IOException {
    Path name = source.getFileName();
    move(inputDirectory.resolve("files").resolve(name), source);
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
    Files.delete(files.resolve(character));
  }

  /**
   * Moves the alternative versus textures from the files/vs directory to the files directory for a
   * given character and its respective input folder name.
   * 
   * @param character The three letter character name folder in the files directory.
   * @param inputFolder The folder name in the character name folder in the files directory.
   * @throws IOException If an I/O error occurs.
   */
  private void moveBackAltVersusTexture(String character, String inputFolder) throws IOException {
    Path files = inputDirectory.resolve("files");
    Path sourceAltTextureFolder = files.resolve("vs").resolve(character).resolve(inputFolder);
    Path targetAltTextureFolder = files.resolve(character).resolve(inputFolder);
    move(targetAltTextureFolder, sourceAltTextureFolder);
    Files.delete(sourceAltTextureFolder);
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
