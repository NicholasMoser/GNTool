package com.github.nicholasmoser.gnt4.ui;

import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEditBuilder;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A class used to fix the duplicate costume logic in GNT4.
 * <p>
 * The existing code of the game treats costumes 1 and 3 as identical. Same for 2 and 4. It probably
 * does this since the clothes are the same (despite face/hair changing). Also, when the code
 * detects a duplicate costume it goes from costume 1->2, 2->1, 3->4, 4->3.
 * <p>
 * Both of these behaviors are an issue. Going from 3->4 crashes if there's no costume 4. Also, you
 * can't have costume 1 and 3 fight each other because the game thinks they're duplicates. Same for
 * 2 and 4. I fixed this by writing new code that overwrites the existing behavior 1 and 3, 2 and 4
 * are no longer considered duplicates Also, 3 no longer falls back to 4 in case of duplicate, now
 * it goes to 2
 */
public class DupeCostumeFix {

  private static final String EXT_PARAM_1_NAME = "Fix Costume Duplicate Check (extension_parameters_1)";
  private static final int EXT_PARAM_1_OFFSET = 0x5798;
  private static final int EXT_PARAM_1_LENGTH = 0x18;
  private static final byte[] EXT_PARAM_1_NEW_BYTES = ByteUtils.hexTextToBytes("""
      3C010000 0000000C 7FFFFF2D
      3C010000 0000000F 7FFFFF2E
      3C010000 00000006 7FFFFF24
      3C010000 00000009 7FFFFF25
      """);

  private static final String EXT_PARAM_2_NAME = "Fix Costume Duplicate Check (extension_parameters_2)";
  private static final int EXT_PARAM_2_OFFSET = 0x57E4;
  private static final int EXT_PARAM_2_LENGTH = 0x18;
  private static final byte[] EXT_PARAM_2_NEW_BYTES = ByteUtils.hexTextToBytes("""
      3C010000 0000000F 7FFFFF2D
      3C010000 0000000C 7FFFFF2E
      3C010000 00000009 7FFFFF24
      3C010000 00000006 7FFFFF25
      """);

  private static final String EXT_PARAM_3_NAME = "Fix Costume Duplicate Check (extension_parameters_3)";
  private static final int EXT_PARAM_3_OFFSET = 0x916C;
  private static final int EXT_PARAM_3_LENGTH = 0x18;
  private static final byte[] EXT_PARAM_3_NEW_BYTES = ByteUtils.hexTextToBytes("""
      3C000000 0000000F 7FFFFF2D
      3C000000 0000000C 7FFFFF2E
      3C000000 00000009 7FFFFF24
      3C000000 00000006 7FFFFF25
      """);

  private static final String EXT_PARAM_4_NAME = "Fix Costume Duplicate Check (extension_parameters_4)";
  private static final int EXT_PARAM_4_OFFSET = 0x548C;
  private static final int EXT_PARAM_4_LENGTH = 0x18;
  private static final byte[] EXT_PARAM_4_NEW_BYTES = ByteUtils.hexTextToBytes("""
      3C000000 0000000C 7FFFFF2D
      3C000000 0000000F 7FFFFF2E
      3C000000 00000006 7FFFFF24
      3C000000 00000009 7FFFFF25
      """);

  private static final String COMPARE_VALUES_NAME = "Fix Costume Duplicate Check (compare_values)";
  private static final int COMPARE_VALUES_OFFSET = 0x5670;
  private static final int COMPARE_VALUES_LENGTH = 0x10;
  private static final byte[] COMPARE_VALUES_NEW_BYTES = ByteUtils.hexTextToBytes("""
      3C160000 0000F530 7FFFFF2D 7FFFFF27
      3C160000 0000F530 7FFFFF2E 7FFFFF2E
      3B020000 000056C4 7FFFFF27 7FFFFF2E
      3C160000 0000F530 7FFFFF24 7FFFFF27
      3C160000 0000F530 7FFFFF25 7FFFFF25
      3C030000 00000001 7FFFFF27
      3C030000 00000001 7FFFFF25
      3B020000 000056C4 7FFFFF27 7FFFFF25
      3B010000 00005680 7FFFFF2E 00000000
      3B020000 00005680 7FFFFF25 00000000
      3C170000 0000F530 7FFFFF2D 00000000
      """);

  private static final String NORMAL_FLOW_NAME = "Fix Costume Duplicate Check (normal_flow)";
  private static final int NORMAL_FLOW_OFFSET = 0x5680;
  private static final int NORMAL_FLOW_LENGTH = 0x10;
  private static final byte[] NORMAL_FLOW_NEW_BYTES = ByteUtils.hexTextToBytes("""
      3B010000 000056B4 00000001 7FFFFF25
      3B010000 000056B4 00000003 7FFFFF25
      """);

