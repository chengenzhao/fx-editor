package com.whitewoodcity.control;

import com.almasb.fxgl.dsl.FXGL;
import com.whitewoodcity.FXEditor;
import com.whitewoodcity.GameApp;
import com.whitewoodcity.fxgl.transition.Frames;
import com.whitewoodcity.javafx.jvg.JVG;
import com.whitewoodcity.javafx.jvg.JVGLayer;
import com.whitewoodcity.javafx.jvg.JVGRectangle;
import com.whitewoodcity.node.NumberField;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BottomPane extends Pane {
  HBox hBox = new HBox();

  private Frames frames;
  private NumberField durationTime = new NumberField(10);

  public BottomPane() {

    var loopButton = new Button("loop");
    var playButton = new Button("play");
    var stopButton = new Button("stop");
    var saveButton = new Button("save");

    durationTime.setText("1.0");

    hBox.getChildren().addAll(new Label("Duration time in seconds:"),durationTime,
        loopButton, playButton, stopButton, saveButton);

    hBox.setAlignment(Pos.BASELINE_CENTER);

    hBox.prefWidthProperty().bind(this.widthProperty());

    this.getChildren().addAll(hBox);

    loopButton.setOnAction(_ -> generateFrames().loop());
    playButton.setOnAction(_ -> generateFrames().play());
    stopButton.setOnAction(_ -> {
      if (frames != null) {
        frames.stop();
      }
      FXGL.<GameApp>getAppCast().update();
    });
    saveButton.setOnAction(_->{
      var chooser = new FileChooser();
      LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
      chooser.setInitialFileName(now.format(formatter));
      var file = chooser.showSaveDialog(this.getScene().getWindow());
      if(file!=null&&file.mkdir()){
        for (var item : FXEditor.getFXEditor().leftColumn.getTreeItems()) {
          var rect = FXGL.<GameApp>getAppCast().getRectBiMap().get(item);
          if (rect.getNode() instanceof JVG jvg) {
            try {
              jvg = generateJVG(jvg);
              Files.write(Paths.get(file.getAbsolutePath(), FXEditor.getFXEditor().leftColumn.getText(item)), jvg.toJsonString().getBytes());
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
        }
      }
    });
  }

  public Frames generateFrames() {
    var jvgs = generateJVGs();
    var imgs = Frames.toImages(jvgs);
    var imageview = new ImageView();
    var entity = FXGL.<GameApp>getAppCast().getEntity();
    FXGL.<GameApp>getAppCast().clear();
    entity.getViewComponent().addChild(imageview);
    frames = new Frames(imageview, imgs, durationTime.getDouble() / imgs.length);
    return frames;
  }

  public List<JVG> generateJVGs(){
    var jvgs = FXGL.<GameApp>getAppCast().getRectBiMap().values();

    var copies = new ArrayList<JVG>();
    for (var rect : jvgs) {
      var node = rect.getNode();
      if (node instanceof JVG jvg) {
        copies.add(generateJVG(jvg));
      }
    }
    return copies;
  }

  public JVG generateJVG(JVG jvg){
    var copy = jvg.copy();
    copy.getChildren().add((Node) generateBordersRectangle());
    return copy;
  }

  public JVGLayer generateBordersRectangle() {
    var rect = FXGL.<GameApp>getAppCast().getBorders();

    var jvgl = new JVGRectangle();
    jvgl.setStrokeWidth(0);
    jvgl.setFill(Color.TRANSPARENT);
    jvgl.setStroke(Color.TRANSPARENT);

    jvgl.setX(rect.getX());
    jvgl.setY(rect.getY());
    jvgl.setWidth(rect.getWidth());
    jvgl.setHeight(rect.getHeight());
    return jvgl;
  }


}
