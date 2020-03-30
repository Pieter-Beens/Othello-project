package nl.hanze.game.client.games;

import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.games.utils.Field;
import nl.hanze.game.client.util.Move;

import java.util.Random;

public class OthelloGame extends Game {


    private Player activePlayer;
    private int turnCounter;
    public Field[][] board;

    public static final int[][] directions = {{1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}};

    public OthelloGame(int boardSize, Player... players) {
        super(boardSize, players);

        turnCounter = 1;

        board = new Field[boardSize][boardSize];
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                board[r][c] = new Field();
            }
        }

        setupGame();
    }


    public void setupGame() {
        // sets up the opening state of the game with two fields of each color in the middle of the board
        getField(boardSize/2, boardSize/2).setOwner(players.get(0));
        getField(boardSize/2 - 1, boardSize/2 - 1).setOwner(players.get(0));
        getField(boardSize/2 - 1, boardSize/2).setOwner(players.get(1));
        getField(boardSize/2, boardSize/2 - 1).setOwner(players.get(1));

        // randomises starting player
        activePlayer = players.get(new Random().nextInt(2));
        activePlayer.setStartingColors();
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public int getTurnCount() {
        return turnCounter;
    }

    public Field getField(int columnID, int rowID) {
        return board[columnID][rowID];
    }

    @Override
    public int getMinAmountOfPlayers() {
        return 2;
    }

    @Override
    public int getMaxAmountOfPlayers() {
        return 2;
    }

    @Override
    public int getBoardSize() {
        return boardSize;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean validMove(Move move) {
        return true;
    }

    @Override
    public void recordMove(Move move) {
        board[move.getX()][move.getY()].setOwner(activePlayer);

        notifyObservers(move);
    }

    @Override
    public Player getWinner() {
        return null;
    }
}
