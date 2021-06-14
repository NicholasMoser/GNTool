package com.github.nicholasmoser.gnt4.trans;

import com.github.nicholasmoser.utils.CRC32;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A class to translate the 0000.seq files for each character into English. More specifically, this
 * translates the combo text shown under Combos in training mode.
 */
public class ChrTranslator {

  /**
   * Returns the translation state of the chr text. Will return ENGLISH if all chr text is in
   * English and JAPANESE if all chr text is in Japanese. Otherwise, will return UNKNOWN.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @return The current translation state.
   * @throws IOException If any I/O issues occur
   */
  public static TranslationState getTranslationState(Path uncompressedDir) throws IOException {
    TranslationState state = null;
    for (Entry<String, Integer> entry : TRANSLATED_CRC32.entrySet()) {
      String key = entry.getKey();
      Path file = uncompressedDir.resolve(key);
      if (!Files.exists(file)) {
        throw new IOException("File does not exist: " + file);
      }
      int crc32 = CRC32.getHash(file);
      int vanillaHash = VANILLA_CRC32.get(key);
      if (crc32 == entry.getValue()) {
        if (state == null) {
          state = TranslationState.ENGLISH;
        } else if (state != TranslationState.ENGLISH) {
          return TranslationState.UNKNOWN;
        }
      } else if (crc32 == vanillaHash) {
        if (state == null) {
          state = TranslationState.JAPANESE;
        } else if (state != TranslationState.JAPANESE) {
          return TranslationState.UNKNOWN;
        }
      } else {
        return TranslationState.UNKNOWN;
      }
    }
    return state;
  }

  /**
   * Translates the chr text to English.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void translate(Path uncompressedDir) throws IOException {
    for (Entry<String, Integer> chrEntry : CHR_COMBO_OFFSET.entrySet()) {
      String chr = chrEntry.getKey();
      String file = "files/chr/" + chr + "/0000.seq";
      Path filePath = uncompressedDir.resolve(file);
      byte[] translatedBytes;
      String bin = "combos/" + chr + ".bin";
      try (InputStream is = ViewerTranslator.class.getResourceAsStream(bin)) {
        translatedBytes = is.readAllBytes();
      }
      try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "rw")) {
        raf.seek(chrEntry.getValue());
        raf.write(translatedBytes);
      }
    }
  }

  /*
   * All of the vanilla chr seq files (in Japanese) mapped to their CRC32 hash.
   */
  public static final Map<String, Integer> VANILLA_CRC32 = Map.<String, Integer>ofEntries(
      new SimpleEntry<>("files/chr/ank/0000.seq", 0xAEBE6278),
      new SimpleEntry<>("files/chr/bou/0000.seq", 0x19A14F7E),
      new SimpleEntry<>("files/chr/cho/0000.seq", 0x2602F250),
      new SimpleEntry<>("files/chr/dog/0000.seq", 0xF1EB1AF6),
      new SimpleEntry<>("files/chr/gai/0000.seq", 0xCB46B096),
      new SimpleEntry<>("files/chr/gar/0000.seq", 0xCAFB3F05),
      new SimpleEntry<>("files/chr/hak/0000.seq", 0x129F43B9),
      new SimpleEntry<>("files/chr/hi2/0000.seq", 0x7625577F),
      new SimpleEntry<>("files/chr/hin/0000.seq", 0x36FCBB6E),
      new SimpleEntry<>("files/chr/ino/0000.seq", 0x1EB156F5),
      new SimpleEntry<>("files/chr/iru/0000.seq", 0x47D14575),
      new SimpleEntry<>("files/chr/ita/0000.seq", 0x4B185041),
      new SimpleEntry<>("files/chr/jir/0000.seq", 0x6C10B3CC),
      new SimpleEntry<>("files/chr/kab/0000.seq", 0xE5D95D8E),
      new SimpleEntry<>("files/chr/kak/0000.seq", 0x03A83C04),
      new SimpleEntry<>("files/chr/kan/0000.seq", 0xA93E5460),
      new SimpleEntry<>("files/chr/kar/0000.seq", 0x18D3C1D9),
      new SimpleEntry<>("files/chr/kib/0000.seq", 0xA6F618BC),
      new SimpleEntry<>("files/chr/kid/0000.seq", 0xD281433F),
      new SimpleEntry<>("files/chr/kim/0000.seq", 0xA06E62F6),
      new SimpleEntry<>("files/chr/kis/0000.seq", 0x8DC540A8),
      new SimpleEntry<>("files/chr/loc/0000.seq", 0x707541A9),
      new SimpleEntry<>("files/chr/miz/0000.seq", 0xC337F9CD),
      new SimpleEntry<>("files/chr/na9/0000.seq", 0xBA8E80F4),
      new SimpleEntry<>("files/chr/nar/0000.seq", 0x90850A15),
      new SimpleEntry<>("files/chr/nej/0000.seq", 0x490A0854),
      new SimpleEntry<>("files/chr/oro/0000.seq", 0xB27EABA0),
      new SimpleEntry<>("files/chr/sa2/0000.seq", 0x495096B2),
      new SimpleEntry<>("files/chr/sak/0000.seq", 0xDA965A2A),
      new SimpleEntry<>("files/chr/sar/0000.seq", 0xD6F2760B),
      new SimpleEntry<>("files/chr/sas/0000.seq", 0x9E7F64C0),
      new SimpleEntry<>("files/chr/sik/0000.seq", 0xCFDFD296),
      new SimpleEntry<>("files/chr/sin/0000.seq", 0x4BA05E71),
      new SimpleEntry<>("files/chr/sko/0000.seq", 0xE40CFF5A),
      new SimpleEntry<>("files/chr/ta2/0000.seq", 0x5EEB2BAD),
      new SimpleEntry<>("files/chr/tay/0000.seq", 0x5A82BBD8),
      new SimpleEntry<>("files/chr/tem/0000.seq", 0x94E6338A),
      new SimpleEntry<>("files/chr/ten/0000.seq", 0x6F1BDCF0),
      new SimpleEntry<>("files/chr/tsu/0000.seq", 0x939F1710),
      new SimpleEntry<>("files/chr/zab/0000.seq", 0x75A82238)
  );

