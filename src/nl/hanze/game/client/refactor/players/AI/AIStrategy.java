package nl.hanze.game.client.refactor.players.AI;

import nl.hanze.game.client.refactor.players.AI.utils.Move;
import nl.hanze.game.client.refactor.players.Player;
import nl.hanze.game.client.refactor.scenes.games.Cell;

public interface AIStrategy {
    public Move determineNextMove(Cell[][] board, Player player);
}
