package com.github.nicholasmoser.fpk;

import com.github.nicholasmoser.fpk.NativePRS.PRS;
import com.google.common.math.IntMath;
import com.sun.jna.Platform;
import java.math.RoundingMode;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class PRSCompare {
  @Test
  public void testCompare() throws Exception {
    if (!Platform.isWindows()) {
      System.out.println("Platforms other than Windows not supported for this test.");
      return;
    }
    Path filePath = Paths.get("D:/GNT/compressed.bin");
    int compressed_size = 0x70;
    int uncompressed_size = 0x1C4;
    byte[] output = new byte[uncompressed_size * 2];
    byte[] input = Files.readAllBytes(filePath);
    int val = PRS.INSTANCE.prsDecompress(output, input, compressed_size, uncompressed_size);
    System.out.println(String.format("%08X", val));
  }

  private short[] toShortArray(byte[] bytes) {
    int size = IntMath.divide(bytes.length, 2, RoundingMode.CEILING);
    ShortBuffer bb = ShortBuffer.allocate(size);
    for (int i = 0; i < bytes.length; i+=2) {
      bb.put((short)(((bytes[i] & 0xFF) << 8) | (bytes[i+1] & 0xFF)));
    }
    return bb.array();
  }
}
