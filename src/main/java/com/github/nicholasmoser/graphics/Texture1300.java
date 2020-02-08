package com.github.nicholasmoser.graphics;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Texture1300 {
  private static final Logger LOGGER = Logger.getLogger(Texture1300.class.getName());

  public static void mainCharacterFix(Path uncompressed, String character) throws IOException {

    switch(character)
    {
      case GNT4Characters.NARUTO_OTK:
        Path tex1300n = getTexture1300Folder(uncompressed, character);
        Path tpl3Pathn = tex1300n.resolve("3.tpl");
        TPL tpl3n = new TPL(Files.readAllBytes(tpl3Pathn));
        if (tpl3n.getTextures().size() < 2) {
          Files.copy(tex1300n.resolve("0.tpl"), tpl3Pathn, StandardCopyOption.REPLACE_EXISTING);
          String output = TXG2TPL.pack(tex1300n, tex1300n.getParent().resolve("1300.txg"));
          LOGGER.log(Level.INFO, output);
        }
        break;
      case GNT4Characters.ITACHI:
      case GNT4Characters.KABUTO:
      case GNT4Characters.KAKASHI:
      case GNT4Characters.KANKURO:
      case GNT4Characters.KISAME:
      case GNT4Characters.LEE:
      case GNT4Characters.OROCHIMARU:
      case GNT4Characters.SARUTOBI:
      case GNT4Characters.SASUKE:
      case GNT4Characters.SAKON:
      case GNT4Characters.TSUNADE:
        Path tex1300 = getTexture1300Folder(uncompressed, character);
        Path tpl3Path = tex1300.resolve("3.tpl");
        byte[] bytes = Files.readAllBytes(tpl3Path);
        TPL tpl3 = new TPL(bytes);
        if (tpl3.getTextures().size() < 2) {
          Texture texture = tpl3.getTextures().get(0);
          Texture newTexture = Texture.newInstance(texture);
          tpl3.addTexture(newTexture);
          Files.write(tpl3Path, tpl3.getBytes());
          String output = TXG2TPL.pack(tex1300, tex1300.getParent().resolve("1300.txg"));
          LOGGER.log(Level.INFO, output);
        }
        break;
      default:
        break;
    }
  }

  private static Path getTexture1300Folder(Path uncompressed, String character) throws IOException {
    Path chr = uncompressed.resolve("files/chr");
    Path chrFolder = chr.resolve(GNT4Characters.CHAR_FOLDER.get(character));
    Path tex1300 = chrFolder.resolve("1300");
    if (!Files.isDirectory(tex1300)) {
      Path txgFilePath = chrFolder.resolve("1300.txg");
      Files.createDirectories(tex1300);
      String output = TXG2TPL.unpack(txgFilePath, tex1300);
      LOGGER.log(Level.INFO, output);
    }
    return tex1300;
  }
}
