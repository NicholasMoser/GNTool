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

    Multimap<Integer, String> comments = ArrayListMultimap.create();
    comments.put(0xA1C, "// Many actions end by branch and linking to here");
    comments.put(0xAA8, "// Action Offset Lookup Branch #1");
    comments.put(0xB08, "// Action Offset Lookup Branch #2 (Happy path)");
    comments.put(0xB20, "// Convert action id to table offset");
    comments.put(0xB30, "// Loads action offset from chr_tbl");
    comments.put(0x9B88, "// Set chr_tbl address to *seq_p_sp->field_0x38 + offset 0x230");
    comments.put(0x9E7C, "// Set chr_tbl address to *seq_p_sp->field_0x38 + offset 0x230");
    allComments.put(Seqs.IRU_0000, comments);

    Multimap<Integer, String> charSelComments = ArrayListMultimap.create();
    charSelComments.put(0xCE4, "// Random stage value is set here");
    allComments.put(Seqs.CHARSEL, charSelComments);

    return allComments;
  }
}
