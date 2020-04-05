package nl.hanze.game.client.server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerConnection {
    private BlockingQueue<String> commandQueue;
    private Socket socket;
    private ServerSocket serverCommunicator;
    private Thread serverThread;

    public ServerConnection() {
        this.commandQueue = new LinkedBlockingQueue<>();
    }

    public boolean connect(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
        } catch (ConnectException e) {
            e.printStackTrace();
            return false;
        }

        serverCommunicator = new ServerSocket(socket, commandQueue);
        serverThread = new Thread(serverCommunicator);
        serverThread.start();

        return true;
    }

    public boolean hasConnection() {
        return socket != null;
    }

    public void logout() throws IOException {
        serverCommunicator.logout();
        if(!serverThread.isAlive()) socket.close();
    }

    public void login(String ign) {
        commandQueue.add("login " + ign);
    }

    public void subscribe(String gameType) {
        commandQueue.add("subscribe " + gameType);
    }

    public void getPlayerList() { commandQueue.add("get playerlist"); }

    public void getGameList() {
        commandQueue.add("get gamelist");
    }

    public void challenge(String player, String game){
        commandQueue.add("challenge \"" + player + "\" \"" + game + "\"");
    }

    public void challengeAccept(int id){
        commandQueue.add("challenge accept "+id);
    }

    public void help() {
        commandQueue.add("help");
    }

    public void help(String s) {
        commandQueue.add("help "+s);
    }

    public void move(int cell) {
        commandQueue.add("move "+ cell);
    }

    public void forfeit(){
        commandQueue.add("forfeit");
    }

    public void addObserver(Observer o) {
        if (serverCommunicator != null) {
            serverCommunicator.addObserver(o);
        }
    }

    public void removeObserver(Observer o) {
        if (serverCommunicator != null) {
            serverCommunicator.removeObserver(o);
        }
    }
}
