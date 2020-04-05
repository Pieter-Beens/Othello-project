package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.AIPlayer;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.Field;
import nl.hanze.game.client.scenes.games.othello.OthelloController;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;
import nl.hanze.game.client.scenes.utils.Colors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Pieter Beens
 */

public class OthelloBoard extends GridPane {
    OthelloModel model;
    OthelloController controller;

    public OthelloBoard(OthelloModel model, OthelloController controller) {
        this.model = model;
        this.controller = controller;

        for (int r = 0; r < model.getBoardSize(); r++) {
            for (int c = 0; c < model.getBoardSize(); c++) {
                this.add(new FieldButton(r,c), r, c);
            }
        }

        scale();
        setPadding(new Insets(5, 5, 5, 5));

        setStyle("-fx-background-image: url('src/resources/logo.png')"); //TODO: put a full board image as background
        setStyle("-fx-background-color: " + "#FFFFFF");
    }

    public void enableValidFields() {
        int i = 0;
        for (Field[] row : model.getBoard()) {
            for (Field field : row) {
                if (field.getValidity()) {
                    getChildren().get(i).setDisable(false);
                } else {
                    getChildren().get(i).setDisable(true);
                }
                i++;
            }
        }
    }

    public void disableAllFields() {
        for (Node node : getChildren()) {
            FieldButton button = (FieldButton) node;
            button.setDisable(true);
        }
    }

    // called by listener when resizing
    public void scale() { //TODO: fix this
        double smallestDimension = Main.primaryStage.getWidth();
        if (Main.primaryStage.getHeight() < smallestDimension) smallestDimension = Main.primaryStage.getHeight();

        double fieldSize = (smallestDimension / model.getBoardSize()) * 0.90;
        for (Node fieldButton : getChildren()) {
            FieldButton fb = (FieldButton) fieldButton;
            fb.setMinSize(fieldSize, fieldSize);
        }

        double fieldSpacing = (smallestDimension / model.getBoardSize()) * 0.05;
        setVgap(fieldSpacing);
        setHgap(fieldSpacing);
    }

    public void update() {
        // update FieldButton colors (or images??)
        for (Node fieldNode : this.getChildren()) {
            FieldButton fieldButton = (FieldButton) fieldNode;
            try {
//                String[] fieldColors = model.getField(fieldButton.getRowID(), fieldButton.getColumnID()).getOwner().getColors();
//                fieldButton.setStyle("-fx-background-color: " + fieldColors[0] + "; -fx-text-fill: " + fieldColors[1]);

                ImageView iv = new ImageView(model.getField(fieldButton.getRowID(), fieldButton.getColumnID()).getOwner().getReversiImage());
                //iv.setFitHeight(fieldSize);
                //iv.setFitWidth(fieldSize);
                fieldButton.setGraphic(iv);
            } catch (NullPointerException ignore) {}
        }

        //TODO: mark and unmark recent moves (update Fields field in model.placeStone())

        disableAllFields();
    }
    public OthelloController getController() { return controller; }
}