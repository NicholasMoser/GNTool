package com.github.nicholasmoser;

import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import com.google.common.base.Verify;

/**
 * Test class for PRS compression and uncompression algorithms.
 */
public class PRSCompressionTest
{
    /**
     * Tests compressing and uncompressing ten zeroes. The ten bytes of zeroes and output of compressing and uncompressing should be the same.
     * 
     * @throws Exception If any exception occurs.
     */
    @Test
    public void testTenZeroes() throws Exception
    {
        byte[] originalBytes = new byte[] {
                        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        int originalSize = originalBytes.length;
        System.out.println(String.format("Original Size: %d", originalSize));

        PRSCompressor compressor = new PRSCompressor(originalBytes);
        byte[] compressedBytes = compressor.compress();
        int compressedSize = compressedBytes.length;
        System.out.println(String.format("Compressed Size: %d", compressedSize));

        PRSUncompressor uncompressor = new PRSUncompressor(compressedBytes, originalSize);
        byte[] outputBytes = uncompressor.uncompress();
        int uncompressedSize = outputBytes.length;
        System.out.println(String.format("Uncompressed Size: %d", uncompressedSize));

        Verify.verify(Arrays.equals(originalBytes, outputBytes), "Data has been lost during compression/uncompression.");
    }

    /**
     * Tests compressing and uncompressing a file with random hex values. The bytes of the original file and output of compressing and uncompressing should be the same.
     * 
     * @throws Exception If any exception occurs.
     */
    @Test
    public void testRandomHex() throws Exception
    {
        byte[] originalBytes = IOUtils.toByteArray(getClass().getResourceAsStream("random_hex.seq"));
        int originalSize = originalBytes.length;
        System.out.println(String.format("Original Size: %d", originalSize));

        PRSCompressor compressor = new PRSCompressor(originalBytes);
        byte[] compressedBytes = compressor.compress();
        int compressedSize = compressedBytes.length;
        System.out.println(String.format("Compressed Size: %d", compressedSize));

        PRSUncompressor uncompressor = new PRSUncompressor(compressedBytes, originalSize);
        byte[] outputBytes = uncompressor.uncompress();
        int uncompressedSize = outputBytes.length;
        System.out.println(String.format("Uncompressed Size: %d", uncompressedSize));

        Verify.verify(Arrays.equals(originalBytes, outputBytes), "Data has been lost during compression/uncompression.");
    }
}
