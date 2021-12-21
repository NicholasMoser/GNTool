package com.github.nicholasmoser;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.gnt4.GNT4Files;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FileUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

public class FPKBenchmark {
  // Before
  // Benchmark                         Mode  Cnt   Score   Error  Units
  // FPKBenchmark.packLargeNumbersFPK  avgt    5  22.150 ± 0.110   s/op

  // After
  // Benchmark                         Mode  Cnt   Score   Error  Units
  // FPKBenchmark.packLargeNumbersFPK  avgt    5  22.029 ± 0.122   s/op
  @Fork(value = 1, warmups = 1)
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public void packLargeNumbersFPK() throws Exception {
    Path defaultState = Paths.get(
        "src/main/resources/com/github/nicholasmoser/gnt4/vanilla_with_fpks.bin");
    GNT4Files GNT4_FILES = new GNT4Files(null, defaultState);
    GNT4_FILES.loadExistingState();
    List<GNTChildFile> children = GNT4_FILES.getFPKChildren("files/fpack/seq0000.fpk");
    assertFalse(children.isEmpty());
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path testFPK = FileUtils.getTempDirectory().resolve(UUID.randomUUID().toString());
    try {
      FPKPacker.repackFPK(children, testFPK, uncompressedDir, true, false);
    } finally {
      Files.deleteIfExists(testFPK);
    }
  }

  @Fork(value = 1, warmups = 1)
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public void packLargeSizeFPK() throws Exception {
    Path defaultState = Paths.get(
        "src/main/resources/com/github/nicholasmoser/gnt4/vanilla_with_fpks.bin");
    GNT4Files GNT4_FILES = new GNT4Files(null, defaultState);
    GNT4_FILES.loadExistingState();
    List<GNTChildFile> children = GNT4_FILES.getFPKChildren("files/fpack/game0001.fpk");
    assertFalse(children.isEmpty());
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path testFPK = FileUtils.getTempDirectory().resolve(UUID.randomUUID().toString());
    try {
      FPKPacker.repackFPK(children, testFPK, uncompressedDir, true, false);
    } finally {
      Files.deleteIfExists(testFPK);
    }
  }

  @Fork(value = 1, warmups = 1)
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  public void packSmallFPK() throws Exception {
    Path defaultState = Paths.get(
        "src/main/resources/com/github/nicholasmoser/gnt4/vanilla_with_fpks.bin");
    GNT4Files GNT4_FILES = new GNT4Files(null, defaultState);
    GNT4_FILES.loadExistingState();
    List<GNTChildFile> children = GNT4_FILES.getFPKChildren("files/fpack/story/story0124.fpk");
    assertFalse(children.isEmpty());
    Path uncompressedDir = Prereqs.getUncompressedGNT4();
    Path testFPK = FileUtils.getTempDirectory().resolve(UUID.randomUUID().toString());
    try {
      FPKPacker.repackFPK(children, testFPK, uncompressedDir, true, false);
    } finally {
      Files.deleteIfExists(testFPK);
    }
  }
}
