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
  MenuItem clear = new MenuItem("Clear");
  MenuItem clearBitmap = new MenuItem("Clear bitmaps ");

  Menu settingMenu = new Menu("Settings");
  MenuItem globalSetting = new MenuItem("Dev Setting");
  MenuItem solid = new MenuItem("Solid");

  public static final String DELETE_BUTTON_PREFIX = "deleteButton";
  public static final String NAME = "name";
  public static final String JSON = "json";
  public static final String ITEMS = "items";
  public static final String KEY_FRAMES = "keyFrames";
  public static final String INHERITANCE = "inheritance";

  public MainMenu() {
    fileMenu.getItems().addAll(save, load, clear, clearBitmap);
    settingMenu.getItems().addAll(globalSetting, solid);
    this.getMenus().addAll(fileMenu, settingMenu);

    load.setOnAction(_ -> {
      var fileChooser = new FileChooser();
      fileChooser.setTitle("What file would you like to load?");
      fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("jvg & bitmap files", "*.jvg", "*.ajvg", "*.png", "*.jpg", "*.gif"));
      var window = this.getScene().getWindow();
      var file = fileChooser.showOpenDialog(window);
      if (file != null) {
        try {
          switch (file.getName()) {
            case String s when s.toLowerCase().endsWith(".frms") -> {
            }
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
    });

    clear.setOnAction(_ -> clear());
    clearBitmap.setOnAction(_ -> clearBitmap());

    save.setOnAction(_ -> {
      clearBitmap();
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

    globalSetting.setOnAction(_ -> {
//      FXGL.<GameApp>getAppCast().globalSettingStage.show();
    });

    solid.setOnAction(_ -> {
      for (var item : FXEditor.getFXEditor().leftColumn.getTreeItems()) {
//        FXEditor.getFXEditor().bottomPane.keyFrames.stream()
//            .map(kf -> kf.getRectBiMap().get(item).getNode())
//            .filter(n -> n instanceof JVG)
//            .map(JVG.class::cast)
//            .forEach(JVG::solid);
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

  public void clear() {
    var editor = FXEditor.getFXEditor();
    var list = new ArrayList<Button>();

//    var stream0 = editor.bottomPane.getChildren().stream();
    var stream1 = editor.leftColumn.getTreeItems().stream().map(TreeItem::getValue)
        .filter(HBox.class::isInstance).map(HBox.class::cast)
        .flatMap(e -> e.getChildren().stream());

//    Stream.concat(stream0, stream1)
//        .filter(Button.class::isInstance).map(Button.class::cast)
//        .filter(b -> b.getId() != null & b.getId().startsWith(DELETE_BUTTON_PREFIX))
//        .forEach(list::add);

    list.forEach(Button::fire);
  }

  public void clearBitmap() {
    var editor = FXEditor.getFXEditor();
    var list = new ArrayList<Button>();
//    editor.leftColumn.getTreeItems().stream().filter(item -> editor.bottomPane.keyFrames.getFirst().getRectBiMap().get(item).getNode() instanceof ImageView)
//        .map(TreeItem::getValue)
//        .filter(HBox.class::isInstance).map(HBox.class::cast)
//        .flatMap(e -> e.getChildren().stream()).filter(Button.class::isInstance).map(Button.class::cast)
//        .filter(b -> b.getId() != null & b.getId().startsWith(DELETE_BUTTON_PREFIX))
//        .forEach(list::add);
    list.forEach(Button::fire);
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