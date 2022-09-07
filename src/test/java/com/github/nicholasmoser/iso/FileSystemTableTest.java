package com.github.nicholasmoser.iso;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FileSystemTableTest {
  @Test
  public void vanillaFileSystemCanBeRead() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    Path fst = uncompressed.resolve("sys/fst.bin");
    List<ISOItem> items = FileSystemTable.read(fst);
    assertThat(items.size()).isEqualTo(563);
    assertThat(Files.size(fst)).isEqualTo(0x33E7);
    assertThat(items).doesNotContainNull();
  }
}
