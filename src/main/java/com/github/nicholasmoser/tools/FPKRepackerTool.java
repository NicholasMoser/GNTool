package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.fpk.FPKFile;
import com.github.nicholasmoser.fpk.FPKFileHeader;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.PRSCompressor;
import com.github.nicholasmoser.utils.FPKUtils;
import com.github.nicholasmoser.utils.GUIUtils;
import com.google.common.primitives.Bytes;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * A tool to repack FPK files.
 */
public class FPKRepackerTool {

  private static final Logger LOGGER = Logger.getLogger(FPKRepackerTool.class.getName());

  private static File currentDirectory = GNTool.USER_HOME;

  /**
   * Repack an FPK file on the GameCube.
   *
   * @throws IOException If there is an I/O related exception.
   */
  public static void repackGamecubeFPK() throws IOException {
    Optional<Path> optionalFPK = Choosers.getInputFPK(GNTool.USER_HOME);
    if (optionalFPK.isEmpty()) {
      return;
    }
    createRepackWindow(optionalFPK.get(), false, true);
  }

  /**
   * Repack an FPK file on the Wii.
   *
   * @throws IOException If there is an I/O related exception.
   */
  public static void repackWiiFPK() throws IOException {
    Optional<Path> optionalFPK = Choosers.getInputFPK(GNTool.USER_HOME);
    if (optionalFPK.isEmpty()) {
      return;
    }
    createRepackWindow(optionalFPK.get(), true, true);
  }

  /**
   * Repack an FPK file on the PS2/PSP.
   *
   * @throws IOException If there is an I/O related exception.
   */
  public static void repackPS2FPK() throws IOException {
    Optional<Path> optionalFPK = Choosers.getInputFPK(GNTool.USER_HOME);
    if (optionalFPK.isEmpty()) {
      return;
    }
    createRepackWindow(optionalFPK.get(), true, false);
  }

