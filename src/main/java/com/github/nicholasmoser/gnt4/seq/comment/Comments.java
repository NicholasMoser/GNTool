package com.github.nicholasmoser.gnt4.seq.comment;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to handle comments on SEQ files.
 */
public class Comments {

  // TODO: Add notes
  // 0xB30 For Iruka, this is where the Action ID offset is read from the Action ID table

  private static final Logger LOGGER = Logger.getLogger(Comments.class.getName());
  private static Map<String, Multimap<Integer, String>> COMMENTS;

  /**
   * Get a mapping of all comments for the given SEQ file. The mapping is of the SEQ file offset
   * to the comment.
   *
   * @param fileName The name of the SEQ file to get comments for.
   * @return The SEQ file offset to comment mapping.
   */
  public static Multimap<Integer, String> getComments(String fileName) {
    if (COMMENTS == null) {
      COMMENTS = getAllComments();
    }
    Multimap<Integer, String> comments =  COMMENTS.get(fileName);
    if (comments == null) {
      LOGGER.log(Level.INFO, "No comments found for file " + fileName);
      return ArrayListMultimap.create();
    }
    return comments;
  }

  private static Map<String, Multimap<Integer, String>> getAllComments() {
    Map<String, Multimap<Integer, String>> allComments = new HashMap<>();

    Multimap<Integer, String> ank0000Comments = ArrayListMultimap.create();
    ank0000Comments.put(0x5D50, "IncreaseChrModelSize:");
    ank0000Comments.put(0x5D50, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.ANK_0000, ank0000Comments);

    Multimap<Integer, String> bou0000Comments = ArrayListMultimap.create();
    bou0000Comments.put(0x5E60, "IncreaseChrModelSize:");
    bou0000Comments.put(0x5E60, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.BOU_0000, bou0000Comments);

    Multimap<Integer, String> cho0000Comments = ArrayListMultimap.create();
    cho0000Comments.put(0x5EF0, "IncreaseChrModelSize:");
    cho0000Comments.put(0x5EF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.CHO_0000, cho0000Comments);

    Multimap<Integer, String> dog0000Comments = ArrayListMultimap.create();
    dog0000Comments.put(0x5D30, "IncreaseChrModelSize:");
    dog0000Comments.put(0x5D30, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.DOG_0000, dog0000Comments);

    Multimap<Integer, String> gai0000Comments = ArrayListMultimap.create();
    gai0000Comments.put(0x5C90, "IncreaseChrModelSize:");
    gai0000Comments.put(0x5C90, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.GAI_0000, gai0000Comments);

    Multimap<Integer, String> gar0000Comments = ArrayListMultimap.create();
    gar0000Comments.put(0x5CE0, "IncreaseChrModelSize:");
    gar0000Comments.put(0x5CE0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.GAR_0000, gar0000Comments);

    Multimap<Integer, String> hak0000Comments = ArrayListMultimap.create();
    hak0000Comments.put(0x61A0, "IncreaseChrModelSize:");
    hak0000Comments.put(0x61A0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.HAK_0000, hak0000Comments);

    Multimap<Integer, String> hi20000Comments = ArrayListMultimap.create();
    hi20000Comments.put(0x5E40, "IncreaseChrModelSize:");
    hi20000Comments.put(0x5E40, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.HI2_0000, hi20000Comments);

    Multimap<Integer, String> hin0000Comments = ArrayListMultimap.create();
    hin0000Comments.put(0x5E10, "IncreaseChrModelSize:");
    hin0000Comments.put(0x5E10, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.HIN_0000, hin0000Comments);

    Multimap<Integer, String> ino0000Comments = ArrayListMultimap.create();
    ino0000Comments.put(0x5CF0, "IncreaseChrModelSize:");
    ino0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.INO_0000, ino0000Comments);

    Multimap<Integer, String> iru0000Comments = ArrayListMultimap.create();
    iru0000Comments.put(0x5CF0, "IncreaseChrModelSize:");
    iru0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");

