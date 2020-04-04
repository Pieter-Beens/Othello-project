package nl.hanze.game.client.scenes.games;

import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.utils.Popup;

import java.util.ArrayList;

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

    /**
     * @author Pieter Beens
     */

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

    public void setPlayer1(Player player1) {
        this.players[1] = player1;
    }

    public void setPlayer2(Player player2) {
        this.players[0] = player2;
    }

    public Player getActivePlayer() {
        return players[turnCounter%2];
    }

    public Player getInactivePlayer() {
        return players[(turnCounter+1)%2];
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

    public void endGame() {
        System.out.println("Neither player was able to move, so the game has ended!");
        gameHasEnded = true;
        String msg;
        if (players[0].getScore() > players[1].getScore()) {
            msg = players[0].getName() + " has won!";
        } else if (players[0].getScore() < players[1].getScore()) {
            msg = players[1].getName() + " has won!";
        } else {
            msg = players[1].getName() + " and " + players[0].getName() + " have tied for second place!";
        }
        System.out.println(msg);
        Popup.display(msg, "GAME END", 300, 200);
    }
}
