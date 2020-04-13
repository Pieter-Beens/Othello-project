package nl.hanze.game.client.scenes.games;

import javafx.application.Platform;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.utils.Field;
import nl.hanze.game.client.scenes.menu.offline.OfflineMenuModel;
import nl.hanze.game.client.scenes.utils.Popup;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class serves as a template class (Template method pattern)
 * for all GameModels.
 * A concrete implementation of this Model only requires
 * the controller to implement the recordMove() and updateFieldValidity() methods.
 *
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
    protected int turnTime;
    protected int elapsedTime;

    public static final int[][] DIRECTIONS = {{1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}};
    public static int serverTurnTime;
    public static String serverName;
    public static String skippedTurnText;

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
        elapsedTime = turnTime;

        turnCounter++;

        boardHistory.add(board); // at the end of every turn, save the new turn's board data to boardHistory ArrayList

        updateFieldValidity();
    }

    public void setup() {
        players[turnCounter%2].setStartingColors();

        boardHistory.add(board); // save first turn's board data to boardHistory ArrayList

        updateFieldValidity();
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

    /**
     * Returns the player that is currently waiting on its opponent.
     *
     * @author Roy Voetman
     * @return The inactive player.
     */
    public Player getInactivePlayer() {
        return players[(turnCounter+1)%2];
    }

    /**
     * Returns player 0 or 1 based on the given name.
     *
     * @author Roy Voetman
     * @param name The searched player name
     * @return A player object corresponding with the given name.
     */
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

    public static String getSkippedTurnText() {
        return skippedTurnText;
    }

    public void endGame(boolean timedOut)  {
        System.out.println("<GAME END>");
        gameHasEnded = true;


        String msg = "";
        int winner = determineOthelloWinner();
        if (timedOut) {
            winner = (turnCounter+1)%2;
            msg += "TIME-OUT: ";
        }
        if (winner == 0) {
            msg += players[0].getName() + " won with " + players[0].getScore() + " points!";
        } else if (winner == 1) {
            msg += players[1].getName() + " won with " + players[1].getScore() + " points!";
        } else {
            msg += "DRAW: " + players[1].getName() + " and " + players[0].getName() + " tied for second place!";
        }

        /**
         * @author Jasper van Dijken
         */
        //Send the result of the game, redirect to lobby
        try {
            if (!Main.serverConnection.hasConnection()) {
                //Popup.display(msg);
                OfflineMenuModel.setResultMessage(msg);
                Controller.loadScene("menu/offline/offline.fxml");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int determineOthelloWinner() {
        if (players[0].getScore() > players[1].getScore()) {
            return 0;
        } else if (players[0].getScore() < players[1].getScore()) {
            return 1;
        } else {
            return 2;
        }
    }

    public boolean isValidMove(Move move) {
        if (move == null) return false;
        Field field = this.board[move.getRow()][move.getColumn()];

        return field.getValidity();
    }

    public void forfeitGame(Player losingPlayer) {
        String msg = losingPlayer.getName() + " forfeited, losing by default.";
        Popup.display(msg, "GAME END", 300, 200);
    }

    public boolean hasGameEnded() {
        return gameHasEnded;
    }

    public void setTurnTime(int turnTime) {
        this.elapsedTime = turnTime - 1;
        this.turnTime = turnTime - 1;
    }

    /**
     * Getter for elapsed time value, used in the timer.
     *
     * @author Roy Voetman
     * @return The current value of elapsed time
     */
    public int getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Decreases the value of the elapsed time field.
     *
     * @author Roy Voetman
     */
    public void decreaseElapsedTime() {
        elapsedTime--;
    }

    public abstract void recordMove(Move move);

    public abstract void updateFieldValidity();
}
