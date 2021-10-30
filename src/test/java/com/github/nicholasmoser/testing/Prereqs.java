package com.github.nicholasmoser.testing;

import com.github.nicholasmoser.FPKUnpacker;
import com.github.nicholasmoser.fpk.FileNames;
import com.github.nicholasmoser.gnt4.GNT4FileNames;
import com.github.nicholasmoser.iso.ISOExtractor;
import com.github.nicholasmoser.utils.FileUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * A class for generating and retrieving prerequisites needed for unit and integration tests.
 */
public class Prereqs {

  /**
   * Gets the uncompressed GNT4 directory for testing. The files in this directory should be
   * unmodified. Avoid running tests concurrency on this directory or race conditions may occur
   * relating to the guarantee of files in this directory being unmodified.
   *
   * @return The uncompressed GNT4 directory.
   * @throws IOException If there are any issues decompressing GNT4.
   */
  public static Path getUncompressedGNT4() throws IOException {
    // Validate that the test GNT4 iso exists
    Path gnt4Iso = Paths.get("src/test/gnt/GNT4.iso");
    if (!Files.isRegularFile(gnt4Iso)) {
      String hash = "049525a944c8fa158f752bc71a3441f4";
      String msg = "Unable to find GNT4.iso in src/test/gnt directory. Please make sure that you ";
      throw new IOException(msg + "have added this ISO with an MD5 hash of " + hash);
    }
    // Check if the uncompressed files exist and create the compressed files if not
    Path gnt4Path = Paths.get("src/test/gnt/gnt4");
    Path uncompressed = gnt4Path.resolve("uncompressed");
    if (Files.isDirectory(uncompressed)) {
      return uncompressed;
    }
    Path compressed = gnt4Path.resolve("compressed");
    Files.createDirectories(compressed);
    ISOExtractor extractor = new ISOExtractor(gnt4Iso, compressed);
    extractor.extract();
    // Decompress the files into the uncompressed directory
    Files.createDirectories(uncompressed);
    FileUtils.copyFolder(compressed, uncompressed);
    Optional<FileNames> gnt4FileNames = Optional.of(new GNT4FileNames());
    FPKUnpacker unpacker = new FPKUnpacker(uncompressed, gnt4FileNames, false, true);
    unpacker.unpackDirectory();
    return uncompressed;
  }
}
