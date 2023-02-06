package com.github.nicholasmoser.gnt4.stg;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Arrays;

// Each offset at 0x2370 and 0x23F0 are set by the stage order

// char_sel.seq offset 0x23F0 references to stage display IDs
// these correlate to each index of files/sel/0051/3.tpl

// Things that need to be done:
//   - Update dol code at 0x800ae084 to store the new number of stages
//   - Set new Stage IDs and Stage Display IDs at offset 0x2370 in char_sel.seq and charsel4.seq
//   - Update offsets in each seq file to those offsets
//     - 0x195C, 0x2200 for Stage IDs in char_sel.seq and charsel4.seq
//     - 0xA04, 0xA74, 0xAE0, 0xB50, 0xBC0, 0xC2C, 0xC98, 0xD10, 0xDE8, 0x13D8, 0x19A0 in char_sel.seq and charsel4.seq

// BIG TODO: I THINK IT ALSO NEEDS TO BE ADDED TO 800ae064
// REMOVE BRANCH AT 0x800ae050 AND INCREASE VAR AT 0x800ae038

// 0051.txg -> 3.tpl textures are set by offset 0x23F0 in char_sel.seq
// 0051.txg -> x.tpl is set by ????????????

public class StageSelectExtender {
  public static boolean isExtended(Path uncompressedDir) throws IOException {
    Path charSel = uncompressedDir.resolve(Seqs.CHARSEL);
    try(RandomAccessFile raf = new RandomAccessFile(charSel.toFile(), "r")) {
      byte[] actual = new byte[0x100];
      byte[] expected = new byte[0x100];
      Arrays.fill(expected, (byte) 0xCC);
      // Read Stage IDs and Stage Display IDs. If they are null, it is being extended.
      raf.seek(0x23F0);
      raf.read(actual);
      return Arrays.equals(actual, expected);
    }
  }

  public static void addStage(Path uncompressedDir) throws IOException {
    Path charSel = uncompressedDir.resolve(Seqs.CHARSEL);
    Path charSel4 = uncompressedDir.resolve(Seqs.CHARSEL_4);
  }

  public static void removeStage(Path uncompressedDir) throws IOException {

  }
}
