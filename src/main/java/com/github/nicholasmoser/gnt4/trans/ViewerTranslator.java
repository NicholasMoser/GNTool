package com.github.nicholasmoser.gnt4.trans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class used to translate m_viewer.seq into English.
 */
public class ViewerTranslator {

  public static String VIEWER = "files/maki/m_viewer.seq";
  public static long TEXT_OFFSET = 0xD484;
  public static long ORIGINAL_SIZE = 0xDAB0;
  public static long TRANSLATED_SIZE = 0x135E0;

  /**
   * Returns the translation state of the m_viewer file. Will return ENGLISH if m_viewer.seq is in
   * English and JAPANESE if m_viewer.seq is in Japanese. Otherwise, will return UNKNOWN.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @return The current translation state.
   * @throws IOException If any I/O issues occur
   */
  public static TranslationState getTranslationState(Path uncompressedDir) throws IOException {
    long fileSize = Files.size(uncompressedDir.resolve(VIEWER));
    if (fileSize == ORIGINAL_SIZE) {
      return TranslationState.JAPANESE;
    } else if (fileSize == TRANSLATED_SIZE) {
      return TranslationState.ENGLISH;
    }
    return TranslationState.UNKNOWN;
  }

  /**
   * Translates m_viewer.seq to English.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void translate(Path uncompressedDir) throws IOException {
    byte[] translatedBytes;
    try (InputStream is = ViewerTranslator.class.getResourceAsStream("m_viewer_english.bin")) {
      translatedBytes = is.readAllBytes();
    }
    File file = uncompressedDir.resolve(VIEWER).toFile();
    try(RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
      raf.seek(TEXT_OFFSET);
      raf.write(translatedBytes);
    }
  }
}
