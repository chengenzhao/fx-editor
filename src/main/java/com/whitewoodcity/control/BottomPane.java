package com.whitewoodcity.control;

import com.almasb.fxgl.dsl.FXGL;
import com.whitewoodcity.GameApp;
import com.whitewoodcity.fxgl.transition.Frames;
import com.whitewoodcity.javafx.jvg.JVG;
import com.whitewoodcity.javafx.jvg.JVGLayer;
import com.whitewoodcity.javafx.jvg.JVGRectangle;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

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
      var jvgs = FXGL.<GameApp>getAppCast().getRectBiMap().values();

      var copies = new ArrayList<JVG>();
      for(var rect:jvgs){
        var node = rect.getNode();
        if(node instanceof JVG jvg){
          var copy = jvg.copy();
          copy.getChildren().add((Node)generateBordersRectangle());
          copies.add(copy);
        }
      }
      var imgs = Frames.toImages(copies);
      var imageview = new ImageView();
      var entity = FXGL.<GameApp>getAppCast().getEntity();
      FXGL.<GameApp>getAppCast().clear();
      entity.getViewComponent().addChild(imageview);
      var frames = new Frames(imageview, imgs, 1.0/ imgs.length);
      frames.loop();
    });
  }

  public JVGLayer generateBordersRectangle(){
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

  public JVGLayer getRectangle(){

    var jvgs = FXGL.<GameApp>getAppCast().getRectBiMap().values();
    var topleft = new Point2D(0,0);
    var bottomRight = new Point2D(0,0);
    var delta = new Dimension2D(0,0);
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
        //calculate delta x&y
        var img = jvg.snapshot();
        if(Math.abs(img.getWidth() - d.getWidth()) > delta.getWidth()){
          delta = new Dimension2D(Math.abs(img.getWidth() - d.getWidth()), delta.getHeight());
        }
        if(Math.abs(img.getHeight() - d.getHeight()) > delta.getHeight()){
          delta = new Dimension2D(delta.getWidth(), Math.abs(img.getHeight() - d.getHeight()));
        }
      }
    }

    var jvgl = new JVGRectangle();
    jvgl.setStrokeWidth(0);
    jvgl.setFill(Color.TRANSPARENT);
    jvgl.setStroke(Color.TRANSPARENT);

    jvgl.setX(topleft.getX() - delta.getWidth());
    jvgl.setY(topleft.getY() - delta.getHeight());
    jvgl.setWidth(bottomRight.getX() - topleft.getX() + delta.getWidth()*2);
    jvgl.setHeight(bottomRight.getY() - topleft.getY() + delta.getHeight()*2);

    return jvgl;
  }
}
