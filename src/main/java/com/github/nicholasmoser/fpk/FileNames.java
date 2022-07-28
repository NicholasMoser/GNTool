package com.github.nicholasmoser.fpk;

/**
 * File names used to fix truncated file names in compressed fpk files. Only really an issue for
 * fpk files with 16-byte file paths.
 */
public interface FileNames {
  String fix(String fileName);
  String getCompressedName(String fileName);
}
