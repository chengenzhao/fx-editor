package com.whitewoodcity.control;

import module io.vertx.core;
import module java.base;
import module javafx.controls;
import com.almasb.fxgl.dsl.FXGL;
import com.whitewoodcity.FXEditor;
import com.whitewoodcity.GameApp;
import com.whitewoodcity.javafx.jvg.JVG;
import com.whitewoodcity.node.EditableRectangle;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

public class MainMenu extends MenuBar {
  Menu fileMenu = new Menu("Files");
  MenuItem save = new MenuItem("Save");
  MenuItem load = new MenuItem("Load");

  public static final String DELETE_BUTTON_PREFIX = "deleteButton";
  public static final String NAME = "name";
  public static final String JSON = "json";
  public static final String ITEMS = "items";
  public static final String KEY_FRAMES = "keyFrames";
  public static final String INHERITANCE = "inheritance";

  public MainMenu() {
    fileMenu.getItems().addAll(save, load);
    this.getMenus().addAll(fileMenu);

    load.setOnAction(_ -> {
      var fileChooser = new FileChooser();
      fileChooser.setTitle("What file would you like to load?");
      fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jvg files", "*.jvg"));
      var window = this.getScene().getWindow();
      var files = fileChooser.showOpenMultipleDialog(window);
      if (files != null) {
        for(var file:files){
          try {
            switch (file.getName()) {
              case String s when s.toLowerCase().endsWith(".jvg") -> {
                var jsonString = Files.readString(Paths.get(file.getPath()));
                buildItem(file.getName(), jsonString);
              }
              default -> {
              }
            }
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
    });

    save.setOnAction(_ -> {
      var fileChooser = new FileChooser();
      fileChooser.setTitle("What file would you like to save?");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ajvg files", "*.ajvg"));
      fileChooser.setInitialFileName("config");
      var file = fileChooser.showSaveDialog(this.getScene().getWindow());
      if (file == null) return;
      try {
        var json = new JsonObject();
        json.put(ITEMS, buildItemJson());
//        json.put(KEY_FRAMES, FXEditor.getFXEditor().bottomPane.buildTransitionJson());
//        json.put(INHERITANCE, buildInheritanceJson());
        Files.write(Paths.get(file.getPath()), json.toString().getBytes());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void buildItem(String itemName, Object material) {
    var app = FXEditor.getFXEditor();
    var item = app.leftColumn.addNode(itemName);
    Node node = switch (material) {
      case Image image -> new ImageView(image);
      case String json -> new JVG(json).trim();
      default -> null;
    };
    if (node != null) {
      node.setOpacity(.25);
      FXGL.<GameApp>getAppCast().getRectBiMap().put(item, createRect(node));
    }
    FXGL.<GameApp>getAppCast().update();
  }

  JsonArray buildItemJson() {
    var arrayNode = new JsonArray();

    for (var item : FXEditor.getFXEditor().leftColumn.getTreeItems()) {
//      var node = FXEditor.getFXEditor().bottomPane.keyFrames.getFirst().getRectBiMap().get(item).getNode();
//      switch (node) {
//        case JVG jvg -> {
//          var json = new JsonObject();
//          json.put(JSON,new JsonArray(jvg.toJsonString()));
//          json.put(NAME,FXEditor.getFXEditor().leftColumn.getText(item));
//          arrayNode.add(json);
//        }
//        case ImageView view -> {
//          //todo
//        }
//        default -> {
//        }
//      }
    }

    return arrayNode;
  }

  public EditableRectangle createRect(Node node) {
    var rect = new EditableRectangle(node);

    switch (node) {
      case JVG jvg -> {
        var d = jvg.getDimension();
        rect.setWidth(d.getWidth());
        rect.setHeight(d.getHeight());

        var xy = jvg.getXY();
        rect.setX(xy.getX());
        rect.setY(xy.getY());
      }
      case ImageView imageView -> {
        imageView.setFitWidth(imageView.getImage().getWidth());
        imageView.setFitHeight(imageView.getImage().getHeight());
        rect.setWidth(imageView.getFitWidth());
        rect.setHeight(imageView.getFitHeight());
        rect.setX(imageView.getX());
        rect.setY(imageView.getY());
      }
      default -> {
      }
    }

    return rect;
  }

}