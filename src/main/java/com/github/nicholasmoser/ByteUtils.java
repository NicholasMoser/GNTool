package com.github.nicholasmoser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class ByteUtils
{
	/**
	 * Converts a Java integer to a 4 byte big-endian byte array.
	 * 
	 * @param myInteger
	 *            The integer to use.
	 * @return The 4 byte big-endian array.
	 */
	public static byte[] intToBytes(int myInteger)
	{
		return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(myInteger).array();
	}

	/**
	 * Converts a Java String to a 4 byte big-endian byte array.
	 * 
	 * @param myString
	 *            The String to use.
	 * @return The 4 byte big-endian byte array.
	 */
	public static byte[] stringToBytes(String myString)
	{
		return myString.getBytes(Charset.forName("Windows-1252"));
	}
}
