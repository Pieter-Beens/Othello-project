package nl.hanze.game.client.util;

public interface GameObserver {
    public void update(char[][] board, Move move);
}
