package com.github.nicholasmoser.gnt4;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Path;
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

  @Test
  public void shortNamesArentCompressed() {
    GNT4FileNames names = new GNT4FileNames();
    assertThat(names.getCompressedName("game/m_vs.seq")).isEqualTo("game/m_vs.seq");
    assertThat(names.getCompressedName("files/game/m_vs.seq")).isEqualTo("game/m_vs.seq");
    assertThat(names.getCompressedName("uncompressed/files/game/m_vs.seq")).isEqualTo(
        "game/m_vs.seq");
    assertThat(names.getCompressedName("files/uncompressed/files/game/m_vs.seq")).isEqualTo(
        "game/m_vs.seq");
    assertThat(names.getCompressedName("files/files/uncompressed/files/game/m_vs.seq")).isEqualTo(
        "game/m_vs.seq");
    assertThat(names.getCompressedName(
        "files/uncompressed/files/uncompressed/files/game/m_vs.seq")).isEqualTo(
        "game/m_vs.seq");
  }
}
