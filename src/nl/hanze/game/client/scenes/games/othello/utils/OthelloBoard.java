package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
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
    Image validMoveDot = new Image("File:src/resources/validMoveDot.png");

    /**
     * OthelloBoard's constructor simply calls the abstract parent's constructor.
     * @param model The data model containing all variables for this board.
     * @param controller The associated controller for this View class.
     */
    public OthelloBoard(GameModel model, OthelloController controller) {
        super(model, controller);
    }

    /**
     * Implements the BoardPane interface's enableValidField class by, rather than enabling buttons, marking the buttons
     * which represent valid moves.
     */
    @Override
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

    /**
     * Updates the FieldButton objects in this board by checking the information provided by the OthelloModel. Sets the
     * stone graphics on the right fields and sets the background on the Field where the most recent move was played.
     */
    @Override
    public void update() {
        for (FieldButton[] row : fieldButtons) for(Node fieldNode : row){
                FieldButton fieldButton = (FieldButton) fieldNode;
                try {
                    // mark fields with the proper stone png
                    Field fieldModel = model.getField(fieldButton.getRowID(), fieldButton.getColumnID());
                    Player fieldOwner  = fieldModel.getOwner();
                    ImageView iv = new ImageView(fieldOwner.getReversiImage());
                    fieldButton.setGraphic(iv);

                    // mark the most recently occupied field
                    fieldButton.setStyle((fieldModel.getRecentMove()) ? "-fx-background-color: #CA5007" : "-fx-background-color: transparent");
                } catch (NullPointerException ignore) {}
        }

        disableAllFields();
    }

    /**
     * This method prevents all FieldButton objects from receiving input. While the effect of a move is being calculated
     * and during the turns of an AI or Remote player, a Local player should not be able to input any moves.
     */
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