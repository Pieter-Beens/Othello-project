package nl.hanze.game.client.games.players;

import nl.hanze.game.client.util.Move;

public abstract class Player {
    private String ign;

    public Player(String ign) {
        this.ign = ign;
    }

    public String getIgn() {
        return ign;
    }
    
    abstract public Move move(char[][] board);
}
