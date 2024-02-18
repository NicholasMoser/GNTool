package com.github.nicholasmoser.gnt4.seq.util;

import com.github.nicholasmoser.utils.ByteStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeqText {

  public static final byte[] END = new byte[] { 0x00, 0x00, 0x00, 0x1A };

  public static final byte[] NEWLINE = new byte[] { 0x00, 0x00, 0x00, 0x0A };

  public static List<Integer> readEntryOffsets(ByteStream bs, int size) throws IOException {
    if (size % 4 != 0) {
      throw new IllegalArgumentException("Size must be factor of 4: " + size);
    }
    List<Integer> offsets = new ArrayList<>(size / 4);
    for (int i = 0; i < size; i += 4) {
      offsets.add(bs.readWord());
    }
    return offsets;
  }
  public static List<SeqTextEntry> readEntries(ByteStream bs, List<Integer> offsets) throws IOException {
    List<SeqTextEntry> entries = new ArrayList<>(offsets.size());
    for (int offset : offsets) {
      if (offset == 0) {
        entries.add(new SeqTextEntry("", 0));
        continue;
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      bs.seek(offset);
      byte[] buffer = bs.readNBytes(4);
      while (!Arrays.equals(buffer, END)) {
        if (Arrays.equals(buffer, NEWLINE)) {
          baos.write('\n');
        } else {
          baos.write(buffer[3]);
          baos.write(buffer[2]);
        }
        buffer = bs.readNBytes(4);
      }
      SeqTextEntry entry = new SeqTextEntry(baos.toString(Charset.forName("shift-jis")), offset);
      entries.add(entry);
    }
    return entries;
  }
}
