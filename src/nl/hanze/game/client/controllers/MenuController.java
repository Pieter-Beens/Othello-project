package nl.hanze.game.client.controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.hanze.game.client.games.OthelloGame;
import nl.hanze.game.client.games.TicTacToeGame;
import nl.hanze.game.client.games.players.othello.OthelloAIPlayer;
import nl.hanze.game.client.games.players.othello.OthelloManualPlayer;
import nl.hanze.game.client.games.players.othello.OthelloPlayer;
import nl.hanze.game.client.games.players.tictactoe.TicTacToeAIPlayer;
import nl.hanze.game.client.games.players.tictactoe.TicTacToeManualPlayer;
import nl.hanze.game.client.games.players.tictactoe.TicTacToePlayer;
import nl.hanze.game.client.views.OthelloView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MenuController implements Controller {

    private Stage primaryStage;

    public MenuController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void startBtnClicked(String game, String ignPlayer1, String ignPlayer2, int boardSize, boolean fullscreen, boolean isMultiPlayer) {
        // corrects overlong and empty playernames
        if (ignPlayer1.length() > 10) ignPlayer1 = ignPlayer1.substring(0,11);
        else if (ignPlayer1.length() == 0) ignPlayer1 = "player1";
        if (ignPlayer2.length() > 10) ignPlayer2 = ignPlayer2.substring(0,11);
        else if (ignPlayer2.length() == 0) ignPlayer2 = "player2";

        try {
            Method method = this.getClass().getMethod("start" + game, String.class, String.class, int.class, boolean.class, boolean.class);
            method.invoke(this, ignPlayer1, ignPlayer2, boardSize, fullscreen, isMultiPlayer);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            System.out.println("No public method with name: start" + game + " in MenuController");
        }
    }

    public void startTicTacToe(String ignPlayer1, String ignPlayer2, int boardSize, boolean fullscreen, boolean isMultiPlayer) {
        System.out.println("TicTacToe");
        TicTacToePlayer player1 = new TicTacToeManualPlayer(ignPlayer1);
        TicTacToePlayer player2 = (isMultiPlayer) ? new TicTacToeManualPlayer(ignPlayer2) : new TicTacToeAIPlayer(ignPlayer2);;

        TicTacToeController ticTacToeController = new TicTacToeController(primaryStage, new TicTacToeGame(player1, player2));

        System.out.println("TODO: create tic tac toe board");
    }

    public void startOthello(String ignPlayer1, String ignPlayer2, int boardSize, boolean fullscreen, boolean isMultiPlayer) {
        System.out.println("Othello");
        OthelloPlayer player1 = new OthelloManualPlayer(ignPlayer1);
        OthelloPlayer player2 = (isMultiPlayer) ? new OthelloManualPlayer(ignPlayer2) : new OthelloAIPlayer(ignPlayer2);;

        OthelloController othelloController = new OthelloController(primaryStage, new OthelloGame(boardSize, player1, player2));
        OthelloView othelloView = new OthelloView(othelloController, boardSize, fullscreen, player1, player2);

        othelloController.setView(othelloView);

        primaryStage.setScene(new Scene(othelloView));
        primaryStage.setFullScreen(fullscreen);
    }
}
