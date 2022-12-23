package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.gnt4.GNT4Characters;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEditBuilder;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

/**
 * In the character 0000.seq file, there is a branch table where for each costume, it is believed
 * that gravity or extra physics are added to specific bones on a model. Examples include the
 * gravity for Kisame's sword wrappings and Temari's sash. This Java class will modify the branch
 * table to use a different offset for costumes 3 and 4. Most characters are simple, and will use
 * the same offset for all four costumes. Other characters require the user to decide which costume
 * to apply where.
 */
public class CostumeBranchTableFix {

  private static final Logger LOGGER = Logger.getLogger(CostumeBranchTableFix.class.getName());

  // Character with 2 costumes in vanilla
  private static final int ANK_TABLE_OFFSET = 0x2500C;
  private static final int BOU_TABLE_OFFSET = 0x27AC4;
  private static final int CHO_TABLE_OFFSET = 0x26790;
  private static final int DOG_TABLE_OFFSET = 0x1C020; // TODO: Dog has existing logic for costumes 3 and 4
  private static final int GAI_TABLE_OFFSET = 0x2420C;
  private static final int GAR_TABLE_OFFSET = 0x27FC4;
  private static final int HI2_TABLE_OFFSET = 0x23ED0;
  private static final int HIN_TABLE_OFFSET = 0x255B8;
  private static final int IRU_TABLE_OFFSET = 0x22C84;
  private static final int ITA_TABLE_OFFSET = 0x28D9C;
  private static final int JIR_TABLE_OFFSET = 0x26160;
  private static final int KAB_TABLE_OFFSET = 0x24C54;
  private static final int KAK_TABLE_OFFSET = 0x31F24;
  private static final int KAN_TABLE_OFFSET = 0x24D8C;
  private static final int KAR_TABLE_OFFSET = 0x1F950; // TODO: Karasu has existing logic for costumes 3 and 4
  private static final int KIB_TABLE_OFFSET = 0x288BC;
  private static final int KID_TABLE_OFFSET = 0x263C4; // TODO: Kidomaru has existing logic for costumes 3 and 4
  private static final int KIM_TABLE_OFFSET = 0x26B9C;
  private static final int KIS_TABLE_OFFSET = 0x24C3C;
  private static final int LOC_TABLE_OFFSET = 0x277B0;
  private static final int MIZ_TABLE_OFFSET = 0x22E24;
  private static final int NA9_TABLE_OFFSET = 0x24368; // TODO: OTK has existing logic for costumes 3 and 4
  private static final int NAR_TABLE_OFFSET = 0x2F348;
  private static final int NEJ_TABLE_OFFSET = 0x25644;
  private static final int OBO_TABLE_OFFSET = 0x1A090;
  private static final int ORO_TABLE_OFFSET = 0x26928;
  private static final int SA2_TABLE_OFFSET = 0x25E00;
  private static final int SAR_TABLE_OFFSET = 0x24CF0;
  private static final int SIK_TABLE_OFFSET = 0x264E4;
  private static final int SIN_TABLE_OFFSET = 0x252EC;
  private static final int SKO_TABLE_OFFSET = 0x259C8; // TODO: Sakon has existing logic for costumes 3 and 4
  private static final int TA2_TABLE_OFFSET = 0x191C0;
  private static final int TAY_TABLE_OFFSET = 0x27D98;
  private static final int TEN_TABLE_OFFSET = 0x2B110;
  private static final int TSU_TABLE_OFFSET = 0x255AC;
  private static final int ZAB_TABLE_OFFSET = 0x22F04;

  // Characters with 4 costumes in vanilla
  private static final int HAK_TABLE_OFFSET = 0x24568; // TODO: Implement Haku, currently complicated because of the mask
  private static final int HAK_BRANCH1_OFFSET = -1; // TODO: Implement Haku, currently complicated because of the mask
  private static final int HAK_BRANCH2_OFFSET = -1; // TODO: Implement Haku, currently complicated because of the mask
  private static final int INO_TABLE_OFFSET = 0x265BC;
  private static final int INO_SHORT_HAIR_OFFSET = 0x265D4;
  private static final int INO_LONG_HAIR_OFFSET = 0x267CC;
  private static final int SAS_TABLE_OFFSET = -1; // Sasuke has the same code for all costumes, no need to implement

