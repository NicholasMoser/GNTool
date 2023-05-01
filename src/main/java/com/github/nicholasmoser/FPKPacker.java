package com.github.nicholasmoser;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.github.nicholasmoser.fpk.FPKFile;
import com.github.nicholasmoser.fpk.FPKFileHeader;
import com.github.nicholasmoser.fpk.FPKOptions;
import com.github.nicholasmoser.fpk.FileNames;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.FPKUtils;
import com.github.nicholasmoser.workspace.WorkspaceFile;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

  private final FileNames fileNames;

  /**
   * Creates a new FPK packer for a workspace.
   *
   * @param workspace The workspace to pack the FPKs for.
   */
  public FPKPacker(Workspace workspace) {
    this.workspace = workspace;
    this.compressedDirectory = workspace.getCompressedDirectory();
    this.uncompressedDirectory = workspace.getUncompressedDirectory();
    FPKOptions options = workspace.getFPKOptions();
    this.longPaths = options.longPaths();
    this.bigEndian = options.bigEndian();
    this.fileNames = options.fileNames();
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
  public void pack(Collection<String> changedFiles, boolean parallel) throws IOException {
    // Get data needed to repack
    Set<String> changedFPKFiles = new HashSet<>();
    Set<String> changedNonFPKFiles = new HashSet<>();
    List<WorkspaceFile> allFiles = workspace.getAllFiles();
    Multimap<String, WorkspaceFile> fpkToFiles = getFPKToFiles(allFiles);

    // Get changed FPK files and non-FPK files
    for (WorkspaceFile file : allFiles) {
      if (changedFiles.contains(file.filePath())) {
        String fpkFilePath = file.fpkFilePath();
        if (fpkFilePath != null) {
          changedFPKFiles.add(fpkFilePath);
        } else {
          changedNonFPKFiles.add(file.filePath());
        }
      }
    }

    // Copy non-FPK files as-is
    for (String changedNonFPK : changedNonFPKFiles) {
      Path newFile = uncompressedDirectory.resolve(changedNonFPK);
      Path oldFile = compressedDirectory.resolve(changedNonFPK);
      Files.createDirectories(oldFile.getParent());
      Files.copy(newFile, oldFile, REPLACE_EXISTING);
    }
    if (changedNonFPKFiles.isEmpty()) {
      LOGGER.info("No non-FPK files were copied.");
    } else {
      LOGGER.info(String.format("The following files were copied: %s", changedNonFPKFiles));
    }

    // Repack FPK files
    LOGGER.info(String.format("%d FPK file(s) need to be packed.", changedFPKFiles.size()));
    if (parallel) {
      changedFPKFiles.parallelStream().forEach(fpk -> {
            try {
              LOGGER.info(String.format("Packing %s...", fpk));
              repackFPK(fpk, fpkToFiles.get(fpk));
              LOGGER.info(String.format("Packed %s", fpk));
            } catch (IOException e) {
              String message = String.format("Failed to pack %s", fpk);
              throw new RuntimeException(message, e);
            }
          }
      );
    } else {
      for (String fpk : changedFPKFiles) {
        LOGGER.info(String.format("Packing %s...", fpk));
        repackFPK(fpk, fpkToFiles.get(fpk));
        LOGGER.info(String.format("Packed %s", fpk));
      }
    }
    LOGGER.info("FPK files have been packed at " + compressedDirectory);
  }

  /**
   * Returns a multimapping of FPK file paths to their file children. The only FPK file paths that
   * will be included are those with children included in the method parameter.
   *
   * @param files The FPK child files.
   * @return A multimapping of FPK file paths to their file children.
   */
  Multimap<String, WorkspaceFile> getFPKToFiles(List<WorkspaceFile> files) {
    Multimap<String, WorkspaceFile> fpktoFiles = ArrayListMultimap.create();
    for (WorkspaceFile file : files) {
      String fpkFilePath = file.fpkFilePath();
      if (fpkFilePath != null) {
        fpktoFiles.put(fpkFilePath, file);
      }
    }
    return fpktoFiles;
  }

  /**
   * Repacks the given FPK file. Finds the children of the FPK and individually compresses them from
   * the input directory and packs them into an FPK file at the output directory. If the file
   * already exists in the output directory it will be overridden. The input directory must have the
   * uncompressed child files.
   *
   * @param fpkPath The FPK file to repack.
   * @param files   The children files of the FPK file.
   * @return The repacked FPK full file path.
   * @throws IOException If there is an I/O issue repacking or moving the files.
   */
  public Path repackFPK(String fpkPath, Collection<WorkspaceFile> files) throws IOException {
    for (WorkspaceFile file : files) {
      if (!fpkPath.equals(file.fpkFilePath())) {
        throw new IllegalArgumentException(
            String.format("fpk parent paths differ: %s %s", fpkPath, file.fpkFilePath()));
      }
    }
    List<FPKFile> fpkChildren = new ArrayList<>(files.size());
    for (WorkspaceFile file : files) {
      byte[] input = Files.readAllBytes(uncompressedDirectory.resolve(file.filePath()));
      byte[] output;

      if (file.compressed()) {
        PRSCompressor compressor = new PRSCompressor(input);
        output = compressor.compress();
      } else {
        output = input;
      }

      // Set the offset to -1 for now, we cannot figure it out until we have all of
      // the files
      String compressedName = fileNames.getCompressedName(file.filePath());
      String shiftJisPath = ByteUtils.encodeShiftJis(compressedName);
      // TODO: Remove GameCube FPK format assumption (short paths, big endian)
      FPKFileHeader header = new FPKFileHeader(shiftJisPath, output.length, input.length, false,
          true);
      fpkChildren.add(new FPKFile(header, output));
      LOGGER.info(String.format("%s has been compressed from %d bytes to %d bytes.",
          file.filePath(), input.length, output.length));
    }

    int outputSize = 16; // FPK header is 16 bytes so start with that.
    outputSize += fpkChildren.size() * 32; // Each FPK file header is 32 bytes
    for (FPKFile file : fpkChildren) {
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
    byte[] fpkBytes = FPKUtils.createFPKHeader(fpkChildren.size(), outputSize, bigEndian);
    // File headers
    for (FPKFile file : fpkChildren) {
      fpkBytes = Bytes.concat(fpkBytes, file.getHeader().getBytes());
    }
    // File Data
    for (FPKFile file : fpkChildren) {
      fpkBytes = Bytes.concat(fpkBytes, file.getData());
    }
    Path outputFPK = compressedDirectory.resolve(fpkPath);
    if (!Files.isDirectory(outputFPK.getParent())) {
      Files.createDirectories(outputFPK.getParent());
    }
    Files.write(outputFPK, fpkBytes);
    return outputFPK;
  }
}
