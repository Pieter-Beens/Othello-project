package nl.hanze.game.client.server;

import java.io.IOException;
import java.net.Socket;

public class ServerSocket {
    private final Socket socket;
    private final ServerCommunicator serverCommunicator;
    private final Thread communicatorThread;

    public ServerSocket(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        serverCommunicator = new ServerCommunicator(socket);
        communicatorThread = new Thread(serverCommunicator);
        communicatorThread.start();
    }


    public void login(String ign) {
        serverCommunicator.addCommand("login " + ign);
    }

    private void logoutAndClose() {
        serverCommunicator.addCommand("logout");
    }

    public void subscribe(String gameType) {
        serverCommunicator.addCommand("subscribe " + gameType);
    }

    public void getPlayerList() {
        serverCommunicator.addCommand("get playerlist");
    }

    public void getGameList() {
        serverCommunicator.addCommand("get gamelist");
    }

    public void challenge(String player, String game){
        serverCommunicator.addCommand("challenge "+player+" "+game);
    }

    public void challengeAccept(int id){
        serverCommunicator.addCommand("challenge accept "+id);
    }

    public void help(){
        serverCommunicator.addCommand("help");
    }

    public void help(String s){
        serverCommunicator.addCommand("help "+s);
    }

    public void move(Object move){
        //TODO: wat is het datatype van een zet?
        serverCommunicator.addCommand("move "+move);
    }

    public void forfeit(){
        serverCommunicator.addCommand("forfeit");
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
            Thread.sleep(1000);
            serverSocket.login("user1");
            //serverSocket.login("user2");
            Thread.sleep(1000);
            serverSocket.getGameList();
            Thread.sleep(2000);
            serverSocket.getPlayerList();
            Thread.sleep(2000);
            serverSocket.logoutAndClose();

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