  // Temari has 2 costumes but separate logic for each
  private static final int TEM_TABLE_OFFSET = 0x29B1C;
  private static final int TEM_LONG_SASH_OFFSET = 0x29B34;
  private static final int TEM_SHORT_SASH_OFFSET = 0x29B84;

  // Sakura's branch table has 5 indices and all with the same offset, no need to implement
  private static final int SAK_TABLE_OFFSET = 0x29094;

  private static final String CODE_NAME = "Costume Branch Table Fix";
  private static final byte[] BRANCH_TABLE_4_BRANCHES = ByteUtils.hexTextToBytes(
      "01500013 00000004");

  /**
   * Implement a simple case of the costume branch table fix. This involves writing a new branch
   * table where all four branches will be the same offset (the code immediately after the branch
   * table).
   *
   * @param tableOffset The offset in the seq file to the branch table.
   * @param seqPath     The path to the character 0000.seq file.
   */
  private static void enableSimpleCase(int tableOffset, Path seqPath) throws IOException {
    for (SeqEdit edit : SeqExt.getEdits(seqPath)) {
      if (edit.getName().equals(CODE_NAME)) {
        return; // Code is already applied, do nothing
      }
    }
    int codeOffset = tableOffset + 0x18;
    byte[] codeOffsetBytes = ByteUtils.fromInt32(codeOffset);
    byte[] bytes = Bytes.concat(BRANCH_TABLE_4_BRANCHES, codeOffsetBytes, codeOffsetBytes,
        codeOffsetBytes, codeOffsetBytes);
    SeqEdit edit = SeqEditBuilder.getBuilder()
        .name(CODE_NAME)
        .newBytes(bytes)
        .seqPath(seqPath)
        .startOffset(tableOffset)
        .endOffset(codeOffset)
        .create();
    SeqExt.addEdit(edit, seqPath);
  }

  private static void enableTemari(Path uncompressedDir) throws IOException {
    Path seqPath = uncompressedDir.resolve(Seqs.TEM_0000);
    for (SeqEdit edit : SeqExt.getEdits(seqPath)) {
      if (edit.getName().equals(CODE_NAME)) {
        SeqExt.removeEdit(edit, seqPath); // Remove existing edit
      }
    }

    // Get the branch for costume 3
    ButtonType longSash = new ButtonType("Long Sash", ButtonData.OK_DONE);
    ButtonType shortSash = new ButtonType("Short Sash", ButtonData.OK_DONE);
    Alert alert = new Alert(AlertType.WARNING,
        """
            Please select whether Temari's costume 3 will use her model with the long sash
            (costume 1) or the short sash (costume 2). This is necessary even if Temari costume 3
            is not being used.
            """,
        longSash,
        shortSash);
    alert.setTitle("Temari Costume 3 Fix");
    Optional<ButtonType> result = alert.showAndWait();
    boolean usingLongSash = result.orElse(longSash) == longSash;
    byte[] costume3 = ByteUtils.fromInt32(
        usingLongSash ? TEM_LONG_SASH_OFFSET : TEM_SHORT_SASH_OFFSET);

    // Get the branch for costume 4
    longSash = new ButtonType("Long Sash", ButtonData.OK_DONE);
    shortSash = new ButtonType("Short Sash", ButtonData.OK_DONE);
    alert = new Alert(AlertType.WARNING,
        """
            Please select whether Temari's costume 4 will use her model with the long sash
            (costume 1) or the short sash (costume 2). This is necessary even if Temari costume 4
            is not being used.
            """,
        longSash,
        shortSash);
    alert.setTitle("Temari Costume 4 Fix");
    result = alert.showAndWait();
    usingLongSash = result.orElse(longSash) == longSash;
    byte[] costume4 = ByteUtils.fromInt32(
        usingLongSash ? TEM_LONG_SASH_OFFSET : TEM_SHORT_SASH_OFFSET);

    // Write the code
    int codeOffset = TEM_TABLE_OFFSET + 0x18;
    byte[] longSashBytes = ByteUtils.fromInt32(TEM_LONG_SASH_OFFSET);
    byte[] shortSashBytes = ByteUtils.fromInt32(TEM_SHORT_SASH_OFFSET);
    byte[] bytes = Bytes.concat(BRANCH_TABLE_4_BRANCHES, longSashBytes, shortSashBytes,
        costume3, costume4);
    SeqEdit edit = SeqEditBuilder.getBuilder()
        .name(CODE_NAME)
        .newBytes(bytes)
        .seqPath(seqPath)
        .startOffset(TEM_TABLE_OFFSET)
        .endOffset(codeOffset)
        .create();
    SeqExt.addEdit(edit, seqPath);
  }

