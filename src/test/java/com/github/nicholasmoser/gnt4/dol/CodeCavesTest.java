package com.github.nicholasmoser.gnt4.dol;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.gnt4.dol.CodeCaves.CodeCave;
import com.github.nicholasmoser.testing.Prereqs;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class CodeCavesTest {

  @Test
  public void testEXI2CodeCave() throws Exception {
    assertEquals(CodeCaves.EXI2_START_ADDRESS, CodeCaves.getStartAddress(CodeCave.EXI2));
    assertEquals(CodeCaves.EXI2_END_ADDRESS, CodeCaves.getEndAddress(CodeCave.EXI2));
    assertEquals(CodeCaves.EXI2_START_OFFSET, CodeCaves.getStartOffset(CodeCave.EXI2));
    assertEquals(CodeCaves.EXI2_END_OFFSET, CodeCaves.getEndOffset(CodeCave.EXI2));
    assertEquals(CodeCaves.EXI2_SIZE, CodeCaves.getSize(CodeCave.EXI2));
    assertEquals(CodeCaves.EXI2_SIZE, CodeCaves.EXI2_END_ADDRESS - CodeCaves.EXI2_START_ADDRESS);
    assertEquals(CodeCaves.EXI2_SIZE, CodeCaves.EXI2_END_OFFSET - CodeCaves.EXI2_START_OFFSET);
    byte[] expectedBytes = CodeCaves.getEXI2Bytes();
    byte[] actualBytes = new byte[CodeCaves.EXI2_SIZE];
    Path dol = Prereqs.getUncompressedGNT4().resolve("sys/main.dol");
    try (RandomAccessFile raf = new RandomAccessFile(dol.toFile(), "r")) {
      raf.seek(CodeCaves.EXI2_START_OFFSET);
      raf.read(actualBytes);
    }
    assertArrayEquals(expectedBytes, actualBytes);
  }

  @Test
  public void testTRKCodeCave() throws Exception {
    assertEquals(CodeCaves.TRK_START_ADDRESS, CodeCaves.getStartAddress(CodeCave.TRK));
    assertEquals(CodeCaves.TRK_END_ADDRESS, CodeCaves.getEndAddress(CodeCave.TRK));
    assertEquals(CodeCaves.TRK_START_OFFSET, CodeCaves.getStartOffset(CodeCave.TRK));
    assertEquals(CodeCaves.TRK_END_OFFSET, CodeCaves.getEndOffset(CodeCave.TRK));
    assertEquals(CodeCaves.TRK_SIZE, CodeCaves.getSize(CodeCave.TRK));
    assertEquals(CodeCaves.TRK_SIZE, CodeCaves.TRK_END_ADDRESS - CodeCaves.TRK_START_ADDRESS);
    assertEquals(CodeCaves.TRK_SIZE, CodeCaves.TRK_END_OFFSET - CodeCaves.TRK_START_OFFSET);
    byte[] expectedBytes = CodeCaves.getTRKBytes();
    byte[] actualBytes = new byte[CodeCaves.TRK_SIZE];
    Path dol = Prereqs.getUncompressedGNT4().resolve("sys/main.dol");
    try (RandomAccessFile raf = new RandomAccessFile(dol.toFile(), "r")) {
      raf.seek(CodeCaves.TRK_START_OFFSET);
      raf.read(actualBytes);
    }
    assertArrayEquals(expectedBytes, actualBytes);
  }

  @Test
  public void testRecordingCodeCave() throws Exception {
    assertEquals(CodeCaves.RECORDING_START_ADDRESS, CodeCaves.getStartAddress(CodeCave.RECORDING));
    assertEquals(CodeCaves.RECORDING_END_ADDRESS, CodeCaves.getEndAddress(CodeCave.RECORDING));
    assertEquals(CodeCaves.RECORDING_START_OFFSET, CodeCaves.getStartOffset(CodeCave.RECORDING));
    assertEquals(CodeCaves.RECORDING_END_OFFSET, CodeCaves.getEndOffset(CodeCave.RECORDING));
    assertEquals(CodeCaves.RECORDING_SIZE, CodeCaves.getSize(CodeCave.RECORDING));
    assertEquals(CodeCaves.RECORDING_SIZE,
        CodeCaves.RECORDING_END_ADDRESS - CodeCaves.RECORDING_START_ADDRESS);
    assertEquals(CodeCaves.RECORDING_SIZE,
        CodeCaves.RECORDING_END_OFFSET - CodeCaves.RECORDING_START_OFFSET);
    byte[] expectedBytes = CodeCaves.getRecordingBytes();
    byte[] actualBytes = new byte[CodeCaves.RECORDING_SIZE];
    Path dol = Prereqs.getUncompressedGNT4().resolve("sys/main.dol");
    try (RandomAccessFile raf = new RandomAccessFile(dol.toFile(), "r")) {
      raf.seek(CodeCaves.RECORDING_START_OFFSET);
      raf.read(actualBytes);
    }
    assertArrayEquals(expectedBytes, actualBytes);
  }
}
