package com.github.nicholasmoser.tools;

import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.p;
import static j2html.TagCreator.style;
import static j2html.TagCreator.title;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.WorkspaceView;
import com.github.nicholasmoser.gnt4.GNT4WorkspaceView;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import com.github.nicholasmoser.utils.GUIUtils;
import j2html.Config;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.specialized.PTag;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class WorkspaceDiffTool {

  private static final Logger LOGGER = Logger.getLogger(WorkspaceDiffTool.class.getName());

  private static File currentDirectory = GNTool.USER_HOME;

  public static void diff() {
    Optional<Path> first = Choosers.getInputWorkspaceDirectory(currentDirectory);
    if (first.isEmpty()) {
      return;
    }
    currentDirectory = first.get().getParent().toFile();
    Optional<Path> second = Choosers.getInputWorkspaceDirectory(currentDirectory);
    if (second.isEmpty()) {
      return;
    }
    Optional<Path> output = Choosers.getOutputHTML(second.get().getParent().toFile());
    if (output.isEmpty()) {
      return;
    }
    diff(first.get(), second.get(), output.get());
  }

  public static void diff(Path first) {
    currentDirectory = first.getParent().toFile();
    Optional<Path> second = Choosers.getInputWorkspaceDirectory(currentDirectory);
    if (second.isEmpty()) {
      return;
    }
    Optional<Path> output = Choosers.getOutputHTML(second.get().getParent().toFile());
    if (output.isEmpty()) {
      return;
    }
    diff(first, second.get(), output.get());
  }

  private static void diff(Path first, Path second, Path output) {

    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws IOException {
        try {
          // Get the HTML body
          Config.textEscaper = text -> text;
          Path firstUncompressed = first.resolve("uncompressed");
          Path secondUncompressed = second.resolve("uncompressed");
          PTag p = p();
          Set<Path> visited = new HashSet<>();
          // First look at files only in the first dir or files shared between dirs
          Files.walkFileTree(firstUncompressed, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file,
                BasicFileAttributes attrs)
                throws IOException {
              if (visited.size() % 5 == 0) {
                updateMessage("Diffed " + visited.size() + " files");
              }
              FileVisitResult result = super.visitFile(file, attrs);
              Path relativize = firstUncompressed.relativize(file);
              Path fileInOther = secondUncompressed.resolve(relativize);
              visited.add(relativize);
              if (Files.isRegularFile(fileInOther)) {
                byte[] theseBytes = Files.readAllBytes(file);
                byte[] otherBytes = Files.readAllBytes(fileInOther);
                if (!Arrays.equals(otherBytes, theseBytes)) {
                  String fileName = file.getFileName().toString();
                  p.with(h2(relativize.toString()));
                  if (fileName.endsWith(".seq")) {
                    // seq file differences
                    List<SeqEdit> theseEdits = SeqExt.getEdits(theseBytes);
                    List<SeqEdit> otherEdits = SeqExt.getEdits(otherBytes);
                    for (SeqEdit edit : theseEdits) {
                      if (!otherEdits.contains(edit)) {
                        p.with(p(file + " has edit: " + edit.getName()));
                      }
                    }
                    for (SeqEdit edit : otherEdits) {
                      if (!theseEdits.contains(edit)) {
                        p.with(p(fileInOther + " has edit: " + edit.getName()));
                      }
                    }
                  } {
                    int off = getFirstDifference(theseBytes, otherBytes);
                    p.with(p(String.format("First difference at offset %d (0x%X)", off, off)));
                  }
                  p.with(p( theseBytes.length + " bytes in file " + file));
                  p.with(p(otherBytes.length + " bytes in file " + fileInOther));
                }
              } else {
                p.with(h2("Only in first:  " + file));
              }
              return result;
            }
          });
          // Then look at files only in the second dir
          Files.walkFileTree(secondUncompressed, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file,
                BasicFileAttributes attrs)
                throws IOException {
              if (visited.size() % 5 == 0) {
                updateMessage("Diffed " + visited.size() + " files");
              }
              FileVisitResult result = super.visitFile(file, attrs);
              Path relativize = secondUncompressed.relativize(file);
              Path fileInOther = firstUncompressed.resolve(relativize);
              if (!visited.contains(relativize)) {
                visited.add(relativize);
                if (!Files.isRegularFile(fileInOther)) {
                  p.with(h2("Only in second: " + file));
                }
              }
              visited.add(relativize);
              return result;
            }
          });

          // If no difference, add message for that
          if (p.getNumChildren() == 0) {
            p.with(div("No differences found"));
          }

          // Build and write the HTML
          String html = html(
              getHead(),
              body(
                  h1(first + " vs " + second),
                  p
              )
          ).withLang("en").render();
          Files.writeString(output, html);
          return null;
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error Diffing Workspaces", e);
          throw e;
        }
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Diffing Workspaces", task);
    task.setOnSucceeded(event -> {
      loadingWindow.close();
      LOGGER.info("Workspace diff complete and has been saved to " + output);
      Message.info("Diff Complete", "Workspace diff has been saved to " + output);
      GUIUtils.open(output);
    });
    task.setOnFailed(event -> {
      loadingWindow.close();
      Message.error("Error Diffing Workspaces", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  private static int getFirstDifference(byte[] theseBytes, byte[] otherBytes) {
    if (theseBytes.length < otherBytes.length) {
      for (int i = 0; i < theseBytes.length; i++) {
        if (theseBytes[i] != otherBytes[i]) {
          return i;
        }
      }
    } else {
      for (int i = 0; i < otherBytes.length; i++) {
        if (theseBytes[i] != otherBytes[i]) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * @return The HTML head of the document.
   */
  private static ContainerTag getHead() {
    String css = getCSS();
    return head(
        title("SEQ Report"),
        style(css)
    );
  }

  /**
   * @return The CSS for the entire document.
   */
  private static String getCSS() {
    return """
        body {
          background-color: #1E1E1E;
          color: #D4D4D4;
          font-family: Lucida Console;
          padding-left: 10%;
        }
        h1 {
          text-align: center;
          padding-left: 0;
        }
        """;
  }
}