  private static void enableIno(Path uncompressedDir) throws IOException {
    Path seqPath = uncompressedDir.resolve(Seqs.INO_0000);
    for (SeqEdit edit : SeqExt.getEdits(seqPath)) {
      if (edit.getName().equals(CODE_NAME)) {
        SeqExt.removeEdit(edit, seqPath); // Remove existing edit
      }
    }

    // Get the branch for costume 3
    ButtonType longSash = new ButtonType("Short Hair", ButtonData.OK_DONE);
    ButtonType shortSash = new ButtonType("Long Hair", ButtonData.OK_DONE);
    Alert alert = new Alert(AlertType.WARNING,
        """
            Please select whether Ino's costume 3 will use her model with the short hair
            (costume 1) or the long hair (costume 3). This is necessary even if Ino costume 3
            is not being used.
            """,
        longSash,
        shortSash);
    alert.setTitle("Ino Costume 3 Fix");
    Optional<ButtonType> result = alert.showAndWait();
    boolean usingLongSash = result.orElse(longSash) == longSash;
    byte[] costume3 = ByteUtils.fromInt32(
        usingLongSash ? INO_SHORT_HAIR_OFFSET : INO_LONG_HAIR_OFFSET);

    // Get the branch for costume 4
    longSash = new ButtonType("Short Hair", ButtonData.OK_DONE);
    shortSash = new ButtonType("Long Hair", ButtonData.OK_DONE);
    alert = new Alert(AlertType.WARNING,
        """
            Please select whether Ino's costume 4 will use her model with the short hair
            (costume 1) or the long hair (costume 3). This is necessary even if Ino costume 4
            is not being used.
            """,
        longSash,
        shortSash);
    alert.setTitle("Ino Costume 4 Fix");
    result = alert.showAndWait();
    usingLongSash = result.orElse(longSash) == longSash;
    byte[] costume4 = ByteUtils.fromInt32(
        usingLongSash ? INO_SHORT_HAIR_OFFSET : INO_LONG_HAIR_OFFSET);

    // Write the code
    int codeOffset = INO_TABLE_OFFSET + 0x18;
    byte[] shortHairBytes = ByteUtils.fromInt32(INO_SHORT_HAIR_OFFSET);
    byte[] bytes = Bytes.concat(BRANCH_TABLE_4_BRANCHES, shortHairBytes, shortHairBytes,
        costume3, costume4);
    SeqEdit edit = SeqEditBuilder.getBuilder()
        .name(CODE_NAME)
        .newBytes(bytes)
        .seqPath(seqPath)
        .startOffset(INO_TABLE_OFFSET)
        .endOffset(codeOffset)
        .create();
    SeqExt.addEdit(edit, seqPath);
  }

  private static void enableHaku(Path uncompressedDir) throws IOException {
    // TODO: Implement Haku costume extension
    LOGGER.info("Unsupported character for CostumeBranchTableFix: Haku");
  }

