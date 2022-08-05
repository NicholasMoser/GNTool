package com.github.nicholasmoser.fpk;

/**
 * Options for reading and writing FPK files for a specific title. Long paths are 32-bytes long.
 * If they are not long paths they are short paths, which are 16-bytes long. Also includes if the
 * FPK files are big or little endian. File names are to help with compressing and decompressing
 * shortened file paths.
 */
public record FPKOptions(boolean longPaths, boolean bigEndian, FileNames fileNames) {

}
