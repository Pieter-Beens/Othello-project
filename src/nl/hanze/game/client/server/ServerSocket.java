package nl.hanze.game.client.server;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerSocket {
    private final Socket socket;
    ServerCommunicator serverCommunicator;

    public ServerSocket(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        serverCommunicator = new ServerCommunicator(socket);
    }

    private void close() throws IOException {
        serverCommunicator.close();
        if(!serverCommunicator.isAlive()) socket.close();
    }

    public void login(String ign) {
        serverCommunicator.sendCommand("login " + ign);
    }

    private void logout() {
        serverCommunicator.sendCommand("logout");
    }

    public void subscribe(String gameType) {
        serverCommunicator.sendCommand("subscribe " + gameType);
    }

    public void getPlayerList() {
        serverCommunicator.sendCommand("get playerlist");
    }

    public void getGameList() {
        serverCommunicator.sendCommand("get gamelist");
    }

    public void challenge(String player, String game){
        serverCommunicator.sendCommand("challenge "+player+" "+game);
    }

    public void challengeAccept(int id){
        serverCommunicator.sendCommand("challenge accept "+id);
    }

    public void help(){
        serverCommunicator.sendCommand("help");
    }

    public void help(String s){
        serverCommunicator.sendCommand("help "+s);
    }

    public void move(Move move){
        serverCommunicator.sendCommand("move "+ move.getRow() * 8 + move.getColumn()); //TODO: get "8" directly from GameModel for generic use
    }

    public void forfeit(){
        serverCommunicator.sendCommand("forfeit");
    }

    public void addObserver(Observer o){
        serverCommunicator.addObserver(o);
    }

    public void removeObserver(Observer o){
        serverCommunicator.removeObserver(o);
    }

    public static void main(String[] args){
        try{
            ServerSocket serverSocket = new ServerSocket("127.0.0.1",7789);

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
            serverSocket.logout();
            serverSocket.close();

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
