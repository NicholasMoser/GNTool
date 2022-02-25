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
    short short5 = (short) 0x0000; // 0.0
    float float1 = (float) -0.0; // 0x8000
    float float2 = (float) 3.125; // 0x4048
    float float3 = (float) 0.017944336; // 0x3C93
    float float4 = (float) 3.21875; // 0x404E
    float float5 = (float) 0.0; // 0x0000

    // Assert that float values can be returned
    Coordinate coordinate = new Coordinate(short1, short2, short3, short4);
    assertEquals(float1, coordinate.getFloatX());
    assertEquals(float2, coordinate.getFloatY());
    assertEquals(float3, coordinate.getFloatZ());
    assertEquals(float4, coordinate.getFloatW());

    // Assert that equals works
    Coordinate duplicateCoordinate = new Coordinate(short1, short2, short3, short4);
    assertEquals(coordinate, duplicateCoordinate);

    // Assert that values can be changed and result in different coordinates
    coordinate.setFloatX(float5);
    assertNotEquals(coordinate, duplicateCoordinate);
    assertEquals(short5, coordinate.getX());
    assertEquals(float5, coordinate.getFloatX());
    coordinate.setFloatY(float4);
    assertEquals(short4, coordinate.getY());
    assertEquals(float4, coordinate.getFloatY());

    // Validate that truncating floats to a short results in precision loss
    float pi = (float) 3.1415927;
    coordinate.setFloatW(pi); // truncated to ~3.140625
    assertNotEquals(pi, coordinate.getFloatW(), 0.0001); // Too much precision is lost so fail
    assertEquals(pi, coordinate.getFloatW(), 0.001);

    // Test the alternate constructor
    Coordinate otherCoordinate = new Coordinate(float1, float2, float3, float4);
    System.out.printf("otherCoordinate.getX() 0x%X\n", otherCoordinate.getX());
    System.out.printf("duplicateCoordinate.getX() 0x%X\n", duplicateCoordinate.getX());
    System.out.printf("otherCoordinate.getY() 0x%X\n", otherCoordinate.getY());
    System.out.printf("duplicateCoordinate.getY() 0x%X\n", duplicateCoordinate.getY());
    System.out.printf("otherCoordinate.getZ() 0x%X\n", otherCoordinate.getZ());
    System.out.printf("duplicateCoordinate.getZ() 0x%X\n", duplicateCoordinate.getZ());
    System.out.println(otherCoordinate.getFloatZ());
    System.out.println(duplicateCoordinate.getFloatZ());
    assertEquals(otherCoordinate, duplicateCoordinate);

    // Validate that the alternate constructor also results in precision loss
    Coordinate otherCoordinate2 = new Coordinate(pi, pi, pi, pi);
    assertNotEquals(pi, otherCoordinate2.getFloatX(), 0.0001); // Too much precision is lost so fail
    assertEquals(pi, otherCoordinate2.getFloatX(), 0.001);
    assertNotEquals(pi, otherCoordinate2.getFloatY(), 0.0001); // Too much precision is lost so fail
    assertEquals(pi, otherCoordinate2.getFloatY(), 0.001);
    assertNotEquals(pi, otherCoordinate2.getFloatZ(), 0.0001); // Too much precision is lost so fail
    assertEquals(pi, otherCoordinate2.getFloatZ(), 0.001);
    assertNotEquals(pi, otherCoordinate2.getFloatW(), 0.0001); // Too much precision is lost so fail
    assertEquals(pi, otherCoordinate2.getFloatW(), 0.001);
  }
}
