package nl.hanze.game.client.views.boards.othello;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import nl.hanze.game.client.Application;
import nl.hanze.game.client.controllers.OthelloController;
import nl.hanze.game.client.games.players.Player;

public class InfoBox extends HBox {
    private final OthelloController controller;
    private final Player player1;
    private final Player player2;
    private final Label turnLabel;
    private final Label player1ScoreBar;
    private final Label player2ScoreBar;

    public InfoBox(OthelloController controller, int boardSize, Player player1, Player player2) {
        this.controller = controller;
        this.player1 = player1;
        this.player2 = player2;

        turnLabel = new Label();
        turnLabel.setMinWidth(400);
        turnLabel.setTextAlignment(TextAlignment.CENTER);
        turnLabel.setFont(new Font("Sans", 30));
        turnLabel.setStyle("-fx-font-weight: bold");

        player1ScoreBar = new Label();
        player1ScoreBar.setMinWidth(200);
        player1ScoreBar.setTextAlignment(TextAlignment.RIGHT);
        player1ScoreBar.setFont(new Font("Sans", 30));
        player1ScoreBar.setStyle("-fx-font-weight: bold");
        player1ScoreBar.setStyle("-fx-background-color: " + Application.BTN_COLOR + "; -fx-text-fill: " + Application.BTN_TEXT_COLOR);

        player2ScoreBar = new Label();
        player2ScoreBar.setMinWidth(200);
        player2ScoreBar.setTextAlignment(TextAlignment.RIGHT);
        player2ScoreBar.setFont(new Font("Sans", 30));
        player2ScoreBar.setStyle("-fx-font-weight: bold");
        player2ScoreBar.setStyle("-fx-background-color: " + Application.BTN_ACTIVE_COLOR + "; -fx-text-fill: " + Application.BTN_ACTIVE_TEXT_COLOR);

        getChildren().add(turnLabel);
        getChildren().add(player1ScoreBar);
        getChildren().add(player2ScoreBar);
        setAlignment(Pos.CENTER);

        Platform.runLater(this::scale);
        updateScore();
    }

    // used to recalculate draw size of elements by checking window size
    public void scale() { // triggers when resizing
        turnLabel.setMinWidth(controller.getStageWidth() / 2);
        player1ScoreBar.setMinWidth(controller.getStageWidth() / 4);
        player2ScoreBar.setMinWidth(controller.getStageWidth() / 4);
    }

    public void updateScore() {
        this.turnLabel.setText("  TURN " + controller.getTurnCount() + ": " + controller.getActivePlayer().getName() + "'s move");
        this.player1ScoreBar.setText(" " + player1.getName() + ": " + player1.getScore());
        this.player2ScoreBar.setText(" " + player2.getName() + ": " + player2.getScore());
    }
}
