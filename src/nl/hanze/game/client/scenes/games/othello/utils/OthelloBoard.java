package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import nl.hanze.game.client.scenes.games.othello.OthelloController;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;
import nl.hanze.game.client.scenes.games.utils.BoardPane;
import nl.hanze.game.client.scenes.games.utils.Field;
import nl.hanze.game.client.scenes.games.utils.FieldButton;

/**
 * @author Pieter Beens
 */

public class OthelloBoard extends BoardPane {
    Image validMoveDot;

    public OthelloBoard(OthelloModel model, OthelloController controller) {
        super(model, controller, String.valueOf(Color.TRANSPARENT));
        this.validMoveDot = new Image("File:src/resources/validMoveDot.png");
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

    public OthelloController getController() { return (OthelloController) controller; }
}