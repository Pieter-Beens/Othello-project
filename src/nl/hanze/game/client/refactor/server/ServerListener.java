package nl.hanze.game.client.refactor.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerListener implements Runnable, Observable {

    private final BufferedReader br;
    private final List<Observer> observers;
    private final Object lock;

    protected ServerListener(Socket socket, GameServer gameServer) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.lock = gameServer.lock;
        observers = new ArrayList<>();
    }

    @Override
    public void run(){
        //noinspection InfiniteLoopStatement
        while(true) {
            try {
                String line = br.readLine();
                if (line != null) {
                    //System.out.println("Server: "+line);
                    notifyObservers(line);
                    //if(line.contains("SVR")) synchronized (lock) {lock.notify();}
                }
            } catch (IOException e ) {
                e.printStackTrace();
            }
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
        for(Observer o : observers) o.update(s);
    }
}
