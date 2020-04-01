package nl.hanze.game.client.server;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerSocket {
    private Socket socket;
    public BlockingQueue<String> commandQueue;
    ServerCommunicator serverCommunicator;
    private Thread serverThread;

    public void connect(String ip, int port) throws IOException{
        socket = new Socket(ip, port);
        commandQueue = new LinkedBlockingQueue<>();
        serverCommunicator = new ServerCommunicator(socket, commandQueue);
        serverThread = new Thread(serverCommunicator);
        serverThread.start();
    }

    private void close() throws IOException {
        serverCommunicator.close();
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
        commandQueue.add("challenge "+player+" "+game);
    }

    public void challengeAccept(int id){
        commandQueue.add("challenge accept "+id);
    }

    public void help(){
        commandQueue.add("help");
    }

    public void help(String s){
        commandQueue.add("help "+s);
    }

    public void move(Move move){
        commandQueue.add("move "+ move.getRow() * 8 + move.getColumn()); //TODO: get "8" directly from GameModel for generic use
    }

    public void forfeit(){
        commandQueue.add("forfeit");
    }

    public void addObserver(Observer o){
        serverCommunicator.addObserver(o);
    }

    public void removeObserver(Observer o){
        serverCommunicator.removeObserver(o);
    }

    /*
    public static void main(String[] args){

        try{
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.connect("127.0.0.1",7789);

            serverSocket.addObserver(new testObserver());

            Thread.sleep(2000);
            serverSocket.login("user1");
            Thread.sleep(2000);
            serverSocket.getGameList();
            Thread.sleep(2000);
            serverSocket.subscribe("Reversi");
            Thread.sleep(2000);
            serverSocket.getPlayerList();
            Thread.sleep(2000);
            serverSocket.close();

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }*/
}
