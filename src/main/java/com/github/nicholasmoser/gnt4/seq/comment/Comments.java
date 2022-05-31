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
    allComments.put(Seqs.ANK_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.BOU_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.CHO_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.DOG_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.GAI_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.GAR_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.HAK_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.HI2_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.HIN_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.INO_0000, comments);

    comments = getChrBaseComments();
    comments.put(0x9B88, "// Set chr_tbl address to *seq_p_sp->field_0x38 + offset 0x230");
    comments.put(0x9E7C, "// Set chr_tbl address to *seq_p_sp->field_0x38 + offset 0x230");
    allComments.put(Seqs.IRU_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.ITA_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.JIR_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.KAB_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.KAK_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.KAN_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.KAR_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.KIB_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.KID_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.KIM_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.KIS_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.LOC_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.MIZ_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.NA9_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.NAR_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.NEJ_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.OBO_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.ORO_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.SA2_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.SAK_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.SAR_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.SAS_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.SIK_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.SIN_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.SKO_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.TA2_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.TAY_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.TEM_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.TEN_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.TSU_0000, comments);

    comments = getChrBaseComments();
    allComments.put(Seqs.ZAB_0000, comments);

    Multimap<Integer, String> charSelComments = ArrayListMultimap.create();
    charSelComments.put(0xCE4, "// Random stage value is set here");
    allComments.put(Seqs.CHARSEL, charSelComments);

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
