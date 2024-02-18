package com.github.nicholasmoser.gnt4.seq.util;

import com.github.nicholasmoser.utils.ByteStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SeqTextReader {
  @Test
  public void test() throws Exception {
    //Path seq = Prereqs.getUncompressedGNT4().resolve(Seqs.M_TITLE);
    Path seq = Paths.get("G:\\GNT\\1.7.x\\uncompressed\\files\\maki\\m_title.seq");
    byte[] bytes = Files.readAllBytes(seq);
    ByteStream bs = new ByteStream(bytes);
    bs.seek(0x32410);
    List<Integer> offsets = SeqText.readEntryOffsets(bs, 0x8C0);
    List<SeqTextEntry> entries = SeqText.readEntries(bs, offsets);
    System.out.println(entries);
    try(OutputStream is = Files.newOutputStream(Paths.get("lol.txt"))) {
      for (int i = 0; i < entries.size(); i++) {
        SeqTextEntry entry = entries.get(i);
        is.write(String.format("0x%X", i).getBytes(StandardCharsets.UTF_8));
        is.write('\n');
        is.write(String.format("0x%X", entry.offset()).getBytes(StandardCharsets.UTF_8));
        is.write('\n');
        is.write(entry.text().getBytes(StandardCharsets.UTF_8));
        is.write('\n');
      }
    }
  }
}
