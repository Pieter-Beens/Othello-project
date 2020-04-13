package nl.hanze.game.client.scenes.games.tictactoe.utils;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.tictactoe.TicTacToeController;
import nl.hanze.game.client.scenes.games.utils.BoardPane;
import nl.hanze.game.client.scenes.games.utils.FieldButton;

/**
 * BoardPane representing the Tic Tac Toe Board
 *
 * @author Roy Voetman
 */
public class TicTacToeBoard extends BoardPane {
    /**
     * Constructs a TicTacToeBoard
     *
     * @author Roy Voetman
     * @param model The GameModel associated with the Tic Tac Toe game.
     * @param controller The corresponding controller of the game.
     */
    public TicTacToeBoard(GameModel model, TicTacToeController controller) {
        super(model, controller);
        disableAllFields();
    }

    /**
     * Redraw the board.
     *
     * @author Roy Voetman
     */
    @Override
    public void update() {
        for (FieldButton[] row : fieldButtons) {
            for(Node fieldNode : row) {
                FieldButton fieldButton = (FieldButton) fieldNode;
                try {
                    Player fieldOwner  = model.getField(fieldButton.getRowID(), fieldButton.getColumnID()).getOwner();
                    ImageView iv = new ImageView(fieldOwner.getTictactoeImage());
                    fieldButton.setGraphic(iv);
                } catch (NullPointerException ignore) {}
            }
        }

        // Disable all fields after a redraw. When a manual player has gets the turn
        // all valid fields will be enabled.
        disableAllFields();
    }

    /**
     * In Tic Tac Toe all fields with no owner are a valid field.
     *
     * @author Roy Voetman
     */
    public void enableValidFields() {
        enableAllFields();
    }
}