  public static final Set<String> CODES = Set.of(EXT_PARAM_1_NAME, EXT_PARAM_2_NAME,
      EXT_PARAM_3_NAME, EXT_PARAM_4_NAME, COMPARE_VALUES_NAME, NORMAL_FLOW_NAME);

  /**
   * Enables the fix on char_sel.seq
   *
   * @param charSel The path to char_sel.seq
   * @throws IOException If an I/O error occurs.
   */
  public static void enable(Path charSel) throws IOException {
    SeqEdit edit = SeqEditBuilder.getBuilder()
        .name(EXT_PARAM_1_NAME)
        .newBytes(EXT_PARAM_1_NEW_BYTES)
        .seqPath(charSel)
        .startOffset(EXT_PARAM_1_OFFSET)
        .endOffset(EXT_PARAM_1_OFFSET + EXT_PARAM_1_LENGTH)
        .create();
    SeqExt.addEdit(edit, charSel);

    edit = SeqEditBuilder.getBuilder()
        .name(EXT_PARAM_2_NAME)
        .newBytes(EXT_PARAM_2_NEW_BYTES)
        .seqPath(charSel)
        .startOffset(EXT_PARAM_2_OFFSET)
        .endOffset(EXT_PARAM_2_OFFSET + EXT_PARAM_2_LENGTH)
        .create();
    SeqExt.addEdit(edit, charSel);

    edit = SeqEditBuilder.getBuilder()
        .name(EXT_PARAM_3_NAME)
        .newBytes(EXT_PARAM_3_NEW_BYTES)
        .seqPath(charSel)
        .startOffset(EXT_PARAM_3_OFFSET)
        .endOffset(EXT_PARAM_3_OFFSET + EXT_PARAM_3_LENGTH)
        .create();
    SeqExt.addEdit(edit, charSel);

    edit = SeqEditBuilder.getBuilder()
        .name(EXT_PARAM_4_NAME)
        .newBytes(EXT_PARAM_4_NEW_BYTES)
        .seqPath(charSel)
        .startOffset(EXT_PARAM_4_OFFSET)
        .endOffset(EXT_PARAM_4_OFFSET + EXT_PARAM_4_LENGTH)
        .create();
    SeqExt.addEdit(edit, charSel);

    edit = SeqEditBuilder.getBuilder()
        .name(COMPARE_VALUES_NAME)
        .newBytes(COMPARE_VALUES_NEW_BYTES)
        .seqPath(charSel)
        .startOffset(COMPARE_VALUES_OFFSET)
        .endOffset(COMPARE_VALUES_OFFSET + COMPARE_VALUES_LENGTH)
        .create();
    SeqExt.addEdit(edit, charSel);

    edit = SeqEditBuilder.getBuilder()
        .name(NORMAL_FLOW_NAME)
        .newBytes(NORMAL_FLOW_NEW_BYTES)
        .seqPath(charSel)
        .startOffset(NORMAL_FLOW_OFFSET)
        .endOffset(NORMAL_FLOW_OFFSET + NORMAL_FLOW_LENGTH)
        .create();
    SeqExt.addEdit(edit, charSel);
  }

  /**
   * Disables the fix on char_sel.seq
   *
   * @param charSel The path to char_sel.seq
   * @throws IOException If an I/O error occurs.
   */
  public static void disable(Path charSel) throws IOException {
    for (SeqEdit edit : SeqExt.getEdits(charSel)) {
      if (CODES.contains(edit.getName())) {
        SeqExt.removeEdit(edit, charSel);
      }
    }
  }

  /**
   * Returns whether the duplicate costume fix is fully enabled. This method will return false if it
   * is partially enabled. Use {@link #verifyIntegrity(List)} to see which parts of the fix are
   * missing.
   *
   * @param edits
   * @return
   */
  public static boolean isEnabled(List<SeqEdit> edits) {
    Set<String> editNames = edits.stream()
        .map(edit -> edit.getName())
        .collect(Collectors.toSet());
    for (String code : CODES) {
      if (!editNames.contains(code)) {
        return false;
      }
    }
    return true;
  }

  /**
   * If any part of Fix Costume Duplicate Check is enabled, return any missing codes for it to in
   * order to verify integrity of the codes.
   *
   * @param edits The edits to parse for Fix Costume Duplicate Check.
   * @return If the code is partially enabled, which parts of the code are missing.
   */
  public static Set<String> verifyIntegrity(List<SeqEdit> edits) {
    Set<String> found = new HashSet<>();
    for (SeqEdit edit : edits) {
      if (CODES.contains(edit.getName())) {
        found.add(edit.getName());
      }
    }
    if (found.isEmpty() || CODES.equals(found)) {
      return Collections.emptySet();
    }
    // Missing some of the dupe costume codes, find which ones
    return Sets.difference(CODES, found);
  }
}
