package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.Randomizer;
import com.github.nicholasmoser.audio.DspAdpcmEncoder;
import com.github.nicholasmoser.audio.FFmpeg;
import com.github.nicholasmoser.audio.MusyXExtract;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.FPKPacker;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.gamecube.GameCubeISO;
import com.github.nicholasmoser.utils.GUIUtils;
import com.github.nicholasmoser.utils.ProtobufUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuController {

  private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());
  private static final String ABOUT_URL = "https://github.com/NicholasMoser/GNTool";
  private static final int DEFAULT_DEMO_TIME_OUT_SECONDS = 10;
  private static final int MAX_DEMO_TIME_OUT_SECONDS = 86400;
  private Workspace workspace;

  @FXML
  private ListView<String> changedFiles;

  @FXML
  private ListView<String> missingFiles;

  @FXML
  private CheckBox audioFixCode;

  @FXML
  private CheckBox skipCutscenesCode;

  @FXML
  private Spinner<Integer> cssInitialSpeed;

  @FXML
  private Spinner<Integer> cssMaxSpeed;

  @FXML
  private Spinner<Integer> demoTimeOut;

  @FXML
  private ComboBox<String> musyxSamFile;

  /**
   * Toggles the code for fixing the audio.
   */
  @FXML
  protected void audioFixCode() {
    try {
      boolean selected = audioFixCode.isSelected();
      Path uncompressedDirectory = workspace.getUncompressedDirectory();
      GNT4Codes codes = GNT4Codes.getInstance();
      if (selected) {
        codes.activateAudioFixCode(uncompressedDirectory);
      } else {
        codes.inactivateAudioFixCode(uncompressedDirectory);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to trigger the audio fix code.", e);
      Message.error("Error Triggering Audio Fix Code", "See the log for more information.");
    }
  }

  /**
   * Toggles the code for skipping cutscenes.
   */
  @FXML
  protected void skipCutscenesCode() {
    try {
      boolean selected = skipCutscenesCode.isSelected();
      Path uncompressedDirectory = workspace.getUncompressedDirectory();
      GNT4Codes codes = GNT4Codes.getInstance();
      if (selected) {
        codes.activateSkipCutscenesCode(uncompressedDirectory);
      } else {
        codes.inactivateSkipCutscenesCode(uncompressedDirectory);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to trigger the skip cutscenes code.", e);
      Message.error("Error Triggering Skip Cutscenes Code", "See the log for more information.");
    }
  }

  @FXML
  protected void setCssInitialSpeed() {
    try {
      Path uncompressedDirectory = workspace.getUncompressedDirectory();
      int value = cssInitialSpeed.getValue();
      GNT4Codes codes = GNT4Codes.getInstance();
      codes.setCssInitialSpeed(uncompressedDirectory, value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update the CSS Initial Speed.", e);
      Message.error("Failed to Update the CSS Initial Speed", "See the log for more information.");
    }
  }

  @FXML
  protected void setCssMaxSpeed() {
    try {
      Path uncompressedDirectory = workspace.getUncompressedDirectory();
      int value = cssMaxSpeed.getValue();
      GNT4Codes codes = GNT4Codes.getInstance();
      codes.setCssMaxSpeed(uncompressedDirectory, value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update the CSS Max Speed.", e);
      Message.error("Failed to Update the CSS Max Speed", "See the log for more information.");
    }
  }

  @FXML
  protected void setDemoTimeOut() {
    try {
      Path uncompressedDirectory = workspace.getUncompressedDirectory();
      int value = demoTimeOut.getValue();
      GNT4Codes codes = GNT4Codes.getInstance();
      codes.setTitleDemoTimeout(uncompressedDirectory, value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update the Title Demo Timeout.", e);
      Message.error("Failed to Update the Title Demo Timeout", "See the log for more information.");
    }
  }

  @FXML
  protected void defaultTimeOut() {
    demoTimeOut.getValueFactory().setValue(DEFAULT_DEMO_TIME_OUT_SECONDS);
    setDemoTimeOut();
  }

  @FXML
  protected void maxTimeOut() {
    demoTimeOut.getValueFactory().setValue(MAX_DEMO_TIME_OUT_SECONDS);
    setDemoTimeOut();
  }

  /**
   * Refreshes the current workspace for any changes having occurred outside of GNTool.
   */
  @FXML
  protected void refresh() {
    asyncRefresh();
  }

  /**
   * Builds the GNT4 ISO for the current workspace.
   */
  @FXML
  protected void build() {
    // Force refresh
    try {
      syncRefresh();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error Refreshing Workspace", e);
      Message.error("Error Refreshing Workspace", "See the log for more information.");
      return;
    }

    // Prevent build if files are missing
    if (!missingFiles.getItems().isEmpty()) {
      Message.error("Missing Files",
          "You cannot build the ISO while files are missing.\nSee the Missing Files tab.");
      return;
    }

    // Warn user if audio fix not selected
    if (!audioFixCode.isSelected()) {
      String message =
          "The audio fix code is not currently selected. It is recommended for it to be enabled. Do you still wish to continue?";
      boolean choice = Message.warnConfirmation("Audio Fix Not Selected", message);
      if (!choice) {
        return;
      }
    }

    // Warn user if no files have changed
    final boolean repack;
    if (changedFiles.getItems().isEmpty()) {
      String message =
          "There are no changed files in your workspace. Do you still wish to build an ISO?";
      boolean choice = Message.warnConfirmation("No Changed Files", message);
      if (choice) {
        repack = false;
      } else {
        return;
      }
    } else {
      repack = true;
    }

    // Get output ISO path
    Optional<Path> isoResponse = Choosers.getOutputISO(GNTool.USER_HOME);
    if (isoResponse.isEmpty()) {
      return;
    }

    // Create task to repack FPKs and build ISO
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        if (repack) {
          updateMessage("Repacking FPKs...");
          FPKPacker fpkPacker = new FPKPacker(workspace);
          fpkPacker.pack(changedFiles.getItems());
        }
        updateMessage("Building ISO...");
        GameCubeISO.importFiles(workspace.getRootDirectory(), isoResponse.get());
        updateProgress(1, 1);
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Building ISO", task);

    task.setOnSucceeded(event -> {
      Message.info("ISO Build Complete", "The new ISO was successfully created.");
      loadingWindow.close();
      saveWorkspaceState();
      asyncRefresh();
    });
    task.setOnFailed(event -> {
      Message.error("ISO Build Failure", "See the log for more information.");
      loadingWindow.close();
      // Don't save workspace state to make debugging easier
    });
    new Thread(task).start();
  }

  /**
   * Quits GNTool.
   */
  @FXML
  protected void quit() {
    System.exit(0);
  }

  /**
   * Opens the Github repository web page for GNTool, which serves as the about page.
   */
  @FXML
  protected void about() {
    try {
      Desktop.getDesktop().browse(new URI(ABOUT_URL));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Opening About Page", e);
      Message.error("Error Opening About Page", "See the log for more information.");
    }
  }

  /**
   * Opens the uncompressed files directory in the workspace using the sytem file browser.
   */
  @FXML
  protected void openDirectory() {
    try {
      Desktop.getDesktop().open(workspace.getUncompressedDirectory().toFile());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Opening Workspace Directory", e);
      Message.error("Error Opening Workspace Directory", "See the log for more information.");
    }
  }

  @FXML
  protected void changedFileSelected(MouseEvent event) {
    EventTarget result = event.getTarget();
    if (event.getButton() == MouseButton.SECONDARY && result instanceof Text) {
      try {
        Text text = (Text) result;
        Path filePath = workspace.getUncompressedDirectory().resolve(text.getText());
        Desktop.getDesktop().open(filePath.toFile());
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error Opening File", e);
        Message.error("Error Opening File", "See the log for more information.");
      }
    }
  }

  @FXML
  public void musyxExtract() {
    try {
      Path uncompressed = workspace.getUncompressedDirectory();
      String samFile = musyxSamFile.getSelectionModel().getSelectedItem();
      Path samFilePath = uncompressed.resolve(samFile);
      String sdiFile = samFilePath.toString().replace(".sam", ".sdi");
      Path sdiFilePath = Paths.get(sdiFile);
      String name = samFilePath.getFileName().toString().replace(".sam", "/");
      Path outputPath = samFilePath.getParent().resolve(name);
      if (!Files.isRegularFile(sdiFilePath)) {
        String message = "Cannot find .sdi file: " + sdiFilePath;
        LOGGER.log(Level.SEVERE, message);
        Message.error("Missing .sdi", message);
        return;
      }
      Files.createDirectories(outputPath);
      MusyXExtract.extract_samples(sdiFilePath, samFilePath, outputPath);
      Desktop.getDesktop().open(outputPath.toFile());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Extracting Audio", e);
      Message.error("Error Extracting Audio", "See log for more information");
    }
  }

  @FXML
  public void musyxExtractAll() {
    try {
      Path uncompressed = workspace.getUncompressedDirectory();
      for (String samFile : GNT4Audio.SOUND_EFFECTS) {
        Path samFilePath = uncompressed.resolve(samFile);
        String sdiFile = samFilePath.toString().replace(".sam", ".sdi");
        Path sdiFilePath = Paths.get(sdiFile);
        String name = samFilePath.getFileName().toString().replace(".sam", "/");
        Path outputPath = samFilePath.getParent().resolve(name);
        if (!Files.isRegularFile(sdiFilePath)) {
          String message = "Skipping... cannot find .sdi file: " + sdiFilePath;
          LOGGER.log(Level.SEVERE, message);
          Message.error("Missing .sdi", message);
          continue;
        }
        Files.createDirectories(outputPath);
        MusyXExtract.extract_samples(sdiFilePath, samFilePath, outputPath);
      }
      Message.info("Extraction Complete", ".dsp files have been created.");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Extracting Audio", e);
      Message.error("Error Extracting Audio", "See log for more information");
    }
  }

  @FXML
  public void musyxImport() {
    try {
      Path uncompressed = workspace.getUncompressedDirectory();
      String samFile = musyxSamFile.getSelectionModel().getSelectedItem();
      Path samFilePath = uncompressed.resolve(samFile);
      String sdiFile = samFilePath.toString().replace(".sam", ".sdi");
      Path sdiFilePath = Paths.get(sdiFile);
      String directory = samFilePath.getFileName().toString().replace(".sam", "/");
      Path inputPath = samFilePath.getParent().resolve(directory);
      if (!Files.isDirectory(inputPath)) {
        String message = samFile + " has not been extracted yet.";
        LOGGER.log(Level.WARNING, message);
        Message.error("Missing Extraction", message);
        return;
      }
      MusyXExtract.pack_samples(inputPath, sdiFilePath, samFilePath);
      Message.info("Import Complete", ".sam and .sdi files have been created.");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Importing Audio", e);
      Message.error("Error Importing Audio", "See log for more information");
    }
  }

  public void musyxImportAll() {
    try {
      Path uncompressed = workspace.getUncompressedDirectory();
      for (String samFile : GNT4Audio.SOUND_EFFECTS) {
        Path samFilePath = uncompressed.resolve(samFile);
        String sdiFile = samFilePath.toString().replace(".sam", ".sdi");
        Path sdiFilePath = Paths.get(sdiFile);
        String directory = samFilePath.getFileName().toString().replace(".sam", "/");
        Path inputPath = samFilePath.getParent().resolve(directory);
        if (!Files.isDirectory(inputPath)) {
          String message = "Skipping... the following file has not been extracted yet: " + samFile;
          LOGGER.log(Level.WARNING, message);
          Message.error("Missing Extraction", message);
          continue;
        }
        MusyXExtract.pack_samples(inputPath, sdiFilePath, samFilePath);
      }
      Message.info("Import Complete", ".sam and .sdi files have been created.");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Importing Audio", e);
      Message.error("Error Importing Audio", "See log for more information");
    }
  }

  @FXML
  public void randomizeSoundEffects() {
    try {
      Path uncompressed = workspace.getUncompressedDirectory();
      String samFile = musyxSamFile.getSelectionModel().getSelectedItem();
      Path samFilePath = uncompressed.resolve(samFile);
      String directory = samFilePath.getFileName().toString().replace(".sam", "/");
      Path inputPath = samFilePath.getParent().resolve(directory);
      if (!Files.isDirectory(inputPath)) {
        LOGGER.log(Level.SEVERE, "Must extract before you can randomize.");
        Message.error("Error", "Must extract before you can randomize.");
        return;
      }
      List<Path> files = Files.list(inputPath)
          .sorted()
          .collect(Collectors.toList());
      files.remove(files.size() - 1);
      Randomizer.randomizeFiles(files);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error running randomizer", e);
      Message.error("Error running randomizer", "See log for more information");
    }
  }

  @FXML
  public void soundEffectReplace() {
    try {
      if (DspAdpcmEncoder.isNotAvailable()) {
        Message.info("Nintendo SDK Required",
            "In order to use this functionality, DSPADPCM.exe must be imported from the Nintendo GameCube SDK.");
        boolean isMoved = DspAdpcmEncoder.moveDspAdpcm();
        if (!isMoved) {
          return;
        }
      }
      File uncompressedDirectory = workspace.getUncompressedDirectory().toFile();
      Optional<Path> optionalInput = Choosers.getAudioFile(uncompressedDirectory);
      if (optionalInput.isPresent()) {
        Optional<Path> optionalOutput = Choosers.getDspAudioFile(uncompressedDirectory);
        if (optionalOutput.isPresent()) {
          Path audioFilePath = optionalInput.get();
          String baseName = System.currentTimeMillis() + "temp";
          String wavName = baseName + ".wav";
          String txtName = baseName + ".txt";
          Path tempWavFilePath = audioFilePath.getParent().resolve(wavName);
          Path tempTxtFilePath = Paths.get(txtName);
          Path output = optionalOutput.get();
          String ffmpegOutput = FFmpeg.run(audioFilePath, tempWavFilePath);
          LOGGER.log(Level.INFO, ffmpegOutput);
          String dspAdpcmOutput = DspAdpcmEncoder.run(tempWavFilePath, output);
          LOGGER.log(Level.INFO, dspAdpcmOutput);
          DspAdpcmEncoder.zeroLoopEndOffset(output);
          Files.deleteIfExists(tempWavFilePath);
          Files.deleteIfExists(tempTxtFilePath);
          Message.info("Sound Replacement Done", "Be sure to import for changes to take effect.");
        }
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Replacing Sound Effect", e);
      Message.error("Error Replacing Sound Effect", "See log for more information");
    }
  }

  /**
   * Initializes with a workspace.
   *
   * @param workspace The workspace to add.
   */
  public void init(Workspace workspace) {
    this.workspace = workspace;
    musyxSamFile.getItems().setAll(GNT4Audio.SOUND_EFFECTS);
    musyxSamFile.getSelectionModel().selectFirst();
    asyncRefresh();
  }

  /**
   * Saves the workspace state. This means that refresh will be cleared of changes.
   */
  private void saveWorkspaceState() {
    try {
      workspace.initState();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to save workspace state.", e);
    }
  }

  /**
   * Refresh the workspace synchronously. Will not create any windows.
   *
   * @throws IOException If an I/O error occurs.
   */
  private void syncRefresh() throws IOException {
    GNTFiles newFiles = ProtobufUtils.createBinary(workspace.getUncompressedDirectory());
    refreshMissingFiles(newFiles);
    refreshChangedFiles(newFiles);
    refreshOptions();
  }

  /**
   * Refresh the workspace asynchronously. Will create a loading window for progress.
   */
  private void asyncRefresh() {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        try {
          updateMessage("Refreshing workspace...");
          syncRefresh();
          updateProgress(1, 1);
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Failed to refresh workspace.", e);
          throw e;
        }
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Refreshing Workspace", task);

    task.setOnSucceeded(event -> loadingWindow.close());
    task.setOnFailed(event -> {
      Message.error("Error Refreshing Workspace", "See the log for more information.");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  /**
   * Refreshes the missing files tab from a set of GNTFiles.
   *
   * @param newFiles The GNTFiles to check against.
   */
  private void refreshMissingFiles(GNTFiles newFiles) {
    Platform.runLater(() -> {
      List<String> missingFilenames = workspace.getMissingFiles(newFiles).stream()
          .map(GNTFile::getFilePath).collect(Collectors.toList());
      missingFiles.getItems().setAll(missingFilenames);
    });
  }

  /**
   * Refreshes the changed files tab from a set of GNTFiles.
   *
   * @param newFiles The GNTFiles to check against.
   */
  private void refreshChangedFiles(GNTFiles newFiles) {
    Platform.runLater(() -> {
      List<String> changedFilenames = workspace.getChangedFiles(newFiles).stream()
          .map(GNTFile::getFilePath).collect(Collectors.toList());
      changedFiles.getItems().setAll(changedFilenames);
    });
  }

  /**
   * Refreshes the list of code options.
   */
  private void refreshOptions() {
    GNT4Codes codes = GNT4Codes.getInstance();
    Path uncompressedDirectory = workspace.getUncompressedDirectory();
    try {
      boolean isActive = codes.isAudioFixCodeActivated(uncompressedDirectory);
      audioFixCode.setSelected(isActive);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting Audio Fix Code.", e);
    }
    try {
      boolean isActive = codes.isSkipCutscenesCodeActivated(uncompressedDirectory);
      skipCutscenesCode.setSelected(isActive);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting Skip Cutscenes Code.", e);
    }
    try {
      int value = codes.getCssInitialSpeed(uncompressedDirectory);
      cssInitialSpeed.getValueFactory().setValue(value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting CSS initial speed.", e);
    }
    try {
      int value = codes.getCssMaxSpeed(uncompressedDirectory);
      cssMaxSpeed.getValueFactory().setValue(value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting CSS max speed.", e);
    }
    try {
      int value = codes.getTitleDemoTimeout(uncompressedDirectory);
      demoTimeOut.getValueFactory().setValue(value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting title demo timeout.", e);
    }
  }
}
