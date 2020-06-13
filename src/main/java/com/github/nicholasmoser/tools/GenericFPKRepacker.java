package com.github.nicholasmoser.tools;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.FPKFile;
import com.github.nicholasmoser.FPKFileHeader;
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
public class GenericFPKRepacker {

  private static final Logger LOGGER = Logger.getLogger(GenericFPKRepacker.class.getName());

  private static File currentDirectory = GNTool.USER_HOME;

  /**
   * Repack an FPK file on the GameCube.
   *
   * @throws IOException If there is an I/O related exception.
   */
  public static void genericFPKRepackGamecube() throws IOException {
    Optional<Path> optionalFPK = Choosers.getInputFPK(GNTool.USER_HOME);
    if (optionalFPK.isEmpty()) {
      return;
    }
    List<FPKFileHeader> fpkHeaders = getGCFileHeaders(optionalFPK.get());
    createRepackWindow(fpkHeaders, false);
  }

  /**
   * Repack an FPK file on the Wii.
   *
   * @throws IOException If there is an I/O related exception.
   */
  public static void genericFPKRepackWii() throws IOException {
    Optional<Path> optionalFPK = Choosers.getInputFPK(GNTool.USER_HOME);
    if (optionalFPK.isEmpty()) {
      return;
    }
    List<FPKFileHeader> fpkHeaders = getWiiFileHeaders(optionalFPK.get());
    createRepackWindow(fpkHeaders, true);
  }

  /**
   * Creates an FPK repack window with each FPK file entry.
   *
   * @param fpkHeaders The list of FPK file entry headers.
   * @param isWii Whether the FPK is Wii or not (GameCube otherwise).
   */
  private static void createRepackWindow(List<FPKFileHeader> fpkHeaders, boolean isWii) {
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
      repackFPK(fpkHeaders, filePaths, outputFPK.get(), isWii);
    });

    // Configure the rest of the stage and scene
    repackButton.setFont(new Font(24));
    buttonPane.add(repackButton, 1, numHeaders);
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
   * @param outputFPK  The output FPK.
   */
  private static void repackFPK(List<FPKFileHeader> fpkHeaders, List<String> filePaths,
      Path outputFPK, boolean isWii) {
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
                input.length, isWii);
            newFPKs.add(new FPKFile(newHeader, output));
            LOGGER.info(String.format("%s has been compressed from %d bytes to %d bytes.",
                filePath, input.length, output.length));
          }
          updateMessage("Writing FPK...");

          int outputSize = 16; // FPK header is 16 bytes so start with that.
          if (isWii) {
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
          byte[] fpkBytes = FPKUtils.createFPKHeader(newFPKs.size(), outputSize);
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
          throw new RuntimeException(ex);
        }
        updateMessage("Complete");
        updateProgress(1, 1);
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Repacking FPK", task);
    task.setOnSucceeded(event -> {
      loadingWindow.close();
      Message.info("FPK Repacked", "FPK repacking complete.");
    });
    task.setOnFailed(event -> {
      loadingWindow.close();
      Message.error("Failed to Repack FPK", "See log for more information.");
    });
    new Thread(task).start();
  }

  /**
   * Get the list of file headers from a GameCube FPK at the given path.
   *
   * @param fpkPath The path to the FPK.
   * @return The FPK file headers.
   * @throws IOException If there is an exception relating to the FPK file input.
   */
  private static List<FPKFileHeader> getGCFileHeaders(Path fpkPath) throws IOException {
    try (InputStream is = Files.newInputStream(fpkPath)) {
      int fileCount = FPKUtils.readFPKHeader(is);
      List<FPKFileHeader> fpkHeaders = new ArrayList<>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readGCFPKFileHeader(is));
      }
      return fpkHeaders;
    }
  }

  /**
   * Get the list of file headers from a Wii FPK at the given path.
   *
   * @param fpkPath The path to the FPK.
   * @return The FPK file headers.
   * @throws IOException If there is an exception relating to the FPK file input.
   */
  private static List<FPKFileHeader> getWiiFileHeaders(Path fpkPath) throws IOException {
    try (InputStream is = Files.newInputStream(fpkPath)) {
      int fileCount = FPKUtils.readFPKHeader(is);
      List<FPKFileHeader> fpkHeaders = new ArrayList<>(fileCount);
      for (int i = 0; i < fileCount; i++) {
        fpkHeaders.add(FPKUtils.readWiiFPKFileHeader(is));
      }
      return fpkHeaders;
    }
  }
}