  /**
   * Creates an FPK repack window with each FPK file entry.
   *
   * @param fpkPath   The path to the FPK file.
   * @param longPaths If the FPK inner file paths are 32-bytes (instead of 16-bytes).
   * @param bigEndian If the FPK is big-endian (instead of little-endian).
   */
  private static void createRepackWindow(Path fpkPath, boolean longPaths, boolean bigEndian)
      throws IOException {
    currentDirectory = fpkPath.getParent().toFile();
    List<FPKFileHeader> fpkHeaders = getFileHeaders(fpkPath, longPaths, bigEndian);
    int numHeaders = fpkHeaders.size();
    Stage stage = new Stage();
    GUIUtils.setIcons(stage);

    // Create list of each file packed in the FPK to be replaced
    GridPane buttonPane = new GridPane();
    for (int i = 0; i < numHeaders; i++) {
      FPKFileHeader header = fpkHeaders.get(i);
      Text text = new Text(header.getFileName());
      text.setFont(new Font(20));
      text.getStyleClass().add("text-id");
      buttonPane.add(text, 0, i);
      TextField textField = new TextField();
      textField.setPrefWidth(300);
      buttonPane.add(textField, 1, i);
      Button browse = new Button("Browse");
      browse.setOnAction(e -> {
        Optional<Path> optionalFile = Choosers.getFile(currentDirectory);
        if (optionalFile.isPresent()) {
          Path file = optionalFile.get();
          currentDirectory = file.getParent().toFile();
          textField.setText(file.toString());
        }
      });
      buttonPane.add(browse, 2, i);
    }

    // Repack button and accompanying logic
    Button repackButton = new Button("Repack FPK");
    repackButton.setOnAction(e -> {
      List<String> filePaths = new ArrayList<>(numHeaders);
      // Validate file paths are filled out and exist
      for (int i = 0; i < numHeaders; i++) {
        Optional<Node> node = GUIUtils.getNodeFromGridPane(buttonPane, 1, i);

        if (node.isEmpty()) {
          throw new IllegalStateException("Unable to get node from GridPane.");
        }
        TextField textField = (TextField) node.get();
        String text = textField.getText();
        if (text.isEmpty()) {
          Message.info("Missing File", String.format("File missing for file #%d", i));
          return;
        }
        if (!Files.exists(Paths.get(text))) {
          Message.info("Invalid File Path",
              String.format("Invalid file path for file #%d: %s", i, text));
          return;
        }
        filePaths.add(text);
      }
      Optional<Path> outputFPK = Choosers.getOutputFPK(currentDirectory);
      if (outputFPK.isEmpty()) {
        return;
      }
      repackFPK(fpkHeaders, filePaths, outputFPK.get(), longPaths, bigEndian);
    });

    // Save template button and accompanying logic
    Button saveTemplate = new Button("Save Template");
    saveTemplate.setOnAction(e -> {
      Optional<Path> inputTxt = Choosers.getOutputTXT(currentDirectory);
      if (inputTxt.isEmpty()) {
        return;
      }
      try {
        Path txtPath = inputTxt.get();
        List<String> lines = new ArrayList<>(numHeaders);
        for (int i = 0; i < numHeaders; i++) {
          Optional<Node> secondNode = GUIUtils.getNodeFromGridPane(buttonPane, 1, i);
          if (secondNode.isEmpty()) {
            throw new IllegalStateException("Unable to get node from GridPane.");
          }
          TextField textFieldTwo = (TextField) secondNode.get();
          String text = textFieldTwo.getText();
          if (text.isBlank()) {
            Optional<Node> firstNode = GUIUtils.getNodeFromGridPane(buttonPane, 0, i);
            if (firstNode.isEmpty()) {
              throw new IllegalStateException("Unable to get node from GridPane.");
            }
            Text textFieldOne = (Text) firstNode.get();
            text = textFieldOne.getText();
          }
          lines.add(text);
        }
        Files.write(txtPath, lines);
      } catch (Exception ex) {
        LOGGER.log(Level.SEVERE, "Error Reading Template", ex);
        Message.error("Error Reading Template", "See the log for more information.");
      }
    });

    // Load template button and accompanying logic
    Button loadTemplate = new Button("Load Template");
    loadTemplate.setOnAction(e -> {
      Optional<Path> inputTxt = Choosers.getInputTxt(currentDirectory);
      if (inputTxt.isEmpty()) {
        return;
      }
      try {
        Path txtPath = inputTxt.get();
        List<String> lines = Files.readAllLines(txtPath);
        LOGGER.info(String.format("Read %d lines from %s", lines.size(), txtPath));
        for (int i = 0; i < lines.size(); i++) {
          if (i >= numHeaders) {
            String msg = "Template has too many lines, skipping lines after line " + numHeaders;
            LOGGER.info(msg);
            Message.info("Too Many Lines", msg);
            break;
          }
          String line = lines.get(i);
          Optional<Node> node = GUIUtils.getNodeFromGridPane(buttonPane, 1, i);

          if (node.isEmpty()) {
            throw new IllegalStateException("Unable to get node from GridPane.");
          }
          TextField textField = (TextField) node.get();
          textField.setText(line.trim());
        }

      } catch (Exception ex) {
        LOGGER.log(Level.SEVERE, "Error Reading Template", ex);
        Message.error("Error Reading Template", "See the log for more information.");
      }
    });

    // Configure the rest of the stage and scene
    repackButton.setFont(new Font(24));
    saveTemplate.setFont(new Font(24));
    loadTemplate.setFont(new Font(24));
    buttonPane.add(repackButton, 0, numHeaders);
    buttonPane.add(saveTemplate, 1, numHeaders);
    buttonPane.add(loadTemplate, 2, numHeaders);
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setPrefSize(800, 600);
    buttonPane.setVgap(10);
    buttonPane.setHgap(10);
    buttonPane.setPadding(new Insets(12, 12, 12, 12));
    scrollPane.setContent(buttonPane);
    Scene scene = new Scene(scrollPane);
    GUIUtils.initDarkMode(scene);
    stage.setTitle("GNTool");
    stage.setScene(scene);
    stage.centerOnScreen();
    stage.show();
  }

