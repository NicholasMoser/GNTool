package com.github.nicholasmoser.gnt4.seq;

import com.github.nicholasmoser.utils.ByteUtils;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class Dupe4pCharsPatch {

  public static String CHARSEL4 = "files/maki/charsel4.seq";

  /**
   * Apply the patch for allowing duplicate characters in 4-player mode.
   *
   * @param uncompressedDir The uncompressed directory of the GNT4 workspace.
   * @throws IOException If any I/O issues occur
   */
  public static void apply(Path uncompressedDir) throws IOException {
    try (RandomAccessFile raf = new RandomAccessFile(uncompressedDir.resolve(CHARSEL4).toFile(),
        "rw")) {
      // Player 1 Doesn't Lock Character
      raf.seek(0x40e0);
      raf.write(jumpTo(0x40F0));
      // Player 2 Doesn't Lock Character
      raf.seek(0x43cc);
      raf.write(jumpTo(0x43DC));
      // Player 3 Doesn't Lock Character
      raf.seek(0x4684);
      raf.write(jumpTo(0x4694));
      // Player 4 Doesn't Lock Character
      raf.seek(0x48e8);
      raf.write(jumpTo(0x48f8));
      // Player 2 Can Have Same Character as Player 1
      raf.seek(0x3B00);
      raf.write(jumpTo(0x3B10));
      // Player 3 Can Have Same Character as Player 1
      raf.seek(0x3C88);
      raf.write(jumpTo(0x3C98));
      // Player 4 Can Have Same Character as Player 1
      raf.seek(0x3E10);
      raf.write(jumpTo(0x3E20));
      // Player 1 Can Have Same Character as Player 2
      raf.seek(0x3978);
      raf.write(jumpTo(0x3988));
      // Player 3 Can Have Same Character as Player 2
      raf.seek(0x3CE4);
      raf.write(jumpTo(0x3CF4));
      // Player 4 Can Have Same Character as Player 2
      raf.seek(0x3E6C);
      raf.write(jumpTo(0x3E7C));
      // Player 1 Can Have Same Character As Player 3
      raf.seek(0x39D4);
      raf.write(jumpTo(0x39E4));
      // Player 2 Can Have Same Character As Player 3
      raf.seek(0x3B5C);
      raf.write(jumpTo(0x3B6C));
      // Player 4 Can Have Same Character As Player 3
      raf.seek(0x3EC8);
      raf.write(jumpTo(0x3ED8));
      // Player 1 Can Have Same Character As Player 4
      raf.seek(0x3A30);
      raf.write(jumpTo(0x3A40));
      // Player 2 Can Have Same Character As Player 4
      raf.seek(0x3BB8);
      raf.write(jumpTo(0x3BC8));
      // Player 3 Can Have Same Character As Player 4
      raf.seek(0x3D40);
      raf.write(jumpTo(0x3D50));
    }
  }

  /**
   * Returns a 16 byte long unconditional jump to the given offset.
   *
   * @param offset The offset to jump to.
   * @return a 16 byte long unconditional jump to the given offset.
   */
  private static byte[] jumpTo(int offset) {
    byte[] jump = new byte[]{0x01, 0x32, 0x00, 0x00, 0x00, 0x00, 0x00,
        (byte) 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    byte[] offsetBytes = ByteUtils.fromInt32(offset);
    System.arraycopy(offsetBytes, 0, jump, 4, offsetBytes.length);
    return jump;
  }
}
