package com.github.nicholasmoser.gnt4;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import org.junit.jupiter.api.Test;

public class GNT4FileNamesTest {

  @Test
  public void canConvertToAndFromCompressedName() throws Exception {
    GNT4FileNames names = new GNT4FileNames();
    GNTFiles files = GNT4Files.getVanillaFiles();
    for (GNTFile file : files.getGntFileList()) {
      for (GNTChildFile child : file.getGntChildFileList()) {
        String filePath = child.getFilePath().replace("files/", "");
        String compressedPath = child.getCompressedPath();
        assertThat(names.getCompressedName(filePath)).isEqualTo(compressedPath);
        assertThat(names.fix(compressedPath)).isEqualTo(filePath);
      }
    }
  }
}
