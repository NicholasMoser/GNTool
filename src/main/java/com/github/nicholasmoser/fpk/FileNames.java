package com.github.nicholasmoser.fpk;

/**
 * File names used to fix truncated file names in compressed fpk files.
 */
public interface FileNames {
  String fix(String fileName);
}
