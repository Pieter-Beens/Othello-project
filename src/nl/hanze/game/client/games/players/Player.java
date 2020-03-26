package nl.hanze.game.client.games.players;

import nl.hanze.game.client.util.Move;

public abstract class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    abstract public Move move(char[][] board);

    public abstract String[] getColors();
}
