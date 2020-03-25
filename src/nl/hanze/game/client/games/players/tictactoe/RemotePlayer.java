package nl.hanze.game.client.games.players.tictactoe;

import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.util.Move;

import java.net.Socket;

public class RemotePlayer extends TicTacToePlayer {
    private Socket server;

    public RemotePlayer(String ign, Socket server) {
        super(ign);
        this.server = server;
    }

    @Override
    public Move move(char[][] board) {
        return new Move(this, 0, 0);
    }
}
