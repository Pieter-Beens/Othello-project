package nl.hanze.game.client.server;

import nl.hanze.game.client.players.AI.utils.Move;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    private BlockingQueue<String> commandQueue;
    private Socket socket;
    private ServerSocket serverCommunicator;
    private Thread serverThread;

    public Client() {
        this.commandQueue = new LinkedBlockingQueue<>();
    }

    public void connect(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        serverCommunicator = new ServerSocket(socket, commandQueue);
        serverThread = new Thread(serverCommunicator);
        serverThread.start();
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

    public void getPlayerList() {
        commandQueue.add("get playerlist");
    }

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

    public void move(Move move) {
        commandQueue.add("move "+ move.getRow() * 8 + move.getColumn()); //TODO: get "8" directly from GameModel for generic use
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
