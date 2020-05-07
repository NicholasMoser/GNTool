package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.FPKPacker;
import com.github.nicholasmoser.GNTFileProtos.GNTFile;
import com.github.nicholasmoser.GNTFileProtos.GNTFiles;
import com.github.nicholasmoser.GNTool;
import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.Randomizer;
import com.github.nicholasmoser.Workspace;
import com.github.nicholasmoser.audio.DspAdpcmEncoder;
import com.github.nicholasmoser.audio.DtkMake;
import com.github.nicholasmoser.audio.FFmpeg;
import com.github.nicholasmoser.audio.MusyXExtract;
import com.github.nicholasmoser.gamecube.GameCubeISO;
import com.github.nicholasmoser.gnt4.seq.SeqKage;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.graphics.TXG2TPL;
import com.github.nicholasmoser.graphics.Texture1300;
import com.github.nicholasmoser.utils.GUIUtils;
import com.github.nicholasmoser.utils.ProtobufUtils;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
  private Stage stage;
  private Path uncompressedDirectory;

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

  @FXML
  private ComboBox<String> txg2tplTexture;

  @FXML
  private ComboBox<String> mainMenuCharacter;

  @FXML
  private CheckMenuItem parallelBuild;

  @FXML
  private ComboBox<String> seqs;

  /**
   * Toggles the code for fixing the audio.
   */
  @FXML
  protected void audioFixCode() {
    try {
      boolean selected = audioFixCode.isSelected();
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

  @FXML
  public void changeMainMenuCharacter() {
    try {
      String character = mainMenuCharacter.getSelectionModel().getSelectedItem();
      GNT4Codes codes = GNT4Codes.getInstance();
      codes.setMainMenuCharacter(uncompressedDirectory, character);
      Texture1300.mainCharacterFix(uncompressedDirectory, character);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update Main Menu Character.", e);
      Message.error("Failed to Update Main Menu Character", "See the log for more information.");
    }
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
        try {
          if (repack) {
            updateMessage("Repacking FPKs...");
            FPKPacker fpkPacker = new FPKPacker(workspace);
            fpkPacker.pack(changedFiles.getItems(), parallelBuild.isSelected());
          }
          updateMessage("Building ISO...");
          GameCubeISO.importFiles(workspace.getCompressedDirectory(), isoResponse.get());
          updateProgress(1, 1);
          return null;
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Failed to repack FPKs and build ISO", e);
          throw e;
        }
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
      Desktop.getDesktop().open(uncompressedDirectory.toFile());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Opening Workspace Directory", e);
      Message.error("Error Opening Workspace Directory", "See the log for more information.");
    }
  }

  @FXML
  protected void changedFileSelected(MouseEvent event) {
    EventTarget result = event.getTarget();
    if (event.getButton() == MouseButton.SECONDARY && result instanceof Text) {
      Text text = (Text) result;
      ContextMenu menu = getChangedFileContextMenu(text.getText());
      menu.show(stage, event.getScreenX(), event.getScreenY());
    }
  }

  @FXML
  protected void musyxExtract() {
    try {
      String samFile = musyxSamFile.getSelectionModel().getSelectedItem();
      Path samFilePath = uncompressedDirectory.resolve(samFile);
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
  protected void musyxExtractAll() {
    try {
      for (String samFile : GNT4Audio.SOUND_EFFECTS) {
        Path samFilePath = uncompressedDirectory.resolve(samFile);
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
  protected void musyxImport() {
    try {
      String samFile = musyxSamFile.getSelectionModel().getSelectedItem();
      Path samFilePath = uncompressedDirectory.resolve(samFile);
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

  @FXML
  protected void musyxImportAll() {
    try {
      for (String samFile : GNT4Audio.SOUND_EFFECTS) {
        Path samFilePath = uncompressedDirectory.resolve(samFile);
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
  protected void randomizeSoundEffects() {
    try {
      String samFile = musyxSamFile.getSelectionModel().getSelectedItem();
      Path samFilePath = uncompressedDirectory.resolve(samFile);
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
  protected void soundEffectReplace() {
    try {
      if (DspAdpcmEncoder.isNotAvailable()) {
        Message.info("Nintendo SDK Required",
            "In order to use this functionality, DSPADPCM.exe must be imported from the Nintendo GameCube SDK.");
        boolean isMoved = DspAdpcmEncoder.copyDspAdpcm();
        if (!isMoved) {
          return;
        }
      }
      Optional<Path> optionalInput = Choosers.getAudioFile(GNTool.USER_HOME);
      if (optionalInput.isPresent()) {
        Optional<Path> optionalOutput = Choosers.getDspAudioFile(uncompressedDirectory.toFile());
        if (optionalOutput.isPresent()) {
          Path audioFilePath = optionalInput.get();
          String baseName = System.currentTimeMillis() + "temp";
          String wavName = baseName + ".wav";
          String txtName = baseName + ".txt";
          Path tempWavFilePath = audioFilePath.getParent().resolve(wavName);
          Path tempTxtFilePath = Paths.get(txtName);
          Path output = optionalOutput.get();
          String ffmpegOutput = FFmpeg.prepareSoundEffect(audioFilePath, tempWavFilePath);
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

  @FXML
  protected void randomizeMusic() {
    try {
      List<Path> fullMusicPaths = GNT4Audio.FULL_MUSIC_TO_RANDOMIZE.stream()
          .map(uncompressedDirectory::resolve)
          .collect(Collectors.toList());
      Randomizer.randomizeFiles(fullMusicPaths);
      List<Path> shortMusicPaths = GNT4Audio.SHORT_MUSIC_TO_RANDOMIZE.stream()
          .map(uncompressedDirectory::resolve)
          .collect(Collectors.toList());
      Randomizer.randomizeFiles(shortMusicPaths);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error running randomizer", e);
      Message.error("Error running randomizer", "See log for more information");
    }
  }

  @FXML
  protected void musicReplace() {
    try {
      if (DtkMake.isNotAvailable()) {
        Message.info("Nintendo SDK Required",
            "In order to use this functionality, dtkmake.exe must be imported from the Nintendo GameCube SDK.");
        boolean isMoved = DtkMake.copyDtkMake();
        if (!isMoved) {
          return;
        }
      }
      Optional<Path> optionalInput = Choosers.getAudioFile(GNTool.USER_HOME);
      if (optionalInput.isPresent()) {
        Path bgm = uncompressedDirectory.resolve("files/audio/bgm");
        Optional<Path> optionalOutput = Choosers.getTrkAudioFile(bgm.toFile());
        if (optionalOutput.isPresent()) {
          Path audioFilePath = optionalInput.get();
          String wavName = System.currentTimeMillis() + "temp.wav";
          Path tempWavFilePath = audioFilePath.getParent().resolve(wavName);
          Path output = optionalOutput.get();
          String ffmpegOutput = FFmpeg.prepareMusic(audioFilePath, tempWavFilePath);
          LOGGER.log(Level.INFO, ffmpegOutput);
          DtkMake.fixWavHeader(tempWavFilePath);
          String trkOutput = DtkMake.run(tempWavFilePath, output);
          LOGGER.log(Level.INFO, trkOutput);
          Files.deleteIfExists(tempWavFilePath);
          Message.info("Music Replacement Done", "Music Replacement Done.");
        }
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Replacing Music", e);
      Message.error("Error Replacing Music", "See log for more information");
    }
  }

  @FXML
  protected void txg2tplExtract() {
    try {
      String txgFile = txg2tplTexture.getSelectionModel().getSelectedItem();
      Path txgFilePath = uncompressedDirectory.resolve(txgFile);
      String name = txgFilePath.getFileName().toString().replace(".txg", "/");
      Path outputPath = txgFilePath.getParent().resolve(name);
      if (!Files.isRegularFile(txgFilePath)) {
        String message = "Cannot find .txg file: " + txgFilePath;
        LOGGER.log(Level.SEVERE, message);
        Message.error("Missing .txg", message);
        return;
      }
      Files.createDirectories(outputPath);
      String output = TXG2TPL.unpack(txgFilePath, outputPath);
      LOGGER.log(Level.INFO, output);
      Desktop.getDesktop().open(outputPath.toFile());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Extracting Textures", e);
      Message.error("Error Extracting Textures", "See log for more information");
    }
  }

  @FXML
  protected void txg2tplImport() {
    try {
      String txgFile = txg2tplTexture.getSelectionModel().getSelectedItem();
      Path txgFilePath = uncompressedDirectory.resolve(txgFile);
      String name = txgFilePath.getFileName().toString().replace(".txg", "/");
      Path inputPath = txgFilePath.getParent().resolve(name);
      if (!Files.isDirectory(inputPath)) {
        String message = txgFile + " has not been extracted yet.";
        LOGGER.log(Level.WARNING, message);
        Message.error("Missing Extraction", message);
        return;
      }
      String output = TXG2TPL.pack(inputPath, txgFilePath);
      LOGGER.log(Level.INFO, output);
      Message.info("Import Complete", ".txg file has been created.");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Importing Texture", e);
      Message.error("Error Importing Texture", "See log for more information");
    }
  }

  @FXML
  protected void txg2tplExtractAll() {
    Task<Object> task = new Task<>() {
      @Override
      public Workspace call() throws Exception {
        try {
          int total = GNT4Graphics.TEXTURES.size();
          for (int i = 0; i < total; i++) {
            String texture = GNT4Graphics.TEXTURES.get(i);
            Path txgFilePath = uncompressedDirectory.resolve(texture);
            String name = txgFilePath.getFileName().toString().replace(".txg", "/");
            Path outputPath = txgFilePath.getParent().resolve(name);
            if (!Files.isRegularFile(txgFilePath)) {
              String message = "Skipping... cannot find .txg file: " + txgFilePath;
              LOGGER.log(Level.SEVERE, message);
              continue;
            }
            Files.createDirectories(outputPath);
            TXG2TPL.unpack(txgFilePath, outputPath);
            updateMessage(texture);
            updateProgress(i, total);
          }
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error Extracting Textures", e);
          throw e;
        }
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Extracting All Textures", task);
    task.setOnSucceeded(event -> {
      LOGGER.log(Level.INFO, "Extraction Complete");
      Message.info("Extraction Complete", ".tpl files have been created.");
      loadingWindow.close();
    });
    task.setOnFailed(event -> {
      LOGGER.log(Level.SEVERE, "Error Extracting Textures");
      Message.error("Error Extracting Textures", "See log for more information");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  @FXML
  protected void txg2tplImportAll() {
    Task<Object> task = new Task<>() {
      @Override
      public Workspace call() throws Exception {
        try {
          int total = GNT4Graphics.TEXTURES.size();
          for (int i = 0; i < total; i++) {
            String texture = GNT4Graphics.TEXTURES.get(i);
            Path txgFilePath = uncompressedDirectory.resolve(texture);
            String name = txgFilePath.getFileName().toString().replace(".txg", "/");
            Path inputPath = txgFilePath.getParent().resolve(name);
            if (!Files.isDirectory(inputPath)) {
              String message = txgFilePath + " has not been extracted yet.";
              LOGGER.log(Level.WARNING, message);
              continue;
            }
            TXG2TPL.pack(inputPath, txgFilePath);
            updateMessage(texture);
            updateProgress(i, total);
          }
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error Importing Textures", e);
          throw e;
        }
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Importing All Textures", task);
    task.setOnSucceeded(event -> {
      LOGGER.log(Level.INFO, "Import Complete");
      Message.info("Import Complete", ".txg files have been created.");
      loadingWindow.close();
    });
    task.setOnFailed(event -> {
      LOGGER.log(Level.SEVERE, "Error Importing Textures");
      Message.error("Error Importing Textures", "See log for more information");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  @FXML
  protected void seqKage(MouseEvent mouseEvent) {
    try {
      if (SeqKage.isNotAvailable()) {
        Message.info("SEQKage 3.1 Required",
            "In order to use this functionality, SEQKage.exe 3.1 must be imported.");
        boolean isMoved = SeqKage.copySeqKage();
        if (!isMoved) {
          return;
        }
      }
      String seq = seqs.getSelectionModel().getSelectedItem();
      Path seqPath = uncompressedDirectory.resolve(seq);
      if (!Files.exists(seqPath)) {
        LOGGER.log(Level.SEVERE, "Unable to find " + seqPath);
      }
      String output = SeqKage.run(seqPath);
      LOGGER.log(Level.INFO, output);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Running SEQKage", e);
      Message.error("Error Running SEQKage", "See log for more information");
    }
  }

  /**
   * Initializes with a workspace.
   *
   * @param workspace The workspace to add.
   * @param stage     The stage for the application.
   */
  public void init(Workspace workspace, Stage stage) {
    this.workspace = workspace;
    this.stage = stage;
    this.uncompressedDirectory = workspace.getUncompressedDirectory();
    musyxSamFile.getItems().setAll(GNT4Audio.SOUND_EFFECTS);
    musyxSamFile.getSelectionModel().selectFirst();
    txg2tplTexture.getItems().setAll(GNT4Graphics.TEXTURES);
    txg2tplTexture.getSelectionModel().selectFirst();
    seqs.getItems().setAll(Seqs.ALL);
    seqs.getSelectionModel().selectFirst();
    mainMenuCharacter.getItems().setAll(GNT4Characters.MAIN_MENU_CHARS);
    mainMenuCharacter.getSelectionModel().select(GNT4Characters.SAKURA);
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
    GNTFiles newFiles = workspace.getNewWorkspaceState();
    refreshMissingFiles(newFiles);
    refreshChangedFiles(newFiles);
    refreshOptions();
    refreshMainMenuCharacter();
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
      Collections.sort(missingFiles.getItems());
    });
  }

  /**
   * Refreshes the changed files tab from a set of
   *
   * @param newFiles The GNTFiles to check against.
   */
  private void refreshChangedFiles(GNTFiles newFiles) {
    Platform.runLater(() -> {
      List<String> changedFilenames = workspace.getChangedFiles(newFiles).stream()
          .map(GNTFile::getFilePath).collect(Collectors.toList());
      changedFiles.getItems().setAll(changedFilenames);
      Collections.sort(changedFiles.getItems());
    });
  }

  /**
   * Refreshes the list of code options.
   */
  private void refreshOptions() {
    GNT4Codes codes = GNT4Codes.getInstance();
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

  /**
   * Sets the main menu character ComboBox from the workspace upon initialization.
   */
  private void refreshMainMenuCharacter() {
    Platform.runLater(() -> {
      try {
        GNT4Codes codes = GNT4Codes.getInstance();
        String character = codes.getMainMenuCharacter(uncompressedDirectory);
        mainMenuCharacter.getSelectionModel().select(character);
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error Setting Current Main Menu Character", e);
        Message.error("Error Setting Main Menu Character", "See log for more information");
      }
    });
  }

  /**
   * Returns a context menu for a changed file. The options of this context menu include opening the
   * file, opening the directory where the file is located, and reverting the file.
   *
   * @param filePath The path of the file.
   * @return The changed file context menu.
   */
  private ContextMenu getChangedFileContextMenu(String filePath) {
    ContextMenu contextMenu = new ContextMenu();
    Path fullFilePath = uncompressedDirectory.resolve(filePath);
    MenuItem openFile = new MenuItem("Open File");
    openFile.setOnAction(event -> {
      try {
        Desktop.getDesktop().open(fullFilePath.toFile());
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Error Opening File", e);
      }
    });
    MenuItem openDirectory = new MenuItem("Open Directory");
    openDirectory.setOnAction(event -> {
      try {
        Desktop.getDesktop().open(fullFilePath.getParent().toFile());
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Error Opening Directory", e);
      }
    });
    MenuItem revertChanges = new MenuItem("Revert Changes");
    revertChanges.setOnAction(event -> {
      try {
        workspace.revertFile(filePath);
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Error Reverting File", e);
      }
    });
    contextMenu.getItems().addAll(openFile, openDirectory, revertChanges);
    return contextMenu;
  }
}
