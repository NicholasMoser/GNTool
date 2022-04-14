package com.github.nicholasmoser.mot;

import com.github.nicholasmoser.Message;
import com.github.nicholasmoser.gnt4.seq.ext.SeqEditor;
import com.github.nicholasmoser.tools.MOTRepackerTool;
import java.awt.Desktop;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * A GUI editor for .gnta files. Each .gnta file represent a single animation.
 */
public class GNTAEditor {

  private static final Logger LOGGER = Logger.getLogger(GNTAEditor.class.getName());
  private static final String MOT_EDITOR_INFO_URL = "https://github.com/NicholasMoser/GNTool/blob/master/docs/animation.md#modify-gnta-file";
  private Stage stage;
  private Path gntaPath;
  private SeqEditor.Mode mode;
  private GNTAnimation gnta;
  private Path currentMot;
  public Label leftStatus;
  public Label rightStatus;

  // Gnta values
  public TextField id;
  public TextField playSpeed;
  public TextField endTime;
  public LineChart keyFrameProgression;
  public Button apply;

  // Bone animation values
  public ListView boneAnimations;
  public TextField offset;
  public TextField flags1;
  public TextField boneId;
  public TextField totalTimeFrames;
  public CheckBox translateToggle;
  public CheckBox scaleToggle;
  public CheckBox rotateToggle;
  public CheckBox enabledToggle;
  public CheckBox disabledToggle;

  // Key frame values
  public ListView keyFrames;
  public TextField x;
  public TextField y;
  public TextField z;
  public TextField w;
  public TextField frame;

  public enum Mode {
    EDIT
  }

