package nl.hanze.game.client.scenes.games.tictactoe;

import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.tictactoe.utils.TicTacToeBoard;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Roy Voetman
 */
public class TicTacToeController extends GameController {

    public TicTacToeController(TicTacToeModel model, int turnTime) {
        super(model, turnTime);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        gameBoard = new TicTacToeBoard(model, this);
        gameBoardPane.getChildren().add(gameBoard);
        gameTitle.setText("Tic Tac Toe");
        boardGridPane.setId("TicTacToeBoard");
    }
}
