package nl.hanze.game.client.games.players.tictactoe;

import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.util.Move;

public class AIPlayer extends TicTacToePlayer {
    public AIPlayer(String ign) {
        super(ign);
    }

    @Override
    public Move move(char[][] board) {
        return new Move(this, 0, 0);
    }
}
