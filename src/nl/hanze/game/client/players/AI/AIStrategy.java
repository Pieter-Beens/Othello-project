package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.utils.Field;

/**
 * An interface that all AI's should implement. This interface has only
 * one method which is used to determine a new mode. Using an interface assures this functionality
 * is open for extensibility (Strategy Pattern).
 *
 * @author Roy Voetman
 */
public interface AIStrategy {
    public Move determineNextMove(Field[][] board, Player player, Player opponent);
}
