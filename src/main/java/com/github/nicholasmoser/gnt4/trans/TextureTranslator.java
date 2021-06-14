package com.github.nicholasmoser.gnt4.trans;

import com.github.nicholasmoser.utils.CRC32;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class used to translate textures into English.
 */
public class TextureTranslator {

  /**
   * Returns the translation state of the textures. Will return ENGLISH if all textures are in
   * English and JAPANESE if all textures are in Japanese. Otherwise, will return UNKNOWN.
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
   * Translates the textures to English.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void translate(Path uncompressedDir) throws IOException {
    byte[] buffer = new byte[1024];
    try (ZipInputStream zis = new ZipInputStream(
        ViewerTranslator.class.getResourceAsStream("EnglishTranslatedTextures.zip"))) {
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
        if (!zipEntry.isDirectory()) {
          Path outputFile = uncompressedDir.resolve(zipEntry.getName());
          if (!Files.isRegularFile(outputFile)) {
            throw new IOException(String.format(
                "Zip entry %s cannot be patched against %s", zipEntry.getName(), outputFile));
          }
          try (OutputStream os = Files.newOutputStream(outputFile)) {
            int len;
            while ((len = zis.read(buffer)) > 0) {
              os.write(buffer, 0, len);
            }
          }
        }
        zipEntry = zis.getNextEntry();
      }
      zis.closeEntry();
    }
  }

  /*
   * All of the vanilla textures (in Japanese) mapped to their CRC32 hash.
   */
  public static final Map<String, Integer> VANILLA_CRC32 = Map.<String, Integer>ofEntries(
      new SimpleEntry<>("files/vs/oro/4200.txg", 0x8F9AE4A3),
      new SimpleEntry<>("files/chr/na9/4100.txg", 0xA90AF1F9),
      new SimpleEntry<>("files/vs/sa2/4200.txg", 0x8D6CA856),
      new SimpleEntry<>("files/vs/tsu/4200.txg", 0x48D290A3),
      new SimpleEntry<>("files/vs/sko/4200.txg", 0x26E67BBF),
      new SimpleEntry<>("files/vs/tem/4200.txg", 0x716DDEFD),
      new SimpleEntry<>("files/vs/ino/4200.txg", 0x689C1A71),
      new SimpleEntry<>("files/game/0100.txg", 0x6AF1BEF1),
      new SimpleEntry<>("files/vs/iru/4200.txg", 0x7D5966A3),
      new SimpleEntry<>("files/chr/hak/4100.txg", 0x207EAC3E),
      new SimpleEntry<>("files/chr/zab/4100.txg", 0xA8419714),
      new SimpleEntry<>("files/chr/loc/4100.txg", 0x5A7A04AC),
      new SimpleEntry<>("files/chr/sin/4100.txg", 0xAD63EC42),
      new SimpleEntry<>("files/chr/kar/4100.txg", 0xC64D1336),
      new SimpleEntry<>("files/vs/sik/4200.txg", 0x9B7E59A6),
      new SimpleEntry<>("files/vs/sar/4200.txg", 0x2DD60F2F),
      new SimpleEntry<>("files/vs/kim/4200.txg", 0xD3ED8D88),
      new SimpleEntry<>("files/title/0205.txg", 0x1EB563D2),
      new SimpleEntry<>("files/title/0001.txg", 0x5034E0AA),
      new SimpleEntry<>("files/chr/hin/4100.txg", 0x66812470),
      new SimpleEntry<>("files/cmn/0001.txg", 0x7D0DA513),
      new SimpleEntry<>("files/title/0203.txg", 0xAA576A03),
      new SimpleEntry<>("files/vs/cho/4200.txg", 0x64FD9437),
      new SimpleEntry<>("files/title/0100.txg", 0xD6B67B24),
      new SimpleEntry<>("files/title/0201.txg", 0xF66AFA23),
      new SimpleEntry<>("files/chr/gar/4100.txg", 0xAE37E3C4),
      new SimpleEntry<>("files/chr/jir/4100.txg", 0xBBF7AC0B),
      new SimpleEntry<>("files/vs/na9/4200.txg", 0x96AE582D),
      new SimpleEntry<>("files/vs/miz/4200.txg", 0xD6769EB6),
      new SimpleEntry<>("files/chr/ten/4100.txg", 0x78ACA17C),
      new SimpleEntry<>("files/chr/kab/4100.txg", 0x31FB079B),
      new SimpleEntry<>("files/game/0404.txg", 0x8B07C722),
      new SimpleEntry<>("files/chr/kid/4100.txg", 0x9060ED78),
      new SimpleEntry<>("files/sel/0055.txg", 0x091959DF),
      new SimpleEntry<>("files/chr/nej/4100.txg", 0xAA5E8A4B),
      new SimpleEntry<>("files/chr/kak/4100.txg", 0x04DFC297),
      new SimpleEntry<>("files/game/0402.txg", 0x2D495233),
      new SimpleEntry<>("files/chr/nar/4100.txg", 0x9DDF8DCB),
      new SimpleEntry<>("files/chr/dog/4100.txg", 0x1B880E8B),
      new SimpleEntry<>("files/sel/0053.txg", 0x06BFDB98),
      new SimpleEntry<>("files/chr/sas/4100.txg", 0xE9EEEBB2),
      new SimpleEntry<>("files/vs/zab/4200.txg", 0xB16E3BA0),
      new SimpleEntry<>("files/vs/hak/4200.txg", 0x82E608F3),
      new SimpleEntry<>("files/vs/sin/4200.txg", 0xA61AC906),
      new SimpleEntry<>("files/vs/loc/4200.txg", 0x97E32E30),
      new SimpleEntry<>("files/vs/kar/4200.txg", 0x7EE7DFF2),
      new SimpleEntry<>("files/sel/0051.txg", 0x1182D407),
      new SimpleEntry<>("files/chr/kis/4100.txg", 0x3DCEA4B1),
      new SimpleEntry<>("files/vs/hin/4200.txg", 0xA463FC3C),
      new SimpleEntry<>("files/game/0012.txg", 0x33FBE353),
      new SimpleEntry<>("files/chr/ank/4100.txg", 0x72F3F88B),
      new SimpleEntry<>("files/chr/kib/4100.txg", 0xCF352194),
      new SimpleEntry<>("files/vs/gar/4200.txg", 0x23D400CF),
      new SimpleEntry<>("files/vs/jir/4200.txg", 0x6FC427B0),
      new SimpleEntry<>("files/vs/ten/4200.txg", 0x2AAEEBE6),
      new SimpleEntry<>("files/vs/kid/4200.txg", 0xF20B5EC0),
      new SimpleEntry<>("files/vs/kab/4200.txg", 0x0B69B9C2),
      new SimpleEntry<>("files/chr/tay/4100.txg", 0x32942BAA),
      new SimpleEntry<>("files/omake/0050.txg", 0x903DB356),
      new SimpleEntry<>("files/chr/kan/4100.txg", 0x624BA42C),
      new SimpleEntry<>("files/vs/nej/4200.txg", 0x65F457E1),
      new SimpleEntry<>("files/vs/kak/4200.txg", 0x4CEE464E),
      new SimpleEntry<>("files/vs/nar/4200.txg", 0xA08CF611),
      new SimpleEntry<>("files/chr/ta2/4100.txg", 0xF74D8E7A),
      new SimpleEntry<>("files/vs/dog/4200.txg", 0x9B52C572),
      new SimpleEntry<>("files/vs/sas/4200.txg", 0xFE065DA5),
      new SimpleEntry<>("files/chr/bou/4100.txg", 0xACFA1B4F),
      new SimpleEntry<>("files/chr/gai/4100.txg", 0xE4E0A1C3),
      new SimpleEntry<>("files/omake/0040.txg", 0xCA1D548C),
      new SimpleEntry<>("files/vs/kis/4200.txg", 0x77F50D6B),
      new SimpleEntry<>("files/omake/0030.txg", 0x22E8D59D),
      new SimpleEntry<>("files/vs/ank/4200.txg", 0xFDD956C3),
      new SimpleEntry<>("files/title/0000.txg", 0x6C9E4AA6),
      new SimpleEntry<>("files/chr/hi2/4100.txg", 0xA249783D),
      new SimpleEntry<>("files/vs/kib/4200.txg", 0x5367A74D),
      new SimpleEntry<>("files/cmn/0000.txg", 0x122FC5AB),
      new SimpleEntry<>("files/chr/ita/4100.txg", 0xF0523BA5),
      new SimpleEntry<>("files/title/0202.txg", 0x5B1872C2),
      new SimpleEntry<>("files/chr/sak/4100.txg", 0x55F311EB),
      new SimpleEntry<>("files/vs/tay/4200.txg", 0x443348AC),
      new SimpleEntry<>("files/vs/kan/4200.txg", 0x7F5268BB),
      new SimpleEntry<>("files/omake/0020.txg", 0xF46F307A),
      new SimpleEntry<>("files/title/0200.txg", 0x13372D50),
      new SimpleEntry<>("files/chr/obo/4100.txg", 0x4A3AE650),
      new SimpleEntry<>("files/vs/bou/4200.txg", 0x09F986C2),
      new SimpleEntry<>("files/vs/gai/4200.txg", 0xDAD476EB),
      new SimpleEntry<>("files/sel/0054.txg", 0xBFD9D7FA),
      new SimpleEntry<>("files/chr/oro/4100.txg", 0x11DB0523),
      new SimpleEntry<>("files/chr/sa2/4100.txg", 0x07C7578B),
      new SimpleEntry<>("files/omake/0010.txg", 0xDA26DC8E),
      new SimpleEntry<>("files/game/0401.txg", 0x8395A612),
      new SimpleEntry<>("files/chr/tsu/4100.txg", 0xE75FFCAA),
      new SimpleEntry<>("files/chr/sko/4100.txg", 0x6DEF488C),
      new SimpleEntry<>("files/sel/0052.txg", 0x4FA2C6D2),
      new SimpleEntry<>("files/chr/tem/4100.txg", 0x7AAA0B08),
      new SimpleEntry<>("files/chr/ino/4100.txg", 0x5732B7E1),
      new SimpleEntry<>("files/sel/0050.txg", 0xEAE8FEC3),
      new SimpleEntry<>("files/chr/iru/4100.txg", 0x05650C8D),
      new SimpleEntry<>("files/vs/hi2/4200.txg", 0x32B0328B),
      new SimpleEntry<>("files/chr/sik/4100.txg", 0xF860E430),
      new SimpleEntry<>("files/vs/ita/4200.txg", 0x925D5000),
      new SimpleEntry<>("files/chr/sar/4100.txg", 0x1479CB39),
      new SimpleEntry<>("files/chr/kim/4100.txg", 0x94155855),
      new SimpleEntry<>("files/vs/sak/4200.txg", 0xD799C94C),
      new SimpleEntry<>("files/chr/cho/4100.txg", 0xA70076CA),
      new SimpleEntry<>("files/vs/obo/4200.txg", 0xF36B3832),
      new SimpleEntry<>("files/chr/miz/4100.txg", 0x245A3990)
  );

