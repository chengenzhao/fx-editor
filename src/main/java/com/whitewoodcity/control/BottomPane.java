package com.whitewoodcity.control;

import com.almasb.fxgl.dsl.FXGL;
import com.whitewoodcity.GameApp;
import com.whitewoodcity.javafx.jvg.JVG;
import com.whitewoodcity.javafx.jvg.JVGLayer;
import com.whitewoodcity.javafx.jvg.JVGPath;
import com.whitewoodcity.javafx.jvg.JVGRectangle;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class BottomPane extends Pane {
  HBox hBox = new HBox();

  public BottomPane() {

    var loopButton = new Button("loop");
    var playButton = new Button("play");


    hBox.getChildren().addAll(loopButton, playButton);

    hBox.setAlignment(Pos.BASELINE_CENTER);

    hBox.prefWidthProperty().bind(this.widthProperty());

    this.getChildren().addAll(hBox);

    playButton.setOnAction(_->{
      var jvgLayer = getRectangle();
      var jvgs = FXGL.<GameApp>getAppCast().getRectBiMap().values();

      for(var rect:jvgs){
        var node = rect.getNode();
        if(node instanceof JVG jvg){
          jvg.getChildren().add((Node)jvgLayer);
          IO.println(jvg.toJson());
        }
      }
    });
  }

  public JVGLayer getRectangle(){

    var jvgs = FXGL.<GameApp>getAppCast().getRectBiMap().values();
    var topleft = new Point2D(0,0);
    var bottomRight = new Point2D(0,0);
    for(var rect:jvgs){
      var node = rect.getNode();
      if(node instanceof JVG jvg){
        var xy = jvg.getXY();
        if(xy.getX() < topleft.getX()){
          topleft = new Point2D(xy.getX(), topleft.getY());
        }
        if(xy.getY() < topleft.getY()){
          topleft = new Point2D(topleft.getX(), xy.getY());
        }
        var d = jvg.getDimension();
        if(xy.getX() + d.getWidth() > bottomRight.getX()){
          bottomRight = new Point2D(xy.getX() + d.getWidth(), bottomRight.getY());
        }
        if(xy.getY() + d.getHeight() > bottomRight.getY()){
          bottomRight = new Point2D(bottomRight.getX(), xy.getY() + d.getHeight());
        }
      }
    }

    var jvgl = new JVGRectangle();
    jvgl.setStrokeWidth(0);
    jvgl.setFill(Color.TRANSPARENT);
    jvgl.setStroke(Color.TRANSPARENT);

    jvgl.setX(topleft.getX());
    jvgl.setY(topleft.getY());
    jvgl.setWidth(bottomRight.getX() - topleft.getX());
    jvgl.setHeight(bottomRight.getY() - topleft.getY());

    return jvgl;
  }
}
