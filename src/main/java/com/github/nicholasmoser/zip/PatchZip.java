package com.github.nicholasmoser.zip;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A class used to patch an output directory of files using a zip file.
 */
public class PatchZip {

  private static final Logger LOGGER = Logger.getLogger(PatchZip.class.getName());

  /**
   * Patch an output directory using the given patch zip file. Each file entry in the
   * zip file should correlate to the file in the output directory it should overwrite.
   *
   * @param patchZip The patch zip file.
   * @param outputDir The output directory.
   * @throws IOException If the output directory cannot be patched from the zip file
   */
  public static void patch(Path patchZip, Path outputDir) throws IOException {
    boolean validEntry = false;
    byte[] buffer = new byte[1024];
    try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(patchZip))) {
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
        if (!zipEntry.isDirectory()) {
          Path outputFile = outputDir.resolve(zipEntry.getName());
          if (!Files.isRegularFile(outputFile)) {
            throw new IOException(String.format(
                "Zip entry %s cannot be patched against %s", zipEntry.getName(), outputFile));
          }
          try(OutputStream os = Files.newOutputStream(outputFile)) {
            int len;
            while ((len = zis.read(buffer)) > 0) {
              os.write(buffer, 0, len);
            }
          }
          validEntry = true;
          LOGGER.info("Patched " + outputFile.toAbsolutePath());
        }
        zipEntry = zis.getNextEntry();
      }
      zis.closeEntry();
    }
    if (!validEntry) {
      throw new IOException("No zip entries were found for file: " + patchZip);
    }
  }
}
