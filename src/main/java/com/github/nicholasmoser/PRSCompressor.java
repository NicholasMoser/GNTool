package com.github.nicholasmoser;

import java.util.Arrays;

/**
 * A compressor for Eighting PRS compressed files. Takes in the full byte array of the file and returns the uncompressed byte stream. Thanks to Luigi Auriemma for porting to QuickBMS. Thanks to
 * RupertAvery for originally writing it: https://gbatemp.net/threads/tvc-fpk-tool.207232/ http://www.mediafire.com/?vty5jymlmm2
 */
public class PRSCompressor
{
    // Input bytes to compress.
    private byte[] input;

    // The location in the output bytes of the current flag byte.
    private int flagIndex;

    // The current index of the input bytes.
    private int inputIndex;

    // The current index of the output bytes.
    private int outputIndex;

    // The current bit index inside of the flag byte.
    private int flagBitIndex;

    private int length;

    private int pos;

    /**
     * Creates a new Eighting PRS compressor.
     * 
     * @param input The input bytes to compress.
     */
    public PRSCompressor(byte[] input)
    {
        this.input = input;
        flagIndex = 0;
        outputIndex = 1;
        flagBitIndex = 7;
        length = 0;
        pos = 0;
        inputIndex = 0;
    }

    /**
     * Compresses the file by using the Eighting PRS compression algorithm.
     * 
     * @return The compressed bytes.
     */
    public byte[] compress()
    {
        byte[] output = new byte[input.length * 2];

        while (inputIndex < input.length)
        {
            if (checkRunLengthEncoding())
            {
                writeCompressedBytes(output, length, pos);
                inputIndex += length;
            }
            else
            {
                if (check_window(inputIndex))
                {
                    writeCompressedBytes(output, length, pos);
                    inputIndex += length;
                }
                else
                {
                    writeUncompressedByte(output);
                    inputIndex++;
                }
            }
        }
        terminateFile(output);

        // Return the length of the output plus three so that the file ends with three
        // 0s.
        return Arrays.copyOfRange(output, 0, (outputIndex) + 3);
    }

    /**
     * Terminates the file by adding a 0 and 1 bit to the flag. This is equivalent to a long search, but yields no result.
     * 
     * @param output The output bytes.
     */
    private void terminateFile(byte[] output)
    {
        writeBit(output, 0);
        writeBit(output, 1);
    }

    /**
     * Tests the current buffer position for run length encoding (RLE). This will scan ahead of the current position until the byte changes or the maximum of 256 is reached.
     * 
     * @return If run length encoding is found.
     */
    private boolean checkRunLengthEncoding()
    {
        int p = inputIndex;
        length = 0;
        pos = 1;

        if (inputIndex < pos)
        {
            return false;
        }
        while (p < input.length && input[inputIndex - pos] == input[p] && length < 256)
        {
            length++;
            p++;
        }

        return length > 1;
    }

    /**
     * Scans backwards from the current buffer position
     * 
     * @param newsptr
     * @return If previous repeated occurrences were found.
     */
    private boolean check_window(int newsptr)
    {
        int tlength = 1;
        int tpos = -1;

        if (newsptr < tlength)
        {
            return false;
        }

        int tlengthMax = 256;
        int bytesLeft = input.length - inputIndex;
        if (bytesLeft < tlengthMax)
        {
            tlengthMax = bytesLeft + 1;
        }

        int p = newsptr - tlength;
        int curr = newsptr;
        int savep = p;

        // scan area = 8192 bytes
        // no scanning beyond start of buffer
        // limit to 256 bytes match length
        while (((curr - p) < 8192) && (p >= 0) && (tlength < tlengthMax))
        {
            while (memcmp(curr, p, tlength) && (tlength < tlengthMax))
            {
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

        return tlength >= 2;
    }

    /**
     * Partial Java re-implementation of the memcmp(...) function in C. Given two indices for the input bytes, checks that the given size number of bytes are the same.
     * 
     * @param index1 The first index.
     * @param index2 The second index.
     * @param size The number of bytes to compare.
     * @return Whether the bytes are the same.
     */
    private boolean memcmp(int index1, int index2, int size)
    {
        byte[] range1 = Arrays.copyOfRange(input, index1, index1 + size);
        byte[] range2 = Arrays.copyOfRange(input, index2, index2 + size);
        return Arrays.equals(range1, range2);
    }

    /**
     * Writes a bit to the current flag byte. If there are no more bits left in the current flag byte, creates a new flag byte at the current output index and writes to it.
     * 
     * @param output The output bytes.
     * @param bit The bit to write.
     */
    private void writeBit(byte[] output, int bit)
    {
        if (flagBitIndex == -1)
        {
            flagBitIndex = 7;
            flagIndex = outputIndex;
            outputIndex = flagIndex + 1;
        }
        output[flagIndex] |= bit << flagBitIndex;
        flagBitIndex--;
    }

    /**
     * Compresses between 2 and 5 bytes reachable through a short search. This includes the bits 0 and 0 in the flag byte, which indicates that this byte is compressed and is short.
     * 
     * @param output The output bytes.
     * @param len
     * @param posy
     */
    private void writeBytesShortCompression(byte[] output, int len, int posy)
    {
        int ctr = 2;
        writeBit(output, 0);
        writeBit(output, 0);

        len -= 2;
        while (ctr > 0)
        {
            writeBit(output, (len >> 1) & 0x01);
            len = (len << 1) & 0x02;
            ctr--;
        }

        output[outputIndex++] = (byte) ((~posy + 1) & 0xFF);
    }

    /**
     * Compressed a long length of bytes. This includes the bits 0 and 1 in the flag byte, which indicates that this byte is compressed and is long.
     * 
     * @param output The output bytes.
     * @param len
     * @param posy
     */
    private void writeBytesLongCompression(byte[] output, int len, int posy)
    {
        writeBit(output, 0);
        writeBit(output, 1);

        posy = (~posy + 1) << 3;

        if (len <= 9)
        {
            posy |= ((len - 2) & 0x07);
        }
        // else lower 3 bits are empty...

        output[outputIndex++] = (byte) ((posy >> 8) & 0xFF);
        output[outputIndex++] = (byte) (posy & 0xFF);

        // ... and next byte encodes full length
        if (len > 9)
        {
            output[outputIndex++] = (byte) ((len - 1) & 0xFF);
        }
    }

    /**
     * @param output The output bytes.
     * @param len
     * @param posy
     */
    private void writeCompressedBytes(byte[] output, int len, int posy)
    {
        if (posy > 255 || length > 5)
        {
            writeBytesLongCompression(output, len, posy);
        }
        else
        {
            writeBytesShortCompression(output, len, posy);
        }
    }

    /**
     * Writes an uncompressed byte to the output. This includes the single bit 1 in the flag byte, which indicates that this byte is uncompressed.
     * 
     * @param output The output bytes.
     */
    private void writeUncompressedByte(byte[] output)
    {
        writeBit(output, 1);
        output[outputIndex++] = (byte) (input[inputIndex] & 0xFF);
    }
}
