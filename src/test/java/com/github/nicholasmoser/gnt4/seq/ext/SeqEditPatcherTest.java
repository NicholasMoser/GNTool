package com.github.nicholasmoser.gnt4.seq.ext;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.nicholasmoser.gnt4.chr.KisamePhantomSwordFix;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SeqEditPatcherTest {

  @Test
  public void seqEditStringConversionWorks() throws Exception {
    // Get two unmodified Kisame 0000.seq files
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path original = uncompressedDir.resolve(Seqs.KIS_0000);
    Path expected = Files.createTempFile("SeqEditPatcherTest_expected", ".seq");
    Path actual = Files.createTempFile("SeqEditPatcherTest_actual", ".seq");
    Path editFile = Files.createTempFile("SeqEditPatcherTest_editFile", ".seq");

    try {
      // Copy contents to temp files
      Files.copy(original, expected, REPLACE_EXISTING);
      Files.copy(original, actual, REPLACE_EXISTING);

      // Add the phantom sword fix to the first and export it to a file
      SeqEdit psFix = KisamePhantomSwordFix.getSeqEdit(expected);
      SeqExt.addEdit(psFix, expected);
      SeqEditPatcher.exportToFile(psFix, Seqs.KIS_0000, editFile);

      // Import the file into the other seq file and compare
      SeqEditPatcher.importFromFile(editFile, actual, true);
      byte[] actualBytes = Files.readAllBytes(actual);
      byte[] expectedBytes = Files.readAllBytes(expected);
      assertThat(actualBytes).isEqualTo(expectedBytes);
    } finally {
      Files.deleteIfExists(expected);
      Files.deleteIfExists(actual);
      Files.deleteIfExists(editFile);
    }
  }

  @Test
  public void canImportEditWithNewPosition() throws Exception {
    // Get two unmodified Kisame 0000.seq files
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path original = uncompressedDir.resolve(Seqs.KIS_0000);
    Path expected = Files.createTempFile("SeqEditPatcherTest_expected", ".seq");
    Path actual = Files.createTempFile("SeqEditPatcherTest_actual", ".seq");
    Path editFile = Files.createTempFile("SeqEditPatcherTest_editFile", ".seq");

    try {
      // Copy contents to temp files
      Files.copy(original, expected, REPLACE_EXISTING);
      Files.copy(original, actual, REPLACE_EXISTING);

      // Add the phantom sword fix to the first and export it to a file
      SeqEdit psFix = KisamePhantomSwordFix.getSeqEdit(expected);
      SeqExt.addEdit(psFix, expected);
      SeqEditPatcher.exportToFile(psFix, Seqs.KIS_0000, editFile);

      // Add a new random edit to the old Kisame 0000.seq file
      SeqEdit randomEdit = SeqEditBuilder.getBuilder()
          .name("Random Edit")
          .newBytes(new byte[] {0x22, 0x02, 0x02, 0x3F, 0x00, 0x00, 0x00, 0x03})
          .seqPath(original)
          .startOffset(0x15F0)
          .endOffset(0x15F8)
          .create();
      SeqExt.addEdit(randomEdit, expected);

      // Import the file into the other seq file and compare
      SeqEditPatcher.importFromFile(editFile, actual, true);
      List<SeqEdit> actualEdits = SeqExt.getEdits(actual);
      List<SeqEdit> expectedEdits = SeqExt.getEdits(expected);
      assertThat(actualEdits).hasSize(1);
      assertThat(expectedEdits).hasSize(2);
      assertThat(actualEdits.get(0)).isEqualTo(expectedEdits.get(0));
      assertThat(actualEdits.get(0).getPosition()).isEqualTo(expectedEdits.get(0).getPosition());
    } finally {
      Files.deleteIfExists(expected);
      Files.deleteIfExists(actual);
      Files.deleteIfExists(editFile);
    }
  }

  @Test
  public void canImportEditWithNewPositionNewOrder() throws Exception {
    // Get two unmodified Kisame 0000.seq files
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path original = uncompressedDir.resolve(Seqs.KIS_0000);
    Path expected = Files.createTempFile("SeqEditPatcherTest_expected", ".seq");
    Path actual = Files.createTempFile("SeqEditPatcherTest_actual", ".seq");
    Path editFile = Files.createTempFile("SeqEditPatcherTest_editFile", ".seq");

    try {
      // Copy contents to temp files
      Files.copy(original, expected, REPLACE_EXISTING);
      Files.copy(original, actual, REPLACE_EXISTING);

      // Add the phantom sword fix to the first
      SeqEdit psFix = KisamePhantomSwordFix.getSeqEdit(expected);
      SeqExt.addEdit(psFix, expected);

      // Add a new random edit to the old Kisame 0000.seq file and export to file
      SeqEdit randomEdit = SeqEditBuilder.getBuilder()
          .name("Random Edit")
          .newBytes(new byte[] {0x22, 0x02, 0x02, 0x3F, 0x00, 0x00, 0x00, 0x03})
          .seqPath(original)
          .startOffset(0x15F0)
          .endOffset(0x15F8)
          .create();
      SeqExt.addEdit(randomEdit, expected);
      SeqEditPatcher.exportToFile(randomEdit, Seqs.KIS_0000, editFile);

      // Import the file into the other seq file and compare
      SeqEditPatcher.importFromFile(editFile, actual, true);
      List<SeqEdit> actualEdits = SeqExt.getEdits(actual);
      List<SeqEdit> expectedEdits = SeqExt.getEdits(expected);
      assertThat(actualEdits).hasSize(1);
      assertThat(expectedEdits).hasSize(2);
      assertThat(actualEdits.get(0)).isEqualTo(expectedEdits.get(1));
      assertThat(actualEdits.get(0).getPosition()).isNotEqualTo(expectedEdits.get(1).getPosition());
    } finally {
      Files.deleteIfExists(expected);
      Files.deleteIfExists(actual);
      Files.deleteIfExists(editFile);
    }
  }

  @Test
  public void mergeConflictThrowsIllegalArgumentException() throws Exception {
    // Get two unmodified Kisame 0000.seq files
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path original = uncompressedDir.resolve(Seqs.KIS_0000);
    Path expected = Files.createTempFile("SeqEditPatcherTest_expected", ".seq");
    Path actual = Files.createTempFile("SeqEditPatcherTest_actual", ".seq");
    Path editFile = Files.createTempFile("SeqEditPatcherTest_editFile", ".seq");

    try {
      // Copy contents to temp files
      Files.copy(original, expected, REPLACE_EXISTING);
      Files.copy(original, actual, REPLACE_EXISTING);

      // Add the phantom sword fix to the first and export to file
      SeqEdit psFix = KisamePhantomSwordFix.getSeqEdit(expected);
      SeqExt.addEdit(psFix, expected);
      SeqEditPatcher.exportToFile(psFix, Seqs.KIS_0000, editFile);

      // Add a new random edit to the other Kisame 0000.seq file in same location as original edit
      SeqEdit randomEdit = SeqEditBuilder.getBuilder()
          .name("Random Edit")
          .newBytes(new byte[] {0x22, 0x02, 0x02, 0x3F, 0x00, 0x00, 0x00, 0x03})
          .seqPath(original)
          .startOffset(KisamePhantomSwordFix.OFFSET)
          .endOffset(KisamePhantomSwordFix.OFFSET + 8)
          .create();
      SeqExt.addEdit(randomEdit, actual);

      // Import the file into the other seq file and validate IllegalArgumentException is thrown
      assertThrows(IllegalArgumentException.class, () -> SeqEditPatcher.importFromFile(editFile, actual, true));
    } finally {
      Files.deleteIfExists(expected);
      Files.deleteIfExists(actual);
      Files.deleteIfExists(editFile);
    }
  }

  @Test
  public void notForcingMustMatchFilePath() throws Exception {
    // Get two unmodified Kisame 0000.seq files
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path original = uncompressedDir.resolve(Seqs.KIS_0000);
    Path expected = Files.createTempFile("SeqEditPatcherTest_expected", ".seq");
    Path actual = Files.createTempFile("SeqEditPatcherTest_actual", ".seq");
    Path editFile = Files.createTempFile("SeqEditPatcherTest_editFile", ".seq");

    try {
      // Copy contents to temp files
      Files.copy(original, expected, REPLACE_EXISTING);
      Files.copy(original, actual, REPLACE_EXISTING);

      // Add the phantom sword fix to the first and export it to a file
      SeqEdit psFix = KisamePhantomSwordFix.getSeqEdit(expected);
      SeqExt.addEdit(psFix, expected);
      SeqEditPatcher.exportToFile(psFix, Seqs.KIS_0000, editFile);

      // Import the file into the other seq file and validate IllegalArgumentException is thrown
      assertThrows(IllegalArgumentException.class, () -> SeqEditPatcher.importFromFile(editFile, actual, false));
    } finally {
      Files.deleteIfExists(expected);
      Files.deleteIfExists(actual);
      Files.deleteIfExists(editFile);
    }
  }
}