  /**
   * Given a list of existing FPK headers and new files, repacks the files to a target output FPK.
   * Each index of the two provided lists must correlate with the same FPK entry.
   *
   * @param fpkHeaders The list of existing FPK headers.
   * @param filePaths  The list of new file paths for the FPK.
   * @param longPaths If the FPK inner file paths are 32-bytes (instead of 16-bytes).
   * @param bigEndian If the FPK is big-endian (instead of little-endian).
   */
  private static void repackFPK(List<FPKFileHeader> fpkHeaders, List<String> filePaths,
      Path outputFPK, boolean longPaths, boolean bigEndian) {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() {
        try {
          int numHeaders = fpkHeaders.size();
          List<FPKFile> newFPKs = new ArrayList<>(numHeaders);
          for (int i = 0; i < numHeaders; i++) {
            FPKFileHeader header = fpkHeaders.get(i);
            String filePath = filePaths.get(i);
            updateMessage(String.format("Packing %s", header.getFileName()));
            Path path = Paths.get(filePath);
            byte[] input = Files.readAllBytes(path);
            byte[] output;
            if (header.getCompressedSize() != header.getUncompressedSize()) {
              PRSCompressor compressor = new PRSCompressor(input);
              output = compressor.compress();
            } else {
              output = input;
            }
            // Set the offset to -1 for now, we cannot figure it out until we have all of
            // the files
            FPKFileHeader newHeader = new FPKFileHeader(header.getFileName(), output.length,
                input.length, longPaths, bigEndian);
            newFPKs.add(new FPKFile(newHeader, output));
            LOGGER.info(String.format("%s has been compressed from %d bytes to %d bytes.",
                filePath, input.length, output.length));
          }
          updateMessage("Writing FPK...");

          int outputSize = 16; // FPK header is 16 bytes so start with that.
          if (longPaths) {
            outputSize += newFPKs.size() * 48; // Each Wii FPK file header is 48 bytes
          } else {
            outputSize += newFPKs.size() * 32; // Each GameCube FPK file header is 32 bytes
          }
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
          Files.write(outputFPK, fpkBytes);
        } catch (IOException ex) {
          LOGGER.log(Level.SEVERE, "Error", ex);
          throw new RuntimeException(ex);
        }
        updateMessage("Complete");
        updateProgress(1, 1);
        return null;
      }
    };
    double width = longPaths ? 600 : 450;
    double height = 200;
    Stage loadingWindow = GUIUtils.createLoadingWindow("Repacking FPK", task, width, height);
    task.setOnSucceeded(event -> {
      Message.info("FPK Repacked", "FPK repacking complete.");
      loadingWindow.close();
    });
    task.setOnFailed(event -> {
      Message.error("Failed to Repack FPK", "See log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  /**
   * Get the list of file headers from a GameCube FPK at the given path.
   *
   * @param fpkPath   The path to the FPK.
   * @param longPaths If the FPK inner file paths are 32-bytes (instead of 16-bytes).
   * @param bigEndian If the FPK is big-endian (instead of little-endian).
   * @return The FPK file headers.
   * @throws IOException If there is an exception relating to the FPK file input.
   */
  private static List<FPKFileHeader> getFileHeaders(Path fpkPath, boolean longPaths,
      boolean bigEndian) throws IOException {
    try (InputStream is = Files.newInputStream(fpkPath)) {
      int fileCount = FPKUtils.readFPKHeader(is, bigEndian);
      List<FPKFileHeader> fpkHeaders = new ArrayList<>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readFPKFileHeader(is, longPaths, bigEndian));
      }
      return fpkHeaders;
    }
  }
}