    iru0000Comments.put(0xA1C, "// Many actions end by branch and linking to here");
    iru0000Comments.put(0xAA8, "// Action Offset Lookup Branch #1");

    iru0000Comments.put(0xB08, "// Action Offset Lookup Branch #2 (Happy path)");
    iru0000Comments.put(0xB18, "// Load current action id into gpr2");
    iru0000Comments.put(0xB20, "// Convert action id to table offset");
    iru0000Comments.put(0xB28, "// Load memory address of chr_tbl into gpr19");
    iru0000Comments.put(0xB30, "// Loads action offset from chr_tbl");

    iru0000Comments.put(0x9B88, "// Set chr_tbl address to *seq_p_sp->field_0x260 + offset 0x230");
    iru0000Comments.put(0x9E7C, "// Set chr_tbl address to *seq_p_sp->field_0x260 + offset 0x230");
    allComments.put(Seqs.IRU_0000, iru0000Comments);

    Multimap<Integer, String> jir0000Comments = ArrayListMultimap.create();
    jir0000Comments.put(0x5CC0, "IncreaseChrModelSize:");
    jir0000Comments.put(0x5CC0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.JIR_0000, jir0000Comments);

    Multimap<Integer, String> kab0000Comments = ArrayListMultimap.create();
    kab0000Comments.put(0x5CF0, "IncreaseChrModelSize:");
    kab0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KAB_0000, kab0000Comments);

    Multimap<Integer, String> kak0000Comments = ArrayListMultimap.create();
    kak0000Comments.put(0x5D80, "IncreaseChrModelSize:");
    kak0000Comments.put(0x5D80, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KAK_0000, kak0000Comments);

    Multimap<Integer, String> kan0000Comments = ArrayListMultimap.create();
    kan0000Comments.put(0x5D30, "IncreaseChrModelSize:");
    kan0000Comments.put(0x5D30, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KAN_0000, kan0000Comments);

    Multimap<Integer, String> kar0000Comments = ArrayListMultimap.create();
    kar0000Comments.put(0x5CD0, "IncreaseChrModelSize:");
    kar0000Comments.put(0x5CD0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KAR_0000, kar0000Comments);

    Multimap<Integer, String> kib0000Comments = ArrayListMultimap.create();
    kib0000Comments.put(0x5C90, "IncreaseChrModelSize:");
    kib0000Comments.put(0x5C90, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KIB_0000, kib0000Comments);

    Multimap<Integer, String> kid0000Comments = ArrayListMultimap.create();
    kid0000Comments.put(0x5D60, "IncreaseChrModelSize:");
    kid0000Comments.put(0x5D60, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KID_0000, kid0000Comments);

    Multimap<Integer, String> kim0000Comments = ArrayListMultimap.create();
    kim0000Comments.put(0x5DA0, "IncreaseChrModelSize:");
    kim0000Comments.put(0x5DA0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KIM_0000, kim0000Comments);

    Multimap<Integer, String> kis0000Comments = ArrayListMultimap.create();
    kis0000Comments.put(0x5D30, "IncreaseChrModelSize:");
    kis0000Comments.put(0x5D30, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KIS_0000, kis0000Comments);

    Multimap<Integer, String> loc0000Comments = ArrayListMultimap.create();
    loc0000Comments.put(0x5CF0, "IncreaseChrModelSize:");
    loc0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.LOC_0000, loc0000Comments);

    Multimap<Integer, String> miz0000Comments = ArrayListMultimap.create();
    miz0000Comments.put(0x5CF0, "IncreaseChrModelSize:");
    miz0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.MIZ_0000, miz0000Comments);

    Multimap<Integer, String> na90000Comments = ArrayListMultimap.create();
    na90000Comments.put(0x5D00, "IncreaseChrModelSize:");
    na90000Comments.put(0x5D00, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.NA9_0000, na90000Comments);

    Multimap<Integer, String> nar0000Comments = ArrayListMultimap.create();
    nar0000Comments.put(0x5EE0, "IncreaseChrModelSize:");
    nar0000Comments.put(0x5EE0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.NAR_0000, nar0000Comments);

    Multimap<Integer, String> nej0000Comments = ArrayListMultimap.create();
    nej0000Comments.put(0x5D30, "IncreaseChrModelSize:");
    nej0000Comments.put(0x5D30, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.NEJ_0000, nej0000Comments);

    Multimap<Integer, String> obo0000Comments = ArrayListMultimap.create();
    obo0000Comments.put(0x5CF0, "IncreaseChrModelSize:");
    obo0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.OBO_0000, obo0000Comments);

    Multimap<Integer, String> oro0000Comments = ArrayListMultimap.create();
    oro0000Comments.put(0x5D40, "IncreaseChrModelSize:");
    oro0000Comments.put(0x5D40, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.ORO_0000, oro0000Comments);

    Multimap<Integer, String> sa20000Comments = ArrayListMultimap.create();
    sa20000Comments.put(0x5DA0, "IncreaseChrModelSize:");
    sa20000Comments.put(0x5DA0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SA2_0000, sa20000Comments);

    Multimap<Integer, String> sak0000Comments = ArrayListMultimap.create();
    sak0000Comments.put(0x5CF0, "IncreaseChrModelSize:");
    sak0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SAK_0000, sak0000Comments);

    Multimap<Integer, String> sar0000Comments = ArrayListMultimap.create();
    sar0000Comments.put(0x5D40, "IncreaseChrModelSize:");
    sar0000Comments.put(0x5D40, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SAR_0000, sar0000Comments);

    Multimap<Integer, String> sas0000Comments = ArrayListMultimap.create();
    sas0000Comments.put(0x5D60, "IncreaseChrModelSize:");
    sas0000Comments.put(0x5D60, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SAS_0000, sas0000Comments);

    Multimap<Integer, String> sik0000Comments = ArrayListMultimap.create();
    sik0000Comments.put(0x5CF0, "IncreaseChrModelSize:");
    sik0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SIK_0000, sik0000Comments);

    Multimap<Integer, String> sin0000Comments = ArrayListMultimap.create();
    sin0000Comments.put(0x5CC0, "IncreaseChrModelSize:");
    sin0000Comments.put(0x5CC0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SIN_0000, sin0000Comments);

    Multimap<Integer, String> sko0000Comments = ArrayListMultimap.create();
    sko0000Comments.put(0x5DB0, "IncreaseChrModelSize:");
    sko0000Comments.put(0x5DB0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SKO_0000, sko0000Comments);

    Multimap<Integer, String> ta20000Comments = ArrayListMultimap.create();
    ta20000Comments.put(0x5C90, "IncreaseChrModelSize:");
    ta20000Comments.put(0x5C90, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.TA2_0000, ta20000Comments);

    Multimap<Integer, String> tay0000Comments = ArrayListMultimap.create();
    tay0000Comments.put(0x5E00, "IncreaseChrModelSize:");
    tay0000Comments.put(0x5E00, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.TAY_0000, tay0000Comments);

    Multimap<Integer, String> tem0000Comments = ArrayListMultimap.create();
    tem0000Comments.put(0x5D80, "IncreaseChrModelSize:");
    tem0000Comments.put(0x5D80, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.TEM_0000, tem0000Comments);

    Multimap<Integer, String> ten0000Comments = ArrayListMultimap.create();
    ten0000Comments.put(0x5D30, "IncreaseChrModelSize:");
    ten0000Comments.put(0x5D30, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.TEN_0000, ten0000Comments);

    Multimap<Integer, String> tsu0000Comments = ArrayListMultimap.create();
    tsu0000Comments.put(0x5CA0, "IncreaseChrModelSize:");
    tsu0000Comments.put(0x5CA0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.TSU_0000, tsu0000Comments);

    Multimap<Integer, String> zab0000Comments = ArrayListMultimap.create();
    zab0000Comments.put(0x5D40, "IncreaseChrModelSize:");
    zab0000Comments.put(0x5D40, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.ZAB_0000, zab0000Comments);

    return allComments;
  }
}
