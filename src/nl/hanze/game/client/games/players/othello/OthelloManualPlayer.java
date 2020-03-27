package nl.hanze.game.client.games.players.othello;

import nl.hanze.game.client.util.Move;

public class OthelloManualPlayer extends OthelloPlayer {
    public OthelloManualPlayer(String ign) {
        super(ign);
    }

    @Override
    public Move move(char[][] board) {
        return new Move(this, 0, 0);
    }
}
