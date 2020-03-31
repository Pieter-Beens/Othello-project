package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;
import nl.hanze.game.client.scenes.utils.Colors;

public class InfoBox extends HBox {
    private final OthelloModel model;
    private final Label turnLabel;
    private final Label player1ScoreBar;
    private final Label player2ScoreBar;

    public InfoBox(OthelloModel model) {
        this.model = model;

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
        player1ScoreBar.setStyle("-fx-background-color: " + Colors.BTN_COLOR + "; -fx-text-fill: " + Colors.BTN_TEXT_COLOR);

        player2ScoreBar = new Label();
        player2ScoreBar.setMinWidth(200);
        player2ScoreBar.setTextAlignment(TextAlignment.RIGHT);
        player2ScoreBar.setFont(new Font("Sans", 30));
        player2ScoreBar.setStyle("-fx-font-weight: bold");
        player2ScoreBar.setStyle("-fx-background-color: " + Colors.BTN_ACTIVE_COLOR + "; -fx-text-fill: " + Colors.BTN_ACTIVE_TEXT_COLOR);

        getChildren().add(turnLabel);
        getChildren().add(player1ScoreBar);
        getChildren().add(player2ScoreBar);
        setAlignment(Pos.CENTER);

        Platform.runLater(this::scale);
    }

    // used to recalculate draw size of elements by checking window size
    public void scale() { // triggers when resizing
        turnLabel.setMinWidth(Main.primaryStage.getWidth() / 2);
        player1ScoreBar.setMinWidth(Main.primaryStage.getWidth() / 4);
        player2ScoreBar.setMinWidth(Main.primaryStage.getWidth() / 4);
    }

    public void update() {
        this.turnLabel.setText("  TURN " + model.getTurnCount() + ": " + model.getActivePlayer().getName() + "'s move");
        this.player1ScoreBar.setText(" " + model.getPlayer(0).getName() + ": " + model.getPlayer(0).getScore());
        this.player2ScoreBar.setText(" " + model.getPlayer(1).getName() + ": " + model.getPlayer(1).getScore());
    }
}
