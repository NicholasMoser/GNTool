package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.seq.SeqKing;
import com.github.nicholasmoser.utils.GUIUtils;
import java.awt.Desktop;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public class SeqDisassemblerTool {

  private static final Logger LOGGER = Logger.getLogger(SeqDisassemblerTool.class.getName());

  private static File currentDirectory = GNTool.USER_HOME;

  /**
   * Disassembles a seq file to html.
   */
  public static void disassembleToHTML() {
    disassembleToHTML(currentDirectory);
  }

  /**
   * Disassembles a seq file to html.
   *
   * @param initialDirectory The initial directory to start at when selecting a seq file.
   */
  public static void disassembleToHTML(File initialDirectory) {
    Optional<Path> optionalSeq = Choosers.getInputSeq(initialDirectory);
    if (optionalSeq.isEmpty()) {
      return;
    }
    Path seqPath = optionalSeq.get();
    currentDirectory = seqPath.getParent().toFile();
    Optional<Path> outputPath;
    outputPath = Choosers.getOutputHTML(currentDirectory);
    if (outputPath.isEmpty()) {
      return;
    }
    runAsync(seqPath, outputPath.get(), true);
  }

  /**
   * Disassembles the given seq file to html.
   *
   * @param initialDirectory The initial directory to start at when selecting the output HTML file.
   * @param seqPath The path to the seq file to disassemble.
   */
  public static void disassembleToHTML(File initialDirectory, Path seqPath) {
    Optional<Path> outputPath = Choosers.getOutputHTML(initialDirectory);
    if (outputPath.isEmpty()) {
      return;
    }
    currentDirectory = seqPath.getParent().toFile();
    runAsync(seqPath, outputPath.get(), true);
  }

  /**
   * Disassembles a seq file to txt.
   */
  public static void disassembleToTXT() {
    disassembleToTXT(currentDirectory);
  }

  /**
   * Disassembles a seq file to txt.
   *
   * @param initialDirectory The initial directory to start at when selecting a seq file.
   */
  public static void disassembleToTXT(File initialDirectory) {
    Optional<Path> optionalSeq = Choosers.getInputSeq(initialDirectory);
    if (optionalSeq.isEmpty()) {
      return;
    }
    Path seqPath = optionalSeq.get();
    currentDirectory = seqPath.getParent().toFile();
    Optional<Path> outputPath;
    outputPath = Choosers.getOutputTXT(currentDirectory);
    if (outputPath.isEmpty()) {
      return;
    }
    runAsync(seqPath, outputPath.get(), false);
  }

  /**
   * Disassembles the given seq file to txt.
   *
   * @param initialDirectory The initial directory to start at when selecting the output TXT file.
   * @param seqPath The path to the seq file to disassemble.
   */
  public static void disassembleToTXT(File initialDirectory, Path seqPath) {
    Optional<Path> outputPath = Choosers.getOutputTXT(initialDirectory);
    if (outputPath.isEmpty()) {
      return;
    }
    currentDirectory = seqPath.getParent().toFile();
    runAsync(seqPath, outputPath.get(), false);
  }

  /**
   * Asynchronously runs the seq disassembler. This will include a loading window that shows the
   * status of the operation.
   *
   * @param seqPath The seq to disassemble.
   * @param outputFile The output file to write to.
   * @param html If the output is an html file, txt otherwise.
   */
  private static void runAsync(Path seqPath, Path outputFile, boolean html) {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() {
        try {
          updateMessage(String.format("Disassembling %s", seqPath.getFileName()));
          if (html) {
            SeqKing.generateHTML(seqPath, outputFile, false);
          } else {
            SeqKing.generateTXT(seqPath, outputFile, false);
          }
          updateMessage("Complete");
          updateProgress(1, 1);
        } catch (Exception e) {
          updateMessage("Failed");
          updateProgress(1, 1);
          LOGGER.log(Level.SEVERE, "Error", e);
          throw new RuntimeException(e);
        }
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Disassembling Seq", task);
    task.setOnSucceeded(event -> {
      tryToOpen(outputFile);
      loadingWindow.close();
    });
    task.setOnFailed(event -> {
      Message.error("Failed to Disassemble Seq", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  /**
   * Asks the user if they want to open the file and attempts to do so if so.
   *
   * @param outputFile The file to open.
   */
  private static void tryToOpen(Path outputFile) {
    String msg = "Would you like to open the file now?";
    boolean yes = Message.infoConfirmation("Seq Successfully Disassembled", msg);
    if (yes) {
      Task<Void> task = new Task<>() {
        @Override
        public Void call() throws Exception {
          Desktop.getDesktop().open(outputFile.toFile());
          return null;
        }
      };
      task.exceptionProperty().addListener((observable,oldValue, e) -> {
        if (e!=null){
          LOGGER.log(Level.SEVERE, "Failed to Open File", e);
          Message.error("Failed to Open File", e.getMessage());
        }
      });
      new Thread(task).start();
    }
  }
}
