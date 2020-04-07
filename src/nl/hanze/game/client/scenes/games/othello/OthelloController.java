package nl.hanze.game.client.scenes.games.othello;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.othello.utils.InfoBox;
import nl.hanze.game.client.scenes.games.othello.utils.OthelloBoard;
import nl.hanze.game.client.scenes.games.utils.BoardPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Pieter Beens
 */

public class OthelloController extends GameController {
    @FXML
    public HBox info;

    private OthelloBoard boardPane;

    private InfoBox infoBox;

    public OthelloController(OthelloModel model) {
        super(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        boardPane = new OthelloBoard(model, this);
        infoBox = new InfoBox(model);

        boardContainer.getChildren().add(boardPane);
        info.getChildren().add(infoBox);
    }

    @Override
    public void setup() {
        OthelloModel model = (OthelloModel) this.model;

        model.setup();
        model.updateFieldValidity();
        updateViews();
        boardPane.markValidFields();
    }

    //TODO: refactor to use GameController.updateViews() after Game Views have been made generic
    @Override
    public void updateViews() {
        boardPane.update();
        infoBox.update();
    }

    //TODO: refactor to use GameController.move()
    @Override
    public void move(Move move) {
        OthelloModel model = (OthelloModel) this.model;

        if (model.isValidMove(move)) {
            forfeitButton.setDisable(true);
            model.recordMove(move); // includes nextTurn() call
            Platform.runLater(this::updateViews);
        }
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
            boardPane.markValidFields();
        }
    }

    @Override
    public BoardPane getBoardPane() {
        return boardPane;
    }
}
