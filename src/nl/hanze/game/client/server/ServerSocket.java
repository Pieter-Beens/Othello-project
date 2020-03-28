package nl.hanze.game.client.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSocket implements Observable {
    private Socket socket;

    private ServerCommand serverCommand;
    private ServerListener serverListener;

    private List<Object> observers;

    public ServerSocket() throws IOException {
        socket = new Socket("127.0.0.1",7789);
        serverCommand = new ServerCommand(socket);
        serverListener = new ServerListener(socket);
        observers = new ArrayList<>();
    }

    public void close() throws IOException {
        socket.close();
    }

    @Override
    public void addObserver(Object o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Object o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Object observer : observers) observer.notify();
    }

    public static void main(String[] args){

        try{
            ServerSocket serverSocket = new ServerSocket();
            Thread t1 = new Thread(serverSocket.serverCommand);
            Thread t2 = new Thread(serverSocket.serverListener);
            t1.start();
            t2.start();

            serverSocket.serverCommand.login("user1");

            while(true) {}
        } catch (IOException e){
            e.printStackTrace();
        }


    }
}
