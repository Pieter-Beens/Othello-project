package nl.hanze.game.client.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ServerSocket implements Runnable, Observable {
    private final List<Observer> observers;
    private BlockingQueue<String> commandQueue;
    private PrintWriter out;
    private BufferedReader in;
    private boolean running = true;
    private List<String> ignoredResponses = Arrays.asList(
            "Strategic Game Server Fixed [Version 1.1.0]",
            "(C) Copyright 2015 Hanzehogeschool Groningen"
    );

    public ServerSocket(Socket socket, BlockingQueue<String> queue) throws IOException {
        commandQueue = queue;
        this.observers = Collections.synchronizedList(new ArrayList<>());
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }


    @Override
    public void run() {
        String command;
        String response;

        while (running) {
            while ((command = commandQueue.poll()) != null) {
                System.out.println(command);
                out.println(command);
                out.flush();
            }

            try {
                while (in.ready() && (response = in.readLine()) != null) {
                    if (ignoredResponses.contains(response)) {
                        continue;
                    }

                    notifyObservers(response);
                }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    protected void logout() {
        commandQueue.add("logout");
        while(true) if (commandQueue.isEmpty()) {
            running = false;
            break;
        }

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
        // Iterator of a thread safe list is not thread safe
        synchronized (observers) {
            for(Observer o : observers) o.update(s);
        }
    }
}
