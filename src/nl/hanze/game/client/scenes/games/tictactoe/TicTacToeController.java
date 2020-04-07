package nl.hanze.game.client.scenes.games.tictactoe;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;
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

    //TODO: refactor to use GameController.acceptNewMoves()
    public void acceptNewMoves() {
        TicTacToeModel model = (TicTacToeModel) this.model;

        // check if the next turn belongs to an AIPlayer and if so, request a move
        if (model.getActivePlayer().getPlayerType() == PlayerType.AI && model.getCurrentState() == TicTacToeModel.State.IN_PROGRESS) {
            //move(model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer()));

            new Thread(() -> {
                Move move = model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer());
                Platform.runLater(() -> {
                    move(move);
                    acceptNewMoves();
                });
            }).start();
        }
        else if (model.getActivePlayer().getPlayerType() == PlayerType.LOCAL && model.getCurrentState() == TicTacToeModel.State.IN_PROGRESS) {
            forfeitButton.setDisable(false);
            boardPane.enableAllFields();
        }
    }

    @Override
    public void updateViews() {
        boardPane.update();
    }

    @Override
    public GameModel getModel() {
        return model;
    }

    @Override
    public BoardPane getBoardPane() {
        return boardPane;
    }

    /**
     * @author Pieter Beens
     */
    //TODO: refactor to use GameController.forfeit()
    public void forfeit(ActionEvent e) {
        model.forfeitGame(model.getActivePlayer());
        if (Main.serverConnection.hasConnection()) {
            Main.serverConnection.forfeit();
        }
    }
}
