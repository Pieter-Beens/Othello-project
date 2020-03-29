package nl.hanze.game.client.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.Socket;

public class ServerListener implements Runnable {

    private final BufferedReader br;
    private final GameServer gameServer;
    private final Object lock;

    public ServerListener(Socket socket, GameServer gameServer) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.gameServer = gameServer;
        this.lock = gameServer.lock;
    }

    @Override
    public void run(){

        //noinspection InfiniteLoopStatement
        while(true) {
            //System.out.println("SL");
            try {
                String line = br.readLine();
                if(line!=null) System.out.println("Server: "+line);
                //if(line !=null && line.contains("SVR")) synchronized (lock) {lock.wait();}
                //if(line !=null && line.contains("OK")) synchronized (lock) {lock.notifyAll();}
                //gameServer.update();

            } catch (IOException  e) {
                e.printStackTrace();
            }
        }
    }
}
