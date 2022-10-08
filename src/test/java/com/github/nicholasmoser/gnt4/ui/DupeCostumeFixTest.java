package com.github.nicholasmoser.gnt4.ui;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEditBuilder;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.FileUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class DupeCostumeFixTest {

  private static final String EXT_PARAM_1_NAME = "Fix Costume Duplicate Check (extension_parameters_1)";
  private static final int EXT_PARAM_1_OFFSET = 0x5798;
  private static final int EXT_PARAM_1_LENGTH = 0x18;
  private static final byte[] EXT_PARAM_1_NEW_BYTES = ByteUtils.hexTextToBytes("""
      3C010000 0000000C 7FFFFF2D
      3C010000 0000000F 7FFFFF2E
      3C010000 00000006 7FFFFF24
      3C010000 00000009 7FFFFF25
      """);

  @Test
  public void dupeCostumFixWorks() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    Path tempDir = FileUtils.getTempDirectory();
    Path testFile = tempDir.resolve(UUID.randomUUID().toString());

    Path charSel = uncompressed.resolve("files/maki/char_sel.seq");
    Files.copy(charSel, testFile);
    try {
      // No edits, integrity is valid, code is not enabled
      List<SeqEdit> edits = SeqExt.getEdits(testFile);
      assertThat(edits).isEmpty();
      assertThat(DupeCostumeFix.verifyIntegrity(edits)).isEmpty();
      assertThat(DupeCostumeFix.isEnabled(edits)).isFalse();

      // Enable the fix, integrity is valid, code is enabled
      DupeCostumeFix.enable(testFile);
      edits = SeqExt.getEdits(testFile);
      assertThat(edits).hasSize(6);
      assertThat(DupeCostumeFix.verifyIntegrity(edits)).isEmpty();
      assertThat(DupeCostumeFix.isEnabled(edits)).isTrue();

      // Disable the fix, integrity is valid, code is disabled
      DupeCostumeFix.disable(testFile);
      edits = SeqExt.getEdits(testFile);
      assertThat(edits).isEmpty();
      assertThat(DupeCostumeFix.verifyIntegrity(edits)).isEmpty();
      assertThat(DupeCostumeFix.isEnabled(edits)).isFalse();

      // Partially apply the fix, integrity is invalid, code is disabled
      SeqEdit edit = SeqEditBuilder.getBuilder()
          .name(EXT_PARAM_1_NAME)
          .newBytes(EXT_PARAM_1_NEW_BYTES)
          .seqPath(testFile)
          .startOffset(EXT_PARAM_1_OFFSET)
          .endOffset(EXT_PARAM_1_OFFSET + EXT_PARAM_1_LENGTH)
          .create();
      SeqExt.addEdit(edit, testFile);
      edits = SeqExt.getEdits(testFile);
      assertThat(edits).hasSize(1);
      assertThat(DupeCostumeFix.verifyIntegrity(edits)).hasSize(5);
      assertThat(DupeCostumeFix.isEnabled(edits)).isFalse();
    } finally {
      Files.deleteIfExists(testFile);
    }
  }
}
