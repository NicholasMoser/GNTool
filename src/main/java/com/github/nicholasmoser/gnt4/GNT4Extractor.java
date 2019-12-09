package com.github.nicholasmoser.gnt4;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import com.github.nicholasmoser.Extractor;
import com.github.nicholasmoser.FPKUnpacker;
import com.github.nicholasmoser.gamecube.GameCubeISO;
import com.github.nicholasmoser.Workspace;

/**
 * Extracts a GNT4 ISO into a GNT4Workspace.
 */
public class GNT4Extractor implements Extractor {

  File iso;

  File extractionPath;

  boolean extracted;

  boolean unpacked;

  /**
   * @param iso The GNT4 ISO to extract.
   * @param extractionPath The path to extract the ISO to.
   */
  public GNT4Extractor(File iso, File extractionPath) {
    this.iso = iso;
    this.extractionPath = extractionPath;
    this.extracted = false;
    this.unpacked = false;
  }

  @Override
  public void extractISO() throws IOException {
    if (!extracted) {
      GameCubeISO.exportFiles(iso, extractionPath);
      extracted = true;
    }
  }

  @Override
  public Workspace unpackFPKs() throws IOException {
    if (!extracted) {
      throw new IllegalStateException("Must extract the ISO before you can unpack the FPKs.");
    }
    if (!unpacked) {
      Path root = extractionPath.toPath().resolve("root");
      FPKUnpacker unpacker = new FPKUnpacker(root);
      unpacker.unpack();
      unpacked = true;
    }
    return new GNT4Workspace(extractionPath);
  }

  @Override
  public String getISO() {
    return iso.getAbsolutePath();
  }
}
