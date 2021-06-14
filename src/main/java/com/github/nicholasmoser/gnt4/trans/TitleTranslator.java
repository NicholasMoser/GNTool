package com.github.nicholasmoser.gnt4.trans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class used to translate m_title.seq into English.
 */
public class TitleTranslator {

  public static String TITLE = "files/maki/m_title.seq";
  public static long TEXT_OFFSET = 0x32410;
  public static long ORIGINAL_SIZE = 0x32CD0;
  public static long TRANSLATED_SIZE = 0x492A0;

  /**
   * Returns the translation state of the m_title seq file. Will return ENGLISH if m_title.seq is in
   * English and JAPANESE if m_title.seq is in Japanese. Otherwise, will return UNKNOWN.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @return The current translation state.
   * @throws IOException If any I/O issues occur
   */
  public static TranslationState getTranslationState(Path uncompressedDir) throws IOException {
    long fileSize = Files.size(uncompressedDir.resolve(TITLE));
    if (fileSize == ORIGINAL_SIZE) {
      return TranslationState.JAPANESE;
    } else if (fileSize == TRANSLATED_SIZE) {
      return TranslationState.ENGLISH;
    }
    return TranslationState.UNKNOWN;
  }

  /**
   * Translates m_title.seq to English.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void translate(Path uncompressedDir) throws IOException {
    byte[] translatedBytes;
    try (InputStream is = ViewerTranslator.class.getResourceAsStream("m_title_english.bin")) {
      translatedBytes = is.readAllBytes();
    }
    File file = uncompressedDir.resolve(TITLE).toFile();
    try(RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
      // These two bytes are much further away than the rest, so write them separately
      raf.seek(0x1ED10);
      raf.write(0x02);
      raf.seek(0x1ED1A);
      raf.write(0x7C);
      raf.seek(TEXT_OFFSET);
      raf.write(translatedBytes);
    }
  }
}
