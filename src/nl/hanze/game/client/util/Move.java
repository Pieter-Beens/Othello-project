package nl.hanze.game.client.util;

import nl.hanze.game.client.games.players.Player;

public class Move {
    Player player;
    int x;
    int y;

    public Move(Player player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public Player getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
