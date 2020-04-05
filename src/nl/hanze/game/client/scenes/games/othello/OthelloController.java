package nl.hanze.game.client.scenes.games.othello;

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
import nl.hanze.game.client.scenes.games.othello.utils.InfoBox;
import nl.hanze.game.client.scenes.games.othello.utils.OthelloBoard;
import nl.hanze.game.client.server.ServerSocket;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Pieter Beens
 */

public class OthelloController extends GameController implements Initializable {
    @FXML
    public HBox boardContainer;

    @FXML
    public HBox info;

    @FXML
    public Button forfeitButton;

    private OthelloBoard boardPane;

    private InfoBox infoBox;

    private OthelloModel model = new OthelloModel(8); //TODO: boardSize should not vary for OthelloModel

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardPane = new OthelloBoard(model, this);
        infoBox = new InfoBox(model);

        boardContainer.getChildren().add(boardPane);
        info.getChildren().add(infoBox);
    }

    @Override
    public void setup() {
        model.setup();
        boardPane.disableAllFields();
        model.updateFieldValidity();
        updateViews();
        boardPane.enableValidFields();
    }

    @Override
    public void updateViews() {
        boardPane.update();
        infoBox.update();
    }

    //TODO: refactor to use GameController.move()
    @Override
    public void move(Move move) {
        forfeitButton.setDisable(true);
        model.recordMove(move); // includes nextTurn() call
        Platform.runLater(this::updateViews);
    }

    /**
     * @author Pieter Beens
     */
    //TODO: refactor to use GameController.acceptNewMoves()
    public void acceptNewMoves() {
        // check if the next turn belongs to an AIPlayer and if so, request a move
        if (model.getActivePlayer().getPlayerType() == PlayerType.AI && !model.gameHasEnded) {
            //move(model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer()));

            new Thread(() -> {
                move(model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer()));
                Platform.runLater(this::acceptNewMoves);
            }).start();
        }
        else if (model.getActivePlayer().getPlayerType() == PlayerType.LOCAL && !model.gameHasEnded) {
            forfeitButton.setDisable(false);
            boardPane.enableValidFields(); // makes FieldButtons representing valid moves clickable
        }
    }

    //TODO: refactor to use GameController.getModel()
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
