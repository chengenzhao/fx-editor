package com.whitewoodcity;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.whitewoodcity.fxgl.service.AbstractGameScene;
import com.whitewoodcity.node.EditableRectangle;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameApp extends GameApplication {

  double HEIGHT = AbstractGameScene.calculateScreenHeight();
  double WIDTH = AbstractGameScene.calculateScreenWidth();

  Entity entity;

  private final BiMap<TreeItem<Node>, EditableRectangle> rectBiMap = HashBiMap.create();
  private EditableRectangle currentRect = null;

  @Override
  protected void initSettings(GameSettings settings) {
    settings.setHeight((int) HEIGHT);
    settings.setWidth((int) WIDTH);
  }

  @Override
  protected void initGame() {
    entity = new Entity();
    FXGL.getGameWorld().addEntities(entity);
    var redDot = new Circle(3);
    redDot.setFill(Color.RED);
    entity.getViewComponent().addDevChild(redDot);
    entity.setX(WIDTH /4);
    entity.setY(HEIGHT /4);
  }

  @Override
  protected void initUI() {
    FXGL.getGameScene().setCursor(Cursor.DEFAULT);
  }

  public void update(){
    clear();
    for(var item:FXEditor.getFXEditor().leftColumn.getTreeItems().reversed()){
      var rect = FXGL.<GameApp>getAppCast().getRectBiMap().get(item);
      entity.getViewComponent().addChild(rect.getNode());
      entity.getViewComponent().addDevChild(rect);
      rect.setOnMousePressed(_ -> selectRect(rect));
    }
  }

  public void clear(){
//    FXEditor.getFXEditor().bottomPane.clearTransition();
    for(var v:entity.getViewComponent().getChildren()){
      var rect = EditableRectangle.getRectByNode(v);
      if(rect==null) continue;
      deSelectRect(rect);
      entity.getViewComponent().removeDevChild(rect);
    }
    entity.getViewComponent().clearChildren();
  }

  public BiMap<TreeItem<Node>, EditableRectangle> getRectBiMap() {
    return rectBiMap;
  }

  public void selectRect(EditableRectangle rect){
    deSelectRect();
    currentRect = rect;

    FXEditor.getFXEditor().leftColumn.select(FXGL.<GameApp>getAppCast().getRectBiMap().inverse().get(rect));

    rect.getStrokeDashArray().clear();
    rect.setStroke(Color.web("#039ED3"));

    rect.setOnMousePressed(e -> {
      switch (e.getButton()){
        case PRIMARY -> {
          var op = rect.clone().transform(new Point2D(e.getX(), e.getY()));//new Point2D(e.getX(), e.getY());
          var x = op.getX();
          var y = op.getY();
          var ax = rect.getRotation().getPivotX();
          var ay = rect.getRotation().getPivotY();
          var rx = rect.getX();
          var ry = rect.getY();

          rect.setOnMouseDragged(ee -> {
            var p = rect.clone().transform(new Point2D(ee.getX(), ee.getY()));//new Point2D(ee.getX(), ee.getY());
            var dx = p.getX() - x;
            var dy = p.getY() - y;
            rect.setX(rx + dx);
            rect.setY(ry + dy);
            rect.getRotation().setPivotX(ax + dx);
            rect.getRotation().setPivotY(ay + dy);

            rect.update();
          });
        }
        case SECONDARY -> deSelectRect(rect);
      }
    });
  }

  public void deSelectRect(){
    if(currentRect!=null)
      deSelectRect(currentRect);
    currentRect = null;
  }

  public void deSelectRect(EditableRectangle rect){
    currentRect = null;
    if(rect == null) return;
    rect.setStroke(null);
    rect.setOnMouseDragged(null);
    rect.setOnMousePressed(_ -> selectRect(rect));
  }
}
