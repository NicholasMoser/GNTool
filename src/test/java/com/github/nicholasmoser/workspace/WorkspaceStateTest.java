package com.github.nicholasmoser.workspace;

import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.gnt4.GNT4Files;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class WorkspaceStateTest {
  @Test
  public void test() throws Exception {
    GNTFiles gntFiles;
    try (InputStream is = GNT4Files.class.getResourceAsStream("vanilla_with_fpks.bin")) {
      gntFiles = GNTFiles.parseFrom(is);
    }
    Path stateFile = Paths.get("src/test/resources/" + UUID.randomUUID() + ".db");
    WorkspaceState state = WorkspaceState.create(gntFiles, stateFile);
  }
}
