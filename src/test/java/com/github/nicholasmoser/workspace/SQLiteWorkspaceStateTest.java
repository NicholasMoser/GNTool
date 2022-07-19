package com.github.nicholasmoser.workspace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.fpk.FPKOptions;
import com.github.nicholasmoser.gnt4.GNT4FileNames;
import com.github.nicholasmoser.testing.Prereqs;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SQLiteWorkspaceStateTest {

  /**
   * Make sure to only perform read operations on this state so that other tests do not fail
   */
  private static WorkspaceState state;

  @BeforeAll
  static void setup() throws Exception {
    Path stateFile = Paths.get("src/test/resources/new" + UUID.randomUUID() + ".db");
    state = SQLiteWorkspaceState.create(stateFile);
    FPKOptions options = new FPKOptions(false, true, new GNT4FileNames());
    Path workspaceDir = Prereqs.getWorkspaceDir();
    state.init(workspaceDir, options);
  }

  @Test
  public void canGetModifiedDtTms() throws Exception {
    Map<String, Long> mapping = state.getFilePathToModifiedDtTm();
    assertEquals(2574, mapping.size());
    long year2000 = LocalDate.parse("2000-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)
        .toEpochMilli();
    long year2500 = LocalDate.parse("2500-01-01").atStartOfDay().toInstant(ZoneOffset.UTC)
        .toEpochMilli();
    for (Entry<String, Long> entry : mapping.entrySet()) {
      assertThat(entry.getKey()).containsAnyOf("files/", "sys/");
      assertThat(entry.getValue()).isGreaterThan(year2000).isLessThan(year2500);
    }
  }

  @Test
  public void passivityWithProtobuf() throws Exception {
    // Create a workspace from a directory and a workspace from an existing workspace protobuf
    // Creating a SQLite workspace state takes around 33 seconds on my HDD, and creating an old
    // protobuf workspace state took around 23 seconds.
    GNTFiles gntFiles;
    try (InputStream is = SQLiteWorkspaceStateTest.class.getResourceAsStream(
        "vanilla_workspace.bin")) {
      if (is == null) {
        fail("vanilla_workspace.bin is missing from test resources");
      }
      gntFiles = GNTFiles.parseFrom(is);
    }
    Path stateFile2 = Paths.get("src/test/resources/passive" + UUID.randomUUID() + ".db");
    Path workspaceDir = Prereqs.getWorkspaceDir();
    SQLiteWorkspaceState existingState = SQLiteWorkspaceState.create(stateFile2);
    existingState.insertGNTFiles(workspaceDir, gntFiles);

    // Assert that the new and old workspaces are the same
    List<String> freshFPKFilePaths = state.getFPKFilePaths();
    List<String> existingFPKFilePaths = existingState.getFPKFilePaths();
    freshFPKFilePaths.sort(Comparator.nullsFirst(Comparator.comparing(String::toString)));
    existingFPKFilePaths.sort(Comparator.nullsFirst(Comparator.comparing(String::toString)));
    assertThat(freshFPKFilePaths).hasSameElementsAs(existingFPKFilePaths);
    List<WorkspaceFile> freshFiles = state.getAllFiles();
    List<WorkspaceFile> existingFiles = existingState.getAllFiles();
    freshFiles.sort(Comparator.nullsFirst(Comparator.comparing(WorkspaceFile::filePath)));
    existingFiles.sort(Comparator.nullsFirst(Comparator.comparing(WorkspaceFile::filePath)));
    assertThat(freshFiles).hasSameElementsAs(existingFiles);
  }
}
