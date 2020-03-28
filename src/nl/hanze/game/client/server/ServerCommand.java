package nl.hanze.game.client.server;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerCommand implements Runnable {
    private Socket socket;
    private BlockingQueue<String> commandQueue;

    public ServerCommand(Socket socket) throws IOException {
        this.socket = socket;
        commandQueue = new LinkedBlockingQueue<>();
    }

    public void login(String ign) throws IOException {
        commandQueue.add("login " + ign);
    }
    public void logout() throws IOException {
        commandQueue.add("logout");
    }

    public void subscribe(String gameType) throws IOException {
        commandQueue.add("subscribe " + gameType);
    }

    public void getPlayerList() {
        commandQueue.add("get playerlist");
        //return null;
    }

    public void getGameList() throws IOException {
        commandQueue.add("get gamelist");
        //return null;

    }

    private void sendCommand(String command) throws IOException {
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
        out.println(command);
        out.flush();
    }

    @Override
    public void run(){
        //TODO: process commandQueue
        try {
            //noinspection InfiniteLoopStatement
            while(true) if (!commandQueue.isEmpty()) {
                String command = commandQueue.poll();
                System.out.println("Client: " + command);
                sendCommand(command);
            }
            } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
