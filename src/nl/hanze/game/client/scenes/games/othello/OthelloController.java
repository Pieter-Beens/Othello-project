package nl.hanze.game.client.scenes.games.othello;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.othello.utils.OthelloBoard;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Pieter Beens & Bart van Poele
 */

public class OthelloController extends GameController {
    @FXML private Label scoreLabel1;
    @FXML private Label scoreLabel2;
    private ImageView graphic1;
    private ImageView graphic2;

    private String scoreLabel1Text;
    private String scoreLabel2Text;

    public OthelloController(OthelloModel model, int turnTime) {
        super(model, turnTime);
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

    public void setup() {
        String player1Name = model.getPlayer(0).getName();
        String player2Name = model.getPlayer(1).getName();

        if (player1Name.equals("You")) scoreLabel1Text = "Your score: ";
        else scoreLabel1Text = player1Name + "'s score: ";
        scoreLabel1.setText(scoreLabel1Text);

        if (player2Name.equals("You")) scoreLabel2Text = "Your score: ";
        else scoreLabel2Text = player2Name + "'s score: ";
        scoreLabel2.setText(scoreLabel2Text);


        //TODO: make this work and set visible/invisble on correct turns
        try {
            graphic1 = new ImageView(new Image(new FileInputStream("src/resources/blackstone.png")));
            graphic2 = new ImageView(new Image(new FileInputStream("src/resources/whitestone.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        super.setup();
    }

    @Override
    public void updateViews() {
        super.updateViews();
        updateScoreLabels();

        int boardScore = OthelloModel.getBoardScore(model.getBoard(), getActivePlayer(), model.getInactivePlayer());

        boardScoreLabel.setText("Position evaluation: " + boardScore);

        System.out.println("=====Turn " + model.getTurnCount() + ": how is the board looking for " + getActivePlayer().getName() + "? " + boardScore);
    }

    public void updateScoreLabels(){
        scoreLabel1.setText(scoreLabel1Text + model.getPlayer(0).getScore());
        scoreLabel2.setText(scoreLabel2Text + model.getPlayer(1).getScore());
    }
}
