package com.github.nicholasmoser;

import com.github.nicholasmoser.fpk.FileNames;
import com.github.nicholasmoser.gnt4.GNT4FileNames;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class FPKUnpackerTest {
  @Test
  public void testUnpackFPK() throws Exception {
    Path compressedDir = Prereqs.getCompressedGNT4();
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path tempDir = FileUtils.getTempDirectory();
    Path unpackDir = tempDir.resolve(UUID.randomUUID().toString());
    try {
      Files.createDirectories(unpackDir);
      FileUtils.copyFolder(compressedDir, unpackDir);
      Optional<FileNames> gnt4FileNames = Optional.of(new GNT4FileNames());
      FPKUnpacker unpacker = new FPKUnpacker(unpackDir, gnt4FileNames, false, true);
      unpacker.unpackDirectory();
      FileUtils.areDirectoriesEqual(uncompressedDir, unpackDir);
    } finally {
      if (Files.isDirectory(unpackDir)) {
        MoreFiles.deleteRecursively(unpackDir, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }
}
