package nl.hanze.game.client.games.players;

import nl.hanze.game.client.games.players.AImodules.AI;
import nl.hanze.game.client.games.players.AImodules.PieterAI;
import nl.hanze.game.client.util.Move;

public class AIPlayer extends Player {

    AI AImodule;

    public AIPlayer(String ign) {
        super(ign);
        AImodule = new PieterAI();
    }

//    @Override
//    public Move move(char[][] board) {
//        return new Move(this, 0, 0);
//    }

    @Override
    public String[] getColors() {
        return new String[0];
    }
}
