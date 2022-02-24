package com.github.nicholasmoser.gnt4.mot;

import com.github.nicholasmoser.gnt4.seq.ext.SeqEditor;
import com.github.nicholasmoser.mot.BoneAnimation;
import com.github.nicholasmoser.mot.Coordinate;
import com.github.nicholasmoser.mot.GNTAnimation;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.List;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GNTAEditor {

  private Stage stage;
  private Path gntaPath;
  private SeqEditor.Mode mode;
  private GNTAnimation gnta;
  public Label leftStatus;
  public Label rightStatus;

  // Gnta values
  public TextField id;
  public TextField bounciness;
  public TextField repeatDelay;
  public LineChart functionCurveChart;
  public Button apply;

  // Bone animation values
  public ListView boneAnimations;
  public TextField offset;
  public TextField flags1;
  public TextField flags2;
  public TextField boneID;
  public TextField lastFunctionCurveValue;

  // Key frame values
  public ListView keyFrames;
  public TextField x;
  public TextField y;
  public TextField z;
  public TextField w;
  public TextField functionCurve;

  public enum Mode {
    NONE_SELECTED,
    EDIT
  }

  public void init(Path gntaPath, Stage stage) throws IOException {
    this.stage = stage;
    this.gntaPath = gntaPath;
    this.mode = SeqEditor.Mode.NONE_SELECTED;
    this.rightStatus.setText(mode.toString());
    this.leftStatus.setText(gntaPath.toAbsolutePath().toString());
    this.gnta = parseGnta(gntaPath);
    updateAllControls();
    boneAnimations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      if (newSelection != null) {
        int value = (int) newSelection;
        selectBoneAnimation(value - 1); // index starts at 1
      }
    });
    keyFrames.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      if (newSelection != null) {
        int value = (int) newSelection;
        selectKeyFrame(value - 1); // index starts at 1
      }
    });
  }

  public void apply() {
  }

  public void quit() {
    stage.close();
  }

  private void updateAllControls() {
    // Fill out gnta pane
    id.setText(String.format("0x%X", gnta.getId()));
    bounciness.setText(Float.toString(gnta.getBounciness()));
    repeatDelay.setText(Float.toString(gnta.getRepeatDelay()));

    // Fill out bone animations pane
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    for (int i = 1; i <= boneAnims.size(); i++) {
      boneAnimations.getItems().add(i);
    }
    boneAnimations.getSelectionModel().select(0);
    BoneAnimation boneAnim = boneAnims.get(0);
    offset.setText(String.format("0x%X", boneAnim.getOffset()));
    flags1.setText(String.format("0x%04X", boneAnim.getFlags1()));
    flags2.setText(String.format("0x%04X", boneAnim.getFlags2()));
    boneID.setText(String.format("0x%X", boneAnim.getBoneId()));
    lastFunctionCurveValue.setText(Float.toString(boneAnim.getLastFunctionCurveValue()));

    // Fill out key frames pane
    List<Coordinate> coordinates = boneAnim.getCoordinates();
    List<Float> functionCurveValues = boneAnim.getFunctionCurveValues();
    for (int i = 1; i <= functionCurveValues.size(); i++) {
      keyFrames.getItems().add(i);
    }
    keyFrames.getSelectionModel().select(0);
    functionCurve.setText(Float.toString(boneAnim.getFunctionCurveValues().get(0)));
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

    // Fill out the function curve chart
    updateFunctionCurveChart();
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

  private void updateFunctionCurveChart() {
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    BoneAnimation boneAnim = boneAnims.get(boneAnimations.getSelectionModel().getSelectedIndex());
    List<Float> values = boneAnim.getFunctionCurveValues();
    XYChart.Series<Integer, Integer> series = new XYChart.Series();
    for (int i = 0; i < values.size(); i++) {
      series.getData().add((new XYChart.Data(i + 1, values.get(i))));
    }
    series.setName("Function Curve Value");
    functionCurveChart.getData().clear();
    functionCurveChart.getData().add(series);
  }

  private void selectBoneAnimation(int index) {
    // Fill out bone animations pane
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    BoneAnimation boneAnim = boneAnims.get(index);
    offset.setText(String.format("0x%X", boneAnim.getOffset()));
    flags1.setText(String.format("0x%04X", boneAnim.getFlags1()));
    flags2.setText(String.format("0x%04X", boneAnim.getFlags2()));
    boneID.setText(String.format("0x%X", boneAnim.getBoneId()));
    lastFunctionCurveValue.setText(Float.toString(boneAnim.getLastFunctionCurveValue()));

    // Fill out key frames pane
    List<Coordinate> coordinates = boneAnim.getCoordinates();
    List<Float> functionCurveValues = boneAnim.getFunctionCurveValues();
    keyFrames.getItems().clear();
    for (int i = 1; i <= functionCurveValues.size(); i++) {
      keyFrames.getItems().add(i);
    }
    keyFrames.getSelectionModel().selectFirst();
    functionCurve.setText(Float.toString(functionCurveValues.get(0)));
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
    updateFunctionCurveChart();
  }

  private void selectKeyFrame(int index) {
    // Get bone animation of this key frame
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    BoneAnimation boneAnim = boneAnims.get(boneAnimations.getSelectionModel().getSelectedIndex());
    // Get coordinates and function curve of the bone animation
    List<Coordinate> coordinates = boneAnim.getCoordinates();
    List<Float> functionCurveValues = boneAnim.getFunctionCurveValues();
    // Set the function curve. Set the coordinates if they exist.
    functionCurve.setText(Float.toString(functionCurveValues.get(index)));
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
    try(RandomAccessFile raf = new RandomAccessFile(gntaPath.toFile(), "r")) {
      String id = gntaPath.getFileName().toString().replace(".gnta", "");
      return GNTAnimation.parseFrom(raf, Integer.decode(id));
    }
  }
}
