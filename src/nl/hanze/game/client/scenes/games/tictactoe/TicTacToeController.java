package nl.hanze.game.client.scenes.games.tictactoe;

import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.tictactoe.utils.TicTacToeBoard;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Tic Tac Toe game.
 *
 * @author Roy Voetman
 */
public class TicTacToeController extends GameController {
    /**
     * Constructs a TicTacToeController.
     *
     * @author Roy Voetman
     * @param model A TicTacToeModel
     * @param turnTime The time players have to do a move.
     */
    public TicTacToeController(TicTacToeModel model, int turnTime) {
        super(model, turnTime);
    }

    /**
     * When all FXML elements are set build the TicTacToeBoard and draw it onto the stage.
     *
     * @author Roy Voetman
     * @param location Uniform Resource of the FXML
     * @param resources Locale-specific objects
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        gameBoard = new TicTacToeBoard(model, this);
        gameBoardPane.getChildren().add(gameBoard);
        gameTitle.setText("Tic Tac Toe");
        boardGridPane.setId("TicTacToeBoard");
    }
}