  /*
   * All of the translated chr seq files (in English) mapped to their CRC32 hash.
   */
  public static final Map<String, Integer> TRANSLATED_CRC32 = Map.<String, Integer>ofEntries(
      new SimpleEntry<>("files/chr/ank/0000.seq", 0xC1327D8F),
      new SimpleEntry<>("files/chr/bou/0000.seq", 0x542DEA4E),
      new SimpleEntry<>("files/chr/cho/0000.seq", 0x98F3D32E),
      new SimpleEntry<>("files/chr/dog/0000.seq", 0xB08BB7C8),
      new SimpleEntry<>("files/chr/gai/0000.seq", 0x092FDEAE),
      new SimpleEntry<>("files/chr/gar/0000.seq", 0xC3C8F592),
      new SimpleEntry<>("files/chr/hak/0000.seq", 0xA460EABB),
      new SimpleEntry<>("files/chr/hi2/0000.seq", 0x248F05A9),
      new SimpleEntry<>("files/chr/hin/0000.seq", 0xCCDED4F5),
      new SimpleEntry<>("files/chr/ino/0000.seq", 0xB165A819),
      new SimpleEntry<>("files/chr/iru/0000.seq", 0xEDB8A692),
      new SimpleEntry<>("files/chr/ita/0000.seq", 0x429242A6),
      new SimpleEntry<>("files/chr/jir/0000.seq", 0x99643496),
      new SimpleEntry<>("files/chr/kab/0000.seq", 0xA7DCE331),
      new SimpleEntry<>("files/chr/kak/0000.seq", 0x31F115B3),
      new SimpleEntry<>("files/chr/kan/0000.seq", 0xB90F3FE1),
      new SimpleEntry<>("files/chr/kar/0000.seq", 0x0E65BF46),
      new SimpleEntry<>("files/chr/kib/0000.seq", 0xEF0C4A9E),
      new SimpleEntry<>("files/chr/kid/0000.seq", 0x6305C48C),
      new SimpleEntry<>("files/chr/kim/0000.seq", 0xBF5D5111),
      new SimpleEntry<>("files/chr/kis/0000.seq", 0x23A01E0F),
      new SimpleEntry<>("files/chr/loc/0000.seq", 0x8187E81E),
      new SimpleEntry<>("files/chr/miz/0000.seq", 0x2BD4AEBB),
      new SimpleEntry<>("files/chr/na9/0000.seq", 0xB6F4160F),
      new SimpleEntry<>("files/chr/nar/0000.seq", 0x45A8F3F8),
      new SimpleEntry<>("files/chr/nej/0000.seq", 0xB2264B98),
      new SimpleEntry<>("files/chr/oro/0000.seq", 0xF793A983),
      new SimpleEntry<>("files/chr/sa2/0000.seq", 0x73E7F7C3),
      new SimpleEntry<>("files/chr/sak/0000.seq", 0x37E17414),
      new SimpleEntry<>("files/chr/sar/0000.seq", 0x24686E45),
      new SimpleEntry<>("files/chr/sas/0000.seq", 0x2F719734),
      new SimpleEntry<>("files/chr/sik/0000.seq", 0x06026A7A),
      new SimpleEntry<>("files/chr/sin/0000.seq", 0xE8427B49),
      new SimpleEntry<>("files/chr/sko/0000.seq", 0x3BD61FC8),
      new SimpleEntry<>("files/chr/ta2/0000.seq", 0x203501C3),
      new SimpleEntry<>("files/chr/tay/0000.seq", 0xD30BF7FE),
      new SimpleEntry<>("files/chr/tem/0000.seq", 0xEC978965),
      new SimpleEntry<>("files/chr/ten/0000.seq", 0x01E9272B),
      new SimpleEntry<>("files/chr/tsu/0000.seq", 0x71AEBB04),
      new SimpleEntry<>("files/chr/zab/0000.seq", 0x6A59C360)
  );

