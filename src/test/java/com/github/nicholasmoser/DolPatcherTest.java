package com.github.nicholasmoser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

/**
 * Tester for the DolPatcher class.
 */
public class DolPatcherTest
{
	/**
	 * Test an already patched dol returns false.
	 * @throws Exception If any exception occurs.
	 */
	@Test
	public void testPatchedDol() throws Exception
	{
		DolPatcher patcher = new DolPatcher(Paths.get(getClass().getResource("Start_patched.dol").toURI()), true);
		assertFalse(patcher.patchAudioOffset());
	}

	/**
	 * Test an unpatched dol returns true.
	 * @throws Exception If any exception occurs.
	 */
	@Test
	public void testUnpatchedDol() throws Exception
	{
		DolPatcher patcher = new DolPatcher(Paths.get(getClass().getResource("Start_unpatched.dol").toURI()), true);
		assertTrue(patcher.patchAudioOffset());
	}

	/**
	 * Test a corrupted dol throws an IOException.
	 * Corrupted in this case means that it finds the expected instructions at 0x8016fc00
	 * but sees neither a patched nor an unpatched instruction at 0x8016fc08.
	 * @throws Exception If any exception occurs.
	 */
	@Test
	public void testCorruptedDol() throws Exception
	{
		DolPatcher patcher = new DolPatcher(Paths.get(getClass().getResource("Start_corrupted.dol").toURI()), true);
		assertThrows(IOException.class, () ->
		{
			patcher.patchAudioOffset();
		});
	}

	/**
	 * Tests that a non-dol file passed in throws an IOException.
	 * @throws Exception If any exception occurs.
	 */
	@Test
	public void testIncorrectFile() throws Exception
	{
		DolPatcher patcher = new DolPatcher(Paths.get(getClass().getResource("random_hex.seq").toURI()), true);
		assertThrows(IOException.class, () ->
		{
			patcher.patchAudioOffset();
		});
	}
}
