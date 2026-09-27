package com.whitewoodcity.control;

import com.almasb.fxgl.dsl.FXGL;
import com.whitewoodcity.GameApp;
import com.whitewoodcity.node.NumberField;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class RightColumn extends GridPane {

  NumberField x = new NumberField(-Integer.MAX_VALUE,Integer.MAX_VALUE);
  NumberField y = new NumberField(-Integer.MAX_VALUE,Integer.MAX_VALUE);
  NumberField w = new NumberField(0,Integer.MAX_VALUE);
  NumberField h = new NumberField(0,Integer.MAX_VALUE);

  public RightColumn() {
    this.setPadding(new Insets(10));
    this.setVgap(10);
    this.setHgap(10);
    this.add(new Label("Borders' x:"), 0, 0);
    this.add(x, 1, 0);
    this.add(new Label("Borders' y:"), 0, 1);
    this.add(y, 1, 1);
    this.add(new Label("Borders' width:"), 0, 2);
    this.add(w, 1, 2);
    this.add(new Label("Borders' height:"), 0, 3);
    this.add(h, 1, 3);
  }

  public void update(){
    var rect = FXGL.<GameApp>getAppCast().getBorders();

    x.valueProperty().unbindBidirectional(rect.xProperty());
    y.valueProperty().unbindBidirectional(rect.yProperty());
    w.valueProperty().unbindBidirectional(rect.widthProperty());
    h.valueProperty().unbindBidirectional(rect.heightProperty());

    x.valueProperty().bindBidirectional(rect.xProperty());
    y.valueProperty().bindBidirectional(rect.yProperty());
    w.valueProperty().bindBidirectional(rect.widthProperty());
    h.valueProperty().bindBidirectional(rect.heightProperty());

  }
}
