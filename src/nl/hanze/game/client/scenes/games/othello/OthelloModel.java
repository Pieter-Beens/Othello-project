package nl.hanze.game.client.scenes.games.othello;

import nl.hanze.game.client.scenes.games.Cell;
import nl.hanze.game.client.scenes.games.GameModel;

public class OthelloModel extends GameModel {
    private int turnCounter;
    public Cell[][] board;

    public static final int[][] directions = {{1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}};

    public OthelloModel() {
        turnCounter = 1;

        board = new Cell[getBoardSize()][getBoardSize()];
        for (int r = 0; r < getBoardSize(); r++) {
            for (int c = 0; c < getBoardSize(); c++) {
                board[r][c] = new Cell();
            }
        }
    }

    public void setup() {
        // sets up the opening state of the game with two fields of each color in the middle of the board
        getCell(getBoardSize()/2, getBoardSize()/2).setOwner(getPlayer1());
        getCell(getBoardSize()/2 - 1, getBoardSize()/2 - 1).setOwner(getPlayer1());
        getCell(getBoardSize()/2 - 1, getBoardSize()/2).setOwner(getPlayer2());
        getCell(getBoardSize()/2, getBoardSize()/2 - 1).setOwner(getPlayer2());

        // randomises starting player
        setActivePlayer(getPlayer1());
        getActivePlayer().setStartingColors();
    }

    public int getTurnCount() {
        return turnCounter;
    }

    public void incrementTurnCount() {
        turnCounter++;
    }

    public Cell getCell(int columnID, int rowID) {
        return board[columnID][rowID];
    }

    public Cell[][] getBoard() {
        return board;
    }

    @Override
    public int getBoardSize() {
        return 8;
    }
}
