package com.github.nicholasmoser;

import java.io.IOException;

/**
 * Extracts an ISO into a GNT Workspace. You must call extractISO() before unpackFPKs() This
 * separation is to allow a more accurate loading status.
 */
public interface Extractor {

  /**
   * Extracts the ISO to the extraction path.
   *
   * @throws IOException If there is an I/O related exception.
   */
  void extractISO() throws IOException;

  /**
   * Unpacks FPK files at the extraction path and returned a Workspace.
   * 
   * @return The Workspace of decompressed game files.
   * @throws IOException If there is an I/O related exception.
   */
  Workspace unpackFPKs() throws IOException;

  /**
   * @return The path of the ISO this extractor is extracting.
   */
  String getISO();
}
