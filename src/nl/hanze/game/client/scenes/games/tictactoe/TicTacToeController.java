package nl.hanze.game.client.scenes.games.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
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
    @FXML
    public HBox info;

    private TicTacToeBoard boardPane;

    public TicTacToeController(TicTacToeModel model) {
        super(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        boardPane = new TicTacToeBoard(model, this);

        boardContainer.getChildren().add(boardPane);
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
    public void move(Move move) {
        TicTacToeModel model = (TicTacToeModel) this.model;

        if (model.isValidMove(move)) {
            forfeitButton.setDisable(true);
            model.recordMove(move);
            updateViews();

            if (!Main.serverConnection.hasConnection()) {
                model.endGameIfFinished();
            }
        }
    }

    @Override
    public void updateViews() {
        boardPane.update();
    }

    @Override
    public BoardPane getBoardPane() {
        return boardPane;
    }
}