  /**
   * Enables the fix on all character SEQ files
   *
   * @param uncompressedDir The path to the uncompressed directory.
   * @throws IOException If an I/O error occurs.
   */
  public static void enable(Path uncompressedDir, Collection<String> chrs) throws IOException {
    for (String chr : chrs) {
      switch (chr) {
        // First try and handle the easy characters, then handle the more complex characters
        case GNT4Characters.ANKO -> enableSimpleCase(ANK_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.ANK_0000));
        case GNT4Characters.JIROBO -> enableSimpleCase(BOU_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.BOU_0000));
        case GNT4Characters.CHOJI -> enableSimpleCase(CHO_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.CHO_0000));
        case GNT4Characters.AKAMARU -> enableSimpleCase(DOG_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.DOG_0000));
        case GNT4Characters.MIGHT_GUY -> enableSimpleCase(GAI_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.GAI_0000));
        case GNT4Characters.GAARA -> enableSimpleCase(GAR_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.GAR_0000));
        case GNT4Characters.HINATA_AWAKENED -> enableSimpleCase(HI2_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.HI2_0000));
        case GNT4Characters.HINATA -> enableSimpleCase(HIN_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.HIN_0000));
        case GNT4Characters.IRUKA -> enableSimpleCase(IRU_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.IRU_0000));
        case GNT4Characters.ITACHI -> enableSimpleCase(ITA_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.ITA_0000));
        case GNT4Characters.JIRAIYA -> enableSimpleCase(JIR_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.JIR_0000));
        case GNT4Characters.KABUTO -> enableSimpleCase(KAB_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.KAB_0000));
        case GNT4Characters.KAKASHI -> enableSimpleCase(KAK_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.KAK_0000));
        case GNT4Characters.KANKURO -> enableSimpleCase(KAN_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.KAN_0000));
        case GNT4Characters.KARASU -> enableSimpleCase(KAR_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.KAR_0000));
        case GNT4Characters.KIBA -> enableSimpleCase(KIB_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.KIB_0000));
        case GNT4Characters.KIDOMARU -> enableSimpleCase(KID_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.KID_0000));
        case GNT4Characters.KIMIMARO -> enableSimpleCase(KIM_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.KIM_0000));
        case GNT4Characters.KISAME -> enableSimpleCase(KIS_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.KIS_0000));
        case GNT4Characters.LEE -> enableSimpleCase(LOC_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.LOC_0000));
        case GNT4Characters.MIZUKI -> enableSimpleCase(MIZ_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.MIZ_0000));
        case GNT4Characters.NARUTO_OTK -> enableSimpleCase(NA9_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.NA9_0000));
        case GNT4Characters.NARUTO -> enableSimpleCase(NAR_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.NAR_0000));
        case GNT4Characters.NEJI -> enableSimpleCase(NEJ_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.NEJ_0000));
        case GNT4Characters.OBORO -> enableSimpleCase(OBO_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.OBO_0000));
        case GNT4Characters.OROCHIMARU -> enableSimpleCase(ORO_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.ORO_0000));
        case GNT4Characters.SASUKE_CS2 -> enableSimpleCase(SA2_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.SA2_0000));
        case GNT4Characters.SARUTOBI -> enableSimpleCase(SAR_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.SAR_0000));
        case GNT4Characters.SHIKAMARU -> enableSimpleCase(SIK_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.SIK_0000));
        case GNT4Characters.SHINO -> enableSimpleCase(SIN_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.SIN_0000));
        case GNT4Characters.SAKON -> enableSimpleCase(SKO_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.SKO_0000));
        case GNT4Characters.TAYUYA_DOKI -> enableSimpleCase(TA2_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.TA2_0000));
        case GNT4Characters.TAYUYA -> enableSimpleCase(TAY_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.TAY_0000));
        case GNT4Characters.TENTEN -> enableSimpleCase(TEN_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.TEN_0000));
        case GNT4Characters.TSUNADE -> enableSimpleCase(TSU_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.TSU_0000));
        case GNT4Characters.ZABUZA -> enableSimpleCase(ZAB_TABLE_OFFSET,
            uncompressedDir.resolve(Seqs.ZAB_0000));
        // Handle special cases
        case GNT4Characters.HAKU -> enableHaku(uncompressedDir);
        case GNT4Characters.INO -> enableIno(uncompressedDir);
        case GNT4Characters.TEMARI -> enableTemari(uncompressedDir);
        case GNT4Characters.SASUKE, GNT4Characters.SAKURA -> {
        } // Do nothing, no changes necessary
        default -> throw new IOException("Unsupported character for CostumeBranchTableFix: " + chr);
      }
    }
  }
}
