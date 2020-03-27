package nl.hanze.game.client.controllers;

import javafx.stage.Stage;
import nl.hanze.game.client.games.TicTacToeGame;
import nl.hanze.game.client.views.boards.Board;

public class TicTacToeController {
    private Stage primaryStage;
    private TicTacToeGame game;
    private Board board;

    public TicTacToeController(Stage primaryStage, TicTacToeGame game) {
        this.primaryStage = primaryStage;
        this.game = game;
    }

    public void setView(Board board) {
        this.board = board;
    }

    public TicTacToeGame getGame() {
        return game;
    }
}
