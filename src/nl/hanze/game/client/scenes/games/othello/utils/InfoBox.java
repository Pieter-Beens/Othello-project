package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.games.GameModel;

public class InfoBox extends VBox {
    private final GameModel model;
    private final Label turnLabel;
    private final Label player1ScoreBar;
    private final Label player2ScoreBar;

    public InfoBox(GameModel model) {
        this.model = model;

        turnLabel = new Label();
        turnLabel.setMinWidth(300);
        turnLabel.setTextAlignment(TextAlignment.CENTER);
        turnLabel.setFont(new Font("Sans", 30));
        turnLabel.setStyle("-fx-font-weight: bold");

        player1ScoreBar = new Label();
        player1ScoreBar.setMinWidth(120);
        player1ScoreBar.setTextAlignment(TextAlignment.RIGHT);
        player1ScoreBar.setFont(new Font("Sans", 30));
        player1ScoreBar.setStyle("-fx-font-weight: bold");
        player1ScoreBar.setStyle("-fx-background-color: " + "#000000" + "; -fx-text-fill: " + "#FFFFFF");

        player2ScoreBar = new Label();
        player2ScoreBar.setMinWidth(120);
        player2ScoreBar.setTextAlignment(TextAlignment.RIGHT);
        player2ScoreBar.setFont(new Font("Sans", 30));
        player2ScoreBar.setStyle("-fx-font-weight: bold");
        player2ScoreBar.setStyle("-fx-background-color: " + "#FFFFFF" + "; -fx-text-fill: " + "#000000");

        getChildren().add(turnLabel);
        getChildren().add(player1ScoreBar);
        getChildren().add(player2ScoreBar);
        setAlignment(Pos.CENTER);

        Platform.runLater(this::scale);
    }

    // used to recalculate draw size of elements by checking window size
    public void scale() { // triggers when resizing
        turnLabel.setMinWidth(Main.primaryStage.getWidth() * 0.7);
        player1ScoreBar.setMinWidth(Main.primaryStage.getWidth() * 0.15);
        player2ScoreBar.setMinWidth(Main.primaryStage.getWidth() * 0.15);
    }

    public void update() {
        this.turnLabel.setText("  TURN " + model.getTurnCount() + ": " + model.getActivePlayer().getName() + "'s move");
        this.player1ScoreBar.setText(" " + model.getPlayer(1).getScore() + " ");
        this.player2ScoreBar.setText(" " + model.getPlayer(0).getScore() + " ");;
    }
}
