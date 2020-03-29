package nl.hanze.game.client.server;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandQueue implements Runnable {
    private final Socket socket;
    private final GameServer gameserver;
    private final Object lock;
    private final BlockingQueue<String> commandQueue;

    public CommandQueue(Socket socket, GameServer gameServer) {
        this.socket = socket;
        commandQueue = new LinkedBlockingQueue<>();
        this.gameserver = gameServer;
        this.lock = gameServer.lock;
    }

    public void addCommand(String command){
        commandQueue.add(command);
    }

    private void sendCommand(String command) {
        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(command);
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void run(){

        //noinspection InfiniteLoopStatement
        while(true) {
            //System.out.println("CQ");
            if (!commandQueue.isEmpty()) {
                String command = commandQueue.poll();
                System.out.println("Client: " + command);
                sendCommand(command);
            }
        }
    }
}
