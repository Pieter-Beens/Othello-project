package nl.hanze.game.client.players.AI.utils;

import nl.hanze.game.client.players.Player;

/**
 * Class representing a Move on a 2D grid.
 *
 * @author Roy Voetman
 */
public class Move {
    private Player player;
    private int row;
    private int column;

    /**
     * Constructs a Move object.
     *
     * @param player The player is performing this move.
     * @param row The row in the grid.
     * @param column The column in the grid.
     */
    public Move(Player player, int row, int column) {
        this.player = player;
        this.row = row;
        this.column = column;
    }

    /**
     * Calculate the coordinates based on the cell number and board size.
     *
     * @param cell Cell number.
     * @param boardSize Size of the board.
     * @return An array of the coordinates where index 0 = row and index 1 = column.
     */
    public static int[] cellToCords(int cell, int boardSize) {
        return new int[]{cell / boardSize, cell % boardSize};
    }

    /**
     * Calculate the cell numbers based on the coordinates and board size.
     *
     * @param row The row number.
     * @param column The column number.
     * @param boardSize Size of the board.
     * @return The cell number
     */
    public static int cordsToCell(int row, int column, int boardSize) {
        return row * boardSize + column;
    }

    /**
     * Getter for the Player
     *
     * @return The Player performing this move.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter for the row number
     *
     * @return The row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for the column number
     *
     * @return The column number
     */
    public int getColumn() {
        return column;
    }

    /**
     * Create a string representation of the Object.
     *
     * @return String representation.
     */
    @Override
    public String toString() {
        return "Move{" +
                "player=" + player +
                ", row=" + row +
                ", column=" + column +
                '}';
    }
}
