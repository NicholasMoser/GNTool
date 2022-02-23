package com.github.nicholasmoser.gnt4.mot;

import com.github.nicholasmoser.gnt4.seq.ext.SeqEditor;
import com.github.nicholasmoser.mot.BoneAnimation;
import com.github.nicholasmoser.mot.Coordinate;
import com.github.nicholasmoser.mot.GNTAnimation;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
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
  public TextField unknown1;
  public TextField bounciness;
  public TextField repeatDelay;
  public TextField playbackSpeed;
  public TextField unknown2;
  public TextField unknown4_2;
  public TextField dataOffset;
  public TextField boneAnimationHeadersOffset;
  public TextField functionCurveValues;
  public LineChart functionCurveChart;
  public Button apply;

  // Bone animation values
  public ListView boneAnimations;
  public TextField offset;
  public TextField unknown4;
  public TextField unknown5;
  public TextField maybeBoneId;
  public TextField unknown6;

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
    disableUnusedControls();
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
    unknown1.setText(String.format("0x%X", gnta.getUnknown1()));
    bounciness.setText(Float.toString(gnta.getBounciness()));
    repeatDelay.setText(Float.toString(gnta.getRepeatDelay()));
    playbackSpeed.setText(String.format("0x%X", gnta.getPlaybackSpeed()));
    unknown2.setText(String.format("0x%X", gnta.getUnknown2()));
    unknown4_2.setText(Float.toString(gnta.getUnknown4()));
    dataOffset.setText(String.format("0x%X", gnta.getDataOffset()));
    boneAnimationHeadersOffset.setText(String.format("0x%X", gnta.getBoneAnimationHeadersOffset()));
    functionCurveValues.setText(gnta.getFunctionCurveValues().toString());
    updateFunctionCurveChart();

    // Fill out bone animations pane
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    for (int i = 1; i <= boneAnims.size(); i++) {
      boneAnimations.getItems().add(i);
    }
    boneAnimations.getSelectionModel().select(0);
    BoneAnimation boneAnim = boneAnims.get(0);
    offset.setText(String.format("0x%X", boneAnim.getOffset()));
    unknown4.setText(String.format("0x%X", boneAnim.getUnknown4()));
    unknown5.setText(String.format("0x%X", boneAnim.getUnknown5()));
    maybeBoneId.setText(String.format("0x%X", boneAnim.getMaybeBoneId()));
    unknown6.setText(Float.toString(boneAnim.getUnknown6()));

    // Fill out key frames pane
    List<Coordinate> coordinates = boneAnim.getCoordinates();
    for (int i = 1; i <= coordinates.size(); i++) {
      keyFrames.getItems().add(i);
    }
    keyFrames.getSelectionModel().select(0);
    Coordinate coordinate = coordinates.get(0);
    x.setText(Short.toString(coordinate.getX()));
    y.setText(Short.toString(coordinate.getY()));
    z.setText(Short.toString(coordinate.getZ()));
    w.setText(Short.toString(coordinate.getW()));
    functionCurve.setText(Float.toString(coordinate.getFunctionCurveValue()));
  }

  private void updateFunctionCurveChart() {
    List<Float> values = gnta.getFunctionCurveValues();
    XYChart.Series<Integer, Integer> series = new XYChart.Series();
    for (int i = 0; i < values.size(); i++) {
      series.getData().add((new XYChart.Data(Integer.toString(i + 1), values.get(i))));
    }
    functionCurveChart.getData().add(series);
    functionCurveChart.autosize();
  }

  private void selectBoneAnimation(int index) {
    // Fill out bone animations pane
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    BoneAnimation boneAnim = boneAnims.get(index);
    offset.setText(String.format("0x%X", boneAnim.getOffset()));
    unknown4.setText(String.format("0x%X", boneAnim.getUnknown4()));
    unknown5.setText(String.format("0x%X", boneAnim.getUnknown5()));
    maybeBoneId.setText(String.format("0x%X", boneAnim.getMaybeBoneId()));
    unknown6.setText(Float.toString(boneAnim.getUnknown6()));

    // Fill out key frames pane
    List<Coordinate> coordinates = boneAnim.getCoordinates();
    Coordinate coordinate = coordinates.get(keyFrames.getSelectionModel().getSelectedIndex());
    x.setText(Short.toString(coordinate.getX()));
    y.setText(Short.toString(coordinate.getY()));
    z.setText(Short.toString(coordinate.getZ()));
    w.setText(Short.toString(coordinate.getW()));
    functionCurve.setText(Float.toString(coordinate.getFunctionCurveValue()));
  }

  private void selectKeyFrame(int index) {
    List<BoneAnimation> boneAnims = gnta.getBoneAnimations();
    BoneAnimation boneAnim = boneAnims.get(boneAnimations.getSelectionModel().getSelectedIndex());
    Coordinate coordinate = boneAnim.getCoordinates().get(index);
    x.setText(Short.toString(coordinate.getX()));
    y.setText(Short.toString(coordinate.getY()));
    z.setText(Short.toString(coordinate.getZ()));
    w.setText(Short.toString(coordinate.getW()));
    functionCurve.setText(Float.toString(coordinate.getFunctionCurveValue()));
  }

  private void disableUnusedControls() {
    id.setDisable(true);
    dataOffset.setDisable(true);
    boneAnimationHeadersOffset.setDisable(true);
    offset.setDisable(true);
  }

  private GNTAnimation parseGnta(Path gntaPath) throws IOException {
    try(RandomAccessFile raf = new RandomAccessFile(gntaPath.toFile(), "r")) {
      String id = gntaPath.getFileName().toString().replace(".gnta", "");
      return GNTAnimation.parseFrom(raf, Integer.decode(id));
    }
  }
}
