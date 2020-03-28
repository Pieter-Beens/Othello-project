package nl.hanze.game.client.games.players.othello;

import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.games.players.othello.AImodules.AI;
import nl.hanze.game.client.games.players.othello.AImodules.PieterAI;
import nl.hanze.game.client.util.Move;

public class AIPlayer extends OthelloPlayer {

    AI AImodule;

    public AIPlayer(String ign) {
        super(ign);
        AImodule = new PieterAI();
    }

    @Override
    public Move move(char[][] board) {
        return new Move(this, 0, 0);
    }
}
