package nl.hanze.game.client.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameServer implements Observable {
    private final Socket socket;

    private final CommandQueue commandQueue;
    private final ServerListener serverListener;
    private Thread commandThread;
    private Thread listenerThread;

    private final List<Object> observers;
    private final List<String> onlinePlayers;
    private final List<String> onlineGames;

    public final Object lock = new Object();

    public GameServer(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        commandQueue = new CommandQueue(socket, this);
        serverListener = new ServerListener(socket, this);
        observers = new ArrayList<>();
        onlinePlayers = new ArrayList<>();
        onlineGames = new ArrayList<>();
        commandThread = new Thread(commandQueue);
        listenerThread = new Thread(serverListener);
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
        for(Object observer : observers) observer.notifyAll();
    }

    public void start(){
        listenerThread.start();
        commandThread.start();
    }

    public void close() throws IOException {
        this.logout();
        listenerThread.stop();
        commandThread.stop();
        socket.close();
    }

    public boolean login(String ign) {
        commandQueue.addCommand("login " + ign);
        return false;
    }

    public boolean logout() {
        commandQueue.addCommand("logout");
        return false;
    }

    public boolean subscribe(String gameType) {
        commandQueue.addCommand("subscribe " + gameType);
        return false;
    }

    public List<String> getPlayerList() {
        commandQueue.addCommand("get playerlist");
        return null;
    }
    public void addPlayers(String... players){
        onlinePlayers.addAll(Arrays.asList(players));
    }

    public List<String> getGameList() {
        commandQueue.addCommand("get gamelist");
        return null;
    }

    public static void main(String[] args){

        try{
            GameServer gameServer = new GameServer("127.0.0.1",7789);
            gameServer.start();

            Thread.sleep(1000);
            gameServer.login("user1");

            Thread.sleep(1000);
            gameServer.getGameList();
            Thread.sleep(2000);
            gameServer.getPlayerList();
            Thread.sleep(2000);
            gameServer.close();

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
