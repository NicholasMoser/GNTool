package com.github.nicholasmoser.gnt4.seq.comment;

import com.github.nicholasmoser.gnt4.seq.Seqs;
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
  private static Map<String, Map<Integer, String>> COMMENTS;

  /**
   * Get a mapping of all comments for the given SEQ file. The mapping is of the SEQ file offset
   * to the comment.
   *
   * @param fileName The name of the SEQ file to get comments for.
   * @return The SEQ file offset to comment mapping.
   */
  public static Map<Integer, String> getComments(String fileName) {
    if (COMMENTS == null) {
      COMMENTS = getAllComments();
    }
    Map<Integer, String> comments =  COMMENTS.get(fileName);
    if (comments == null) {
      LOGGER.log(Level.INFO, "No comments found for file " + fileName);
      return Collections.emptyMap();
    }
    return comments;
  }

  private static Map<String, Map<Integer, String>> getAllComments() {
    Map<String, Map<Integer, String>> allComments = new HashMap<>();

    Map<Integer, String> ank0000Comments = new HashMap<>();
    ank0000Comments.put(0x5D50, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.ANK_0000, ank0000Comments);

    Map<Integer, String> bou0000Comments = new HashMap<>();
    bou0000Comments.put(0x5E60, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.BOU_0000, bou0000Comments);

    Map<Integer, String> cho0000Comments = new HashMap<>();
    cho0000Comments.put(0x5EF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.CHO_0000, cho0000Comments);

    Map<Integer, String> dog0000Comments = new HashMap<>();
    dog0000Comments.put(0x5D30, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.DOG_0000, dog0000Comments);

    Map<Integer, String> gai0000Comments = new HashMap<>();
    gai0000Comments.put(0x5C90, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.GAI_0000, gai0000Comments);

    Map<Integer, String> gar0000Comments = new HashMap<>();
    gar0000Comments.put(0x5CE0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.GAR_0000, gar0000Comments);

    Map<Integer, String> hak0000Comments = new HashMap<>();
    hak0000Comments.put(0x61A0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.HAK_0000, hak0000Comments);

    Map<Integer, String> hi20000Comments = new HashMap<>();
    hi20000Comments.put(0x5E40, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.HI2_0000, hi20000Comments);

    Map<Integer, String> hin0000Comments = new HashMap<>();
    hin0000Comments.put(0x5E10, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.HIN_0000, hin0000Comments);

    Map<Integer, String> ino0000Comments = new HashMap<>();
    ino0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.INO_0000, ino0000Comments);

    Map<Integer, String> iru0000Comments = new HashMap<>();
    iru0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.IRU_0000, iru0000Comments);

    Map<Integer, String> jir0000Comments = new HashMap<>();
    jir0000Comments.put(0x5CC0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.JIR_0000, jir0000Comments);

    Map<Integer, String> kab0000Comments = new HashMap<>();
    kab0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KAB_0000, kab0000Comments);

    Map<Integer, String> kak0000Comments = new HashMap<>();
    kak0000Comments.put(0x5D80, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KAK_0000, kak0000Comments);

    Map<Integer, String> kan0000Comments = new HashMap<>();
    kan0000Comments.put(0x5D30, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KAN_0000, kan0000Comments);

    Map<Integer, String> kar0000Comments = new HashMap<>();
    kar0000Comments.put(0x5CD0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KAR_0000, kar0000Comments);

    Map<Integer, String> kib0000Comments = new HashMap<>();
    kib0000Comments.put(0x5C90, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KIB_0000, kib0000Comments);

    Map<Integer, String> kid0000Comments = new HashMap<>();
    kid0000Comments.put(0x5D60, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KID_0000, kid0000Comments);

    Map<Integer, String> kim0000Comments = new HashMap<>();
    kim0000Comments.put(0x5DA0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KIM_0000, kim0000Comments);

    Map<Integer, String> kis0000Comments = new HashMap<>();
    kis0000Comments.put(0x5D30, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.KIS_0000, kis0000Comments);

    Map<Integer, String> loc0000Comments = new HashMap<>();
    loc0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.LOC_0000, loc0000Comments);

    Map<Integer, String> miz0000Comments = new HashMap<>();
    miz0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.MIZ_0000, miz0000Comments);

    Map<Integer, String> na90000Comments = new HashMap<>();
    na90000Comments.put(0x5D00, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.NA9_0000, na90000Comments);

    Map<Integer, String> nar0000Comments = new HashMap<>();
    nar0000Comments.put(0x5EE0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.NAR_0000, nar0000Comments);

    Map<Integer, String> nej0000Comments = new HashMap<>();
    nej0000Comments.put(0x5D30, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.NEJ_0000, nej0000Comments);

    Map<Integer, String> obo0000Comments = new HashMap<>();
    obo0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.OBO_0000, obo0000Comments);

    Map<Integer, String> oro0000Comments = new HashMap<>();
    oro0000Comments.put(0x5D40, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.ORO_0000, oro0000Comments);

    Map<Integer, String> sa20000Comments = new HashMap<>();
    sa20000Comments.put(0x5DA0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SA2_0000, sa20000Comments);

    Map<Integer, String> sak0000Comments = new HashMap<>();
    sak0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SAK_0000, sak0000Comments);

    Map<Integer, String> sar0000Comments = new HashMap<>();
    sar0000Comments.put(0x5D40, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SAR_0000, sar0000Comments);

    Map<Integer, String> sas0000Comments = new HashMap<>();
    sas0000Comments.put(0x5D60, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SAS_0000, sas0000Comments);

    Map<Integer, String> sik0000Comments = new HashMap<>();
    sik0000Comments.put(0x5CF0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SIK_0000, sik0000Comments);

    Map<Integer, String> sin0000Comments = new HashMap<>();
    sin0000Comments.put(0x5CC0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SIN_0000, sin0000Comments);

    Map<Integer, String> sko0000Comments = new HashMap<>();
    sko0000Comments.put(0x5DB0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.SKO_0000, sko0000Comments);

    Map<Integer, String> ta20000Comments = new HashMap<>();
    ta20000Comments.put(0x5C90, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.TA2_0000, ta20000Comments);

    Map<Integer, String> tay0000Comments = new HashMap<>();
    tay0000Comments.put(0x5E00, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.TAY_0000, tay0000Comments);

    Map<Integer, String> tem0000Comments = new HashMap<>();
    tem0000Comments.put(0x5D80, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.TEM_0000, tem0000Comments);

    Map<Integer, String> ten0000Comments = new HashMap<>();
    ten0000Comments.put(0x5D30, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.TEN_0000, ten0000Comments);

    Map<Integer, String> tsu0000Comments = new HashMap<>();
    tsu0000Comments.put(0x5CA0, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.TSU_0000, tsu0000Comments);

    Map<Integer, String> zab0000Comments = new HashMap<>();
    zab0000Comments.put(0x5D40, "// Scale chr model larger by 1.1x");
    allComments.put(Seqs.ZAB_0000, zab0000Comments);

    return allComments;
  }
}
