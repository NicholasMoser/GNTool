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
import com.github.nicholasmoser.gecko.GeckoCode;
import com.github.nicholasmoser.gecko.GeckoCodeGroup;
import com.github.nicholasmoser.gecko.GeckoCodeJSON;
import com.github.nicholasmoser.gecko.GeckoReader;
import com.github.nicholasmoser.gecko.GeckoWriter;
import com.github.nicholasmoser.gnt4.dol.DolHijack;
import com.github.nicholasmoser.gnt4.seq.SeqKage;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.graphics.TXG2TPL;
import com.github.nicholasmoser.graphics.Texture1300;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.GUIUtils;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuController {

  private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());
  private static final String ABOUT_URL = "https://github.com/NicholasMoser/GNTool";
  private static final int DEFAULT_DEMO_TIME_OUT_SECONDS = 10;
  private static final int MAX_DEMO_TIME_OUT_SECONDS = 86400;
  private static final String SEE_LOG = "See the log for more information.";
  private static final String DOL = "sys/main.dol";
  private Workspace workspace;
  private Stage stage;
  private Path uncompressedDirectory;
  private Path workspaceDirectory;
  private GNT4Codes codes;
  private List<GeckoCodeGroup> codeGroups;

  @FXML
  private ListView<String> changedFiles;

  @FXML
  private ListView<String> missingFiles;

  @FXML
  private CheckBox audioFixCode;

  @FXML
  private CheckBox skipCutscenesCode;

  @FXML
  private CheckBox playAudioWhilePaused;

  @FXML
  private CheckBox noSlowDownOnKill;

  @FXML
  private CheckBox unlockAll;

  @FXML
  private CheckBox enableWidescreen;

  @FXML
  private CheckBox xDoesNotBreakThrows;

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
  private CheckMenuItem pushToBackOfISO;

  @FXML
  private ComboBox<String> seqs;

  @FXML
  private TextField ztkDamageMultiplier;

  @FXML
  private TextField ukonDamageMultiplier;

  @FXML
  private TextArea geckoCodes;

  @FXML
  private TextField codeName;

  @FXML
  private ListView<String> addedCodes;

  /**
   * Toggles the code for fixing the audio.
   */
  @FXML
  protected void audioFixCode() {
    try {
      boolean selected = audioFixCode.isSelected();
      if (selected) {
        codes.activateCode(GNT4Codes.AUDIO_FIX);
      } else {
        codes.inactivateCode(GNT4Codes.AUDIO_FIX);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Triggering Audio Fix Code", e);
      Message.error("Error Triggering Audio Fix Code", SEE_LOG);
    }
  }

  /**
   * Toggles the code for playing audio while paused.
   */
  @FXML
  protected void playAudioWhilePaused() {
    try {
      boolean selected = playAudioWhilePaused.isSelected();
      if (selected) {
        codes.activateCode(GNT4Codes.PLAY_AUDIO_WHILE_PAUSED);
      } else {
        codes.inactivateCode(GNT4Codes.PLAY_AUDIO_WHILE_PAUSED);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Triggering Play Audio While Paused Code", e);
      Message.error("Error Triggering Play Audio While Paused Code", SEE_LOG);
    }
  }

  /**
   * Toggles the code for no slowdown on kill.
   */
  @FXML
  protected void noSlowDownOnKill() {
    try {
      boolean selected = noSlowDownOnKill.isSelected();
      if (selected) {
        codes.activateCode(GNT4Codes.NO_SLOWDOWN_ON_KILL);
      } else {
        codes.inactivateCode(GNT4Codes.NO_SLOWDOWN_ON_KILL);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Triggering No Slowdown On Kill Code", e);
      Message.error("Error Triggering No Slowdown On Kill Code", SEE_LOG);
    }
  }

  /**
   * Toggles the code for unlocking everything.
   */
  @FXML
  protected void unlockAll() {
    try {
      boolean selected = unlockAll.isSelected();
      if (selected) {
        codes.activateCode(GNT4Codes.UNLOCK_ALL_CODES);
      } else {
        codes.inactivateCode(GNT4Codes.UNLOCK_ALL_CODES);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Triggering Unlock Everything Code", e);
      Message.error("Error Triggering Unlock Everything Code", SEE_LOG);
    }
  }

  @FXML
  protected void enableWidescreen() {
    try {
      boolean selected = enableWidescreen.isSelected();
      if (selected) {
        codes.activateCode(GNT4Codes.WIDESCREEN_CODES);
      } else {
        codes.inactivateCode(GNT4Codes.WIDESCREEN_CODES);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Triggering Widescreen Code", e);
      Message.error("Error Triggering Widescreen Code", SEE_LOG);
    }
  }

  @FXML
  protected void xDoesNotBreakThrows() {
    try {
      boolean selected = xDoesNotBreakThrows.isSelected();
      if (selected) {
        codes.activateCode(GNT4Codes.X_DOES_NOT_BREAK_THROWS);
      } else {
        codes.inactivateCode(GNT4Codes.X_DOES_NOT_BREAK_THROWS);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Triggering X Does Not Break Throws Code", e);
      Message.error("Error Triggering X Does Not Break Throws Code", SEE_LOG);
    }
  }

  /**
   * Toggles the code for skipping cutscenes.
   */
  @FXML
  protected void skipCutscenesCode() {
    try {
      boolean selected = skipCutscenesCode.isSelected();
      if (selected) {
        codes.activateCode(GNT4Codes.SKIP_CUTSCENES);
      } else {
        codes.inactivateCode(GNT4Codes.SKIP_CUTSCENES);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Triggering Skip Cutscenes Code", e);
      Message.error("Error Triggering Skip Cutscenes Code", SEE_LOG);
    }
  }

  public void translate() {

  }

  @FXML
  protected void setCssInitialSpeed() {
    try {
      int value = cssInitialSpeed.getValue();
      codes.setCodeInt(GNT4Codes.INITIAL_SPEEDS_1V1, value);
      codes.setCodeInt(GNT4Codes.INITIAL_SPEEDS_FFA, value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update the CSS Initial Speed", e);
      Message.error("Failed to Update the CSS Initial Speed", SEE_LOG);
    }
  }

  @FXML
  protected void setCssMaxSpeed() {
    try {
      int value = cssMaxSpeed.getValue();
      codes.setCodeInt(GNT4Codes.MAX_SPEEDS_1V1, value);
      codes.setCodeInt(GNT4Codes.MAX_SPEEDS_FFA, value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update the CSS Max Speed", e);
      Message.error("Failed to Update the CSS Max Speed", SEE_LOG);
    }
  }

  @FXML
  protected void setDemoTimeOut() {
    try {
      int frames = secondsToFrames(demoTimeOut.getValue());
      codes.setCodeInt(GNT4Codes.DEMO_TIME_OUT, frames);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update the Title Demo Timeout", e);
      Message.error("Failed to Update the Title Demo Timeout", SEE_LOG);
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
      codes.setMainMenuCharacter(character);
      Texture1300.mainCharacterFix(uncompressedDirectory, character);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update Main Menu Character", e);
      Message.error("Failed to Update Main Menu Character", SEE_LOG);
    }
  }

  @FXML
  protected void applyZTKDamageMultiplier() {
    String multiplier = ztkDamageMultiplier.getText();
    try {
      byte[] bytes = ByteUtils.floatStringToBytes(multiplier);
      codes.setCodeBytes(GNT4Codes.ZTK_DAMAGE_TAKEN_MULTIPLIER, bytes);
    } catch (NumberFormatException e) {
      LOGGER.log(Level.SEVERE, "Failed to Format Number", e);
      Message.error("Invalid Number", multiplier + " is not a valid number.");
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to Update ZTK Damage Multiplier", e);
      Message.error("Failed to Update ZTK Damage Multiplier", SEE_LOG);
    }
  }

  @FXML
  protected void applyUkonDamageMultiplier() {
    String multiplier = ukonDamageMultiplier.getText();
    try {
      byte[] bytes = ByteUtils.floatStringToBytes(multiplier);
      codes.setCodeBytes(GNT4Codes.UKON_DAMAGE_TAKEN_MULTIPLIER, bytes);
    } catch (NumberFormatException e) {
      LOGGER.log(Level.SEVERE, "Failed to format number.", e);
      Message.error("Invalid Number", multiplier + " is not a valid number.");
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to Update Ukon Damage Multiplier", e);
      Message.error("Failed to Update Ukon Damage Multiplier", SEE_LOG);
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
   * Toggles dark mode on and off.
   */
  @FXML
  public void toggleDarkMode() {
    GUIUtils.toggleDarkMode(audioFixCode.getScene());
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
      Message.error("Error Refreshing Workspace", SEE_LOG);
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
          GameCubeISO.importFiles(workspace.getCompressedDirectory(), isoResponse.get(),
              pushToBackOfISO.isSelected());
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
      Message.error("ISO Build Failure", SEE_LOG);
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
      Message.error("Error Opening About Page", SEE_LOG);
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
      Message.error("Error Opening Workspace Directory", SEE_LOG);
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
  protected void seqKage() {
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

  @FXML
  protected void checkCodes() {
    String text = geckoCodes.getText();
    GeckoReader reader = new GeckoReader();
    try {
      List<GeckoCode> codes = reader.parseCodes(text);
      if (DolHijack.checkHijackOverflow(codeGroups, codes)) {
        return;
      }
      if (targetAddressOverlap(codes)) {
        return;
      }
      Message.info("Valid Codes Found", codes.toString());
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.SEVERE, "Error Parsing Codes", e);
      Message.error("Error Parsing Codes", e.getMessage());
    }
  }

  @FXML
  protected void addCodes() {
    String text = geckoCodes.getText();
    GeckoReader reader = new GeckoReader();
    try {
      List<GeckoCode> codes = reader.parseCodes(text);
      if (DolHijack.checkHijackOverflow(codeGroups, codes)) {
        return;
      }
      if (targetAddressOverlap(codes)) {
        return;
      }
      String name = codeName.getText();
      if (!checkNameValid((name))) {
        return;
      }
      long hijackStartAddress = DolHijack.getEndOfHijacking(codeGroups);
      Path dolPath = uncompressedDirectory.resolve(DOL);
      GeckoWriter writer = new GeckoWriter(dolPath);
      GeckoCodeGroup group = writer.writeCodes(codes, name, hijackStartAddress);
      codeGroups.add(group);
      Path codeFile = workspaceDirectory.resolve(GeckoCodeJSON.CODE_FILE);
      GeckoCodeJSON.writeFile(codeGroups, codeFile);
      asyncRefresh();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Parsing Codes", e);
      Message.error("Error Adding Codes", e.getMessage());
    }
  }

  @FXML
  protected void removeCode() {
    String selected = addedCodes.getSelectionModel().getSelectedItem();
    if (selected != null) {
      Optional<GeckoCodeGroup> optionalGroup = codeGroups.stream()
          .filter(group -> selected.equals(group.getName()))
          .findFirst();
      if (optionalGroup.isPresent()) {
        removeCodeGroup(optionalGroup.get());
      } else {
        String message = String.format("Unable to remove code %s, try refreshing?", selected);
        LOGGER.log(Level.SEVERE, message);
        Message.error("Unable to Remove Code", message);
      }
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
    this.workspaceDirectory = workspace.getWorkspaceDirectory();
    this.uncompressedDirectory = workspace.getUncompressedDirectory();
    this.codes = new GNT4Codes(uncompressedDirectory);
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
    // getNewWorkspaceState() is pretty slow and can take 30 seconds or more on a HDD
    // TODO: Look into ways to speed it up
    GNTFiles newFiles = workspace.getNewWorkspaceState();
    refreshMissingFiles(newFiles);
    refreshChangedFiles(newFiles);
    refreshActiveCodes();
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
      Message.error("Error Refreshing Workspace", SEE_LOG);
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
   * Refreshes the changed files tab from a set of GNTFiles.
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
   * Refreshes the active codes from the codes.json file if it exists.
   */
  private void refreshActiveCodes() {
    Platform.runLater(() -> {
      try {
        addedCodes.getItems().clear();
        Path codeFile = workspaceDirectory.resolve(GeckoCodeJSON.CODE_FILE);
        if (Files.isRegularFile(codeFile)) {
          codeGroups = GeckoCodeJSON.parseFile(codeFile);
          for (GeckoCodeGroup codeGroup : codeGroups) {
            addedCodes.getItems().add(codeGroup.getName());
          }
        } else {
          if (DolHijack.handleActiveCodesButNoCodeFile(uncompressedDirectory.resolve(DOL))) {
            // This ISO has injected codes but no associated JSON code file. The previous method
            // call successfully created one, so now let's parse it.
            codeGroups = GeckoCodeJSON.parseFile(codeFile);
            for (GeckoCodeGroup codeGroup : codeGroups) {
              addedCodes.getItems().add(codeGroup.getName());
            }
            String msg = "Codes were found in the dol but no codes.json file exists.\n";
            LOGGER.info(msg + "The following codes were found: " + String.join(", ", addedCodes.getItems()));
          } else {
            // There actually were no codes
            codeGroups = new ArrayList<>();
          }
        }
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error getting list of applied codes.", e);
      }
    });
  }

  /**
   * Refreshes the list of code options.
   */
  private void refreshOptions() {
    try {
      boolean isActive = codes.isCodeActivated(GNT4Codes.AUDIO_FIX);
      audioFixCode.setSelected(isActive);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting Audio Fix Code.", e);
    }
    try {
      boolean isActive = codes.isCodeActivated(GNT4Codes.SKIP_CUTSCENES);
      skipCutscenesCode.setSelected(isActive);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting Skip Cutscenes Code.", e);
    }
    try {
      int value = codes.getCodeInt(GNT4Codes.CSS_INITIAL_SPEED_1V1_1P);
      cssInitialSpeed.getValueFactory().setValue(value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting CSS initial speed.", e);
    }
    try {
      int value = codes.getCodeInt(GNT4Codes.CSS_MAX_SPEED_1V1_1P);
      cssMaxSpeed.getValueFactory().setValue(value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting CSS max speed.", e);
    }
    try {
      int seconds = framesToSeconds(codes.getCodeInt(GNT4Codes.DEMO_TIME_OUT));
      demoTimeOut.getValueFactory().setValue(seconds);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting title demo timeout.", e);
    }
    try {
      boolean isActive = codes.isCodeActivated(GNT4Codes.PLAY_AUDIO_WHILE_PAUSED);
      playAudioWhilePaused.setSelected(isActive);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting Play Audio While Paused Code.", e);
    }
    try {
      boolean isActive = codes.isCodeActivated(GNT4Codes.NO_SLOWDOWN_ON_KILL);
      noSlowDownOnKill.setSelected(isActive);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting No Slowdown On Kill Code.", e);
    }
    try {
      boolean isActive = codes.isCodeActivated(GNT4Codes.UNLOCK_ALL_CODES);
      unlockAll.setSelected(isActive);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting Unlock All Code.", e);
    }
    try {
      byte[] bytes = codes.getCodeBytes(GNT4Codes.ZTK_DAMAGE_TAKEN_MULTIPLIER);
      float value = ByteUtils.bytesToFloat(bytes);
      ztkDamageMultiplier.setText(Float.toString(value));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting ZTK Damage Multiplier.", e);
    }
    try {
      byte[] bytes = codes.getCodeBytes(GNT4Codes.UKON_DAMAGE_TAKEN_MULTIPLIER);
      float value = ByteUtils.bytesToFloat(bytes);
      ukonDamageMultiplier.setText(Float.toString(value));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting Ukon Damage Multiplier.", e);
    }
    try {
      boolean isActive = codes.isCodeActivated(GNT4Codes.WIDESCREEN_CODES);
      enableWidescreen.setSelected(isActive);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting Widescreen Code.", e);
    }
    try {
      boolean isActive = codes.isCodeActivated(GNT4Codes.X_DOES_NOT_BREAK_THROWS);
      xDoesNotBreakThrows.setSelected(isActive);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting X Does Not Break Throws Code.", e);
    }
  }

  /**
   * Sets the main menu character ComboBox from the workspace upon initialization.
   */
  private void refreshMainMenuCharacter() {
    Platform.runLater(() -> {
      try {
        int characterNumber = codes.getCodeInt(GNT4Codes.MAIN_MENU_CHARACTER);
        String character = GNT4Characters.INTERNAL_CHAR_ORDER.inverse().get(characterNumber);
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
        if (DOL.equals(filePath) && dolModded()) {
          return;
        }
        workspace.revertFile(filePath);
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Error Reverting File", e);
      }
    });
    contextMenu.getItems().addAll(openFile, openDirectory, revertChanges);
    return contextMenu;
  }

  /**
   * Converts a number of frames to a number of seconds. GNT4 runs at 60 frames per second,
   * therefore there are 60 frames in a second.
   *
   * @param frames The number of frames to convert.
   * @return The number of seconds.
   */
  private int framesToSeconds(int frames) {
    return frames / 60;
  }

  /**
   * Converts a number of seconds to a number of frames. GNT4 runs at 60 frames per second,
   * therefore there are 60 frames in a second.
   *
   * @param seconds The number of seconds to convert.
   * @return The number of frames.
   */
  private int secondsToFrames(int seconds) {
    return seconds * 60;
  }

  /**
   * Returns whether or not the given Code Name is valid. Logs and displays a message if not.
   *
   * @param name The Code Name.
   * @return If the Code Name is valid.
   */
  private boolean checkNameValid(String name) {
    if (name == null || name.isBlank()) {
      String message = "Code Name required for new codes.";
      LOGGER.log(Level.SEVERE, message);
      Message.error("Code Error", message);
      return false;
    }
    for (GeckoCodeGroup codeGroup : codeGroups) {
      if (name.equals(codeGroup.getName())) {
        String message = "This Code Name already exists.";
        LOGGER.log(Level.SEVERE, message);
        Message.error("Code Error", message);
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if the target addresses of the given codes overlap with any existing codes. Logs and
   * displays a message if so.
   *
   * @param codes The codes to check.
   * @return If any of the target addresses of the codes overlap any of the existing codes.
   */
  private boolean targetAddressOverlap(List<GeckoCode> codes) {
    for (GeckoCode code : codes) {
      long targetAddress = code.getTargetAddress();
      for (GeckoCodeGroup codeGroup : codeGroups) {
        for (GeckoCode existingCode : codeGroup.getCodes()) {
          if (targetAddress == existingCode.getTargetAddress()) {
            String message = "This code overlaps with: " + codeGroup.getName();
            LOGGER.log(Level.SEVERE, message);
            Message.error("Code Error", message);
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Checks if the dol has been modded via code injection. Logs and displays a message if so.
   *
   * @return If the dol is modded via code injection.
   */
  private boolean dolModded() {
    if (!codeGroups.isEmpty()) {
      String message = "There are codes injected in the dol, unable to revert changes.";
      LOGGER.log(Level.SEVERE, message);
      Message.error("Dol Modded Error", message);
      return true;
    }
    return false;
  }

  /**
   * Removes the given GeckoCodeGroup from the dol. This will write the new codes.json file after
   * removal and will refresh.
   *
   * @param group The GeckoCodeGroup to remove.
   */
  private void removeCodeGroup(GeckoCodeGroup group) {
    try {
      Path dolPath = uncompressedDirectory.resolve(DOL);
      GeckoWriter writer = new GeckoWriter(dolPath);
      boolean successful = writer.removeCodes(group);
      if (!successful) {
        String msg = "Some code(s) could not be removed, but others could. Your workspace is "
            + "likely in an invalid state. Please see the log, open an issue on the Github "
            + "repository, and do not touch your workspace any further.";
        Message.error("Unable to Remove Code(s)",
            msg);
      }
      codeGroups.remove(group);
      Path codeFile = workspaceDirectory.resolve(GeckoCodeJSON.CODE_FILE);
      GeckoCodeJSON.writeFile(codeGroups, codeFile);
      asyncRefresh();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to Remove Code", e);
      Message.error("Unable to Remove Code", "Unable to remove code, see log for more details.");
    }
  }
}
