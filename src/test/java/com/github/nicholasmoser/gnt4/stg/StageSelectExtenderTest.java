package com.github.nicholasmoser.gnt4.stg;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class StageSelectExtenderTest {

  @Test
  public void isExtended() throws Exception {
    Path charSel = Prereqs.getUncompressedGNT4().resolve(Seqs.CHARSEL);
    assertFalse(StageSelectExtender.isExtended(charSel));
  }
}
