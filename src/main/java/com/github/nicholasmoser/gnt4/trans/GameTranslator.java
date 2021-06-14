package com.github.nicholasmoser.gnt4.trans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class used to translate game00.seq into English.
 */
public class GameTranslator {

  public static String GAME = "files/game/game00.seq";
  public static long TEXT_OFFSET_1 = 0x90DC;
  public static long ORIGINAL_SIZE = 0x15CF0;
  public static long TRANSLATED_SIZE = 0x16AE8;

  /**
   * Returns the translation state of the game00 file. Will return ENGLISH if game00 is in English
   * and JAPANESE if game00 is in Japanese. Otherwise, will return UNKNOWN.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @return The current translation state.
   * @throws IOException If any I/O issues occur
   */
  public static TranslationState getTranslationState(Path uncompressedDir) throws IOException {
    long fileSize = Files.size(uncompressedDir.resolve(GAME));
    if (fileSize == ORIGINAL_SIZE) {
      return TranslationState.JAPANESE;
    } else if (fileSize == TRANSLATED_SIZE) {
      return TranslationState.ENGLISH;
    }
    return TranslationState.UNKNOWN;
  }

  /**
   * Translates game00.seq to English.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void translate(Path uncompressedDir) throws IOException {
    byte[] translatedBytes1;
    try (InputStream is = ViewerTranslator.class.getResourceAsStream("game00_english_1.bin")) {
      translatedBytes1 = is.readAllBytes();
    }
    byte[] translatedBytes2;
    try (InputStream is = ViewerTranslator.class.getResourceAsStream("game00_english_2.bin")) {
      translatedBytes2 = is.readAllBytes();
    }
    File file = uncompressedDir.resolve(GAME).toFile();
    try(RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
      raf.seek(TEXT_OFFSET_1);
      raf.write(translatedBytes1);
      // These three bytes are much further away than the rest, so write them separately
      raf.seek(0x157ED);
      raf.write(new byte[] { 0x01, 0x5D, (byte) 0xC8} );
      raf.seek(ORIGINAL_SIZE);
      raf.write(translatedBytes2);
    }
  }
}
