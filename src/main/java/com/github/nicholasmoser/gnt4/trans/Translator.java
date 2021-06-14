package com.github.nicholasmoser.gnt4.trans;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Class used to translate GNT4 into English.
 */
public class Translator {

  /**
   * Returns the translation state of the uncompressed directory. Will return ENGLISH if all files
   * are in English and JAPANESE if all files are in Japanese. Otherwise, will return UNKNOWN.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @return The current translation state.
   * @throws IOException If any I/O issues occur
   */
  public static TranslationState getTranslationState(Path uncompressedDir) throws IOException {
    TranslationState state = GameTranslator.getTranslationState(uncompressedDir);
    if (state != TitleTranslator.getTranslationState(uncompressedDir)) {
      return TranslationState.UNKNOWN;
    }
    if (state != ViewerTranslator.getTranslationState(uncompressedDir)) {
      return TranslationState.UNKNOWN;
    }
    if (state != TextureTranslator.getTranslationState(uncompressedDir)) {
      return TranslationState.UNKNOWN;
    }
    if (state != ChrTranslator.getTranslationState(uncompressedDir)) {
      return TranslationState.UNKNOWN;
    }
    return state;
  }

  /**
   * Translates the uncompressed directory to English.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void translate(Path uncompressedDir) throws IOException {
    TitleTranslator.translate(uncompressedDir);
    ViewerTranslator.translate(uncompressedDir);
    GameTranslator.translate(uncompressedDir);
    TextureTranslator.translate(uncompressedDir);
    ChrTranslator.translate(uncompressedDir);
    DolTranslator.translate(uncompressedDir);
  }
}
