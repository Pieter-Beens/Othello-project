package nl.hanze.game.client.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


class ServerCommunicator implements Runnable, Observable {

    private final Socket socket;
    private final BufferedReader br;
    private final BlockingQueue<String> commandQueue;
    private final List<Observer> observers;

    protected ServerCommunicator(Socket s) throws IOException {
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        socket = s;
        observers = new ArrayList<>();
        commandQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run(){
        //noinspection InfiniteLoopStatement
        while(true) {

            try {
                if (!commandQueue.isEmpty()) {
                    String command = commandQueue.poll();
                    //System.out.println("Client: " + command);
                    sendCommand(command);
                }
                String line = br.readLine();
                if (line != null) {
                    //System.out.println("Server: "+line);
                    notifyObservers(line);
                }
            } catch (IOException e ) {
                e.printStackTrace();
            }
        }
    }

    protected void addCommand(String s){
        commandQueue.add(s);
    }

    private void sendCommand(String command) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(command);
        out.flush();
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