  /*
   * All of the translated textures (in English) mapped to their CRC32 hash.
   */
  public static final Map<String, Integer> TRANSLATED_CRC32 = Map.<String, Integer>ofEntries(
      new SimpleEntry<>("files/chr/ank/4100.txg", 0xBBE3F9F8),
      new SimpleEntry<>("files/chr/bou/4100.txg", 0xD7BBC65C),
      new SimpleEntry<>("files/chr/cho/4100.txg", 0x8CA536E9),
      new SimpleEntry<>("files/chr/dog/4100.txg", 0x753D96B4),
      new SimpleEntry<>("files/chr/gai/4100.txg", 0xD3CD02F4),
      new SimpleEntry<>("files/chr/gar/4100.txg", 0x4FD7247B),
      new SimpleEntry<>("files/chr/hak/4100.txg", 0xE1CB3F51),
      new SimpleEntry<>("files/chr/hi2/4100.txg", 0x68F1714C),
      new SimpleEntry<>("files/chr/hin/4100.txg", 0xBAEB9BB5),
      new SimpleEntry<>("files/chr/ino/4100.txg", 0xF72876E7),
      new SimpleEntry<>("files/chr/iru/4100.txg", 0x8BD04597),
      new SimpleEntry<>("files/chr/ita/4100.txg", 0x1A61C262),
      new SimpleEntry<>("files/chr/jir/4100.txg", 0xDACC03B7),
      new SimpleEntry<>("files/chr/kab/4100.txg", 0x3CA555B4),
      new SimpleEntry<>("files/chr/kak/4100.txg", 0x388238F8),
      new SimpleEntry<>("files/chr/kan/4100.txg", 0xD763C92A),
      new SimpleEntry<>("files/chr/kar/4100.txg", 0xF025A108),
      new SimpleEntry<>("files/chr/kib/4100.txg", 0x0EBC0876),
      new SimpleEntry<>("files/chr/kid/4100.txg", 0xFD418CBA),
      new SimpleEntry<>("files/chr/kim/4100.txg", 0x26D123E6),
      new SimpleEntry<>("files/chr/kis/4100.txg", 0xD3276184),
      new SimpleEntry<>("files/chr/loc/4100.txg", 0xFB16FE52),
      new SimpleEntry<>("files/chr/miz/4100.txg", 0x0D21FC5A),
      new SimpleEntry<>("files/chr/na9/4100.txg", 0xCFD96591),
      new SimpleEntry<>("files/chr/nar/4100.txg", 0xBE13E0DB),
      new SimpleEntry<>("files/chr/nej/4100.txg", 0xFF543934),
      new SimpleEntry<>("files/chr/obo/4100.txg", 0x50EC2A80),
      new SimpleEntry<>("files/chr/oro/4100.txg", 0xD563E84A),
      new SimpleEntry<>("files/chr/sa2/4100.txg", 0x0A4E00AC),
      new SimpleEntry<>("files/chr/sak/4100.txg", 0xCBE3E4EB),
      new SimpleEntry<>("files/chr/sar/4100.txg", 0x3CD9A0B7),
      new SimpleEntry<>("files/chr/sas/4100.txg", 0x0D196F7F),
      new SimpleEntry<>("files/chr/sik/4100.txg", 0xEDA95C51),
      new SimpleEntry<>("files/chr/sin/4100.txg", 0xF8C8AC46),
      new SimpleEntry<>("files/chr/sko/4100.txg", 0x89F68463),
      new SimpleEntry<>("files/chr/ta2/4100.txg", 0x3C3A2713),
      new SimpleEntry<>("files/chr/tay/4100.txg", 0x3C3A2713),
      new SimpleEntry<>("files/chr/tem/4100.txg", 0xB482EFD0),
      new SimpleEntry<>("files/chr/ten/4100.txg", 0xB05EF2BD),
      new SimpleEntry<>("files/chr/tsu/4100.txg", 0x480D4655),
      new SimpleEntry<>("files/chr/zab/4100.txg", 0x36FE12C4),
      new SimpleEntry<>("files/cmn/0000.txg", 0x8F117F09),
      new SimpleEntry<>("files/cmn/0001.txg", 0x66B8B36F),
      new SimpleEntry<>("files/game/0012.txg", 0xF4FE5BA7),
      new SimpleEntry<>("files/game/0100.txg", 0x04028929),
      new SimpleEntry<>("files/game/0401.txg", 0xF6446D33),
      new SimpleEntry<>("files/game/0402.txg", 0xAE906B2D),
      new SimpleEntry<>("files/game/0404.txg", 0x5904C283),
      new SimpleEntry<>("files/omake/0010.txg", 0x18841FDD),
      new SimpleEntry<>("files/omake/0020.txg", 0xFCCEE7BA),
      new SimpleEntry<>("files/omake/0030.txg", 0x76F329D1),
      new SimpleEntry<>("files/omake/0040.txg", 0x752B4C72),
      new SimpleEntry<>("files/omake/0050.txg", 0x7237708C),
      new SimpleEntry<>("files/sel/0050.txg", 0xC33D5573),
      new SimpleEntry<>("files/sel/0051.txg", 0xFAFA092B),
      new SimpleEntry<>("files/sel/0052.txg", 0xDB8EECFE),
      new SimpleEntry<>("files/sel/0053.txg", 0x835900F8),
      new SimpleEntry<>("files/sel/0054.txg", 0x3133BA71),
      new SimpleEntry<>("files/sel/0055.txg", 0xA2C9D72B),
      new SimpleEntry<>("files/title/0000.txg", 0x16F53749),
      new SimpleEntry<>("files/title/0001.txg", 0x1A1DC350),
      new SimpleEntry<>("files/title/0100.txg", 0x29847175),
      new SimpleEntry<>("files/title/0200.txg", 0xBBB3224E),
      new SimpleEntry<>("files/title/0201.txg", 0xE681D934),
      new SimpleEntry<>("files/title/0202.txg", 0x2EF7B265),
      new SimpleEntry<>("files/title/0203.txg", 0xB6316849),
      new SimpleEntry<>("files/title/0205.txg", 0xF2AAA9E3),
      new SimpleEntry<>("files/vs/ank/4200.txg", 0x77CEB671),
      new SimpleEntry<>("files/vs/bou/4200.txg", 0x89772781),
      new SimpleEntry<>("files/vs/cho/4200.txg", 0xD940977F),
      new SimpleEntry<>("files/vs/dog/4200.txg", 0xBB677A00),
      new SimpleEntry<>("files/vs/gai/4200.txg", 0xE4349CC4),
      new SimpleEntry<>("files/vs/gar/4200.txg", 0xD7C0C99C),
      new SimpleEntry<>("files/vs/hak/4200.txg", 0xB158E10F),
      new SimpleEntry<>("files/vs/hi2/4200.txg", 0xBCCC4D26),
      new SimpleEntry<>("files/vs/hin/4200.txg", 0x11C05195),
      new SimpleEntry<>("files/vs/ino/4200.txg", 0x804A286F),
      new SimpleEntry<>("files/vs/iru/4200.txg", 0xB2E3E501),
      new SimpleEntry<>("files/vs/ita/4200.txg", 0x11CF93A0),
      new SimpleEntry<>("files/vs/jir/4200.txg", 0xAF6B07FE),
      new SimpleEntry<>("files/vs/kab/4200.txg", 0xBDB63B1B),
      new SimpleEntry<>("files/vs/kak/4200.txg", 0x34A1CB4D),
      new SimpleEntry<>("files/vs/kan/4200.txg", 0x340ACE39),
      new SimpleEntry<>("files/vs/kar/4200.txg", 0x35993E99),
      new SimpleEntry<>("files/vs/kib/4200.txg", 0x8C7A3825),
      new SimpleEntry<>("files/vs/kid/4200.txg", 0xE59599E6),
      new SimpleEntry<>("files/vs/kim/4200.txg", 0x33FD5869),
      new SimpleEntry<>("files/vs/kis/4200.txg", 0x0EC7EEB9),
      new SimpleEntry<>("files/vs/loc/4200.txg", 0x00D8F8C1),
      new SimpleEntry<>("files/vs/miz/4200.txg", 0x6A79F3FE),
      new SimpleEntry<>("files/vs/na9/4200.txg", 0x5E98F7B7),
      new SimpleEntry<>("files/vs/nar/4200.txg", 0xFD4ED687),
      new SimpleEntry<>("files/vs/nej/4200.txg", 0xB998E7F1),
      new SimpleEntry<>("files/vs/obo/4200.txg", 0x169F8E8F),
      new SimpleEntry<>("files/vs/oro/4200.txg", 0x26CAD469),
      new SimpleEntry<>("files/vs/sa2/4200.txg", 0xDA97FF2E),
      new SimpleEntry<>("files/vs/sak/4200.txg", 0xECB79CC5),
      new SimpleEntry<>("files/vs/sar/4200.txg", 0x61DF620C),
      new SimpleEntry<>("files/vs/sas/4200.txg", 0x856C9C15),
      new SimpleEntry<>("files/vs/sik/4200.txg", 0x5553FF38),
      new SimpleEntry<>("files/vs/sin/4200.txg", 0x414B7A5F),
      new SimpleEntry<>("files/vs/sko/4200.txg", 0x5D575EC3),
      new SimpleEntry<>("files/vs/tay/4200.txg", 0xEF9802F1),
      new SimpleEntry<>("files/vs/tem/4200.txg", 0x0BBA0D9B),
      new SimpleEntry<>("files/vs/ten/4200.txg", 0x3A8E3478),
      new SimpleEntry<>("files/vs/tsu/4200.txg", 0x81CB49FA),
      new SimpleEntry<>("files/vs/zab/4200.txg", 0x428476B8)
  );
}
