package com.whitewoodcity;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.whitewoodcity.fxgl.service.AbstractGameScene;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameApp extends GameApplication {

  double HEIGHT = AbstractGameScene.calculateScreenHeight();
  double WIDTH = AbstractGameScene.calculateScreenWidth();

  Entity entity;

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
    entity.setX(WIDTH /2);
    entity.setY(HEIGHT /3);
  }

  @Override
  protected void initUI() {
    FXGL.getGameScene().setCursor(Cursor.DEFAULT);
  }
}
