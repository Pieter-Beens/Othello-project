package nl.hanze.game.client.util;

import nl.hanze.game.client.games.utils.Field;

public interface GameObserver {
    public void update(Field[][] board, Move move);
}
