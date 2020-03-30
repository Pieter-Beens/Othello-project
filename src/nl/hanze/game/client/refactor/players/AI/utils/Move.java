package nl.hanze.game.client.refactor.players.AI.utils;

import nl.hanze.game.client.refactor.players.Player;

public class Move {
    private Player player;
    private int row;
    private int column;

    public Move(Player player, int row, int column) {
        this.player = player;
        this.row = row;
        this.column = column;
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
}
