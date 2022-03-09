package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.fpk.FileNames;
import com.github.nicholasmoser.utils.FileUtils;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.Optional;
import com.github.nicholasmoser.Extractor;
import com.github.nicholasmoser.FPKUnpacker;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.gamecube.GameCubeISO;

/**
 * Extracts a GNT4 ISO into a GNT4Workspace.
 */
public class GNT4Extractor implements Extractor {

  private static final Logger LOGGER = Logger.getLogger(GNT4Extractor.class.getName());

  private final Path iso;

  private final Path extractionPath;

  private final Path compressedPath;

  private boolean extracted;

  private boolean unpacked;

  /**
   * @param iso            The GNT4 ISO to extract.
   * @param extractionPath The path to extract the ISO to.
   */
  public GNT4Extractor(Path iso, Path extractionPath) {
    this.iso = iso;
    this.extractionPath = extractionPath;
    this.compressedPath = extractionPath.resolve(GNT4Files.COMPRESSED_DIRECTORY);
    this.extracted = false;
    this.unpacked = false;
  }

  @Override
  public void extractISO() throws IOException {
    if (!extracted) {
      Files.createDirectories(compressedPath);
      GameCubeISO.exportFiles(iso, compressedPath);
      extracted = true;
    }
  }

  @Override
  public Workspace unpackFPKs() throws IOException {
    if (!extracted) {
      throw new IllegalStateException("Must extract the ISO before you can unpack the FPKs.");
    }
    if (!unpacked) {
      Path compressed = extractionPath.resolve(GNT4Files.COMPRESSED_DIRECTORY);
      Path uncompressed = extractionPath.resolve(GNT4Files.UNCOMPRESSED_DIRECTORY);
      Optional<FileNames> gnt4FileNames = Optional.of(new GNT4FileNames());
      FPKUnpacker unpacker = new FPKUnpacker(compressed, uncompressed, gnt4FileNames, false, true);
      unpacker.unpackDirectory();
      unpacked = true;
    }
    return new GNT4Workspace(extractionPath);
  }

  @Override
  public String getISO() {
    return iso.toString();
  }
}
