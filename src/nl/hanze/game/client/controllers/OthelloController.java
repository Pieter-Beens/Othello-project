package nl.hanze.game.client.controllers;

import javafx.stage.Stage;
import nl.hanze.game.client.games.OthelloGame;
import nl.hanze.game.client.games.players.othello.OthelloPlayer;
import nl.hanze.game.client.views.OthelloView;

public class OthelloController {

    private Stage primaryStage;
    private OthelloGame game;
    private OthelloView view;

    public OthelloController(Stage primaryStage, OthelloGame game) {
        this.primaryStage = primaryStage;
        this.game = game;
    }

    public void setView(OthelloView view) {
        this.view = view;
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            view.scale();
        });
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            view.scale();
        });
    }

    public OthelloGame getGame() {
        return game;
    }

    public OthelloPlayer getActivePlayer() {
        return game.getActivePlayer();
    }

    public int getTurnCount() {
        return this.game.getTurnCount();
    }

    public double getStageWidth() {
        return primaryStage.getWidth();
    }

    public double getStageHeight() {
        return primaryStage.getHeight();
    }
}
