package nl.hanze.game.client.refactor.server;

import java.io.IOException;
import java.net.Socket;

public class GameServer {
    private final Socket socket;

    private final CommandQueue commandQueue;
    private final ServerListener serverListener;
    private Thread commandThread;
    private Thread listenerThread;

    public final Object lock = new Object();

    public GameServer(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        commandQueue = new CommandQueue(socket, this);
        serverListener = new ServerListener(socket, this);
        commandThread = new Thread(commandQueue);
        listenerThread = new Thread(serverListener);
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

    public void login(String ign) {
        commandQueue.addCommand("login " + ign);
    }

    public void logout() {
        commandQueue.addCommand("logout");
    }

    public void subscribe(String gameType) {
        commandQueue.addCommand("subscribe " + gameType);
    }

    public void getPlayerList() {
        commandQueue.addCommand("get playerlist");
    }

    public void getGameList() {
        commandQueue.addCommand("get gamelist");
    }

    public void challenge(String player, String game){
        commandQueue.addCommand("challenge "+player+" "+game);
    }

    public void challengeAccept(int id){
        commandQueue.addCommand("challenge accept "+id);
    }

    public void help(){
        commandQueue.addCommand("help");
    }

    public void help(String s){
        commandQueue.addCommand("help "+s);
    }

    public void move(Object move){
        //TODO: wat is het datatype van een zet?
        commandQueue.addCommand("move "+move);
    }
    public void forfeit(){
        commandQueue.addCommand("forfeit");
    }

    public void addObserver(Observer o){
        this.serverListener.addObserver(o);
    }

    public void removeObserver(Observer o){
        this.serverListener.removeObserver(o);
    }

    public static void main(String[] args){

        try{
            GameServer gameServer = new GameServer("127.0.0.1",7789);
            gameServer.start();
            gameServer.addObserver(new testObserver());
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
