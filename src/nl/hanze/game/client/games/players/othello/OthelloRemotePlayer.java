package nl.hanze.game.client.games.players.othello;

import nl.hanze.game.client.util.Move;

import java.net.Socket;

public class OthelloRemotePlayer extends OthelloPlayer {
    private Socket server;

    public OthelloRemotePlayer(String ign, Socket server) {
        super(ign);
        this.server = server;
    }

    @Override
    public Move move(char[][] board) {
        return new Move(this, 0, 0);
    }
}
