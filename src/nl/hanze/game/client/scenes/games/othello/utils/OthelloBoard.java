package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.othello.OthelloController;
import nl.hanze.game.client.scenes.games.utils.BoardPane;
import nl.hanze.game.client.scenes.games.utils.Field;
import nl.hanze.game.client.scenes.games.utils.FieldButton;

/**
 * @author Pieter Beens
 */

public class OthelloBoard extends BoardPane {
    GameModel model;
    OthelloController controller;
    Image validMoveDot;

    public OthelloBoard(GameModel model, OthelloController controller) {
        this.model = model;
        this.controller = controller;

        this.validMoveDot = new Image("File:src/resources/validMoveDot.png");

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

    public void markValidFields() {
        int i = 0;
        for (Field[] row : model.getBoard()) {
            for (Field field : row) {
                if (field.getOwner() == null) {
                    if (field.getValidity()) {
                        FieldButton button = (FieldButton) getChildren().get(i);
                        button.setGraphic(new ImageView(validMoveDot));
                    } else {
                        FieldButton button = (FieldButton) getChildren().get(i);
                        button.setGraphic(null);
                    }
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
    }
    public OthelloController getController() { return controller; }
}