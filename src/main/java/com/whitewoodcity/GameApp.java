package com.whitewoodcity;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.Cursor;

public class GameApp extends GameApplication {

  @Override
  protected void initSettings(GameSettings settings) {

  }

  @Override
  protected void initUI() {
    FXGL.getGameScene().setCursor(Cursor.DEFAULT);
  }
}
