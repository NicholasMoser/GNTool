package com.github.nicholasmoser;

import java.util.Arrays;

/**
 * A compressor for Eighting PRS compressed files. Takes in the full byte array
 * of the file and returns the uncompressed byte stream.
 *
 * Thanks to Luigi Auriemma for porting to QuickBMS. Thanks to RupertAvery for
 * originally writing it: https://gbatemp.net/threads/tvc-fpk-tool.207232/
 * http://www.mediafire.com/?vty5jymlmm2
 */
public class PRSCompressor
{
	/* Byte array to compress to The Eighting PRS compression. */
	private byte[] input;

	int ctrl_ptr;

	int data_ptr;

	int ctrl_bit;

	int length;

	int pos;

	int sptr;

	public PRSCompressor(byte[] input)
	{
		this.input = input;
		ctrl_ptr = 0;
		data_ptr = 1;
		ctrl_bit = 7;
		length = 0;
		pos = 0;
		sptr = 0;
	}

	public byte[] prs_8ing_compress()
	{
		byte[] output = new byte[input.length * 2];

		while (sptr < input.length)
		{
			if (check_rle())
			{
				write_comp(output, length, pos);
				// System.out.println(String.format("wtf: %d %d", sptr, length));
				sptr += length;

			}
			else
			{
				if (check_window(sptr))
				{
					write_comp(output, length, pos);
					// System.out.println(String.format("lol: %d %d", sptr, length));
					sptr += length;
				}
				else
				{
					write_nocomp(output);
					sptr++;
				}
			}
		}
		terminateFile(output);

		return Arrays.copyOfRange(output, 0, (data_ptr) + 3); // Extra 3 zeroes
	}

	/**
	 * Terminates the file by adding a 0 and 1 bit to the flag. This is equivalent
	 * to a long search, but yields no result.
	 * 
	 * @param output The output bytes.
	 */
	private void terminateFile(byte[] output)
	{
		write_bit(output, 0);
		write_bit(output, 1);
	}

	/**
	 * Tests the current buffer position for run length encoding (RLE). This will
	 * scan ahead of the current position until the byte changes or the maximum of
	 * 256 is reached.
	 * 
	 * @return The RLE encoding.
	 */
	private boolean check_rle()
	{
		// System.out.println(String.format("check_rle <xxxxxxxx %d %d %d>", sptr,
		// length, pos));
		int p = sptr;
		length = 0;
		pos = 1;
		// System.out.println(String.format("<%d>", p));

		if (sptr < pos)
		{
			return false;
		}
		while (p < input.length && input[sptr - pos] == input[p] && length < 256)
		{
			length++;
			p++;
		}
		// System.out.println(String.format("<%d>", p));

		return length > 1;
	}

	// Scans backwards from the current buffer position
	private boolean check_window(int newsptr)
	{
		// System.out.println(String.format("check_window <xxxxxxxx %d %d %d>", newsptr,
		// length, pos));
		int tlength = 1;
		int tpos = -1;

		if (newsptr < tlength)
		{
			return false;
		}

		int tlengthMax = 256;
		int bytesLeft = input.length - sptr;
		if (bytesLeft < tlengthMax)
		{
			tlengthMax = bytesLeft + 1;
		}

		int p = newsptr - tlength;
		int curr = newsptr;
		int savep = p;
		// System.out.println(String.format("<%d %d>", p, curr));

		// scan area = 8192 bytes
		// no scanning beyond start of buffer
		// limit to 256 bytes match length
		while (((curr - p) < 8192) && (p >= 0) && (tlength < tlengthMax))
		{
			while (memcmp(curr, p, tlength) && (tlength < tlengthMax))
			{
				// System.out.println(String.format("match: <%08x> %d %08x %08x", p, tlength,
				// input[curr], input[p]));
				savep = p;
				tlength++;
			}
			p--;
		}

		tlength--;
		tpos = curr - savep;

		length = tlength;
		pos = tpos;

		if ((tlength == 2) && (tpos > 255))
		{
			return false;
		}

		// System.out.println(String.format("last: %d", tlength));
		return tlength >= 2;
	}

	private boolean memcmp(int curr, int p, int tlength)
	{
		byte[] range1 = Arrays.copyOfRange(input, curr, curr + tlength);
		byte[] range2 = Arrays.copyOfRange(input, p, p + tlength);
		return Arrays.equals(range1, range2);
	}

	private void write_bit(byte[] output, int bit)
	{
		if (ctrl_bit == -1)
		{
			ctrl_bit = 7;
			ctrl_ptr = data_ptr;
			data_ptr = ctrl_ptr + 1;
		}
		output[ctrl_ptr] |= bit << ctrl_bit;
		ctrl_bit--;
	}

	private void write_comp_a(byte[] output, int len, int posy)
	{
		// System.out.println(String.format("write_comp_a <%d %d>", len, posy));
		int ctr = 2;
		write_bit(output, 0);
		write_bit(output, 0);

		len -= 2;
		while (ctr > 0)
		{
			write_bit(output, (len >> 1) & 0x01);
			// System.out.println(String.format("<%08x>", ((len >> 1) & 0x01)));
			len = (len << 1) & 0x02;
			// System.out.println(String.format("<%08x>", len));
			ctr--;
		}

		output[data_ptr++] = (byte) ((~posy + 1) & 0xFF);
	}

	private void write_comp_b(byte[] output, int len, int posy)
	{
		// System.out.println(String.format("write_comp_b <%d %d>", len, posy));
		write_bit(output, 0);
		write_bit(output, 1);

		posy = (~posy + 1) << 3;

		if (len <= 9)
		{
			posy |= ((len - 2) & 0x07);
		}
		// else lower 3 bits are empty...

		// TODO
		output[data_ptr++] = (byte) ((posy >> 8) & 0xFF);
		output[data_ptr++] = (byte) (posy & 0xFF);

		// ... and next byte encodes full length
		if (len > 9)
		{
			output[data_ptr++] = (byte) ((len - 1) & 0xFF);
		}
	}

	private void write_comp(byte[] output, int len, int posy)
	{
		if (posy > 255 || length > 5)
		{
			write_comp_b(output, len, posy);
		}
		else
		{
			write_comp_a(output, len, posy);
		}
	}

	private void write_nocomp(byte[] output)
	{
		// System.out.println(String.format("write_nocomp <%d %d>", data_ptr, sptr));
		write_bit(output, 1);
		output[data_ptr++] = (byte) (input[sptr] & 0xFF);
	}
}
