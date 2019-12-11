package com.github.nicholasmoser.gnt4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import org.apache.commons.io.FileUtils;

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
    FileUtils.deleteDirectory(files.resolve(character).toFile());
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
