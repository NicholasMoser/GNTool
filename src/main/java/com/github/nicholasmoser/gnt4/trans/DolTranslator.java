package com.github.nicholasmoser.gnt4.trans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Path;

/**
 * A class to translate the dol into English.
 */
public class DolTranslator {

  public static String DOL = "sys/main.dol";
  private static final long OFFSET_1 = 0x1FAC04;
  private static final long OFFSET_2 = 0x1FC118;
  private static final long OFFSET_3 = 0x1FD0A8;
  private static final long OFFSET_4 = 0x1FD740;
  private static final long OFFSET_5 = 0x1FD864;
  private static final long OFFSET_6 = 0x1FDA74;
  private static final long OFFSET_7 = 0x1FEBE0;
  private static final long OFFSET_8 = 0x1FF1E0;
  private static final long OFFSET_9 = 0x1FF464;
  private static final long OFFSET_10 = 0x207EE4;
  private static final long OFFSET_11 = 0x2220E0;

  /**
   * Translates the dol text to English.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void translate(Path uncompressedDir) throws IOException {
    byte[] translatedBytes1 = getBytes("dol_english_1.bin");
    byte[] translatedBytes2 = getBytes("dol_english_2.bin");
    byte[] translatedBytes3 = getBytes("dol_english_3.bin");
    byte[] translatedBytes4 = getBytes("dol_english_4.bin");
    byte[] translatedBytes5 = getBytes("dol_english_5.bin");
    byte[] translatedBytes6 = getBytes("dol_english_6.bin");
    byte[] translatedBytes7 = getBytes("dol_english_7.bin");
    byte[] translatedBytes8 = getBytes("dol_english_8.bin");
    byte[] translatedBytes9 = getBytes("dol_english_9.bin");
    byte[] translatedBytes10 = getBytes("dol_english_10.bin");
    byte[] translatedBytes11 = getBytes("dol_english_11.bin");
    File file = uncompressedDir.resolve(DOL).toFile();
    try(RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
      raf.seek(OFFSET_1);
      raf.write(translatedBytes1);
      raf.seek(OFFSET_2);
      raf.write(translatedBytes2);
      raf.seek(OFFSET_3);
      raf.write(translatedBytes3);
      raf.seek(OFFSET_4);
      raf.write(translatedBytes4);
      raf.seek(OFFSET_5);
      raf.write(translatedBytes5);
      raf.seek(OFFSET_6);
      raf.write(translatedBytes6);
      raf.seek(OFFSET_7);
      raf.write(translatedBytes7);
      raf.seek(OFFSET_8);
      raf.write(translatedBytes8);
      raf.seek(OFFSET_9);
      raf.write(translatedBytes9);
      raf.seek(OFFSET_10);
      raf.write(translatedBytes10);
      raf.seek(OFFSET_11);
      raf.write(translatedBytes11);
    }
  }

  /**
   * Get the bytes for a file via finding the resource with the given name.
   *
   * @param file The resource name.
   * @return The bytes of the file.
   * @throws IOException If any I/O issues occur
   */
  private static byte[] getBytes(String file) throws IOException {
    try (InputStream is = ViewerTranslator.class.getResourceAsStream(file)) {
      return is.readAllBytes();
    }
  }
}
