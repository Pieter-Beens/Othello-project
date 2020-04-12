package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.othello.OthelloController;
import nl.hanze.game.client.scenes.games.utils.BoardPane;
import nl.hanze.game.client.scenes.games.utils.Field;
import nl.hanze.game.client.scenes.games.utils.FieldButton;

/**
 * @author Pieter Beens
 */

public class OthelloBoard extends BoardPane {
    Image validMoveDot;

    public OthelloBoard(GameModel model, OthelloController controller) {
        super(model, controller);
        super.setPrefSize(680,680);
        this.validMoveDot = new Image("File:src/resources/validMoveDot.png");
    }

    public void enableValidFields() {
        int i = 0;
        for (Field[] row : model.getBoard()) for (Field field : row) {
            if (field.getOwner() == null) {
                if (field.getValidity()) {
                    FieldButton button = (FieldButton) super.getChildren().get(i);
                    button.setGraphic(new ImageView(validMoveDot));
                } else {
                    FieldButton button = (FieldButton) getChildren().get(i);
                    button.setGraphic(null);
                }
            }
            i++;
        }
    }

    @Override
    public void update() {
        // update FieldButton colors (or images??)
        for (FieldButton[] row : fieldButtons) for(Node fieldNode : row){
                FieldButton fieldButton = (FieldButton) fieldNode;
                try {
                    Field fieldModel = model.getField(fieldButton.getRowID(), fieldButton.getColumnID());
                    Player fieldOwner  = fieldModel.getOwner();
                    ImageView iv = new ImageView(fieldOwner.getReversiImage());
                    fieldButton.setStyle((fieldModel.getRecentMove()) ? "-fx-background-color: #FFFFFF" : "-fx-background-color: transparent");
                    fieldButton.setGraphic(iv);
                } catch (NullPointerException ignore) {}
        }

        disableAllFields();

        //TODO: mark and unmark recent moves (update Fields field in model.placeStone())
    }

    @Override
    public void disableAllFields() {
        int i = 0;
        for (Field[] row : model.getBoard()) {
            for (Field field : row) {
                if (field.getOwner() == null) {
                    FieldButton button = (FieldButton) getChildren().get(i);
                    button.setGraphic(null);
                }
                i++;
            }
        }
    }
}