  /*
   * All of the characters mapped to the offset of their combo text for training mode. This offset
   * is for the character's 0000.seq file.
   */
  public static final Map<String, Integer> CHR_COMBO_OFFSET = Map.<String, Integer>ofEntries(
      new SimpleEntry<>("ank", 0x25CA4),
      new SimpleEntry<>("bou", 0x28714),
      new SimpleEntry<>("cho", 0x273A4),
      new SimpleEntry<>("dog", 0x1C584),
      new SimpleEntry<>("gai", 0x24C94),
      new SimpleEntry<>("gar", 0x28BC4),
      new SimpleEntry<>("hak", 0x25184),
      new SimpleEntry<>("hi2", 0x24AC4),
      new SimpleEntry<>("hin", 0x26054),
      new SimpleEntry<>("ino", 0x27574),
      new SimpleEntry<>("iru", 0x23704),
      new SimpleEntry<>("ita", 0x29B34),
      new SimpleEntry<>("jir", 0x275B4),
      new SimpleEntry<>("kab", 0x25C94),
      new SimpleEntry<>("kak", 0x32FF0),
      new SimpleEntry<>("kan", 0x25CE4),
      new SimpleEntry<>("kar", 0x20104),
      new SimpleEntry<>("kib", 0x293D4),
      new SimpleEntry<>("kid", 0x273F4),
      new SimpleEntry<>("kim", 0x27D74),
      new SimpleEntry<>("kis", 0x25CE4),
      new SimpleEntry<>("loc", 0x28294),
      new SimpleEntry<>("miz", 0x23884),
      new SimpleEntry<>("na9", 0x25F54),
      new SimpleEntry<>("nar", 0x30FA0),
      new SimpleEntry<>("nej", 0x26194),
      new SimpleEntry<>("oro", 0x27C04),
      new SimpleEntry<>("sa2", 0x26F54),
      new SimpleEntry<>("sak", 0x27714),
      new SimpleEntry<>("sar", 0x26304),
      new SimpleEntry<>("sas", 0x29CE0),
      new SimpleEntry<>("sik", 0x27104),
      new SimpleEntry<>("sin", 0x25E84),
      new SimpleEntry<>("sko", 0x26AA0),
      new SimpleEntry<>("ta2", 0x195B4),
      new SimpleEntry<>("tay", 0x28FA4),
      new SimpleEntry<>("tem", 0x2ABE4),
      new SimpleEntry<>("ten", 0x2BD14),
      new SimpleEntry<>("tsu", 0x26104),
      new SimpleEntry<>("zab", 0x23F34)
  );
}
