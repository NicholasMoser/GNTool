package com.github.nicholasmoser;

import java.io.File;

/**
 * A workspace for GNTool. Represented by a directory of decompressed game files.
 */
public interface Workspace
{
	/**
	 * @return The root of the decompressed game files.
	 */
	public File getDirectory();
}
