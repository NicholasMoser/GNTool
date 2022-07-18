package com.github.nicholasmoser.workspace;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.is;

import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.fpk.FPKOptions;
import com.github.nicholasmoser.gnt4.GNT4FileNames;
import com.github.nicholasmoser.testing.Prereqs;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class WorkspaceStateTest {

  // Creating a SQLite workspace state takes around 33 seconds on my HDD, and creating an old
  // protobuf workspace state took around 23 seconds.



  @Test
  public void test() throws Exception {
    // Create a workspace from a directory and a workspace from an existing workspace protobuf
    GNTFiles gntFiles;
    try (InputStream is = WorkspaceStateTest.class.getResourceAsStream("vanilla_workspace.bin")) {
      if (is == null) {
        fail("vanilla_workspace.bin is missing from test resources");
      }
      gntFiles = GNTFiles.parseFrom(is);
    }
    Path stateFile = Paths.get("src/test/resources/new" + UUID.randomUUID() + ".db");
    Path stateFile2 = Paths.get("src/test/resources/passive" + UUID.randomUUID() + ".db");
    Path workspaceDir = Prereqs.getWorkspaceDir();
    FPKOptions options = new FPKOptions(false, true, new GNT4FileNames());
    WorkspaceState freshState = SQLiteWorkspaceState.create(stateFile);
    freshState.init(workspaceDir, options);
    SQLiteWorkspaceState existingState = SQLiteWorkspaceState.create(stateFile2);
    existingState.insertGNTFiles(workspaceDir, gntFiles);

    // Assert that the new and old workspaces are the same
    List<String> freshFPKFilePaths = freshState.getFPKFilePaths();
    List<String> existingFPKFilePaths = existingState.getFPKFilePaths();
    freshFPKFilePaths.sort(Comparator.nullsFirst(Comparator.comparing(String::toString)));
    existingFPKFilePaths.sort(Comparator.nullsFirst(Comparator.comparing(String::toString)));
    assertThat(freshFPKFilePaths, is(existingFPKFilePaths));
    List<WorkspaceFile> freshFiles = freshState.getAllFiles();
    List<WorkspaceFile> existingFiles = existingState.getAllFiles();
    freshFiles.sort(Comparator.nullsFirst(Comparator.comparing(WorkspaceFile::filePath)));
    existingFiles.sort(Comparator.nullsFirst(Comparator.comparing(WorkspaceFile::filePath)));
    assertThat(freshFiles, is(existingFiles));
  }
}
