package nl.hanze.game.client.scenes.utils;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * @source http://www.learningaboutelectronics.com/Articles/How-to-create-a-pop-up-window-in-JavaFX.php
 */
public class Popup {
    public static void display(String msg) {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Oeps, something went wrong");

        Label label1= new Label(msg);
        Button button1= new Button("Close");
        button1.setOnAction(e -> popupwindow.close());

        VBox layout= new VBox(10);

        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 260, 100);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }
}

