package nl.hanze.game.client.scenes.games.othello;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
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

    @FXML private OthelloBoard othelloBoard;
    @FXML private Pane gameBoardPane;
    //@FXML private InfoBox infoBox;
    //@FXML public HBox info;

    public OthelloController(OthelloModel model) {
        super(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        othelloBoard = new OthelloBoard(model, this);
        gameBoardPane.getChildren().add(othelloBoard);
        //infoBox = new InfoBox(model);
        //info.getChildren().add(infoBox);
    }

    @Override
    public void setup() {
        OthelloModel model = (OthelloModel) this.model;

        model.setup();
        model.updateFieldValidity();
        updateViews();

        if (!Main.serverConnection.hasConnection())
            acceptNewMoves();
    }

    //TODO: refactor to use GameController.updateViews() after Game Views have been made generic
    @Override
    public void updateViews() {
        othelloBoard.update();
        //infoBox.update();
    }

    //TODO: refactor to use GameController.move()
    @Override
    public void move(Move move) {
        OthelloModel model = (OthelloModel) this.model;

        if (model.isValidMove(move)) {
            forfeitButton.setDisable(true);
            model.recordMove(move); // includes nextTurn() call
            updateViews();
        }
    }

    @Override
    public BoardPane getBoardPane() {
        return othelloBoard;
    }
}
