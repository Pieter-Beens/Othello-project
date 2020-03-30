package nl.hanze.game.client.refactor.players;

import nl.hanze.game.client.refactor.players.AImodules.AI;
import nl.hanze.game.client.refactor.players.AImodules.PieterAI;

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
