package com.github.nicholasmoser.mot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class CoordinateTest {
  @Test
  void testCoordinate() {
    // Test values
    short short1 = (short) 0x8000; // -0.0
    short short2 = (short) 0x4048; // 3.125
    short short3 = (short) 0x3C93; // 0.017944336
    short short4 = (short) 0x404E; // 3.21875
    float float1 = (float) -0.0; // 0x8000
    float float2 = (float) 3.125; // 0x4048
    float float3 = (float) 0.017944336; // 0x3C93
    float float4 = (float) 3.21875; // 0x404E

    // Assert that float values can be returned
    Coordinate coordinate = new Coordinate(short1, short2, short3, short4);
    assertEquals(float1, coordinate.getFloatX());
    assertEquals(float2, coordinate.getFloatY());
    assertEquals(float3, coordinate.getFloatZ());
    assertEquals(float4, coordinate.getFloatW());

    // Assert that equals works
    Coordinate duplicateCoordinate = new Coordinate(short1, short2, short3, short4);
    assertEquals(coordinate, duplicateCoordinate);

    // Test the alternate constructor
    Coordinate otherCoordinate = new Coordinate(float1, float2, float3, float4);
    assertEquals(otherCoordinate, duplicateCoordinate);

    // Validate that the float constructor results in no precision loss
    float pi = (float) 3.1415927;
    Coordinate otherCoordinate2 = new Coordinate(pi, pi, pi, pi);
    assertEquals(pi, otherCoordinate2.getFloatX(), 0.0001);
    assertEquals(pi, otherCoordinate2.getFloatX(), 0.001);
    assertEquals(pi, otherCoordinate2.getFloatY(), 0.0001);
    assertEquals(pi, otherCoordinate2.getFloatY(), 0.001);
    assertEquals(pi, otherCoordinate2.getFloatZ(), 0.0001);
    assertEquals(pi, otherCoordinate2.getFloatZ(), 0.001);
    assertEquals(pi, otherCoordinate2.getFloatW(), 0.0001);
    assertEquals(pi, otherCoordinate2.getFloatW(), 0.001);
  }
}
