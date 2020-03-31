package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.Field;

public interface AIStrategy {
    public Move determineNextMove(Field[][] board, Player player, Player opponent);
}
