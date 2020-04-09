package nl.hanze.game.client.scenes.games.tictactoe.utils;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.tictactoe.TicTacToeController;
import nl.hanze.game.client.scenes.games.utils.BoardPane;
import nl.hanze.game.client.scenes.games.utils.FieldButton;
import nl.hanze.game.client.scenes.utils.Colors;

public class TicTacToeBoard extends BoardPane {
    public TicTacToeBoard(GameModel model, TicTacToeController controller) {
        super(model, controller);
        disableAllFields();
    }

    @Override
    public void update() {
        // Update color/ sign of fields
        for (FieldButton[] row : fieldButtons) for(Node fieldNode : row){
            FieldButton fieldButton = (FieldButton) fieldNode;
            try {
                Player fieldOwner  = model.getField(fieldButton.getRowID(), fieldButton.getColumnID()).getOwner();
                ImageView iv = new ImageView(fieldOwner.getTictactoeImage());
                fieldButton.setGraphic(iv);
            } catch (NullPointerException ignore) {}
        }
        disableAllFields();
    }

    public void enableValidFields() {
        enableAllFields();
    }

}