  public void init(Path gntaPath, Stage stage) throws IOException {
    this.stage = stage;
    this.gntaPath = gntaPath;
    this.mode = SeqEditor.Mode.EDIT;
    this.rightStatus.setText(mode.toString());
    this.leftStatus.setText(gntaPath.toAbsolutePath().toString());
    this.gnta = parseGnta(gntaPath);
    updateAllControls(0, 0);
    boneAnimations.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null) {
            int value = (int) newSelection;
            selectBoneAnimation(value - 1); // index starts at 1
          }
        });
    keyFrames.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null) {
            int value = (int) newSelection;
            selectKeyFrame(value - 1); // index starts at 1
          }
        });
  }

  public void apply() {
    try {
      // First get all values and make sure they are valid
      float playSpeedVal = Float.parseFloat(playSpeed.getText());
      int endTimeFrames = Integer.parseInt(endTime.getText());
      float endTimeVal = Time.framesToFraction(endTimeFrames);
      short flags1Val = Integer.decode(flags1.getText()).shortValue();
      short boneIdVal = Integer.decode(boneId.getText()).shortValue();
      int currentFrame = Integer.parseInt(frame.getText());
      float currentFrameFraction = Time.framesToFraction(currentFrame);
      Coordinate coordinate = null;
      if (!x.isDisabled()) {
        // coordinate fields disabled, avoid creating a coordinate
        float xVal = Float.parseFloat(x.getText());
        float yVal = Float.parseFloat(y.getText());
        float zVal = Float.parseFloat(z.getText());
        float wVal = Float.parseFloat(w.getText());
        coordinate = new Coordinate(xVal, yVal, zVal, wVal);
      }
      short trackFlagVal = 0;
      if (translateToggle.isSelected()) {
        trackFlagVal |= TrackFlag.TRANSLATE;
      }
      if (scaleToggle.isSelected()) {
        trackFlagVal |= TrackFlag.SCALE;
      }
      if (rotateToggle.isSelected()) {
        trackFlagVal |= TrackFlag.ROTATE;
      }
      if (enabledToggle.isSelected()) {
        trackFlagVal |= TrackFlag.ENABLED;
      }
      if (disabledToggle.isSelected()) {
        trackFlagVal |= TrackFlag.DISABLED;
      }

      // Set the new values
      gnta.setPlaySpeed(playSpeedVal);
      gnta.setEndTime(endTimeVal);
      List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
      int boneAnimIndex = boneAnimations.getSelectionModel().getSelectedIndex();
      BoneAnimation boneAnim = boneAnims.get(boneAnimIndex);
      boneAnim.setFlags1(flags1Val);
      boneAnim.setTrackFlag(trackFlagVal);
      boneAnim.setBoneId(boneIdVal);
      int keyFrameIndex = keyFrames.getSelectionModel().getSelectedIndex();
      List<Float> timeValues = boneAnim.getTimeValues();
      timeValues.set(keyFrameIndex, currentFrameFraction);
      if (coordinate != null) {
        List<Coordinate> coordinates = boneAnim.getCoordinates();
        coordinates.set(keyFrameIndex, coordinate);
      }

      // Update the total time if changed
      if (keyFrameIndex == keyFrames.getItems().size() - 1) {
        boneAnim.setTotalTime(currentFrameFraction);
      }

      // Write the data and update the view
      gnta.writeTo(gntaPath);
      updateAllControls(boneAnimIndex, keyFrameIndex);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to Apply Changes", e);
      Message.error("Failed to Apply Changes", e.getMessage());
    }
  }

  public void repackMOT() {
    Optional<Path> output = MOTRepackerTool.run(gntaPath.getParent().toFile());
    if (output.isPresent()) {
      currentMot = output.get();
    }
  }

  public void repackLastMOT() {
    if (currentMot != null) {
      MOTRepackerTool.repack(gntaPath.getParent(), currentMot);
    } else {
      Message.info("Repack MOT First", "You must run Repack MOT first.");
    }
  }

  public void quit() {
    stage.close();
  }

  public void aboutMOTEditor() {
    Task<Void> task = new Task<>() {
      @Override
      public Void call() throws Exception {
        Desktop.getDesktop().browse(new URI(MOT_EDITOR_INFO_URL));
        return null;
      }
    };
    task.exceptionProperty().addListener((observable,oldValue, e) -> {
      if (e!=null){
        LOGGER.log(Level.SEVERE, "Error Opening Help Page", e);
        Message.error("Error Opening Help Page", e.getMessage());
      }
    });
    new Thread(task).start();
  }

  private void updateAllControls(int boneAnimIndex, int keyFrameIndex) {
    // Fill out gnta pane
    id.setText(String.format("0x%X", gnta.getId()));
    playSpeed.setText(Float.toString(gnta.getPlaySpeed()));
    int endTimeFrames = Time.fractionToFrames(gnta.getEndTime());
    endTime.setText(Integer.toString(endTimeFrames));

    // Fill out bone animations pane
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    boneAnimations.getItems().clear();
    for (int i = 1; i <= boneAnims.size(); i++) {
      boneAnimations.getItems().add(i);
    }
    boneAnimations.getSelectionModel().select(boneAnimIndex);
    BoneAnimation boneAnim = boneAnims.get(boneAnimIndex);
    offset.setText(String.format("0x%X", boneAnim.getOffset()));
    flags1.setText(String.format("0x%04X", boneAnim.getFlags1()));
    boneId.setText(String.format("0x%X", boneAnim.getBoneId()));
    int totalTime = Time.fractionToFrames(boneAnim.getTotalTime());
    totalTimeFrames.setText(Integer.toString(totalTime));
    short trackFlag = boneAnim.getTrackFlag();
    translateToggle.setSelected(TrackFlag.isTranslate(trackFlag));
    scaleToggle.setSelected(TrackFlag.isScale(trackFlag));
    rotateToggle.setSelected(TrackFlag.isRotate(trackFlag));
    enabledToggle.setSelected(TrackFlag.isEnabled(trackFlag));
    disabledToggle.setSelected(TrackFlag.isDisabled(trackFlag));

    // Fill out key frames pane
    List<Coordinate> coordinates = boneAnim.getCoordinates();
    List<Float> timeValues = boneAnim.getTimeValues();
    keyFrames.getItems().clear();
    for (int i = 1; i <= timeValues.size(); i++) {
      keyFrames.getItems().add(i);
    }
    keyFrames.getSelectionModel().select(keyFrameIndex);
    float currentFrameFraction = boneAnim.getTimeValues().get(keyFrameIndex);
    frame.setText(Integer.toString(Time.fractionToFrames(currentFrameFraction)));
    if (coordinates.isEmpty()) {
      disableCoordinates(true);
    } else {
      disableCoordinates(false);
      Coordinate coordinate = coordinates.get(keyFrameIndex);
      x.setText(Float.toString(coordinate.getFloatX()));
      y.setText(Float.toString(coordinate.getFloatY()));
      z.setText(Float.toString(coordinate.getFloatZ()));
      w.setText(Float.toString(coordinate.getFloatW()));
    }
    updateKeyFrameProgressionChart();
  }

  private void disableCoordinates(boolean value) {
    x.setDisable(value);
    y.setDisable(value);
    z.setDisable(value);
    w.setDisable(value);
    if (value) {
      x.setText("");
      y.setText("");
      z.setText("");
      w.setText("");
    }
  }

  private void updateKeyFrameProgressionChart() {
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    BoneAnimation boneAnim = boneAnims.get(boneAnimations.getSelectionModel().getSelectedIndex());
    List<Float> values = boneAnim.getTimeValues();
    XYChart.Series<Integer, Integer> series = new XYChart.Series();
    for (int i = 0; i < values.size(); i++) {
      float currentFrameFraction = values.get(i);
      int currentFrame = Time.fractionToFrames(currentFrameFraction);
      series.getData().add((new XYChart.Data(i + 1, currentFrame)));
    }
    series.setName("Key Frame");
    keyFrameProgression.getData().clear();
    keyFrameProgression.getData().add(series);
  }

  private void selectBoneAnimation(int index) {
    // Fill out bone animations pane
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    BoneAnimation boneAnim = boneAnims.get(index);
    offset.setText(String.format("0x%X", boneAnim.getOffset()));
    flags1.setText(String.format("0x%04X", boneAnim.getFlags1()));
    boneId.setText(String.format("0x%X", boneAnim.getBoneId()));
    int totalTime = Time.fractionToFrames(boneAnim.getTotalTime());
    totalTimeFrames.setText(Integer.toString(totalTime));
    short trackFlag = boneAnim.getTrackFlag();
    translateToggle.setSelected(TrackFlag.isTranslate(trackFlag));
    scaleToggle.setSelected(TrackFlag.isScale(trackFlag));
    rotateToggle.setSelected(TrackFlag.isRotate(trackFlag));
    enabledToggle.setSelected(TrackFlag.isEnabled(trackFlag));
    disabledToggle.setSelected(TrackFlag.isDisabled(trackFlag));

    // Fill out key frames pane
    List<Coordinate> coordinates = boneAnim.getCoordinates();
    List<Float> timeValues = boneAnim.getTimeValues();
    keyFrames.getItems().clear();
    for (int i = 1; i <= timeValues.size(); i++) {
      keyFrames.getItems().add(i);
    }
    keyFrames.getSelectionModel().selectFirst();
    float currentFrameFraction = timeValues.get(0);
    frame.setText(Integer.toString(Time.fractionToFrames(currentFrameFraction)));
    if (coordinates.isEmpty()) {
      disableCoordinates(true);
    } else {
      disableCoordinates(false);
      Coordinate coordinate = coordinates.get(0);
      x.setText(Float.toString(coordinate.getFloatX()));
      y.setText(Float.toString(coordinate.getFloatY()));
      z.setText(Float.toString(coordinate.getFloatZ()));
      w.setText(Float.toString(coordinate.getFloatW()));
    }
    updateKeyFrameProgressionChart();
  }

  private void selectKeyFrame(int index) {
    // Get bone animation of this key frame
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    BoneAnimation boneAnim = boneAnims.get(boneAnimations.getSelectionModel().getSelectedIndex());
    // Get coordinates and times values of the bone animation
    List<Coordinate> coordinates = boneAnim.getCoordinates();
    List<Float> timeValues = boneAnim.getTimeValues();
    // Set the time value. Set the coordinates if they exist.
    float currentFrameFraction = timeValues.get(index);
    frame.setText(Integer.toString(Time.fractionToFrames(currentFrameFraction)));
    if (coordinates.isEmpty()) {
      disableCoordinates(true);
    } else {
      disableCoordinates(false);
      Coordinate coordinate = coordinates.get(index);
      x.setText(Float.toString(coordinate.getFloatX()));
      y.setText(Float.toString(coordinate.getFloatY()));
      z.setText(Float.toString(coordinate.getFloatZ()));
      w.setText(Float.toString(coordinate.getFloatW()));
    }
  }

  private GNTAnimation parseGnta(Path gntaPath) throws IOException {
    try (RandomAccessFile raf = new RandomAccessFile(gntaPath.toFile(), "r")) {
      String id = gntaPath.getFileName().toString().replace(".gnta", "");
      return GNTAnimation.parseFrom(raf, Integer.decode(id));
    } catch (NumberFormatException e) {
      String msg = "GNTA filename invalid: " + gntaPath.getFileName();
      throw new IllegalArgumentException(
          msg + "\nFilename must be 0x{hex}.gnta where {hex} is hex values.", e);
    }
  }
}
