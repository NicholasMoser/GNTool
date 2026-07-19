package com.github.nicholasmoser;

import com.github.nicholasmoser.fpk.FPKFileHeader;
import com.github.nicholasmoser.fpk.FPKOptions;
import com.github.nicholasmoser.gnt4.GNT4FileNames;
import com.github.nicholasmoser.testing.Prereqs;
import com.github.nicholasmoser.utils.FPKUtils;
import com.github.nicholasmoser.workspace.SQLiteWorkspaceState;
import com.github.nicholasmoser.workspace.WorkspaceFile;
import com.github.nicholasmoser.workspace.WorkspaceState;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Tests for {@link FPKPacker} and {@link FPKUnpacker}.
 */
public class FPKTest {
  private static Stream<Arguments> files() throws IOException {
    Path stateFile = Paths.get("src/test/gnt/gnt4/state.db");
    WorkspaceState state;
    if (!Files.exists(stateFile)) {
      state = SQLiteWorkspaceState.create(stateFile);
      FPKOptions options = new FPKOptions(false, true, new GNT4FileNames());
      Path workspaceDir = Prereqs.getWorkspaceDir();
      state.init(workspaceDir, options);
      System.out.println("Test database created.");
    } else {
      state = SQLiteWorkspaceState.load(stateFile);
    }
    return state.getAllFiles().stream()
            .filter(f -> f.fpkFilePath() != null)
            .filter(WorkspaceFile::compressed)
            .map(Arguments::of);
  }

  /**
   * Unpack FPKs to a directory and repack them back into FPKs. Compare the results of both actions
   * to the expected outputs. The first time you run most of the unit tests it will generate a
   * compressed and uncompressed folder of files from GNT4 in the test directory. This test will do
   * the same thing but in the temp directory. This test will then compare the new results in the
   * temp directory to the files in the test directory, therefore this is really only useful if NOT
   * running this test for the first time.
   * @throws Exception If any Exceptions occur.
   */
  @ParameterizedTest
  @MethodSource("files")
  public void unpackAndRepack(WorkspaceFile file) throws Exception {
    Path fpk = Prereqs.getCompressedGNT4().resolve(file.fpkFilePath());
    Path uncompressed = Prereqs.getUncompressedGNT4().resolve(file.filePath());
    System.out.println(file);
    byte[] expectedBytes = getExpectedBytes(fpk, uncompressed);
    byte[] uncompressedBytes = Files.readAllBytes(uncompressed);
    PRSCompressor compressor = new PRSCompressor(uncompressedBytes);
    byte[] actualBytes = compressor.compress();
    assertArrayEquals(expectedBytes, actualBytes);
  }

  private byte[] getExpectedBytes(Path fpkPath, Path inFile) throws IOException {
    GNT4FileNames fileNames = new GNT4FileNames();
    int bytesRead = 0;
    try (InputStream is = Files.newInputStream(fpkPath)) {
      int fileCount = FPKUtils.readFPKHeader(is, true);
      bytesRead += 16;

      List<FPKFileHeader> fpkHeaders = new ArrayList<>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readFPKFileHeader(is, false, true));
        bytesRead += 32;
      }

      for (FPKFileHeader header : fpkHeaders) {
        String fileName = header.getFileName();
        fileName = fileNames.fix(fileName);
        int offset = header.getOffset();
        int compressedSize = header.getCompressedSize();
        int uncompressedSize = header.getUncompressedSize();

        // Skip to the next offset if we are not already there
        if (bytesRead < offset) {
          int bytesToMove = offset - bytesRead;
          if (is.skip(bytesToMove) != bytesToMove) {
            String errorMessage = String.format("Failed to skip to binary data of %s", fileName);
            throw new IOException(errorMessage);
          }
          bytesRead += bytesToMove;
        }

        byte[] fileBytes = new byte[compressedSize];
        if (is.read(fileBytes) != compressedSize) {
          String errorMessage = String.format("Failed to read all binary data of %s", fileName);
          throw new IOException(errorMessage);
        }
        bytesRead += compressedSize;

        // Return if correct file
        String fullPath = inFile.toString().replace("\\", "/");
        if (fullPath.contains(fileName)) {
          return fileBytes;
        }
      }
      throw new IOException("Unable to find file " + inFile);
    }
  }
}
