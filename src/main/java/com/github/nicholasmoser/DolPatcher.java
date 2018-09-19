package com.github.nicholasmoser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Patches the Start.dol file to fix the audio issue. The audio issue is that if
 * the offset for an audio file in the game.toc does not end with 0x0000 or
 * 0x8000 the game will crash. This is because at line 0x8016fc08 it does
 * <code>rlwinm. r0, r0, 0, 17, 31 (00007fff)</code> and then on line 0x8016fc0c
 * does <code>beq- 0x8016FC2C</code>. The end result of these two operations is
 * that it will not branch if the offset is something like 0x0c3e7800 and will
 * instead enter OSPanic (crash).
 * 
 * The reason that the offset can be changed in the game.toc it because
 * GCRebuilder.exe will use new offsets for the audio files if the files placed
 * before them in the ISO are too large. Since GCRebuilder.exe cannot be
 * modified, the only two solutions right now are to try and use padding or
 * modify the game code. Modifying the game code seems not to have any side
 * effects, so this is the current accepted solution.
 */
public class DolPatcher
{
	private Path dol;
	
	// Whether or not to actually modify the files (disable for unit tests).
	private boolean dryRun;

	private static final byte[] INSTRUCTIONS_8016fc00 = new byte[]
	{ 0x3b, (byte) 0xe3, 0x61, 0x78, 0x7c, 0x07, (byte) 0xea, 0x14 };

	private static final byte[] INSTRUCTIONS_8016fc08 = new byte[]
	{ 0x54, 0x00, 0x04, 0x7f, 0x41, (byte) 0x82, 0x00, 0x20 };

	private static final byte[] INSTRUCTIONS_8016fc08_PATCHED = new byte[]
	{ 0x54, 0x00, 0x04, 0x7f, 0x48, 0x00, 0x00, 0x21 };

	public DolPatcher(Path dol)
	{
		this.dol = dol;
		this.dryRun = false;
	}

	public DolPatcher(Path dol, boolean dryRun)
	{
		this.dol = dol;
		this.dryRun = dryRun;
	}

	public boolean patchAudioOffset() throws IOException
	{
		boolean codeFound = false;
		int totalBytesRead = 0;
		try (InputStream in = Files.newInputStream(dol))
		{
			byte[] buffer = new byte[8];
			int bytesRead = 0;

			while ((bytesRead = in.read(buffer)) >= 0)
			{
				totalBytesRead += bytesRead;
				if (codeFound)
				{
					if (Arrays.equals(buffer, INSTRUCTIONS_8016fc08))
					{
						break;
					}
					else if (Arrays.equals(buffer, INSTRUCTIONS_8016fc08_PATCHED))
					{
						return false;
					}
					else
					{
						throw new IOException("Unexpected instruction at 0x8016fc08 in Start.dol, actually:" + buffer);
					}
				}
				else if (Arrays.equals(buffer, INSTRUCTIONS_8016fc00))
				{
					codeFound = true;
				}
			}
		}
		if (codeFound)
		{
			if(!dryRun)
			{
				writePatchedBytes(totalBytesRead - 8);
			}
			return true;
		}
		else
		{
			throw new IOException("Unable to find instruction at 0x8016fc00 in Start.dol.");
		}
	}
	
	private void writePatchedBytes(int index) throws FileNotFoundException, IOException
	{
		RandomAccessFile file = new RandomAccessFile(dol.toFile(), "rw");
		file.seek(index);
		file.write(INSTRUCTIONS_8016fc08_PATCHED);
		file.close();
	}
}
