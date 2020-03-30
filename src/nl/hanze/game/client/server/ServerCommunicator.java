package nl.hanze.game.client.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


class ServerCommunicator implements Runnable, Observable {

    private final BufferedReader input;
    private final List<Observer> observers;
    //private final LinkedList<String> commandQueue;
    private final PrintWriter output;
    private boolean running = true;
    private final Object lock = new Object();
    private final Thread t;

    protected ServerCommunicator(Socket s) throws IOException {
        input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        output = new PrintWriter(s.getOutputStream(), true);
        observers = new ArrayList<>();
        //commandQueue = new LinkedList<>();
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run(){
        while(running) {
            try {
                String serverInput = input.readLine();
                if (serverInput != null) {
                    notifyObservers(serverInput);
                    if(serverInput.equals("OK")) {
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    protected void sendCommand(String s){
        commandQueue.add(s);
    }*/

    protected synchronized void sendCommand(String command) {
        synchronized (lock) {
            System.out.println("Client: " + command);
            output.println(command);
            output.flush();
            try {
                if(!command.equals("logout"))lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    protected void close(){
        running = false;
    }
    protected boolean isAlive(){
        return t.isAlive();
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
        for(Observer o : observers) o.update(s);
    }
}
