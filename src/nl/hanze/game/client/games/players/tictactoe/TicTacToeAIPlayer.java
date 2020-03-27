package nl.hanze.game.client.games.players.tictactoe;

import nl.hanze.game.client.util.Move;

public class TicTacToeAIPlayer extends TicTacToePlayer {
    public TicTacToeAIPlayer(String ign) {
        super(ign);
    }

    @Override
    public Move move(char[][] board) {
        return new Move(this, 0, 0);
    }

    @Override
    public String[] getColors() {
        return new String[]{"#"};
    }
}
