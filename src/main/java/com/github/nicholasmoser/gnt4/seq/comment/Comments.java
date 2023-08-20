package com.github.nicholasmoser.gnt4.seq.comment;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to handle comments on SEQ files.
 */
public class Comments {

  private static final Logger LOGGER = Logger.getLogger(Comments.class.getName());
  private static Map<String, Multimap<Integer, String>> COMMENTS;

  /**
   * Get a mapping of all comments for the given SEQ file. The mapping is of the SEQ file offset to
   * the comment.
   *
   * @param fileName The name of the SEQ file to get comments for.
   * @return The SEQ file offset to comment mapping.
   */
  public static Multimap<Integer, String> getComments(String fileName) {
    if (COMMENTS == null) {
      COMMENTS = getAllComments();
    }
    Multimap<Integer, String> comments = COMMENTS.get(fileName);
    if (comments == null) {
      LOGGER.log(Level.INFO, "No comments found for file " + fileName);
      return ArrayListMultimap.create();
    }
    return comments;
  }

  private static Map<String, Multimap<Integer, String>> getAllComments() {
    Map<String, Multimap<Integer, String>> allComments = new HashMap<>();

    Multimap<Integer, String> comments = getChrBaseComments();
    comments.put(0x251A0, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.ANK_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x4450, "// gpr2 is a pointer to the file names, e.g. chr/bou/0300");
    comments.put(0x4450, "// Loads the chr obj and stores it in gpr19 and cr");
    comments.put(0x46E4, "// Errors here when a model is not available for the given costume");
    comments.put(0x7AB0, "// List of filenames to load, is modified (e.g. chr/bou/0300)");
    comments.put(0x7E64, "// Push the chr_id, costume_id for updates below");
    comments.put(0x7E74, "// Update filename paths to load");
    comments.put(0x7E74, "// Update \"chr/loc/0000\"");
    comments.put(0x7E94, "// Update \"chr/loc/0010\"");
    comments.put(0x7EB4, "// Update \"chr/loc/0020\"");
    comments.put(0x7ED4, "// Update \"mot/loc/0000\"");
    comments.put(0x7EF8, "// Update \"mot/loc/0000\"");
    comments.put(0x7F1C, "// Update \"mot/cmn/0000\"");
    comments.put(0x27BA4, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.BOU_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x268D0, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.CHO_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x1C114, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.DOG_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x24270, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.GAI_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x280CC, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.GAR_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x1334, "// Comparison if mask or maybe needles is hidden or not");
    comments.put(0x4494, "// Bitmask for hiding needles in hand, but showing the mask");
    comments.put(0x24768, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.HAK_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x14A80, "// Code for normal kunais (Woke Hinata throws multiple kunai)");
    comments.put(0x14AF0, "// Code for the fast uncharged kunai (Woke Hinata throws multiple kunai)");
    comments.put(0x14B60, "// Code for the fast charged kunai (Woke Hinata throws multiple kunai)");
    comments.put(0x14BD0, "// Code for the fastest charged kunai (Woke Hinata throws multiple kunai)");
    comments.put(0x23F8C, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.HI2_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x25674, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.HIN_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x26AA4, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.INO_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x9B88, "// Set chr_tbl address to *seq_p_sp->field_0x38 + offset 0x230");
    comments.put(0x9E7C, "// Set chr_tbl address to *seq_p_sp->field_0x38 + offset 0x230");
    comments.put(0x22D0C, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.IRU_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x28F78, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    comments.put(0x9C6C, "// Set starting string offset");
    comments.put(0x9F60, "// Set starting string offset");
    allComments.put(Seqs.ITA_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x26270, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.JIR_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x24CDC, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.KAB_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x31FC4, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.KAK_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x24DF0, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.KAN_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x4554, "// Errors here when a model is not available for the given costume");
    comments.put(0x1FB50, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.KAR_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x28920, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.KIB_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x14E40, "// Uncharged 5A projectile");
    comments.put(0x14EF8, "// Two charges 5A projectile");
    comments.put(0x14FC0, "// Unused web shot projectile that goes diagonal up forward");
    comments.put(0x15080, "// JA projectile");
    comments.put(0x15390, "// 6A projectile");
    comments.put(0x15450, "// 8A projectile");
    comments.put(0x15510, "// Unused projectile similar to 6A/8A that aims diagonally down-forward");
    comments.put(0x15758, "// Happy path for both capture state and non-capture state hit");
    comments.put(0x26570, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.KID_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x26CC0, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.KIM_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x24E08, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.KIS_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x27814, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.LOC_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x22EAC, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.MIZ_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x24420, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.NA9_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x2F3C4, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.NAR_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x25788, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.NEJ_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x1A0D8, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.OBO_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x26B84, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.ORO_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x25F74, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.SA2_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x26CA8, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.SAK_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x24E84, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.SAR_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x65E8,
        "0xD is converted to the String \"13\" to load either 1300.txg or 1301.txg");
    comments.put(0x6600, "Load eye texture in gpr12");
    comments.put(0x83F8,
        "0xD is converted to the String \"13\" to load either 1300.txg or 1301.txg");
    comments.put(0x8410, "Load alternate eye texture in gpr12");
    comments.put(0x291A4, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.SAS_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x26554, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.SIK_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x25350, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.SIN_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x25ADC, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.SKO_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x19214, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.TA2_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x27EDC, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.TAY_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x29C04, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.TEM_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x2B1CC, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.TEN_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x25674, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.TSU_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x141C, "// Remove screen darken if interrupted mid-super");
    comments.put(0x23048, "// String info, 42E00000 is the range of throws used at 0x8006007c in the code");
    allComments.put(Seqs.ZAB_0000, comments);

    Multimap<Integer, String> charSelComments = ArrayListMultimap.create();
    charSelComments.put(0xCE4, "// Random stage value is set here");
    charSelComments.put(0x2370, "// Stage IDs");
    charSelComments.put(0x23F0, "// Stage Display IDs");
    charSelComments.put(0x3A14, "// Costume ID is read here, 7FFFFF3D is the costume_id");
    charSelComments.put(0x5680, "// If opponent costume is 1 or 3, goto offset 0x56B4");
    charSelComments.put(0x572C, "// Models specific for costumes 3 and 4 here (Sakura, Ino only)");
    charSelComments.put(0x690C, "// Set the costume_id to 7FFFFF3D");

    charSelComments.put(0xEC68, "// Select Costume 1, Player 1");
    charSelComments.put(0xEC98, "// Select Costume 2, Player 1");
    charSelComments.put(0xECC8, "// Check Costume 3, Player 1 (Haku/Sakura/Ino only)");
    charSelComments.put(0xED0C, "// Check Costume 4, Player 1 (Haku/Sakura/Ino only)");
    charSelComments.put(0xED50, "// Select Costume 3, Player 1");
    charSelComments.put(0xED80, "// Select Costume 4, Player 1");

    charSelComments.put(0xEDB0, "// Select Costume 1, Player 2");
    charSelComments.put(0xEDE0, "// Select Costume 2, Player 2");
    charSelComments.put(0xEE10, "// Check Costume 3, Player 2 (Haku/Sakura/Ino only)");
    charSelComments.put(0xEE54, "// Check Costume 4, Player 2 (Haku/Sakura/Ino only)");
    charSelComments.put(0xEE98, "// Select Costume 3, Player 2");
    charSelComments.put(0xEEC8, "// Select Costume 4, Player 2");
    allComments.put(Seqs.CHARSEL, charSelComments);

    Multimap<Integer, String> charSel4Comments = ArrayListMultimap.create();
    charSel4Comments.put(0x4AF0, "// Models specific for costumes 3 and 4 here (Sakura, Ino only)");
    charSel4Comments.put(0x79A0, "// Select Costume 1, Player 1");
    charSel4Comments.put(0x79C8, "// Select Costume 2, Player 1");
    charSel4Comments.put(0x79F0, "// Check Costume 3, Player 1 (Haku/Sakura/Ino only)");
    charSel4Comments.put(0x7A38, "// Select Costume 3, Player 1");
    charSel4Comments.put(0x7A60, "// Check Costume 4, Player 1 (Haku/Sakura/Ino only)");
    charSel4Comments.put(0x7AA8, "// Select Costume 4, Player 1");

    charSel4Comments.put(0x7AD0, "// Select Costume 1, Player 2");
    charSel4Comments.put(0x7AF8, "// Select Costume 2, Player 2");
    charSel4Comments.put(0x7B20, "// Check Costume 3, Player 2 (Haku/Sakura/Ino only)");
    charSel4Comments.put(0x7B68, "// Select Costume 3, Player 2");
    charSel4Comments.put(0x7B90, "// Check Costume 4, Player 2 (Haku/Sakura/Ino only)");
    charSel4Comments.put(0x7BD8, "// Select Costume 4, Player 2");

    charSel4Comments.put(0x7C00, "// Select Costume 1, Player 3");
    charSel4Comments.put(0x7C28, "// Select Costume 2, Player 3");
    charSel4Comments.put(0x7C50, "// Check Costume 3, Player 3 (Haku/Sakura/Ino only)");
    charSel4Comments.put(0x7C98, "// Select Costume 3, Player 3");
    charSel4Comments.put(0x7CC0, "// Check Costume 4, Player 3 (Haku/Sakura/Ino only)");
    charSel4Comments.put(0x7D08, "// Select Costume 4, Player 3");

    charSel4Comments.put(0x7D30, "// Select Costume 1, Player 4");
    charSel4Comments.put(0x7D58, "// Select Costume 2, Player 4");
    charSel4Comments.put(0x7D80, "// Check Costume 3, Player 4 (Haku/Sakura/Ino only)");
    charSel4Comments.put(0x7E38, "// Select Costume 4, Player 4");
    charSel4Comments.put(0x7DF0, "// Check Costume 4, Player 4 (Haku/Sakura/Ino only)");
    charSel4Comments.put(0x7DC8, "// Select Costume 3, Player 4");
    allComments.put(Seqs.CHARSEL_4, charSel4Comments);

    Multimap<Integer, String> game00Comments = ArrayListMultimap.create();
    game00Comments.put(0x4850, "// Init battle UI graphics, e.g. health and chakra bars");
    game00Comments.put(0x4980, "// Hide health bars for animation");
    game00Comments.put(0x49B0, "// Flip graphics for P2 side");
    game00Comments.put(0x49EC, "// Align graphics around health bars");
    game00Comments.put(0x4A58, "// Move graphics to correct positions");
    game00Comments.put(0x4BAC, "// Move health bars for first part of animation (0x19 frames)");
    game00Comments.put(0x4C14, "// Move health bars for second part of animation (0xA frames)");
    game00Comments.put(0x4CB4, "// Fully show health bars at end of animation");
    allComments.put(Seqs.GAME_00, game00Comments);

    Multimap<Integer, String> titleComments = ArrayListMultimap.create();
    game00Comments.put(0x717C, "// Branch based on month background theme");
    allComments.put(Seqs.M_TITLE, titleComments);

    // m_vs.seq 0x3838 Reads the character costume ID
    // m_vs.seq 0x3840 Does something with the character costume ID

    return allComments;
  }

  /**
   * @return Offset to comment mappings all characters have in common.
   */
  private static Multimap<Integer, String> getChrBaseComments() {
    Multimap<Integer, String> baseComments = ArrayListMultimap.create();
    baseComments.put(0xA6C, "// Update last_act_id to act_id");
    baseComments.put(0xAA8, "// Action Offset Lookup Branch #1");
    baseComments.put(0xB08, "// Action Offset Lookup Branch #2 (Happy path)");
    baseComments.put(0xB20, "// Convert action id to table offset");
    baseComments.put(0xB30, "// Loads action offset from chr_tbl");
    return baseComments;
  }
}
