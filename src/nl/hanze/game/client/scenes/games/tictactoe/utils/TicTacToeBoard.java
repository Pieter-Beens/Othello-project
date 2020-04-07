package nl.hanze.game.client.scenes.games.tictactoe.utils;

import javafx.scene.Node;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.tictactoe.TicTacToeController;
import nl.hanze.game.client.scenes.games.utils.BoardPane;
import nl.hanze.game.client.scenes.games.utils.FieldButton;
import nl.hanze.game.client.scenes.utils.Colors;

public class TicTacToeBoard extends BoardPane {
    public TicTacToeBoard(GameModel model, TicTacToeController controller) {
        super(model, controller, Colors.BG_COLOR);

        disableAllFields();
    }

    public void update() {
        // Update color/ sign of fields
        for (Node fieldNode : this.getChildren()) {
            FieldButton fieldButton = (FieldButton) fieldNode;
            try {
                Player fieldOwner  = model.getField(fieldButton.getRowID(), fieldButton.getColumnID()).getOwner();
                String[] fieldColors = fieldOwner.getColors();
                fieldButton.setStyle("-fx-background-color: " + fieldColors[0] + "; -fx-text-fill: " + fieldColors[1]);
                fieldButton.setText(fieldOwner.getSign());
            } catch (NullPointerException ignore) {}
        }

        disableAllFields();
    }

    public TicTacToeController getController() { return (TicTacToeController) controller; }
}
