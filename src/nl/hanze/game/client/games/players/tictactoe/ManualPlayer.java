package nl.hanze.game.client.games.players.tictactoe;

import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.util.Move;

public class ManualPlayer extends TicTacToePlayer {

    private String symbol;

    public ManualPlayer(String ign) {
        super(ign);
        symbol = "X"; // or O, I don't know how this works
    }

    @Override
    public Move move(char[][] board) {
        return new Move(this, 0, 0);
    }

    @Override
    public String[] getColors() {
        return new String[]{symbol};
    }
}
