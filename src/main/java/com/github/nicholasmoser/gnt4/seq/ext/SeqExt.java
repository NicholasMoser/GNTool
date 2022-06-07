package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.google.common.primitives.Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeqExt {

  // seq_ext\n
  public static final byte[] SEQ_EXT = {0x73, 0x65, 0x71, 0x5F, 0x65, 0x78, 0x74, 0x0A};

  // seq_end\n
  public static final byte[] SEQ_END = {0x73, 0x65, 0x71, 0x5F, 0x65, 0x6E, 0x64, 0x0A};

  /**
   * Get the list of seq edits from a seq file.
   *
   * @param seqPath The path to the seq file.
   * @return The list of seq edits.
   * @throws IOException
   */
  public static List<SeqEdit> getEdits(Path seqPath) throws IOException {
    return getEdits(Files.readAllBytes(seqPath));
  }

  /**
   * Get the list of seq edits from seq file bytes.
   *
   * @param seqBytes The seq file bytes.
   * @return The list of seq edits.
   * @throws IOException
   */
  public static List<SeqEdit> getEdits(byte[] seqBytes) throws IOException {
    if (!hasSeqExtEnd(seqBytes)) {
      return new ArrayList<>();
    }
    int seqExtOffset = getSeqExtOffset(seqBytes);
    int seqLength = seqBytes.length;
    int position;
    // Add and subtract 8 to remove seq_ext\n and seq_end\n
    byte[] seqExtBytes = Arrays.copyOfRange(seqBytes, seqExtOffset + 8, seqLength - 8);
    List<SeqEdit> seqEdits = new ArrayList<>();
    try (ByteStream bs = new ByteStream(seqExtBytes)) {
      while (bs.bytesAreLeft()) {
        String name = new String(SeqHelper.readString(bs), StandardCharsets.UTF_8);
        name = name.replace("\0", "");
        int offset = bs.readWord();
        byte[] oldBytes = readOpcodeBytes(bs);
        position = seqExtOffset + bs.offset() + 8;
        byte[] newBytes = readOpcodeBytes(bs);
        byte[] newBytesWithoutBranchBack = Arrays.copyOfRange(newBytes, 0, newBytes.length - 8);
        seqEdits.add(new SeqEdit(name, offset, oldBytes, newBytesWithoutBranchBack).setPosition(position));
      }
    }
    return seqEdits;
  }

  /**
   * Add a seq edit to the given seq file.
   *
   * @param edit    The edit to add.
   * @param seqPath The seq file path.
   * @throws IOException
   */
  public static void addEdit(SeqEdit edit, Path seqPath) throws IOException {
    byte[] bytes = Files.readAllBytes(seqPath);
    Files.write(seqPath, addEdit(edit, bytes));
  }

  /**
   * Add a seq edit to the given seq file bytes.
   *
   * @param edit     The edit to add.
   * @param seqBytes The seq file bytes.
   * @return The new seq bytes with the seq edit added.
   * @throws IOException
   */
  public static byte[] addEdit(SeqEdit edit, byte[] seqBytes) throws IOException {
    List<SeqEdit> edits = getEdits(seqBytes);
    byte[] originalSeqBytes = getOriginalBytesWithoutEdits(seqBytes, edits);
    edits.add(edit);
    return applyEdits(originalSeqBytes, edits);
  }

  /**
   * Remove a seq edit from the given seq file.
   *
   * @param edit    The edit to remove.
   * @param seqPath The seq file path.
   * @throws IOException
   */
  public static void removeEdit(SeqEdit edit, Path seqPath) throws IOException {
    byte[] bytes = Files.readAllBytes(seqPath);
    Files.write(seqPath, removeEdit(edit, bytes));
  }

  /**
   * Remove a seq edit from the given seq file bytes.
   *
   * @param edit     The edit to remove.
   * @param seqBytes The seq file bytes.
   * @return The new seq bytes with the seq edit added.
   * @throws IOException
   */
  public static byte[] removeEdit(SeqEdit edit, byte[] seqBytes) throws IOException {
    List<SeqEdit> edits = getEdits(seqBytes);
    byte[] originalSeqBytes = getOriginalBytesWithoutEdits(seqBytes, edits);
    edits.remove(edit);
    return applyEdits(originalSeqBytes, edits);
  }

  /**
   * Remove the given list of edits from the seq bytes and return the original seq bytes. If you
   * pass in every current seq edit, it will return the original seq bytes before any edits.
   *
   * @param seqBytes The current seq bytes.
   * @param edits    The list of edits to remove.
   * @return The seq bytes without the given edits.
   */
  private static byte[] getOriginalBytesWithoutEdits(byte[] seqBytes, List<SeqEdit> edits) {
    byte[] bytesBeforeExt = getBytesBeforeExt(seqBytes);
    for (SeqEdit edit : edits) {
      int offset = edit.getOffset();
      byte[] originalBytes = edit.getOldBytes();
      System.arraycopy(originalBytes, 0, bytesBeforeExt, offset, originalBytes.length);
    }
    return bytesBeforeExt;
  }

  /**
   * Apply edits to the given seq bytes. The seq bytes given should not already have an seq ext
   * section in it or it will throw an IllegalArgumentException.
   *
   * @param seqBytes
   * @param edits
   * @return
   */
  private static byte[] applyEdits(byte[] seqBytes, List<SeqEdit> edits) throws IOException {
    if (Bytes.indexOf(seqBytes, SEQ_EXT) != -1) {
      throw new IllegalArgumentException("Given seq bytes already has a seq extension.");
    } else if (edits.isEmpty()) {
      return seqBytes; // no edits, return original seq bytes
    }
    ByteArrayOutputStream seqExtSection = new ByteArrayOutputStream();
    seqExtSection.write(SEQ_EXT);
    for (SeqEdit edit : edits) {
      int offset = edit.getOffset();
      int hijackLength = edit.getOldBytes().length;
      // Offset of the full seq bytes + the current offset in the seq ext + the new bytes offset
      int newBytesOffset = seqBytes.length + seqExtSection.size() + edit.getNewBytesOffset();
      byte[] branchBytes = getBranchBytes(newBytesOffset, hijackLength);
      System.arraycopy(branchBytes, 0, seqBytes, offset, branchBytes.length);
      // seqExtSection.write(edit.getFullBytes());
      edit.setPosition(newBytesOffset);
      seqExtSection.write(edit.getFullBytes());
    }
    seqExtSection.write(SEQ_END);
    return Bytes.concat(seqBytes, seqExtSection.toByteArray());
  }

  private static byte[] getBranchBytes(int offset, int hijackLength) throws IOException {
    if (hijackLength < 4) {
      throw new IllegalStateException("Hijack length cannot be less than 4");
    }
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(new byte[]{0x01, 0x32, 0x00, 0x00}); // branch
    baos.write(ByteUtils.fromInt32(offset)); // offset
    byte[] nullBytes = new byte[hijackLength - 0x8];
    Arrays.fill(nullBytes, (byte) 0xCC);
    baos.write(nullBytes);
    return baos.toByteArray();
  }

  /**
   * Returns if this seq ends with seq_end\n and therefore has an added seq_ext section added to
   * it.
   *
   * @param seqBytes The seq bytes to read from.
   * @return If the seq bytes end with seq_end\n
   * @throws IOException If an I/O error occurs.
   */
  private static boolean hasSeqExtEnd(byte[] seqBytes) throws IOException {
    if (seqBytes.length < 8) {
      return false;
    }
    try (ByteStream bs = new ByteStream(seqBytes)) {
      bs.seek(bs.length() - 8);
      return Arrays.equals(SEQ_END, bs.readBytes(8));
    }
  }

  /**
   * Returns the start of the seq_ext\n header in the seq bytes. Reads from the end of the bytes
   * backwards to the start to find it.
   *
   * @param seqBytes The seq bytes to read from.
   * @return The start of the seq_ext\n header.
   * @throws IOException If an I/O error occurs.
   */
  private static int getSeqExtOffset(byte[] seqBytes) throws IOException {
    try (ByteStream bs = new ByteStream(seqBytes)) {
      // Start at the beginning of seq_end\n
      bs.seek(bs.length() - 8);
      int offset = bs.offset();
      // While there are more bytes, go backwards 4 bytes and see if it matches seq_ext\n
      while (offset > 0) {
        offset = offset - 4;
        bs.seek(offset);
        if (Arrays.equals(bs.readBytes(8), SEQ_EXT)) {
          return offset;
        }
      }
      throw new IOException("Unable to find seq_ext\n in seq bytes.");
    }
  }

  /**
   * Read opcode bytes from the ByteStream until a {@link SeqEdit#STOP} is encountered.
   *
   * @param bs The ByteStream to read from.
   * @return The opcode bytes read.ent calls.
   * @throws IOException If any I/O exception occurs.
   */
  private static byte[] readOpcodeBytes(ByteStream bs) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[4];
    if (bs.read(buffer) != 4) {
      throw new IOException("Failed to read 4 bytes from seq ext edit bytes.");
    }
    while (!Arrays.equals(SeqEdit.STOP, buffer)) {
      baos.write(buffer);
      if (bs.read(buffer) != 4) {
        throw new IOException("Failed to read 4 bytes from seq ext edit bytes.");
      }
    }
    return baos.toByteArray();
  }

  /**
   * Get the bytes of an seq file before the seq extension section.
   *
   * @param seqBytes The seq bytes.
   * @return The bytes before the seq extension section.
   */
  private static byte[] getBytesBeforeExt(byte[] seqBytes) {
    int index = Bytes.indexOf(seqBytes, SEQ_EXT);
    if (index == -1) {
      return seqBytes;
    }
    return Arrays.copyOfRange(seqBytes, 0, index);
  }
}
