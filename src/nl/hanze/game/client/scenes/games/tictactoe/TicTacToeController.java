package nl.hanze.game.client.scenes.games.tictactoe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.tictactoe.utils.TicTacToeBoard;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
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
    }

    public void acceptNewMoves() {
        // check if the next turn belongs to an AIPlayer and if so, request a move
        if (model.getActivePlayer().getPlayerType() == PlayerType.AI && model.getCurrentState() == TicTacToeModel.State.IN_PROGRESS) {
            //move(model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer()));

            new Thread(() -> {
                move(model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer()));
                acceptNewMoves();
            }).start();
        }
        else if (model.getActivePlayer().getPlayerType() == PlayerType.LOCAL && model.getCurrentState() == TicTacToeModel.State.IN_PROGRESS) {
            boardPane.enableAllFields();
        }
    }

    @Override
    protected void gameYourTurn(Map<String, String> map) {
        super.gameYourTurn(map);

        if (model.getActivePlayer().getPlayerType() == PlayerType.AI) {
            move(model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer()));
        } else {
            boardPane.enableAllFields();
        }
    }

    @Override
    protected void gameMove(Map<String, String> map) {
        super.gameMove(map);

        int cell = Integer.parseInt(map.get("MOVE"));
        int[] cords = Move.cellToCords(cell, model.getBoardSize());

        move(new Move(model.getPlayerByName(map.get("PLAYER")), cords[0], cords[1]));
    }

    @Override
    public void setup() {
        model.setup();
        updateViews();

        if (!Main.serverConnection.hasConnection())
            acceptNewMoves();
    }

    @Override
    public void updateViews() {
        boardPane.update();
    }

    @Override
    public GameModel getModel() {
        return model;
    }

    public void btnGoBack(ActionEvent event) throws IOException {
        goBack();
    }
}
