package com.github.nicholasmoser.fpk;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class NativePRS {

  // This is the standard, stable way of mapping, which supports extensive
  // customization and mapping of Java to native types.

  public interface PRS extends Library {

    PRS INSTANCE = (PRS)
        Native.load(("PRS.dll"), PRS.class);

    int prsDecompress(byte[] output_start, byte[] input_start,
        int compressed_size,
        int uncompressed_size);
  }
}
