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

/**
 * @author Bart van Poele
 * This Runnable sends commands and notifies the Observers of inbound server responses
 */

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

    /**
     *
     * @param socket: the socket of the connection
     * @param queue: the Queue of the commands to be send
     * @throws IOException when IO issues occur
     */
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
                // Kill the thread if on logout
                if(command.equals("logout")) running = false;
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

    // Log out of the server
    protected void logout() {
        commandQueue.add("logout");
    }

    // Following three methods are derived from the Observers Pattern
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
        // wait for completion of operations to observers list
        synchronized (observers) {
            for(Observer o : observers) o.update(s);
        }
    }
}
