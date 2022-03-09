package com.github.nicholasmoser;

import static com.github.nicholasmoser.utils.TestUtil.assertDirectoriesEqual;

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
  // Performance of fastcopy branch seems to be negligible. In one case it was 1 second faster,
  // in other cases it was 3 seconds slower. In each case I restarted my PC to avoid disk caching.
  // Without the fastcopy branch the full copy + unpack process takes around 24 seconds on my HDD.
  // This does make sense, considering that the FPK files are only ~10% of the full bytes of the
  // ISO, so this only helps for 10% of the total copying time (17 seconds).
  @Test
  public void testUnpackFPK() throws Exception {
    Path compressedDir = Prereqs.getCompressedGNT4();
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path tempDir = FileUtils.getTempDirectory();
    Path unpackDir = tempDir.resolve(UUID.randomUUID().toString());
    try {
      Files.createDirectories(unpackDir);
      Optional<FileNames> gnt4FileNames = Optional.of(new GNT4FileNames());
      FPKUnpacker unpacker = new FPKUnpacker(compressedDir, unpackDir, gnt4FileNames, false, true);
      unpacker.unpackDirectory();
      assertDirectoriesEqual(uncompressedDir, unpackDir);
    } finally {
      if (Files.isDirectory(unpackDir)) {
        MoreFiles.deleteRecursively(unpackDir, RecursiveDeleteOption.ALLOW_INSECURE);
      }
    }
  }
}
