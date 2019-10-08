package com.github.nicholasmoser;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FPKTest
{	
	// Test directory
	private static Path testDirectory = Paths.get("src/test/resources");

	private static Path tempInput = testDirectory.resolve("tempInput");

	private static Path tempOutput = testDirectory.resolve("tempOutput");
	
	/**
	 * Create test directories.
	 * 
	 * @throws Exception If any exception occurs.
	 */
	@BeforeEach
	public void setup() throws Exception
	{
		assertTrue(Files.isDirectory(testDirectory.resolve("fpack")),
				"Copy the fpack folder from the GNT4 ISO to src/test/resources");
		Files.createDirectories(tempInput);
		Files.createDirectories(tempOutput);
	}
	
	/**
	 * Deletes test directories.
	 * 
	 * @throws Exception If any exception occurs.
	 */
	@AfterEach
	public void teardown() throws Exception
	{
		recursiveDelete(tempInput);
		recursiveDelete(tempOutput);
	}
	
	/**
	 * Tests compressing an entire FPK file.
	 * 
	 * @throws Exception If any exception occurs.
	 */
	@Test
	public void cmn0000() throws Exception
	{
		String fileName = "fpack\\cmn0000.fpk";
		
		Path file = testDirectory.resolve(fileName);
		assertTrue(Files.isRegularFile(file), "Cannot find " + file);
		Path fileCopy = tempInput.resolve(fileName);
		Files.createDirectories(fileCopy.getParent());
		Files.copy(file, fileCopy, REPLACE_EXISTING);
		byte[] inputBytes = Files.readAllBytes(fileCopy);
		FPKUnpacker.extractFPK(fileCopy);
		Path outputPath = FPKPacker.repackFPK("fpack\\cmn0000.fpk", tempInput.toFile(), tempOutput.toFile());
		byte[] outputBytes = Files.readAllBytes(outputPath);
		if (!Arrays.equals(inputBytes, outputBytes))
		{
			failOnDifference(inputBytes, outputBytes);
		}
	}
	
	/**
	 * Finds the difference in the FPK headers and fails.
	 * 
	 * @param before The byte array before re-compression.
	 * @param after The byte array after re-compression.
	 * @throws Exception If any exception occurs.
	 */
	private void failOnDifference(byte[] before, byte[] after) throws Exception
	{
		try (InputStream beforeIs = new ByteArrayInputStream(before);
				InputStream afterIs = new ByteArrayInputStream(after))
		{
	        int beforeFileCount = FPKUnpacker.readFPKHeader(beforeIs);
	        int afterFileCount = FPKUnpacker.readFPKHeader(afterIs);
	        if (beforeFileCount != afterFileCount)
	        {
	        	fail(String.format("Original FPK has %d files, new FPK has %d files.",
	        			beforeFileCount, afterFileCount));
	        }

	        List<FPKFileHeader> beforeFpkHeaders = new ArrayList<FPKFileHeader>(beforeFileCount);
	        List<FPKFileHeader> afterFpkHeaders = new ArrayList<FPKFileHeader>(afterFileCount);
	        for (int i = 0; i < beforeFileCount; i++)
	        {
	        	beforeFpkHeaders.add(FPKUnpacker.readFPKFileHeader(beforeIs));
	        	afterFpkHeaders.add(FPKUnpacker.readFPKFileHeader(afterIs));
	        }

	        for (int i = 0; i < beforeFileCount; i++)
	        {
	        	FPKFileHeader beforeHeader = beforeFpkHeaders.get(i);
	        	FPKFileHeader afterHeader = afterFpkHeaders.get(i);
	            String beforeFileName = beforeHeader.getFileName();
	            String afterFileName = afterHeader.getFileName();
		        if (!beforeFileName.equals(afterFileName))
		        {
		        	fail(String.format("Original FPK has %s child filename, new FPK has %s child filename.",
		        			beforeFileName, afterFileName));
		        }
	            int beforeCompressedSize = beforeHeader.getCompressedSize();
	            int afterCompressedSize = afterHeader.getCompressedSize();
		        if (beforeCompressedSize != afterCompressedSize)
		        {
		        	fail(String.format("%s compression size changed from %d to %d.",
		        			beforeFileName, beforeCompressedSize, afterCompressedSize));
		        }
	        }
		}
	}
	
	/**
	 * Finds the index of the first different byte between two byte arrays.
	 * Uses the length of the second byte array since that is likely the longer one.
	 * 
	 * @param before The byte array before re-compression.
	 * @param after The byte array after re-compression.
	 * @return The index of the first different byte.
	 */
	private int diffpos(byte[] before, byte[] after)
	{
        for (int i = 0; i < after.length; i++) {
            if (before[i] != after[i]) {
                return i;
            }
        }
	    return 0;
	}
	
	/**
	 * Prints and deletes files recursively in a given directory.
	 * 
	 * @param path The directory to recursively delete files from.
	 * @throws Exception If any exception occurs.
	 */
	private void recursiveDelete(Path path) throws Exception
	{
		Files.walk(path)
	    .sorted(Comparator.reverseOrder())
	    .map(Path::toFile)
	    .peek(System.out::println)
	    .forEach(File::delete);
	}
}
