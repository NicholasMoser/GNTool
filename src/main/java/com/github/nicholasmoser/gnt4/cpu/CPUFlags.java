package com.github.nicholasmoser.gnt4.cpu;

import static java.util.Map.entry;

import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class CPUFlags {

  /**
   * Offset in the chr 0010.seq files where the branches for CPU flag actions are.
   */
  public static final int CPU_BRANCHES_OFFSET = 0x27C;

  /**
   * Offset in the chr 0010.seq files where the recording code is at.
   */
  public static final int RECORDING_OFFSET = 0xF50;

  /**
   * Offset in the chr 0010.seq files where the recording counter code is at.
   */
  public static final int RECORDING_COUNTER_OFFSET = 0x140C;

  /**
   * The branch at offset 0x10 in the branch table for chr/obo and chr/ta2
   */
  public static final int OBO_TA2_SPECIFIC_OFFSET = 0x30A8;

  /**
   * Each index is the CPU flag and each int is the offset to branch to for that action. Only the
   * characters obo and ta2 are different for CPU flag 0x10.
   */
  public static final List<Integer> CPU_BRANCHES = List.of(0x0344,
      0x03B8,
      0x05B8,
      0x1BC0,
      0x1CB8,
      0x1D40,
      0x1DC0,
      0x1E08,
      0x1EB8,
      0x1F0C,
      0x1F10,
      0x1F14,
      0x1F18,
      0x1F1C,
      0x1F20,
      0x1F24,
      0x2E34, // Note: This is 0x30A8 for obo and ta2, see OBO_TA2_SPECIFIC_OFFSET
      0x09F8,
      0x0A58,
      0x0AEC,
      0x0B60,
      0x0C44,
      0x0CF4,
      0x0DD0,
      0x0E84,
      0x0EB4,
      0x0EE4,
      0x0F14,
      0x0F4C,
      0x1408,
      0x1910,
      0x1988,
      0x1AE0,
      0x1B64,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B84,
      0x1B88);

  /**
   * Matches CPU flags to their CPU action description.
   */
  public static SortedMap<Integer, String> CPU_FLAG_TO_ACTION = new TreeMap<>(
      Map.ofEntries(entry(0x11, "Stand"), entry(0x12, "Jump"), entry(0x13, "2P Control"),
          entry(0x14, "Approach and Throw"), entry(0x15, "Substitution Jutsu"),
          entry(0x16, "Tech Roll Recovery"), entry(0x17, "Quick Stun Recovery"),
          entry(0x18, "COM Difficulty Lv1"), entry(0x19, "COM Difficulty Lv2"),
          entry(0x1A, "COM Difficulty Lv3"), entry(0x1B, "COM Difficulty Lv4")));

  /**
   * Verify that the branch table for all chr 0010.seq files are consistent. Throws an IOException
   * if an error is encountered reading the files or the branch tables are not consistent.
   *
   * @param uncompressedDir The uncompressed directory.
   * @throws IOException If any I/O exception occurs.
   */
  public static void verify0010FilesConsistent(Path uncompressedDir) throws IOException {
    int recordingCPUFlag = getRecordingCPUFlag(uncompressedDir);
    int recordingCounterCPUFlag = getRecordingCounterCPUFlag(uncompressedDir);
    for (String chr0010 : Seqs.CHRS_0010) {
      Path chr0010File = uncompressedDir.resolve(chr0010);
      try (RandomAccessFile raf = new RandomAccessFile(chr0010File.toFile(), "r")) {
        raf.seek(CPU_BRANCHES_OFFSET);
        for (int i = 0; i < CPU_BRANCHES.size(); i++) {
          int expectedBranch;
          if (i == recordingCPUFlag) {
            expectedBranch = RECORDING_OFFSET;
          } else if (i == recordingCounterCPUFlag) {
            expectedBranch = RECORDING_COUNTER_OFFSET;
          } else if (i == 0x10 && (chr0010.equals(Seqs.OBO_0010) || chr0010.equals(
              Seqs.TA2_0010))) {
            expectedBranch = OBO_TA2_SPECIFIC_OFFSET;
          } else {
            expectedBranch = CPU_BRANCHES.get(i);
          }
          int actualBranch = ByteUtils.readInt32(raf);
          if (expectedBranch != actualBranch) {
            throw new IOException(
                String.format("Expected branch %d in file %s but was %d", expectedBranch,
                    chr0010File, actualBranch));
          }
        }
      }
    }
  }

  /**
   * Returns the index in Anko's 0010.seq file branch table where the recording offset is set.
   * Returns -1 if the recording offset is not being used.
   *
   * @param uncompressedDir The uncompressed directory.
   * @return The index of the recording offset or -1 if it is not being used.
   * @throws IOException If any I/O exception occurs.
   */
  public static int getRecordingCPUFlag(Path uncompressedDir) throws IOException {
    Path ank0010 = uncompressedDir.resolve(Seqs.ANK_0010);
    try (RandomAccessFile raf = new RandomAccessFile(ank0010.toFile(), "r")) {
      raf.seek(CPU_BRANCHES_OFFSET);
      for (int i = 0; i < CPU_BRANCHES.size(); i++) {
        if (ByteUtils.readInt32(raf) == RECORDING_OFFSET) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Returns the index in Anko's 0010.seq file branch table where the recording counter offset is
   * set. Returns -1 if the recording counter offset is not being used.
   *
   * @param uncompressedDir The uncompressed directory.
   * @return The index of the recording counter offset or -1 if it is not being used.
   * @throws IOException If any I/O exception occurs.
   */
  public static int getRecordingCounterCPUFlag(Path uncompressedDir) throws IOException {
    Path ank0010 = uncompressedDir.resolve(Seqs.ANK_0010);
    try (RandomAccessFile raf = new RandomAccessFile(ank0010.toFile(), "r")) {
      raf.seek(CPU_BRANCHES_OFFSET);
      for (int i = 0; i < CPU_BRANCHES.size(); i++) {
        if (ByteUtils.readInt32(raf) == RECORDING_COUNTER_OFFSET) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Sets the branch offset for a specific CPU flag.
   *
   * @param uncompressedDir The uncompressed directory.
   * @param flag The CPU flag (index).
   * @param offset The branch offset to set.
   * @throws IOException If any I/O exception occurs.
   */
  public static void setCPUFlag(Path uncompressedDir, int flag, int offset) throws IOException {
    if (flag < 0 || flag >= CPU_BRANCHES.size()) {
      throw new IllegalArgumentException("Flag " + flag + " is outside of allowed range");
    }
    for (String chr0010 : Seqs.CHRS_0010) {
      Path chr0010File = uncompressedDir.resolve(chr0010);
      try (RandomAccessFile raf = new RandomAccessFile(chr0010File.toFile(), "rw")) {
        raf.seek(CPU_BRANCHES_OFFSET + (flag * 4L));
        raf.write(ByteUtils.fromInt32(offset));
      }
    }
  }

  /**
   * Gets the CPU flag index from an action description.
   *
   * @param action The action description.
   * @return The CPU flag index.
   */
  public static int actionToCPUFlag(String action) {
    for (Entry<Integer, String> entry : CPUFlags.CPU_FLAG_TO_ACTION.entrySet()) {
      if (action.equals(entry.getValue())) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException(action + " action not found");
  }

  /**
   * Fixes the recording text size in the dol. This byte is a bit field of text properties for the
   * recording text (and maybe other text???). The existing bit flag is 0x80. We are adding 0x20,
   * therefore the new bit field is 0xA0.
   *
   * @param dol The dol to fix.
   * @throws IOException If any I/O exception occurs.
   */
  public static void fixRecordingTextSize(Path dol) throws IOException {
    try(RandomAccessFile raf = new RandomAccessFile(dol.toFile(), "rw")) {
      raf.seek(0x8D337);
      raf.write(0xA0);
    }
  }
}
