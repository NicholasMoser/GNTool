package com.github.nicholasmoser.gnt4.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.nicholasmoser.testing.Prereqs;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class CPUFlagsTest {

  @Test
  public void testRecordingFlagChecks() throws Exception {
    Path uncompressed = Prereqs.getUncompressedGNT4();
    CPUFlags.verify0010FilesConsistent(uncompressed);
    assertEquals(-1, CPUFlags.getRecordingCPUFlag(uncompressed));
    assertEquals(-1, CPUFlags.getRecordingCounterCPUFlag(uncompressed));
    assertEquals(0x11, CPUFlags.actionToCPUFlag("Stand"));
    assertEquals(0x17, CPUFlags.actionToCPUFlag("Quick Stun Recovery"));
    assertThrows(IllegalArgumentException.class, () -> CPUFlags.actionToCPUFlag("test"));
  }
}
