package nl.hanze.game.client.scenes.games.othello;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.othello.utils.OthelloBoard;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Pieter Beens
 */

public class OthelloController extends GameController {
    @FXML private Label scoreLabel1;
    @FXML private Label scoreLabel2;
    @FXML private Label graphic1;
    @FXML private Label graphic2;

    public OthelloController(OthelloModel model, int turnTime) {
        super(model, turnTime);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        gameBoard = new OthelloBoard(model, this);
        boardGridPane.setId("OthelloBoard");
        gameBoardPane.getChildren().add(gameBoard);
        gameTitle.setText("Othello");graphic1 = new Label();
        graphic1 = new Label();
        graphic2 = new Label();
        Font font = new Font("System Bold",24);
        graphic1.setFont(font);
        graphic2.setFont(font);
        scoreLabel1.setGraphic(graphic1);
        scoreLabel2.setGraphic(graphic2);
        boardScoreLabel.setText("Intelligent score: ");
        gameBoard.setGridLinesVisible(true);
        drawCoordinates();
    }

    public void setup() {
        String player1Name = model.getPlayer(0).getName();
        String player2Name = model.getPlayer(1).getName();

        if (player1Name.equals("You")) graphic1.setText("Your score");
        else graphic1.setText(player1Name+"'s score");

        if (player2Name.equals("You")) graphic2.setText("Your score");
        else graphic2.setText(player2Name+"'s score");

        super.setup();
    }

    @Override
    public void updateViews() {
        super.updateViews();
        updateScoreLabels();

        int boardScore = OthelloModel.getBoardScore(model.getBoard(), getActivePlayer(), model.getInactivePlayer());

        boardScoreLabel.setText("Intelligent score: " + boardScore);

        System.out.println("=====Turn " + model.getTurnCount() + ": how is the board looking for " + getActivePlayer().getName() + "? " + boardScore);
    }

    public void updateScoreLabels(){
        scoreLabel1.setText(String.valueOf(model.getPlayer(0).getScore()));
        scoreLabel2.setText(String.valueOf(model.getPlayer(1).getScore()));
    }
}
