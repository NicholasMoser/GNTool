package com.github.nicholasmoser.gnt4;

import com.github.nicholasmoser.Choosers;
import com.github.nicholasmoser.FPKPacker;
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
import com.github.nicholasmoser.gnt4.chr.KabutoScalingFix;
import com.github.nicholasmoser.gnt4.chr.KisamePhantomSwordFix;
import com.github.nicholasmoser.gnt4.chr.ZabuzaPhantomSwordFix;
import com.github.nicholasmoser.gnt4.cpu.CPUFlags;
import com.github.nicholasmoser.gnt4.dol.CodeCaves.CodeCave;
import com.github.nicholasmoser.gnt4.dol.DolDefragger;
import com.github.nicholasmoser.gnt4.dol.DolHijack;
import com.github.nicholasmoser.gnt4.seq.Dupe4pCharsPatch;
import com.github.nicholasmoser.gnt4.seq.SeqKage;
import com.github.nicholasmoser.gnt4.seq.Seqs;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEdit;
import com.github.nicholasmoser.gnt4.seq.ext.SeqExt;
import com.github.nicholasmoser.gnt4.trans.TranslationState;
import com.github.nicholasmoser.gnt4.trans.Translator;
import com.github.nicholasmoser.gnt4.ui.ChrOrder;
import com.github.nicholasmoser.gnt4.ui.ChrOrderSave;
import com.github.nicholasmoser.gnt4.ui.CostumeController;
import com.github.nicholasmoser.gnt4.ui.EyeController;
import com.github.nicholasmoser.gnt4.ui.OrderController;
import com.github.nicholasmoser.gnt4.ui.StageOrder;
import com.github.nicholasmoser.gnt4.ui.StageOrderSave;
import com.github.nicholasmoser.graphics.TXG2TPL;
import com.github.nicholasmoser.graphics.Texture1300;
import com.github.nicholasmoser.tools.DolphinSeqListenerTool;
import com.github.nicholasmoser.tools.GNTAEditorTool;
import com.github.nicholasmoser.tools.MOTRepackerTool;
import com.github.nicholasmoser.tools.MOTUnpackerTool;
import com.github.nicholasmoser.tools.SeqDisassemblerTool;
import com.github.nicholasmoser.tools.SeqEditorTool;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.GUIUtils;
import com.github.nicholasmoser.workspace.WorkspaceFile;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
  private static final String ABOUT_URL = "https://github.com/NicholasMoser/GNTool#gntool";
  private static final int DEFAULT_DEMO_TIME_OUT_SECONDS = 10;
  private static final int MAX_DEMO_TIME_OUT_SECONDS = 86400;
  private static final int DEFAULT_CSS_MODEL_LOAD_FRAMES = 60;
  private static final String DOL = "sys/main.dol";
  private Workspace workspace;
  private Stage stage;
  private Path compressedDirectory;
  private Path uncompressedDirectory;
  private Path lastCompressedSubdirectory;
  private Path lastUncompressedSubdirectory;
  private Path workspaceDirectory;
  private Path uncompressedFiles;
  private Path dolPath;
  private GNT4Codes codes;
  private List<GeckoCodeGroup> codeGroups;
  public ListView<String> changedFiles;
  public ListView<String> missingFiles;
  public CheckBox audioFixCode;
  public CheckBox skipCutscenesCode;
  public CheckBox playAudioWhilePaused;
  public CheckBox noSlowDownOnKill;
  public CheckBox unlockAll;
  public CheckBox enableWidescreen;
  public CheckBox xDoesNotBreakThrows;
  public Spinner<Integer> cssInitialSpeed;
  public Spinner<Integer> cssMaxSpeed;
  public Spinner<Integer> demoTimeOut;
  public Spinner<Integer> cssModelLoad;
  public ComboBox<String> musyxSamFile;
  public ComboBox<String> txg2tplTexture;
  public ComboBox<String> mainMenuCharacter;
  public CheckMenuItem parallelBuild;
  public CheckMenuItem pushToBackOfISO;
  public ComboBox<String> selectedSeq;
  public TextField ztkDamageMultiplier;
  public TextField ukonDamageMultiplier;
  public TextArea geckoCodes;
  public TextField codeName;
  public ListView<String> addedCodes;
  public Button validateCodes;
  public Button addCodes;
  public Button removeCode;
  public ComboBox<String> recordingFlag;
  public ComboBox<String> recordingCounterFlag;

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
      Message.error("Error Triggering Audio Fix Code", e.getMessage());
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
      Message.error("Error Triggering Play Audio While Paused Code", e.getMessage());
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
      Message.error("Error Triggering No Slowdown On Kill Code", e.getMessage());
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
      Message.error("Error Triggering Unlock Everything Code", e.getMessage());
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
      Message.error("Error Triggering Widescreen Code", e.getMessage());
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
      Message.error("Error Triggering X Does Not Break Throws Code", e.getMessage());
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
      Message.error("Error Triggering Skip Cutscenes Code", e.getMessage());
    }
  }

  @FXML
  public void translate() {
    try {
      TranslationState state = Translator.getTranslationState(uncompressedDirectory);
      if (state == TranslationState.ENGLISH) {
        Message.info("Game Already in English",
            "Unable to translate, the game is already in English.");
        return;
      } else if (state == TranslationState.UNKNOWN) {
        String message = "Unable to determine translation state. Attempting to translate may cause issues. Are you sure you wish to continue?";
        if (!Message.warnConfirmation("Unknown Translation State", message)) {
          return;
        }
      }
      Translator.translate(uncompressedDirectory);
      Message.info("Translation Complete", "Translation to English completed.");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to translate", e);
      Message.error("Failed to translate", e.getMessage());
    }
  }

  @FXML
  public void allow4pDupeChrs() {
    try {
      Dupe4pCharsPatch.apply(uncompressedDirectory);
      Message.info("Patch Applied", "Duplicate characters are now allowed in 4-player mode.");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Allow Duplicate Characters in 4-Player Mode", e);
      Message.error("Failed to apply patch.", e.getMessage());
    }
  }

  @FXML
  protected void setCssInitialSpeed() {
    try {
      int value = cssInitialSpeed.getValue();
      codes.setCodeInt(GNT4Codes.INITIAL_SPEEDS_1V1, value);
      codes.setCodeInt(GNT4Codes.INITIAL_SPEEDS_FFA, value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update the CSS Initial Speed", e);
      Message.error("Failed to Update the CSS Initial Speed", e.getMessage());
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
      Message.error("Failed to Update the CSS Max Speed", e.getMessage());
    }
  }

  @FXML
  protected void defaultCSSInitialSpeed() {
    cssInitialSpeed.getValueFactory().setValue(12);
  }

  @FXML
  protected void maxCSSInitialSpeed() {
    cssInitialSpeed.getValueFactory().setValue(15);
  }

  @FXML
  protected void defaultCSSMaxSpeed() {
    cssMaxSpeed.getValueFactory().setValue(8);
  }

  @FXML
  protected void maxCSSMaxSpeed() {
    cssMaxSpeed.getValueFactory().setValue(15);
  }

  @FXML
  protected void setDemoTimeOut() {
    try {
      int frames = secondsToFrames(demoTimeOut.getValue());
      codes.setCodeInt(GNT4Codes.DEMO_TIME_OUT, frames);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update the Title Demo Timeout", e);
      Message.error("Failed to Update the Title Demo Timeout", e.getMessage());
    }
  }

  @FXML
  protected void setCssModelLoad() {
    try {
      int frames = cssModelLoad.getValue();
      codes.setCodeInt(GNT4Codes.CSS_LOAD_CHR_MODELS, frames);
      codes.setCodeInt(GNT4Codes.CSS_FFA_LOAD_CHR_MODELS, frames);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update the Frames Until CSS Model Load", e);
      Message.error("Failed to Update the Frames Until CSS Model Load", e.getMessage());
    }
  }

  @FXML
  public void reorderCharacters() {
    for (GeckoCodeGroup group : codeGroups) {
      if ("Add Random Select and Reorder CSS [Nick]".equals(group.getName()) ||
          "Add Random Select to Character Select Screen [Nick]".equals(group.getName()) ||
          "Add Random Select to Character Select Screen v2 [Nick]".equals(group.getName())) {
        String msg = """
            The random select Gecko code defines the specific CSS index to put random select,
            which cannot be changed without writing a new Gecko code.
            Therefore, this Gecko code must be removed before you are allowed to change the CSS character order.
            Confirm removal of the random select Gecko code?
            """;
        boolean ok = Message.warnConfirmation("Conflicting Code", msg);
        if (!ok) {
          return;
        }
        removeCodeGroup(group);
        defragCodeGroups(codeGroups);
      }
    }
    try {
      FXMLLoader loader = new FXMLLoader(OrderController.class.getResource("order.fxml"));
      Scene scene = new Scene(loader.load());
      GUIUtils.initDarkMode(scene);
      OrderController orderController = loader.getController();
      Stage stage = new Stage();
      GUIUtils.setIcons(stage);
      orderController.init(ChrOrder.getCurrentChrOrder(dolPath), new ChrOrderSave(dolPath));
      stage.setScene(scene);
      stage.setTitle("Reorder Characters");
      stage.centerOnScreen();
      stage.show();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Reorder Characters", e);
      Message.error("Failed to Reorder Characters", e.getMessage());
    }
  }

  @FXML
  public void reorderStages() {
    try {
      FXMLLoader loader = new FXMLLoader(OrderController.class.getResource("order.fxml"));
      Scene scene = new Scene(loader.load());
      GUIUtils.initDarkMode(scene);
      OrderController orderController = loader.getController();
      Stage stage = new Stage();
      GUIUtils.setIcons(stage);
      orderController.init(StageOrder.getCurrentStageOrder(uncompressedDirectory),
          new StageOrderSave(uncompressedDirectory));
      stage.setScene(scene);
      stage.setTitle("Reorder Stages");
      stage.centerOnScreen();
      stage.show();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Reorder Stages", e);
      Message.error("Failed to Reorder Stages", e.getMessage());
    }
  }

  @FXML
  public void modifyCostumes() {
    try {
      FXMLLoader loader = new FXMLLoader(OrderController.class.getResource("costumes.fxml"));
      Scene scene = new Scene(loader.load());
      GUIUtils.initDarkMode(scene);
      CostumeController costumeController = loader.getController();
      Stage stage = new Stage();
      GUIUtils.setIcons(stage);
      costumeController.init(uncompressedDirectory);
      stage.setScene(scene);
      stage.setTitle("Modify Costumes");
      stage.centerOnScreen();
      stage.show();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Modify Costumes", e);
      Message.error("Failed to Modify Costumes", e.getMessage());
    }
  }

  @FXML
  public void modifyEyes() {
    try {
      FXMLLoader loader = new FXMLLoader(OrderController.class.getResource("eyes.fxml"));
      Scene scene = new Scene(loader.load());
      GUIUtils.initDarkMode(scene);
      EyeController eyeController = loader.getController();
      Stage stage = new Stage();
      GUIUtils.setIcons(stage);
      eyeController.init(uncompressedDirectory);
      stage.setScene(scene);
      stage.setTitle("Modify Eyes");
      stage.centerOnScreen();
      stage.show();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Modify Eyes", e);
      Message.error("Failed to Modify Eyes", e.getMessage());
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
  public void defaultCSSModelLoad() {
    cssModelLoad.getValueFactory().setValue(DEFAULT_CSS_MODEL_LOAD_FRAMES);
    setCssModelLoad();
  }

  @FXML
  public void maxCSSModelLoad() {
    cssModelLoad.getValueFactory().setValue(GNT4Codes.CSS_LOAD_CHR_MODELS_MAX);
    setCssModelLoad();
  }

  @FXML
  public void changeMainMenuCharacter() {
    try {
      String character = mainMenuCharacter.getSelectionModel().getSelectedItem();
      codes.setMainMenuCharacter(character);
      Texture1300.mainCharacterFix(uncompressedDirectory, character);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Update Main Menu Character", e);
      Message.error("Failed to Update Main Menu Character", e.getMessage());
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
      Message.error("Failed to Update ZTK Damage Multiplier", e.getMessage());
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
      Message.error("Failed to Update Ukon Damage Multiplier", e.getMessage());
    }
  }

  @FXML
  protected void applyKabutoScalingFix() {
    try {
      Path seqPath = uncompressedDirectory.resolve(Seqs.KAB_0000);
      if (KabutoScalingFix.isUsingOldFix(seqPath)) {
        String message = "An old version of this fix has already been applied to this character. ";
        message += "The older version directly modified the file bytes, whereas the new version ";
        message += "of this code adds a seq edit using the seq extension section.\n";
        message += "This code is unable to be reversed. Please get a clean Kabuto 0000.seq and ";
        message += "try again.";
        Message.error("Already Using Old Fix", message);
        return;
      } else if (KabutoScalingFix.isUsingNewFix(seqPath)) {
        Message.info("Fix Already Applied", "This fix has already been applied.");
        return;
      }
      SeqEdit seqEdit = KabutoScalingFix.getSeqEdit(seqPath);
      SeqExt.addEdit(seqEdit, seqPath);
      String header = "Kabuto Scaling Fix Applied";
      String message = "The Kabuto Scaling Fix has been applied to Kabuto's 0000.seq file.";
      Message.info(header, message);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Apply Kabuto Scaling Fix", e);
      Message.error("Failed to Apply Kabuto Scaling Fix", e.getMessage());
    }
  }

  @FXML
  protected void applyKisamePhantomSwordFix() {
    try {
      Path seqPath = uncompressedDirectory.resolve(Seqs.KIS_0000);
      if (KisamePhantomSwordFix.isUsingOldFix(seqPath)) {
        String message = "An old version of this fix has already been applied to this character. ";
        message += "The older version directly modified the file bytes, whereas the new version ";
        message += "of this code adds a seq edit using the seq extension section.\n";
        message += "Do you wish to convert the old code to the new code?";
        boolean confirm = Message.warnConfirmation("Already Using Old Fix", message);
        if (!confirm) {
          return;
        }
        KisamePhantomSwordFix.removeOldFix(seqPath);
      } else if (KisamePhantomSwordFix.isUsingNewFix(seqPath)) {
        Message.info("Fix Already Applied", "This fix has already been applied.");
        return;
      }
      SeqEdit seqEdit = KisamePhantomSwordFix.getSeqEdit(seqPath);
      SeqExt.addEdit(seqEdit, seqPath);
      String header = "Kisame Phantom Sword Fix Applied";
      String message = "The Kisame Phantom Sword Fix has been applied to Kisame's 0000.seq file.";
      Message.info(header, message);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Apply Kisame Phantom Sword Fix", e);
      Message.error("Failed to Apply Kisame Phantom Sword Fix", e.getMessage());
    }
  }

  @FXML
  protected void applyZabuzaPhantomSwordFix() {
    try {
      Path seqPath = uncompressedDirectory.resolve(Seqs.ZAB_0000);
      if (ZabuzaPhantomSwordFix.isUsingOldFix(seqPath)) {
        String message = "An old version of this fix has already been applied to this character. ";
        message += "The older version directly modified the file bytes, whereas the new version ";
        message += "of this code adds a seq edit using the seq extension section.\n";
        message += "Do you wish to convert the old code to the new code?";
        boolean confirm = Message.warnConfirmation("Already Using Old Fix", message);
        if (!confirm) {
          return;
        }
        ZabuzaPhantomSwordFix.removeOldFix(seqPath);
      } else if (ZabuzaPhantomSwordFix.isUsingNewFix(seqPath)) {
        Message.info("Fix Already Applied", "This fix has already been applied.");
        return;
      }
      SeqEdit seqEdit = ZabuzaPhantomSwordFix.getSeqEdit(seqPath);
      SeqExt.addEdit(seqEdit, seqPath);
      String header = "Zabuza Phantom Sword Fix Applied";
      String message = "The Zabuza Phantom Sword Fix has been applied to Zabuza's 0000.seq file.";
      Message.info(header, message);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Apply Zabuza Phantom Sword Fix", e);
      Message.error("Failed to Apply Zabuza Phantom Sword Fix", e.getMessage());
    }
  }

  @FXML
  public void fixRecordingTextSize() {
    try {
      CPUFlags.fixRecordingTextSize(dolPath);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Fixing Recording Text Size", e);
      Message.error("Error Fixing Recording Text Size", e.getMessage());
    }
  }

  @FXML
  public void setRecordingFlag() {
    try {
      String flag = recordingFlag.getValue();
      String otherFlag = recordingCounterFlag.getValue();
      if (flag.equals(otherFlag)) {
        throw new IOException("Cannot modify CPU flag due to duplicate value.");
      }
      int index = CPUFlags.getRecordingCPUFlag(uncompressedDirectory);
      if (index != -1) {
        CPUFlags.setCPUFlag(uncompressedDirectory, index, CPUFlags.CPU_BRANCHES.get(index));
      }
      if (!flag.equals("UNUSED")) {
        CPUFlags.setCPUFlag(uncompressedDirectory, CPUFlags.actionToCPUFlag(flag),
            CPUFlags.RECORDING_OFFSET);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Setting Recording Flag", e);
      Message.error("Error Setting Recording Flag", e.getMessage());
    }
  }

  @FXML
  public void setRecordingCounterFlag() {
    try {
      String flag = recordingCounterFlag.getValue();
      String otherFlag = recordingFlag.getValue();
      if (flag.equals(otherFlag)) {
        throw new IOException("Cannot modify CPU flag due to duplicate value.");
      }
      int index = CPUFlags.getRecordingCounterCPUFlag(uncompressedDirectory);
      if (index != -1) {
        CPUFlags.setCPUFlag(uncompressedDirectory, index, CPUFlags.CPU_BRANCHES.get(index));
      }
      if (!flag.equals("UNUSED")) {
        CPUFlags.setCPUFlag(uncompressedDirectory, CPUFlags.actionToCPUFlag(flag),
            CPUFlags.RECORDING_COUNTER_OFFSET);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Setting Recording Counter Flag", e);
      Message.error("Error Setting Recording Counter Flag", e.getMessage());
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
   * Add a new file to the GNT4 workspace.
   */
  @FXML
  public void addFile() {
    // Get input file
    if (lastUncompressedSubdirectory == null) {
      lastUncompressedSubdirectory = uncompressedDirectory;
    }
    Optional<Path> input = Choosers.getInputUncompressedFile(uncompressedDirectory,
        lastUncompressedSubdirectory);
    if (input.isEmpty()) {
      return;
    }
    lastUncompressedSubdirectory = input.get().getParent();

    String filePath = uncompressedDirectory.relativize(input.get()).toString().replace("\\", "/");
    try {
      workspace.addFile(new WorkspaceFile(filePath, 0, 0, null, false));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Adding File", e);
      Message.error("Error Adding File", e.getMessage());
    }
  }

  /**
   * Add a new file to an FPK file in the GNT4 workspace.
   */
  @FXML
  public void addFileToFPK() {
    // Get input file
    if (lastUncompressedSubdirectory == null) {
      lastUncompressedSubdirectory = uncompressedDirectory;
    }
    Optional<Path> input = Choosers.getInputUncompressedFile(uncompressedDirectory,
        lastUncompressedSubdirectory);
    if (input.isEmpty()) {
      return;
    }
    lastUncompressedSubdirectory = input.get().getParent();

    // Get output file
    if (lastCompressedSubdirectory == null) {
      lastCompressedSubdirectory = compressedDirectory;
    }
    Optional<Path> output = Choosers.getOutputCompressedFPK(compressedDirectory,
        lastCompressedSubdirectory);
    if (output.isEmpty()) {
      return;
    }
    lastCompressedSubdirectory = output.get().getParent();

    boolean compress = Message.infoConfirmation("Compress File", "Should this file be compressed?");

    String filePath = uncompressedDirectory.relativize(input.get()).toString().replace("\\", "/");
    String fpkFilePath = compressedDirectory.relativize(output.get()).toString().replace("\\", "/");
    try {
      workspace.addFile(new WorkspaceFile(filePath, 0, 0, fpkFilePath, compress));
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Adding File", e);
      Message.error("Error Adding File", e.getMessage());
    }
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
    // TODO: Changed files list is updated on FX Application Thread which is slower than this code runs, resulting in not all changed files being included
    try {
      syncRefresh();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Error Refreshing Workspace", e);
      Message.error("Error Refreshing Workspace", e.getMessage());
      return;
    }

    // Prevent build if files are missing
    if (!missingFiles.getItems().isEmpty()) {
      String message = "You cannot build the ISO while files are missing.\n";
      message += "Allow GNTool to replace these missing files with 1 KB filler files?";
      boolean yes = Message.warnConfirmation("Missing Files", message);
      if (yes) {
        createMissingFiles();
      } else {
        return;
      }
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
          GameCubeISO.importFiles(compressedDirectory, isoResponse.get(),
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
      asyncPostBuild();
    });
    task.setOnFailed(event -> {
      Message.error("ISO Build Failure", "See the log for more information");
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
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        Desktop.getDesktop().browse(new URI(ABOUT_URL));
        return null;
      }
    };
    task.exceptionProperty().addListener((observable, oldValue, e) -> {
      if (e != null) {
        LOGGER.log(Level.SEVERE, "Error Opening About Page", e);
        Message.error("Error Opening About Page", e.getMessage());
      }
    });
    new Thread(task).start();
  }

  /**
   * Opens the uncompressed files directory in the workspace using the sytem file browser.
   */
  @FXML
  protected void openDirectory() {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        Desktop.getDesktop().open(uncompressedDirectory.toFile());
        return null;
      }
    };
    task.exceptionProperty().addListener((observable, oldValue, e) -> {
      if (e != null) {
        LOGGER.log(Level.SEVERE, "Error Opening Workspace Directory", e);
        Message.error("Error Opening Workspace Directory", e.getMessage());
      }
    });
    new Thread(task).start();
  }

  @FXML
  protected void changedFileSelected(MouseEvent event) {
    EventTarget result = event.getTarget();
    if (event.getButton() == MouseButton.SECONDARY && result instanceof Text text) {
      ContextMenu menu = getChangedFileContextMenu(text.getText());
      menu.show(stage, event.getScreenX(), event.getScreenY());
    }
  }

  @FXML
  protected void browseSoundEffect() {
    Optional<Path> inputSAM = Choosers.getInputSAM(uncompressedFiles.toFile());
    if (inputSAM.isEmpty()) {
      return;
    }
    musyxSamFile.setValue(inputSAM.get().toString());
  }

  @FXML
  protected void musyxExtract() {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        String samFile = musyxSamFile.getSelectionModel().getSelectedItem();
        Path samFilePath = uncompressedDirectory.resolve(samFile);
        if (!Files.exists(samFilePath)) {
          samFilePath = Paths.get(samFile);
          if (!Files.exists(samFilePath)) {
            throw new IOException("Unable to find SAM file: " + samFile);
          }
        }
        String sdiFile = samFilePath.toString().replace(".sam", ".sdi");
        Path sdiFilePath = Paths.get(sdiFile);
        String name = samFilePath.getFileName().toString().replace(".sam", "/");
        Path outputPath = samFilePath.getParent().resolve(name);
        if (!Files.isRegularFile(sdiFilePath)) {
          String message = "Cannot find .sdi file: " + sdiFilePath;
          LOGGER.log(Level.SEVERE, message);
          Message.error("Missing .sdi", message);
          return null;
        }
        Files.createDirectories(outputPath);
        MusyXExtract.extract_samples(sdiFilePath, samFilePath, outputPath);
        Desktop.getDesktop().open(outputPath.toFile());
        return null;
      }
    };
    task.exceptionProperty().addListener((observable, oldValue, e) -> {
      if (e != null) {
        LOGGER.log(Level.SEVERE, "Error Extracting Audio", e);
        Message.error("Error Extracting Audio", e.getMessage());
      }
    });
    new Thread(task).start();
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
      Message.error("Error Extracting Audio", e.getMessage());
    }
  }

  @FXML
  protected void musyxImport() {
    try {
      String samFile = musyxSamFile.getSelectionModel().getSelectedItem();
      Path samFilePath = uncompressedDirectory.resolve(samFile);
      if (!Files.exists(samFilePath)) {
        samFilePath = Paths.get(samFile);
        if (!Files.exists(samFilePath)) {
          throw new IOException("Unable to find SAM file: " + samFile);
        }
      }
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
      Message.error("Error Importing Audio", e.getMessage());
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
      Message.error("Error Importing Audio", e.getMessage());
    }
  }

  @FXML
  protected void randomizeSoundEffects() {
    try {
      String samFile = musyxSamFile.getSelectionModel().getSelectedItem();
      Path samFilePath = uncompressedDirectory.resolve(samFile);
      if (!Files.exists(samFilePath)) {
        samFilePath = Paths.get(samFile);
        if (!Files.exists(samFilePath)) {
          throw new IOException("Unable to find SAM file: " + samFile);
        }
      }
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
      Message.error("Error running randomizer", e.getMessage());
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
      Message.error("Error Replacing Sound Effect", e.getMessage());
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
      Message.error("Error running randomizer", e.getMessage());
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
          String wavName = System.currentTimeMillis() + "temp.wav"; // TODO: Use temp file api
          Path tempWavFilePath = audioFilePath.getParent().resolve(wavName);
          Path output = optionalOutput.get();
          String ffmpegOutput = FFmpeg.prepareMusic(audioFilePath, tempWavFilePath);
          LOGGER.log(Level.INFO, ffmpegOutput);
          DtkMake.fixWavHeader(tempWavFilePath);
          String trkOutput = DtkMake.run(tempWavFilePath, output);
          LOGGER.log(Level.INFO, trkOutput);
          Files.deleteIfExists(tempWavFilePath); // TODO: Cleanup in finally
          Message.info("Music Replacement Done", "Music Replacement Done.");
        }
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Replacing Music", e);
      Message.error("Error Replacing Music", e.getMessage());
    }
  }

  @FXML
  protected void browseTexture() {
    Optional<Path> inputTXG = Choosers.getInputTXG(uncompressedFiles.toFile());
    if (inputTXG.isEmpty()) {
      return;
    }
    txg2tplTexture.setValue(inputTXG.get().toString());
  }

  @FXML
  protected void txg2tplExtract() {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        String txgFile = txg2tplTexture.getSelectionModel().getSelectedItem();
        Path txgFilePath = uncompressedDirectory.resolve(txgFile);
        if (!Files.exists(txgFilePath)) {
          txgFilePath = Paths.get(txgFile);
          if (!Files.exists(txgFilePath)) {
            throw new IOException("Unable to find TXG file: " + txgFile);
          }
        }
        String name = txgFilePath.getFileName().toString().replace(".txg", "/");
        Path outputPath = txgFilePath.getParent().resolve(name);
        if (!Files.isRegularFile(txgFilePath)) {
          String message = "Cannot find .txg file: " + txgFilePath;
          LOGGER.log(Level.SEVERE, message);
          Message.error("Missing .txg", message);
          return null;
        }
        Files.createDirectories(outputPath);
        String output = TXG2TPL.unpack(txgFilePath, outputPath);
        LOGGER.log(Level.INFO, output);
        Desktop.getDesktop().open(outputPath.toFile());
        return null;
      }
    };
    task.exceptionProperty().addListener((observable, oldValue, e) -> {
      if (e != null) {
        LOGGER.log(Level.SEVERE, "Error Extracting Textures", e);
        Message.error("Error Extracting Textures", e.getMessage());
      }
    });
    new Thread(task).start();
  }

  @FXML
  protected void txg2tplImport() {
    try {
      String txgFile = txg2tplTexture.getSelectionModel().getSelectedItem();
      Path txgFilePath = uncompressedDirectory.resolve(txgFile);
      if (!Files.exists(txgFilePath)) {
        txgFilePath = Paths.get(txgFile);
        if (!Files.exists(txgFilePath)) {
          throw new IOException("Unable to find TXG file: " + txgFile);
        }
      }
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
      Message.error("Error Importing Texture", e.getMessage());
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
  public void unpackMOT() {
    MOTUnpackerTool.run(uncompressedFiles.toFile());
  }

  @FXML
  public void repackMOT() {
    MOTRepackerTool.run(uncompressedFiles.toFile());
  }

  @FXML
  public void modifyGNTA() {
    GNTAEditorTool.open(uncompressedFiles.toFile());
  }

  @FXML
  protected void disassembleSeqToHTML() {
    try {
      String seq = selectedSeq.getSelectionModel().getSelectedItem();
      Path seqPath = uncompressedDirectory.resolve(seq);
      if (!Files.exists(seqPath)) {
        seqPath = Paths.get(seq);
        if (!Files.exists(seqPath)) {
          throw new IOException("Unable to find SEQ file: " + seqPath);
        }
      }
      SeqDisassemblerTool.disassembleToHTML(uncompressedFiles.toFile(), seqPath);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Running SEQ Report", e);
      Message.error("Error Running SEQ Report", e.getMessage());
    }
  }

  @FXML
  protected void disassembleSeqToTXT() {
    try {
      String seq = selectedSeq.getSelectionModel().getSelectedItem();
      Path seqPath = uncompressedDirectory.resolve(seq);
      if (!Files.exists(seqPath)) {
        seqPath = Paths.get(seq);
        if (!Files.exists(seqPath)) {
          throw new IOException("Unable to find SEQ file: " + seqPath);
        }
      }
      SeqDisassemblerTool.disassembleToTXT(uncompressedFiles.toFile(), seqPath);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Running SEQ Report", e);
      Message.error("Error Running SEQ Report", e.getMessage());
    }
  }

  @FXML
  protected void browseSeq() {
    Optional<Path> inputSeq = Choosers.getInputSeq(uncompressedFiles.toFile());
    if (inputSeq.isEmpty()) {
      return;
    }
    selectedSeq.setValue(inputSeq.get().toString());
  }

  @FXML
  protected void seqEditor() {
    try {
      String seq = selectedSeq.getSelectionModel().getSelectedItem();
      Path seqPath = uncompressedDirectory.resolve(seq);
      if (!Files.exists(seqPath)) {
        seqPath = Paths.get(seq);
        if (!Files.exists(seqPath)) {
          throw new IOException("Unable to find " + seqPath);
        }
      }
      SeqEditorTool.open(seqPath);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Running SEQ Editor", e);
      Message.error("Error Running SEQ Editor", e.getMessage());
    }
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
      String seq = selectedSeq.getSelectionModel().getSelectedItem();
      Path seqPath = uncompressedDirectory.resolve(seq);
      if (!Files.exists(seqPath)) {
        seqPath = Paths.get(seq);
        if (!Files.exists(seqPath)) {
          throw new IOException("Unable to find " + seqPath);
        }
      }
      String output = SeqKage.run(seqPath);
      LOGGER.log(Level.INFO, output);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Running SEQKage", e);
      Message.error("Error Running SEQKage", e.getMessage());
    }
  }

  public void dolphinSEQListener() {
    try {
      DolphinSeqListenerTool.run(uncompressedFiles);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error Running Dolphin SEQ Listener", e);
      Message.error("Error Running Dolphin SEQ Listener", e.getMessage());
    }
  }

  @FXML
  protected void checkCodes() {
    String text = geckoCodes.getText();
    GeckoReader reader = new GeckoReader();
    try {
      List<GeckoCode> codes = reader.parseCodes(text);
      if (DolHijack.checkHijackOverflow(codeGroups, codes, CodeCave.EXI2)) {
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
      if (DolHijack.checkHijackOverflow(codeGroups, codes, CodeCave.EXI2)) {
        return;
      }
      if (targetAddressOverlap(codes)) {
        return;
      }
      String name = codeName.getText();
      if (!checkNameValid((name))) {
        return;
      }
      long hijackStartAddress = DolHijack.getEndOfHijacking(codeGroups, CodeCave.EXI2);
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
        defragCodeGroups(codeGroups);
        asyncRefresh();
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
  public void init(Workspace workspace, Stage stage) throws IOException {
    this.workspace = workspace;
    this.stage = stage;
    this.workspaceDirectory = workspace.getWorkspaceDirectory();
    this.compressedDirectory = workspace.getCompressedDirectory();
    this.uncompressedDirectory = workspace.getUncompressedDirectory();
    this.uncompressedFiles = uncompressedDirectory.resolve("files");
    this.dolPath = uncompressedDirectory.resolve(DOL);
    this.codes = new GNT4Codes(uncompressedDirectory);
    musyxSamFile.getItems().setAll(GNT4Audio.SOUND_EFFECTS);
    musyxSamFile.getSelectionModel().selectFirst();
    txg2tplTexture.getItems().setAll(GNT4Graphics.TEXTURES);
    txg2tplTexture.getSelectionModel().selectFirst();
    selectedSeq.getItems().setAll(Seqs.ALL);
    selectedSeq.getSelectionModel().selectFirst();
    mainMenuCharacter.getItems().setAll(GNT4Characters.MAIN_MENU_CHARS);
    mainMenuCharacter.getSelectionModel().select(GNT4Characters.SAKURA);
    initRecordingComboboxes();
    asyncRefresh();
  }

  /**
   * Rebuilds the workspace state. This means that refresh will be cleared of changes.
   */
  private void rebuildWorkspace() {
    try {
      workspace.updateState();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Failed to save workspace state.", e);
      Message.error("Failed to save workspace state.", e.getMessage());
    }
  }

  /**
   * Refresh the workspace synchronously. Will not create any windows.
   *
   * @throws IOException If an I/O error occurs.
   */
  private void syncRefresh() throws IOException {
    LOGGER.log(Level.INFO, "Refreshing workspace.");
    List<WorkspaceFile> allFiles = workspace.getAllFiles();
    // The below two calls can be slow the first time they are called since it needs to check
    // if each file in the workspace exists. I'm not sure if this can be avoided.
    refreshMissingFiles(allFiles);
    refreshChangedFiles(allFiles);
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
      Message.error("Error Refreshing Workspace", "See the log for more information");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  /**
   * Rebuild and refresh the workspace asynchronously. Will create a loading window for progress.
   */
  private void asyncPostBuild() {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        try {
          updateMessage("Rebuilding workspace...");
          rebuildWorkspace();
          syncRefresh();
          updateProgress(1, 1);
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Failed to rebuild workspace.", e);
          throw e;
        }
        return null;
      }
    };
    Stage loadingWindow = GUIUtils.createLoadingWindow("Rebuilding Workspace", task);

    task.setOnSucceeded(event -> loadingWindow.close());
    task.setOnFailed(event -> {
      Message.error("Error Rebuilding Workspace", "See the log for more information");
      loadingWindow.close();
    });
    new Thread(task).start();
  }

  /**
   * Refreshes the missing files tab from a set of GNTFiles.
   *
   * @param allFiles All the workspace files.
   */
  private void refreshMissingFiles(List<WorkspaceFile> allFiles) {
    Set<String> missingFilenames = workspace.getMissingFiles(allFiles);
    Platform.runLater(() -> {
      missingFiles.getItems().setAll(missingFilenames);
      Collections.sort(missingFiles.getItems());
    });
  }

  /**
   * Refreshes the changed files tab from a set of GNTFiles.
   *
   * @param allFiles All the workspace files.
   */
  private void refreshChangedFiles(List<WorkspaceFile> allFiles) throws IOException {
    Set<String> changedFilenames = workspace.getChangedFiles(allFiles);
    Platform.runLater(() -> {
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

        if (DolHijack.isUsingCodeCave(dolPath, CodeCave.RECORDING)) {
          if (DolHijack.isUsingCodeCave(dolPath, CodeCave.EXI2)) {
            throw new IOException(
                "You are overwriting both recording code and EXI2 code, please log an issue to get this fixed.");
          }
          String msg = "Your Gecko codes are currently overwriting recording functionality. ";
          msg += "These codes need to be moved so that recording functionality can be used. Are you okay with this?";
          if (Message.warnConfirmation("Need to Move Codes", msg)) {
            if (!Files.isRegularFile(codeFile)) {
              // We need the code file to do the conversion, create a code file with the old code cave
              if (!DolHijack.handleActiveCodesButNoCodeFile(dolPath, CodeCave.RECORDING)) {
                throw new IOException("Recording code is modified, but unable to get code file.");
              }
            }
            // Do the conversion
            DolHijack.moveCodes(dolPath, codeFile, CodeCave.EXI2);
          } else {
            throw new IOException("Codes must be converted to use code hijacking.");
          }
        }

        // Check code file
        if (Files.isRegularFile(codeFile)) {
          codeGroups = GeckoCodeJSON.parseFile(codeFile);
          for (GeckoCodeGroup codeGroup : codeGroups) {
            addedCodes.getItems().add(codeGroup.getName());
          }
        } else if (DolHijack.handleActiveCodesButNoCodeFile(dolPath, CodeCave.EXI2)) {
          // This ISO has injected codes but no associated JSON code file. The previous method
          // call successfully created one, so now let's parse it.
          codeGroups = GeckoCodeJSON.parseFile(codeFile);
          for (GeckoCodeGroup codeGroup : codeGroups) {
            addedCodes.getItems().add(codeGroup.getName());
          }
          String msg = "Codes were found in the dol but no codes.json file exists.\n";
          LOGGER.info(msg + "The following codes were found: " + String
              .join(", ", addedCodes.getItems()));
        } else {
          // There actually were no codes
          codeGroups = new ArrayList<>();
        }

      } catch (Exception e) {
        validateCodes.setDisable(true);
        addCodes.setDisable(true);
        removeCode.setDisable(true);
        LOGGER.log(Level.SEVERE, "Error getting list of applied codes.", e);
        Message.error("Error Reading Codes", e.getMessage());
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
    try {
      int value = codes.getCodeInt(GNT4Codes.CSS_LOAD_CHR_MODELS_P1);
      cssModelLoad.getValueFactory().setValue(value);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error getting Frames Until CSS Model Load.", e);
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
        Message.error("Error Setting Main Menu Character", e.getMessage());
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
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        Desktop.getDesktop().open(fullFilePath.toFile());
        return null;
      }
    };
    task.exceptionProperty().addListener((observable, oldValue, e) -> {
      if (e != null) {
        LOGGER.log(Level.SEVERE, "Error Opening File", e);
      }
    });
    openFile.setOnAction(event -> new Thread(task).start());
    MenuItem openDirectory = new MenuItem("Open Directory");
    Task<Void> task2 = new Task<>() {
      @Override
      public Void call() throws Exception {
        Desktop.getDesktop().open(fullFilePath.getParent().toFile());
        return null;
      }
    };
    task2.exceptionProperty().addListener((observable, oldValue, e) -> {
      if (e != null) {
        LOGGER.log(Level.SEVERE, "Error Opening Directory", e);
      }
    });
    openDirectory.setOnAction(event -> new Thread(task2).start());
    MenuItem revertChanges = new MenuItem("Revert Changes");
    revertChanges.setOnAction(event -> {
      try {
        if (DOL.equals(filePath) && dolModded()) {
          return;
        }
        workspace.revertFiles(Collections.singletonList(filePath));
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
   * Defrags the list of gecko codes. This will update the code objects for any codes that are moved
   * in the dol as a result. The algorithm used simply iterates over each code and moves it
   * immediately after the previous code. The first code will be moved to START_RAM_ADDRESS.
   *
   * @param codeGroups The code groups to defrag.
   */
  private void defragCodeGroups(List<GeckoCodeGroup> codeGroups) {
    try {
      DolDefragger defragger = new DolDefragger(dolPath, codeGroups, CodeCave.EXI2);
      defragger.run();
      Path codeFile = workspaceDirectory.resolve(GeckoCodeJSON.CODE_FILE);
      GeckoCodeJSON.writeFile(codeGroups, codeFile);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to Defrag Codes", e);
      Message.error("Unable to Defrag Codes", e.getMessage());
    }
  }

  /**
   * Removes the given GeckoCodeGroup from the dol. This will write the new codes.json file after
   * removal and will refresh.
   *
   * @param group The GeckoCodeGroup to remove.
   */
  private void removeCodeGroup(GeckoCodeGroup group) {
    try {
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
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to Remove Code", e);
      Message.error("Unable to Remove Code", e.getMessage());
    }
  }

  /**
   * Iterates over the missing files and replaces them with 1 KB empty files. These files will then
   * be removed from the list of missing files and added to the list of changed files.
   */
  private void createMissingFiles() {
    Iterator<String> files = missingFiles.getItems().iterator();
    byte[] bytes = new byte[1024];
    while (files.hasNext()) {
      String file = files.next();
      Path filePath = uncompressedDirectory.resolve(file);
      try {
        Files.write(filePath, bytes);
        LOGGER.log(Level.INFO, "Created empty file for " + filePath);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      files.remove();
      changedFiles.getItems().add(file);
    }
  }

  private void initRecordingComboboxes() {
    try {
      CPUFlags.verify0010FilesConsistent(uncompressedDirectory);
      int flag = CPUFlags.getRecordingCPUFlag(uncompressedDirectory);
      int counterFlag = CPUFlags.getRecordingCounterCPUFlag(uncompressedDirectory);
      recordingFlag.getItems().add("UNUSED");
      recordingFlag.getItems().addAll(CPUFlags.CPU_FLAG_TO_ACTION.values());
      if (flag == -1) {
        recordingFlag.getSelectionModel().selectFirst();
      } else {
        recordingFlag.getSelectionModel().select(CPUFlags.CPU_FLAG_TO_ACTION.get(flag));
      }
      recordingCounterFlag.getItems().add("UNUSED");
      recordingCounterFlag.getItems().addAll(CPUFlags.CPU_FLAG_TO_ACTION.values());
      if (counterFlag == -1) {
        recordingCounterFlag.getSelectionModel().selectFirst();
      } else {
        recordingCounterFlag.getSelectionModel()
            .select(CPUFlags.CPU_FLAG_TO_ACTION.get(counterFlag));
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Unable to Init CPU Flags for Recording", e);
      Message.error("Unable to Init CPU Flags for Recording", e.getMessage());
      recordingFlag.setDisable(true);
      recordingCounterFlag.setDisable(true);
    }
  }
}
