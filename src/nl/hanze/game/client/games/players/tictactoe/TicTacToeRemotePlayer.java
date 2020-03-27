package nl.hanze.game.client.games.players.tictactoe;

import nl.hanze.game.client.util.Move;

import java.net.Socket;

public class TicTacToeRemotePlayer extends TicTacToePlayer {
    private Socket server;

    public TicTacToeRemotePlayer(String ign, Socket server) {
        super(ign);
        this.server = server;
    }

    @Override
    public Move move(char[][] board) {
        return new Move(this, 0, 0);
    }

    @Override
    public String[] getColors() {
        return new String[]{"@"};
    }
}
