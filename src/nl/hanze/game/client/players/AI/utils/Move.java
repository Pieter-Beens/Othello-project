package nl.hanze.game.client.players.AI.utils;

import nl.hanze.game.client.players.Player;

/**
 * @author Roy Voetman
 */
public class Move {
    private Player player;
    private int row;
    private int column;

    public Move(Player player, int row, int column) {
        this.player = player;
        this.row = row;
        this.column = column;
    }

    public static int[] cellToCords(int cell, int boardSize) {
        return new int[]{cell / boardSize, cell % boardSize};
    }

    public static int cordsToCell(int row, int column, int boardSize) {
        return row * boardSize + column;
    }

    public Player getPlayer() {
        return player;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Move{" +
                "player=" + player +
                ", row=" + row +
                ", column=" + column +
                '}';
    }
}
