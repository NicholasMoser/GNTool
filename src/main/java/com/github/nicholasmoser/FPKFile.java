package com.github.nicholasmoser;

import com.google.common.primitives.Bytes;

/**
 * A compressed data file contained inside of an FPK file. Each FPK file will
 * have one or more of these.
 */
public class FPKFile
{
	private FPKFileHeader header;

	private byte[] data;

	public FPKFile(FPKFileHeader header, byte[] data)
	{
		int modDifference = data.length % 16;
		if (modDifference != 0)
		{
			this.data = Bytes.concat(data, new byte[16 - modDifference]);
		}
		else
		{
			this.data = data;
		}
		this.header = header;
	}

	public FPKFileHeader getHeader()
	{
		return header;
	}

	public byte[] getData()
	{
		return data;
	}
}
