package nl.hanze.game.client.controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.hanze.game.client.games.OthelloGame;
import nl.hanze.game.client.games.players.AIPlayer;
import nl.hanze.game.client.games.players.ManualPlayer;
import nl.hanze.game.client.games.players.othello.OthelloPlayer;
import nl.hanze.game.client.views.OthelloView;

public class OfflineMenuController implements Controller {

    private Stage primaryStage;

    public OfflineMenuController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void startBtnClicked(String ignPlayer1, String ignPlayer2, int boardSize, boolean fullscreen, boolean isMultiPlayer) {
        // corrects overlong and empty playernames
        if (ignPlayer1.length() > 10) ignPlayer1 = ignPlayer1.substring(0,11);
        else if (ignPlayer1.length() == 0) ignPlayer1 = "player1";
        if (ignPlayer2.length() > 10) ignPlayer2 = ignPlayer2.substring(0,11);
        else if (ignPlayer2.length() == 0) ignPlayer2 = "player2";

        OthelloPlayer player1 = new ManualPlayer(ignPlayer1);
        OthelloPlayer player2 = (isMultiPlayer) ? new ManualPlayer(ignPlayer2) : new AIPlayer(ignPlayer2);;

        OthelloController othelloController = new OthelloController(primaryStage, new OthelloGame(boardSize, player1, player2));

        OthelloView othelloView = new OthelloView(othelloController, boardSize, fullscreen, player1, player2);

        othelloController.setView(othelloView);

        primaryStage.setScene(new Scene(othelloView));
        primaryStage.setFullScreen(fullscreen);
    }
}
