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
    protected int timeLeft;

    public static final int[][] DIRECTIONS = {{1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}};
    public static int serverTurnTime;
    public static String serverName;
    public static String skippedTurnText;

    /**
     * Sets up the board model array with the proper amount of fields depending on the game.
     * @param boardSize The proper amount of fields on the board for a certain game (defined in subclass constructors).
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

    /**
     * Defines generic operations done between turns in all games.
     */
    public void nextTurn() {
        timeLeft = turnTime;

        turnCounter++;

        boardHistory.add(board); // at the end of every turn, save the new turn's board data to boardHistory ArrayList

        updateFieldValidity();
    }

    /**
     * Defines generic operations done at setup for all games.
     */
    public void setup() {
        players[turnCounter%2].setStartingColors();

        boardHistory.add(board); // save first turn's board data to boardHistory ArrayList

        updateFieldValidity();
    }

    /**
     * @param i Defines the player requested, where 1 is the starting player and 0 is the other.
     * @return Returns one of two possible players playing a game.
     */
    public Player getPlayer(int i) {
        if (i > 1 || i < 0) throw new IndexOutOfBoundsException("No such player exists.");
        return players[i];
    }

    /**
     * Adds a player to the game on a certain index.
     * @param index Defines the index the player is set to, where 1 is the starting player and 0 is the other.
     * @param player The player being added to the game.
     */
    public void setPlayer(int index, Player player) {
        this.players[index] = player;
    }

    /**
     * @return Returns the active player, meaning the player whose turn it is.
     */
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

    /**
     * @return Returns the number of fields in a single row or column for this game.
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * @return Returns the number of the turn where the game is currently at. Turn 1 has the value 1.
     */
    public int getTurnCount() {
        return turnCounter;
    }

    /**
     * @param columnID The index of the column.
     * @param rowID The index of the row.
     * @return Returns a Field with the given column and row indexes.
     */
    public Field getField(int columnID, int rowID) {
        return board[columnID][rowID];
    }

    /**
     * @return Returns the entirety of the board array containing all Field objects.
     */
    public Field[][] getBoard() {
        return board;
    }

    /**
     * This method handles the operations taking place at the end of the game: deciding a winner, exiting the game
     * scene and displaying result messages.
     * @param timedOut True if the end of the game was caused by a time-out, leading the active player to lose at all times.
     */
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
        //If the game did not take place on the server
        //Set the result of the game, redirect to the offline menu
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

    /**
     * A method for calculating the winner in Othello.
     * @return Returns the index of the player who won.
     */
    public int determineOthelloWinner() {
        if (players[0].getScore() > players[1].getScore()) {
            return 0;
        } else if (players[0].getScore() < players[1].getScore()) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * Passes on a data request to the proper Field object.
     * @param move The move to evaluate.
     * @return Returns true is the move is valid.
     */
    public boolean isValidMove(Move move) {
        if (move == null) return false;
        Field field = this.board[move.getRow()][move.getColumn()];

        return field.getValidity();
    }

    /**
     * A method for handling forfeits, which displays a Popup which exits the game scene, effectively ending the game.
     * @param losingPlayer The player who forfeited and loses by default.
     */
    public void forfeitGame(Player losingPlayer) {
        String msg = losingPlayer.getName() + " forfeited, losing by default.";
        Popup.display(msg, "GAME END", 300, 200);
    }

    /**
     * @return True if the game has come to an end (=endGame() has been called).
     */
    public boolean hasGameEnded() {
        return gameHasEnded;
    }

    /**
     * Sets the maximum turn time used in the game, as well as the time left at the very start of the game. Adjusted by
     * -1 to account for possible server synchronisation issues.
     * @param turnTime The maximum amount of time a player can take before making a move.
     */
    public void setTurnTime(int turnTime) {
        this.timeLeft = turnTime - 1;
        this.turnTime = turnTime - 1;
    }

    /**
     * Getter for elapsed time value, used in the timer.
     *
     * @author Roy Voetman
     * @return The current value of elapsed time
     */
    public int gettimeLeft() {
        return timeLeft;
    }

    /**
     * Decreases the value of the elapsed time field.
     *
     * @author Roy Voetman
     */
    public void decreasetimeLeft() {
        timeLeft--;
    }

    public abstract void recordMove(Move move);

    public abstract void updateFieldValidity();
}
