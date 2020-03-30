package nl.hanze.game.client.players;

import java.net.Socket;

public class RemotePlayer extends Player {
    private Socket server;

    public RemotePlayer(String ign, Socket server) {
        super(ign);
        this.server = server;
    }

//    @Override
//    public Move move(char[][] board) {
//        return new Move(this, 0, 0);
//    }
}
