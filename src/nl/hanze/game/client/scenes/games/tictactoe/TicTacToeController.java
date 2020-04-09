package nl.hanze.game.client.scenes.games.tictactoe;

import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.tictactoe.utils.TicTacToeBoard;
import nl.hanze.game.client.scenes.games.utils.BoardPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Roy Voetman
 */
public class TicTacToeController extends GameController {

    public TicTacToeController(TicTacToeModel model) {
        super(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        boardPane = new TicTacToeBoard(model, this);
        gameBoardPane.getChildren().add(boardPane);
        gameTitle.setText("Tic Tac Toe");
    }

    @Override
    public void setup() {
        model.setup();
        updateViews();

        if (!Main.serverConnection.hasConnection())
            acceptNewMoves();
    }

    //TODO: refactor to use GameController.move()
    @Override
    public boolean move(Move move) {
        TicTacToeModel model = (TicTacToeModel) this.model;

        if (model.isValidMove(move)) {
            forfeitButton.setDisable(true);
            model.recordMove(move);
            updateViews();

            if (!Main.serverConnection.hasConnection()) {
                model.endGameIfFinished();
            }

            return true;
        }

        return false;
    }

    @Override
    public void updateViews() {
        ((TicTacToeBoard)boardPane).update();
        turnLabel.setText(model.getActivePlayer().getName());
    }

    @Override
    public BoardPane getBoardPane() {
        return boardPane;
    }
}
