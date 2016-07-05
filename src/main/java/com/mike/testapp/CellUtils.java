/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mike.testapp;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author Mike-MSI
 */
public class CellUtils {
    public static class HBoxCell extends HBox {
          Label label = new Label();
          Button button = new Button();

          HBoxCell(String labelText, String buttonText) {
               super();

               label.setText(labelText);
               label.setMaxWidth(Double.MAX_VALUE);
               HBox.setHgrow(label, Priority.ALWAYS);

               button.setText(buttonText);

               this.getChildren().addAll(label, button);
          }
     }
}
