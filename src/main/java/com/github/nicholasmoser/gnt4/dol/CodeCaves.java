package com.github.nicholasmoser.gnt4.dol;

import java.io.IOException;
import java.io.InputStream;

/**
 * Locations in the game to inject code. See https://en.wikipedia.org/wiki/Code_cave Addresses are
 * addresses in memory and offsets are offsets in the main.dol file.
 */
public class CodeCaves {

  public enum CodeCave {
    EXI2,
    TRK,
    RECORDING
  }

  public static long getStartAddress(CodeCave codeCave) {
    return switch (codeCave) {
      case EXI2 -> EXI2_START_ADDRESS;
      case TRK -> TRK_START_ADDRESS;
      case RECORDING -> RECORDING_START_ADDRESS;
    };
  }

  public static long getEndAddress(CodeCave codeCave) {
    return switch (codeCave) {
      case EXI2 -> EXI2_END_ADDRESS;
      case TRK -> TRK_END_ADDRESS;
      case RECORDING -> RECORDING_END_ADDRESS;
    };
  }

  public static long getStartOffset(CodeCave codeCave) {
    return switch (codeCave) {
      case EXI2 -> EXI2_START_OFFSET;
      case TRK -> TRK_START_OFFSET;
      case RECORDING -> RECORDING_START_OFFSET;
    };
  }

  public static long getEndOffset(CodeCave codeCave) {
    return switch (codeCave) {
      case EXI2 -> EXI2_END_OFFSET;
      case TRK -> TRK_END_OFFSET;
      case RECORDING -> RECORDING_END_OFFSET;
    };
  }

  public static int getSize(CodeCave codeCave) {
    return switch (codeCave) {
      case EXI2 -> EXI2_SIZE;
      case TRK -> TRK_SIZE;
      case RECORDING -> RECORDING_SIZE;
    };
  }

  public static byte[] getBytes(CodeCave codeCave) {
    return switch (codeCave) {
      case EXI2 -> getEXI2Bytes();
      case TRK -> getTRKBytes();
      case RECORDING -> getRecordingBytes();
    };
  }

  /**
   * Stubs for communication over EXI2 to GameCube development kits.
   */
  public static final long EXI2_START_ADDRESS = 0x80196824L; // Inclusive
  public static final long EXI2_START_OFFSET = 0x193824L; // Inclusive
  public static final long EXI2_END_ADDRESS = 0x801972DCL; // Exclusive
  public static final long EXI2_END_OFFSET = 0x1942DCL; // Exclusive
  public static final int EXI2_SIZE = 0xAB8; // 2744 bytes

  public static byte[] getEXI2Bytes() {
    try (InputStream is = CodeCaves.class.getResourceAsStream("EXI2.bin")) {
      if (is == null) {
        throw new IllegalStateException("Failed to read EXI2.bin");
      }
      return is.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Code from TRK_MINNOW_DOLPHIN.a used for debugging hardware. If this code cave is used, a nop
   * must be added at instruction 0x80192ee4 OR the end of the code cave must be moved to
   * 0x8018decc. This is because the function OSReport calls __TRK_write_console.
   */
  public final static long TRK_START_ADDRESS = 0x801872f4L; // Inclusive
  public final static long TRK_START_OFFSET = 0x1842F4L; // Inclusive
  public final static long TRK_END_ADDRESS = 0x8018e044L; // Exclusive
  public final static long TRK_END_OFFSET = 0x18B044L; // Exclusive
  public final static int TRK_SIZE = 0x6D50; // 27,984 bytes

  public static byte[] getTRKBytes() {
    try (InputStream is = CodeCaves.class.getResourceAsStream("TRK.bin")) {
      if (is == null) {
        throw new IllegalStateException("Failed to read TRK.bin");
      }
      return is.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Logic for training mode recording. Do not use this code cave. It was previously used for Gecko
   * code hijacking, but since training mode recording is now possible, we should avoid using this
   * code cave.
   */
  public final static long RECORDING_START_ADDRESS = 0x80086F58L; // Inclusive
  public final static long RECORDING_START_OFFSET = 0x83F58L; // Inclusive
  public final static long RECORDING_END_ADDRESS = 0x800873FCL; // Exclusive
  public final static long RECORDING_END_OFFSET = 0x843FCL; // Exclusive
  public final static int RECORDING_SIZE = 0x4A4; // 1188 bytes

  public static byte[] getRecordingBytes() {
    try (InputStream is = CodeCaves.class.getResourceAsStream("RECORDING.bin")) {
      if (is == null) {
        throw new IllegalStateException("Failed to read RECORDING.bin");
      }
      return is.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
