package nl.hanze.game.client.server.roy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class SocketClient implements Runnable, Observable {
    private String ip;
    private int port;
    private Socket socket;
    private List<Observer> observers;
    private BlockingQueue<String> queue;
    private PrintWriter out;
    private BufferedReader in;
    private List<String> ignoredResp = Arrays.asList(
            "Strategic Game Server Fixed [Version 1.1.0]",
            "(C) Copyright 2015 Hanzehogeschool Groningen"
    );

    public SocketClient(BlockingQueue<String> queue) {
        this.queue = queue;
        this.observers = new ArrayList<>();
    }

    public void connect() throws IOException {
        this.socket = new Socket(ip, port);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            connect();
        } catch (IOException e) { e.printStackTrace(); }

        String command;
        String response;

        while (true) {
            while ((command = this.queue.poll()) != null) {
                out.println(command);
                out.flush();
                System.out.println("send command: " + command);
            }

            try {
                while (in.ready() && (response = in.readLine()) != null) {
                    if (ignoredResp.contains(response)) {
                        continue;
                    }

                    notifyObservers(response);
                }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String s) {
        for(Observer o : observers) o.commandResponse(s);
    }
}
