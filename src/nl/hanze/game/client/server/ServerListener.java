package nl.hanze.game.client.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerListener implements Runnable {

    private BufferedReader br;

    public ServerListener(Socket socket) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run(){
        try {
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
