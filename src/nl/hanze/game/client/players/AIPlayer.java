package nl.hanze.game.client.players;

import nl.hanze.game.client.players.AI.AIStrategy;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.Cell;

public class AIPlayer extends Player {

    AIStrategy AIStrategy;

    public AIPlayer(String ign, AIStrategy AIStrategy) {
        super(ign);
        this.AIStrategy = AIStrategy;
    }

    public Move move(Cell[][] board) {
        return AIStrategy.determineNextMove(board, this);
    }
}
