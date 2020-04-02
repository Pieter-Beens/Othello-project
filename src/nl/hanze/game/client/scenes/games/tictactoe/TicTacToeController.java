package nl.hanze.game.client.scenes.games.tictactoe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.tictactoe.utils.TicTacToeBoard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Roy Voetman
 */
public class TicTacToeController extends GameController implements Initializable {
    @FXML
    public HBox boardContainer;

    @FXML
    public HBox info;

    private TicTacToeBoard boardPane;

    private TicTacToeModel model = new TicTacToeModel(3);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardPane = new TicTacToeBoard(model, this);

        boardContainer.getChildren().add(boardPane);
    }

    @Override
    public void move(Move move) {
        if (model.isValidMove(move)) {
            model.recordMove(move);
        }

        updateViews();

        if (model.getCurrentState() == TicTacToeModel.State.IN_PROGRESS) {
            // check if the next turn belongs to an AIPlayer and if so, request a move
            if (model.getActivePlayer().getPlayerType() == PlayerType.AI) {
                move(model.getActivePlayer().move(model.getBoard(), model.getDisabledPlayer()));
            }
        }
    }

    @Override
    public void setup() {
        model.setup();
        updateViews();
    }

    @Override
    public void updateViews() {
        boardPane.update();
    }

    @Override
    protected GameModel getModel() {
        return model;
    }

    public void btnGoBack(ActionEvent event) throws IOException {
        goBack();
    }
}
