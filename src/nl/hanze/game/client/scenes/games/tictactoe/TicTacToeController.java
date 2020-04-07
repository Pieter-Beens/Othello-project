package nl.hanze.game.client.scenes.games.tictactoe;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.tictactoe.utils.TicTacToeBoard;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
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

    @FXML
    public Button forfeitButton;

    private TicTacToeBoard boardPane;

    private TicTacToeModel model = new TicTacToeModel(3);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        if (model.isValidMove(move)) {
            forfeitButton.setDisable(true);
            model.recordMove(move);
            updateViews();
            //TODO: updateViews should be Platform.runLater(this::updateViews) because move() is called from non-JavaFX User threads
            //not using runLater here causes the result Popup to malfunction
            //(also because the view is not updated before the Popup, the last move is never seen)
            //currently however, using runLater breaks the game completely and I don't know why
            //vs AI, the AI somehow completes the game (and manages to lose to itself?)
            //in multiplayer, the buttons are never enabled to allow player2 to make a move
        }
    }

    //TODO: refactor to use GameController.acceptNewMoves()
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
            forfeitButton.setDisable(false);
            boardPane.enableAllFields();
        }
    }

    @Override
    public void gameYourTurn(Map<String, String> map) {
        super.gameYourTurn(map);

        if (model.getActivePlayer().getPlayerType() == PlayerType.AI) {
            Move move = model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer());
            move(move);

            if (Main.serverConnection.hasConnection())
                Main.serverConnection.move(Move.cordsToCell(move.getRow(), move.getColumn(), model.getBoardSize()));
        } else {
            forfeitButton.setDisable(false);
            boardPane.enableAllFields();
        }
    }

    @Override
    public void gameMove(Map<String, String> map) {
        super.gameMove(map);

        int cell = Integer.parseInt(map.get("MOVE"));
        int[] cords = Move.cellToCords(cell, model.getBoardSize());

        System.out.println(Arrays.toString(cords) + "----------------------------");
        move(new Move(model.getPlayerByName(map.get("PLAYER")), cords[0], cords[1]));
    }

    @Override
    public void updateViews() {
        boardPane.update();
    }

    @Override
    public GameModel getModel() {
        return model;
    }

    /**
     * @author Pieter Beens
     */
    //TODO: refactor to use GameController.forfeit()
    public void forfeit() {
        model.forfeitGame(model.getActivePlayer());
        if (Main.serverConnection.hasConnection()) {
            Main.serverConnection.forfeit();
        }
    }
}
