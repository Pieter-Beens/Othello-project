package nl.hanze.game.client.scenes.games;

import javafx.application.Platform;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.utils.Field;
import nl.hanze.game.client.scenes.lobby.LobbyController;
import nl.hanze.game.client.scenes.utils.Popup;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Pieter Beens
 */

public abstract class GameModel {
    final static int MAX_PLAYERS = 2; // games can have only 2 players!
    public boolean gameHasEnded = false;
    protected Player[] players = new Player[MAX_PLAYERS];
    protected int boardSize;
    protected int turnCounter = 1;
    protected Field[][] board;
    protected ArrayList<Field[][]> boardHistory = new ArrayList<>();
    public static final int[][] DIRECTIONS = {{1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}};
    public static String serverName;

    public GameModel(int boardSize) {
        this.boardSize = boardSize;

        this.board = new Field[boardSize][boardSize];
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                this.board[r][c] = new Field(r,c);
            }
        }
    }

    public void nextTurn() {
        turnCounter++;

        boardHistory.add(board); // at the end of every turn, save the new turn's board data to boardHistory ArrayList
    }

    public void setup() {
        players[turnCounter%2].setStartingColors();

        boardHistory.add(board); // save first turn's board data to boardHistory ArrayList
    }

    public Player getPlayer(int i) {
        return players[i];
    }

    public void setPlayer(int index, Player player) {
        this.players[index] = player;
    }

    public Player getActivePlayer() {
        return players[turnCounter%2];
    }

    public Player getInactivePlayer() {
        return players[(turnCounter+1)%2];
    }

    public Player getPlayerByName(String name) {
        if (players[0].getName().equals(name)) {
            return players[0];
        }

        return players[1];
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getTurnCount() {
        return turnCounter;
    }

    public Field getField(int columnID, int rowID) {
        return board[columnID][rowID];
    }

    public Field[][] getBoard() {
        return board;
    }

    public void endGameIfFinished() {
        if(hasGameEnded())
            endGame();
    }

    public void endGame()  {

        System.out.println("Neither player was able to move, so the game has ended!");
        gameHasEnded = true;
        String msg;
        int winner = determineWinner();
        if (winner == 0) {
            msg = players[0].getName() + " has won!";
        } else if (winner == 1) {
            msg = players[1].getName() + " has won!";
        } else {
            msg = "tie " + players[1].getName() + " and " + players[0].getName() + " have tied for second place!";
        }

        /**
         * @author Jasper van Dijken
         */
        //Send the result of the game, redirect to lobby
        try {
            if (!Main.serverConnection.hasConnection()) {
                Popup.display(msg); // TODO: remove all pops
                Controller.loadScene("start/start.fxml");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int determineWinner() {
        if (players[0].getScore() > players[1].getScore()) {
            return 0;
        } else if (players[0].getScore() < players[1].getScore()) {
            return 1;
        } else {
            return 2;
        }
    }

    public void forfeitGame(Player losingPlayer) {
        String msg = losingPlayer.getName() + " has forfeited.";
        Popup.display(msg, "GAME END", 300, 200);
    }

    public boolean hasGameEnded() {
        return gameHasEnded;
    }
}
