package com.whitewoodcity;

import com.whitewoodcity.control.LeftColumn;
import com.whitewoodcity.control.MainMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class FXEditor extends Application {

  public MainMenu mainMenu = new MainMenu();
  public LeftColumn leftColumn = new LeftColumn();

  private static FXEditor editorApp;

  public FXEditor() {
    editorApp = this;
  }

  public static FXEditor getFXEditor() {
    return editorApp;
  }

  @Override
  public void start(Stage stage) throws Exception {

    var gamePane = GameApp.embeddedLaunch(new GameApp());
    gamePane.setRenderFill(Color.TRANSPARENT);

    var vbox = new VBox();
    var border = new BorderPane();
    border.setCenter(gamePane);
//    border.setRight(new ScrollPane(rightColumn));
    border.setLeft(leftColumn);
//    border.setBottom(bottomPane);
    vbox.getChildren().addAll(mainMenu, border);

    stage.setScene(new Scene(vbox, Screen.getPrimary().getBounds().getWidth() * .9, Screen.getPrimary().getBounds().getHeight() * .9));

    gamePane.prefWidthProperty().bind(stage.getScene().widthProperty());
    gamePane.prefHeightProperty().bind(stage.getScene().heightProperty());
    gamePane.renderWidthProperty().bind(stage.getScene().widthProperty());
    gamePane.renderHeightProperty().bind(stage.getScene().heightProperty());

    stage.show();
  }

  static void main(String... args) {
    System.setProperty("prism.lcdtext", "false");
    FXEditor.launch(FXEditor.class, args);
  }
}
