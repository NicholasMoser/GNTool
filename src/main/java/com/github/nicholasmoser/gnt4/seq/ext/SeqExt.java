package com.github.nicholasmoser.gnt4.seq.ext;

import com.github.nicholasmoser.gnt4.seq.SeqHelper;
import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SeqExt {

  // seq_ext\n
  public static final byte[] SEQ_EXT = { 0x73, 0x65, 0x71, 0x5F, 0x65, 0x78, 0x74, 0x0A };

  // seq_end\n
  public static final byte[] SEQ_END = { 0x73, 0x65, 0x71, 0x5F, 0x65, 0x6E, 0x64, 0x0A };

  public static List<SeqEdit> getEdits(Path seqPath) throws IOException {
    return getEdits(Files.readAllBytes(seqPath));
  }

  public static List<SeqEdit> getEdits(byte[] seqBytes) throws IOException {
    if (!hasSeqExtEnd(seqBytes)) {
      return Collections.emptyList();
    }
    int seqExtOffset = getSeqExtOffset(seqBytes);
    int seqLength = seqBytes.length;
    // Add and subtract 8 to remove seq_ext\n and seq_end\n
    byte[] seqExtBytes = Arrays.copyOfRange(seqBytes, seqExtOffset + 8, seqLength - 8);
    List<SeqEdit> seqEdits = new ArrayList<>();
    try (ByteStream bs = new ByteStream(seqExtBytes)) {
      while (bs.bytesAreLeft()) {
        String name = new String(SeqHelper.readString(bs), StandardCharsets.UTF_8);
        int offset = bs.readWord();
        byte[] oldBytes = readEditBytes(bs);
        byte[] newBytes = readEditBytes(bs);
        seqEdits.add(new SeqEdit(name, offset, oldBytes, newBytes));
      }
    }
    return seqEdits;
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

  private static byte[] readEditBytes(ByteStream bs) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[4];
    if (bs.read(buffer) != 4) {
      throw new IOException("Failed to read 4 bytes from seq ext edit bytes.");
    }
    while(!Arrays.equals(SeqEdit.STOP, buffer)) {
      baos.write(buffer);
      if (bs.read(buffer) != 4) {
        throw new IOException("Failed to read 4 bytes from seq ext edit bytes.");
      }
    }
    return baos.toByteArray();
  }
}
