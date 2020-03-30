package nl.hanze.game.client.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerSocket {
    private final Socket socket;
    protected final BlockingQueue<String> commandQueue;
    private final Thread communicatorThread;
    ServerCommunicator sc;
    public ServerSocket(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        commandQueue = new LinkedBlockingQueue<>();
        sc = new ServerCommunicator(socket, commandQueue);
        communicatorThread = new Thread(sc);
    }

    public void login(String ign) {
        commandQueue.add("login " + ign);
    }

    private void logout() {
        commandQueue.add("logout");
    }

    public void start(){
        communicatorThread.start();
    }

    private void close() throws IOException {
        socket.close();
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

    public void move(Object move){
        //TODO: wat is het datatype van een zet?
        commandQueue.add("move "+move);
    }

    public void forfeit(){
        commandQueue.add("forfeit");
    }

    public void addObserver(Observer o){
        sc.addObserver(o);
    }

    public void removeObserver(Observer o){
        sc.removeObserver(o);
    }

    public static void main(String[] args){
        try{
            ServerSocket serverSocket = new ServerSocket("127.0.0.1",7789);

            serverSocket.addObserver(new testObserver());
            serverSocket.start();
            //Thread.sleep(1000);
            serverSocket.login("user1");
            //serverSocket.login("user2");
            Thread.sleep(1000);
            serverSocket.getGameList();
            Thread.sleep(2000);
            serverSocket.getPlayerList();
            Thread.sleep(2000);
            serverSocket.logout();

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
