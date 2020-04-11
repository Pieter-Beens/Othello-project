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
import nl.hanze.game.client.scenes.lobby.LobbyController;
import nl.hanze.game.client.scenes.menu.online.OnlineMenuController;

import java.awt.*;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Pieter Beens
 */

public class OthelloController extends GameController {
    @FXML private Label scoreLabel1;
    @FXML private Label scoreLabel2;
    Label graphic1;
    Label graphic2;

    public OthelloController(OthelloModel model) {
        super(model);
        graphic1 = new Label();
        graphic2 =  new Label();
        Font font = new Font("System Bold",24);
        graphic1.setFont(font);
        graphic2.setFont(font);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        gameBoard = new OthelloBoard(model, this);
        boardGridPane.setId("OthelloBoard");
        gameBoardPane.getChildren().add(gameBoard);
        gameTitle.setText("Othello");

        scoreLabel1.setGraphic(graphic1);
        scoreLabel2.setGraphic(graphic2);
        gameBoard.setGridLinesVisible(true);
        drawCoordinates();
    }

    @Override
    public void updateViews() {
        super.updateViews();
        updateScoreLabels();
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
        graphic1.setText(model.getPlayer(0).getName()+"'s score");
        graphic2.setText(model.getPlayer(1).getName()+"'s score");
        scoreLabel1.setText(String.valueOf(model.getPlayer(0).getScore()));
        scoreLabel2.setText(String.valueOf(model.getPlayer(1).getScore()));
    }
}
