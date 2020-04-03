package nl.hanze.game.client.players;

import nl.hanze.game.client.players.AI.AIStrategy;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.Field;

public class AIPlayer extends Player {

    AIStrategy AIStrategy;

    public AIPlayer(String ign, PlayerType playerType, AIStrategy AIStrategy) {
        super(ign, playerType);
        this.AIStrategy = AIStrategy;
    }

    @Override
    public Move move(Field[][] board, Player opponent) {
        return AIStrategy.determineNextMove(board, this, opponent);
    }

    public void determineNextMove() {
        //new Thread(AIStrategy).start();
    }
}
