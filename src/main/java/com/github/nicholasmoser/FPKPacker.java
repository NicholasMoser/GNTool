package com.github.nicholasmoser;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.github.nicholasmoser.GNTFileProtos.GNTChildFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.FPKUtils;
import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Packs FPK files. This includes compressing them with the Eighting PRS algorithm and modding the
 * Start.dol with the audio fix.
 */
public class FPKPacker {

  private static final Logger LOGGER = Logger.getLogger(FPKPacker.class.getName());

  private final Path compressedDirectory;

  private final Path uncompressedDirectory;

  private final Workspace workspace;

  private final boolean longPaths;

  private final boolean bigEndian;

  /**
   * Creates a new FPK packer for a workspace.
   *
   * @param workspace The workspace to pack the FPKs for.
   */
  public FPKPacker(Workspace workspace, boolean longPaths, boolean bigEndian) {
    this.workspace = workspace;
    this.compressedDirectory = workspace.getCompressedDirectory();
    this.uncompressedDirectory = workspace.getUncompressedDirectory();
    this.longPaths = longPaths;
    this.bigEndian = bigEndian;
  }

  /**
   * Packs and compresses FPK files. First will prompt the user for an input and output directory.
   * The dol will then be patched with the audio fix. The input directory will be checked for any
   * modifications using the CRC32 hash function. Any files that have been changed will be packed
   * and compressed into their original FPK file. This new FPK file will override the FPK file in
   * the output directory.
   *
   * @param changedFiles The files that have been changed.
   * @param parallel     If the repacking should attempt to be done in parallel.
   * @throws IOException If there is an I/O issue repacking or moving the files.
   */
  public void pack(List<String> changedFiles, boolean parallel) throws IOException {
    Set<GNTFile> changedFPKs = new HashSet<>();
    Set<String> changedNonFPKs = new HashSet<>();

    for (String changedFile : changedFiles) {
      Optional<GNTFile> parent = workspace.getParentFPK(changedFile);
      // If there is no parent, it does not belong to an FPK
      if (parent.isPresent()) {
        changedFPKs.add(parent.get());
      } else {
        changedNonFPKs.add(changedFile);
      }
    }

    for (String changedNonFPK : changedNonFPKs) {
      Path newFile = uncompressedDirectory.resolve(changedNonFPK);
      Path oldFile = compressedDirectory.resolve(changedNonFPK);
      Files.copy(newFile, oldFile, REPLACE_EXISTING);
    }
    if (changedNonFPKs.isEmpty()) {
      LOGGER.info("No non-FPK files were copied.");
    } else {
      LOGGER.info(String.format("The following files were copied: %s", changedNonFPKs));
    }

    LOGGER.info(String.format("%d FPK file(s) need to be packed.", changedFPKs.size()));
    if (parallel) {
      changedFPKs.parallelStream().forEach(fpk -> {
            try {
              LOGGER.info(String.format("Packing %s...", fpk.getFilePath()));
              repackFPK(fpk);
              LOGGER.info(String.format("Packed %s", fpk.getFilePath()));
            } catch (IOException e) {
              String message = String.format("Failed to pack %s", fpk.getFilePath());
              throw new RuntimeException(message, e);
            }
          }
      );
    } else {
      for (GNTFile fpk : changedFPKs) {
        LOGGER.info(String.format("Packing %s...", fpk.getFilePath()));
        repackFPK(fpk);
        LOGGER.info(String.format("Packed %s", fpk.getFilePath()));
      }
    }
    LOGGER.info("FPK files have been packed at " + compressedDirectory);
  }

  /**
   * Repacks the given FPK file. Finds the children of the FPK and individually compresses them from
   * the input directory and packs them into an FPK file at the output directory. If the file
   * already exists in the output directory it will be overridden. The input directory must have the
   * uncompressed child files.
   *
   * @param fpk The FPK GNTFile.
   * @return The path to the repacked FPK file.
   * @throws IOException If there is an issue reading/writing bytes to the file.
   */
  public Path repackFPK(GNTFile fpk) throws IOException {
    List<GNTChildFile> fpkChildren = fpk.getGntChildFileList();
    List<FPKFile> newFPKs = new ArrayList<>(fpkChildren.size());
    for (GNTChildFile child : fpkChildren) {
      byte[] input = Files.readAllBytes(uncompressedDirectory.resolve(child.getFilePath()));
      byte[] output;

      if (child.getCompressed()) {
        PRSCompressor compressor = new PRSCompressor(input);
        output = compressor.compress();
      } else {
        output = input;
      }

      // Set the offset to -1 for now, we cannot figure it out until we have all of
      // the files
      String shiftJisPath = ByteUtils.encodeShiftJis(child.getCompressedPath());
      FPKFileHeader header = new FPKFileHeader(shiftJisPath, output.length, input.length, false);
      newFPKs.add(new FPKFile(header, output));
      LOGGER.info(String.format("%s has been compressed from %d bytes to %d bytes.",
          child.getFilePath(), input.length, output.length));
    }

    int outputSize = 16; // FPK header is 16 bytes so start with that.
    outputSize += newFPKs.size() * 32; // Each FPK file header is 32 bytes
    for (FPKFile file : newFPKs) {
      FPKFileHeader header = file.getHeader();
      header.setOffset(outputSize);
      int compressedSize = header.getCompressedSize();
      int modDifference = compressedSize % 16;
      if (modDifference == 0) {
        outputSize += compressedSize;
      } else {
        // Make sure the offset is divisible by 16
        outputSize += compressedSize + (16 - modDifference);
      }
    }

    // FPK Header
    byte[] fpkBytes = FPKUtils.createFPKHeader(newFPKs.size(), outputSize, bigEndian);
    // File headers
    for (FPKFile file : newFPKs) {
      fpkBytes = Bytes.concat(fpkBytes, file.getHeader().getBytes());
    }
    // File Data
    for (FPKFile file : newFPKs) {
      fpkBytes = Bytes.concat(fpkBytes, file.getData());
    }
    Path outputFPK = compressedDirectory.resolve(fpk.getFilePath());
    if (!Files.isDirectory(outputFPK.getParent())) {
      Files.createDirectories(outputFPK.getParent());
    }
    Files.write(outputFPK, fpkBytes);
    return outputFPK;
  }
}
