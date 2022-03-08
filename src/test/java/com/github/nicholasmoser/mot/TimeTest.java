package com.github.nicholasmoser.mot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.nicholasmoser.utils.ByteUtils;
import org.junit.jupiter.api.Test;

public class TimeTest {

  /**
   * Tests that converting frames to fractions yields the expected results. The expected results in
   * this case were taken directly from binary GNT4 files, which unfortunately do not reflect
   * the rounding used by Java. Therefore, additional logic was added to account for the rounding
   * difference.
   */
  @Test
  public void testConvertFramesFractions() {
    compareConvertFramesFractions(0x00000000, 0);
    compareConvertFramesFractions(0x3C888889, 1);
    compareConvertFramesFractions(0x3D088889, 2);
    compareConvertFramesFractions(0x3D4CCCCE, 3);
    compareConvertFramesFractions(0x3D888889, 4);
    compareConvertFramesFractions(0x3DAAAAAB, 5);
    compareConvertFramesFractions(0x3DCCCCCE, 6);
    compareConvertFramesFractions(0x40200001, 150);
    compareConvertFramesFractions(0x40880000, 255);
    compareConvertFramesFractions(0x40888889, 256);
    compareConvertFramesFractions(0x40891112, 257);
    compareConvertFramesFractions(0x41455556, 740); // last cached value
    compareConvertFramesFractions(0x41855555, 1000);
    compareConvertFramesFractions(0x41B24444, 1337);
    compareConvertFramesFractions(0x4326AAAB, 10000);
    compareConvertFramesFractions(0x447A0000, 60000);
  }

  @Test
  public void testConvertFractionsToFrames() {
    assertEquals(0, Time.fractionToFrames(0.0f));
    assertEquals(1, Time.fractionToFrames(0.016666668f));
    assertEquals(2, Time.fractionToFrames(0.033333335f));
    assertEquals(3, Time.fractionToFrames(0.050000004f));
    assertEquals(4, Time.fractionToFrames(0.066666670f));
    assertEquals(5, Time.fractionToFrames(0.083333336f));
    assertEquals(6, Time.fractionToFrames(0.100000009f));
    assertEquals(7, Time.fractionToFrames(0.116666675f));
    assertEquals(8, Time.fractionToFrames(0.133333340f));
    assertEquals(9, Time.fractionToFrames(0.150000006f));
    assertEquals(10, Time.fractionToFrames(0.166666672f));
    assertEquals(59, Time.fractionToFrames(0.983333409f));
    assertEquals(60, Time.fractionToFrames(1.0f));
    assertEquals(60, Time.fractionToFrames(1.000000000f));
    assertEquals(61, Time.fractionToFrames(1.016666770f));
    assertEquals(526, Time.fractionToFrames(8.766667366f));
    assertEquals(640, Time.fractionToFrames(10.666666985f));
  }

  private void compareConvertFramesFractions(int expectedFloatHex, int frames) {
    byte[] bytes = ByteUtils.fromInt32(expectedFloatHex);
    float expectedFloat = ByteUtils.toFloat(bytes);
    assertEquals(expectedFloat, Time.framesToFraction(frames));
  }
}
