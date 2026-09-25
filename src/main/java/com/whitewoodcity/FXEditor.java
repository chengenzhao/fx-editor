package com.whitewoodcity;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class FXEditor extends Application {

  @Override
  public void start(Stage stage) throws Exception {

    var gamePane = GameApp.embeddedLaunch(new GameApp());
    gamePane.setRenderFill(Color.TRANSPARENT);

    stage.setScene(new Scene(gamePane, Screen.getPrimary().getBounds().getWidth() * .9, Screen.getPrimary().getBounds().getHeight() * .9));

    gamePane.prefWidthProperty().bind(stage.getScene().widthProperty());
    gamePane.prefHeightProperty().bind(stage.getScene().heightProperty());
    gamePane.renderWidthProperty().bind(stage.getScene().widthProperty());
    gamePane.renderHeightProperty().bind(stage.getScene().heightProperty());

    stage.show();
  }
}
