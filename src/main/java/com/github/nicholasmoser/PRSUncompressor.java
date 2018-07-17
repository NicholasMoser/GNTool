package com.github.nicholasmoser;

/**
 * An uncompressor for Eighting PRS compressed files. Takes in the full byte
 * array of the file and returns the uncompressed byte stream.
 * 
 * Thanks to Luigi Auriemma for porting to QuickBMS. Thanks to tpu for
 * originally writing it: http://forum.xentax.com/viewtopic.php?p=30387#p30387
 */
public class PRSUncompressor
{
	/* The Eighting PRS compressed byte array. */
	private byte[] input;

	/* The expected output length (uncompressed size). */
	private int outputLength;

	/* The current byte location in the input byte array we are at. */
	private int inputPtr;

	/* Number of bits still remaining to look at in each byte. */
	private int bitsLeft;

	/* Buffer that holds an input byte to be uncompressed. */
	private byte buffer;

	/**
	 * PRSUncompressor constructor using an input byte array and output length.
	 * 
	 * @param input
	 *            Eighting PRS compressed byte array.
	 * @param outputLength
	 *            Expected output length (uncompressed size).
	 */
	public PRSUncompressor(byte[] input, int outputLength)
	{
		this.input = input;
		this.outputLength = outputLength;
		inputPtr = 0;
		bitsLeft = 0;
		buffer = 0;
	}

	/**
	 * Runs the uncompressor and returns the associated uncompressed byte array.
	 * 
	 * @return The associated uncompressed byte array.
	 */
	public byte[] prs_8ing_uncomp()
	{
		byte[] output = new byte[outputLength];
		int outputPtr = 0;
		int flag = 0;
		int len = 0;
		int pos = 0;
		while (inputPtr < input.length)
		{
			flag = prs_8ing_get_bits(1);
			if (flag == 1) // Uncompressed value
			{
				/*
				 * if (inputPtr < 256) { System.out.println(String.format("uncomp %02x %d",
				 * input[inputPtr], bitsLeft)); }
				 */
				if (outputPtr < output.length)
					output[outputPtr++] = input[inputPtr++];
			} else // Compressed value
			{
				flag = prs_8ing_get_bits(1);
				if (flag == 0) // Short search (length between 2 and 5)
				{
					len = prs_8ing_get_bits(2) + 2;
					pos = input[inputPtr++] | 0xffffff00;
				} else // Long search
				{
					pos = (input[inputPtr++] << 8) | 0xffff0000;
					pos |= input[inputPtr++] & 0xff;
					len = pos & 0x07;
					pos >>= 3;
					if (len == 0)
					{
						len = (input[inputPtr++] & 0xff) + 1;
					} else
					{
						len += 2;
					}
				}
				/*
				 * if (inputPtr < 256) {
				 * System.out.println(String.format("uncomp <%08x(%08x): %08x %d %d>",
				 * outputPtr, output.length, pos, len, bitsLeft)); }
				 */
				pos += outputPtr;
				for (int i = 0; i < len; i++)
				{
					if (outputPtr < output.length)
						output[outputPtr++] = output[pos++];
				}
			}
		}

		return output;
	}

	/**
	 * PRS get bit form lsb to msb, FPK get it form msb to lsb.
	 * 
	 * @param n
	 *            The number of bits to get.
	 * @return The bits requested.
	 */
	private int prs_8ing_get_bits(int n)
	{
		int retv = 0;

		retv = 0;
		while (n > 0)
		{
			retv <<= 1;
			if (bitsLeft == 0)
			{
				buffer = input[inputPtr];
				/*
				 * if (inputPtr < 256) { System.out.println(String.format("getbit [%02x] %d",
				 * buffer & 0xff, bitsLeft)); }
				 */
				inputPtr++;
				bitsLeft = 8;
			}

			if ((buffer & 0x80) > 0)
			{
				retv |= 1;
			}

			buffer <<= 1;
			bitsLeft--;
			n--;
		}
		return retv;
	}
}
