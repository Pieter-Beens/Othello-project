package nl.hanze.game.client.scenes.games.othello;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.othello.utils.OthelloBoard;
import nl.hanze.game.client.scenes.games.utils.BoardPane;
import nl.hanze.game.client.scenes.menu.online.OnlineMenuController;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Pieter Beens
 */

public class OthelloController extends GameController {
    @FXML private Label scoreLabel1;
    @FXML private Label scoreLabel2;

    public OthelloController(OthelloModel model) {
        super(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        gameBoard = new OthelloBoard(model, this);
        boardGridPane.setId("OthelloBoard");
        gameBoardPane.getChildren().add(gameBoard);
        gameTitle.setText("Othello");
        Label graphic1 = new Label("Your score");
        Label graphic2 = new Label("Opponent score");
        Font font = new Font("System Bold",24);
        graphic1.setFont(font);
        graphic2.setFont(font);
        scoreLabel1.setGraphic(graphic1);
        scoreLabel2.setGraphic(graphic2);
        gameBoard.setGridLinesVisible(true);
        drawCoordinates();
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
        gameBoard.update();
        updateScoreLabels();
        updateTurnLabel();
    }

    //TODO: refactor to use GameController.move()
    @Override
    public boolean move(Move move) {
        OthelloModel model = (OthelloModel) this.model;

        if (model.isValidMove(move)) {
            forfeitButton.setDisable(true);
            model.recordMove(move); // includes nextTurn() call
            updateViews();

            return true;
        }
        return false;
    }

    public void updateScoreLabels(){
        if(model.getPlayer(0).isThisMe()){
            scoreLabel1.setText(String.valueOf(model.getPlayer(0).getScore()));
            scoreLabel2.setText(String.valueOf(model.getPlayer(1).getScore()));
        } else{
            scoreLabel1.setText(String.valueOf(model.getPlayer(1).getScore()));
            scoreLabel2.setText(String.valueOf(model.getPlayer(0).getScore()));
        }
    }

}
