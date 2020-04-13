package nl.hanze.game.client.server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Bart van Poele
 * @description This class provides a simplified way of sending the server commands
 */

public class ServerConnection {
    private BlockingQueue<String> commandQueue;
    private Socket socket;
    private ServerSocket serverCommunicator;
    private Thread serverThread;

    // Construct a Queue which stores the commands until they are outbound
    public ServerConnection() {
        this.commandQueue = new LinkedBlockingQueue<>();
    }

    /**
     * Connect to the server
     * @param ip: IP of the server
     * @param port: Port of the server
     * @return Whether or not the connection establishment succeeded
     */
    public boolean connect(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
        } catch (ConnectException e) {
            return false;
        }

        serverCommunicator = new ServerSocket(socket, commandQueue);
        serverThread = new Thread(serverCommunicator);
        serverThread.start();

        return true;
    }

    /**
     *  Test for a live connection
     * @return Whether of not the connection is alive
     */
    public boolean hasConnection() {
        return socket != null;
    }

    // Logout of the server and close the socket
    public void logout() throws IOException {
        serverCommunicator.logout();
        if(!serverThread.isAlive()) socket.close();
        socket = null;
    }

    /** Log in the server using the specified username
     * @param ign: the username
     */
    public void login(String ign) {
        commandQueue.add("login " + ign);
    }

    /**
     * Start a match if an opponent is found
     * @param gameType: The game to play
     */
    public void subscribe(String gameType) {
        commandQueue.add("subscribe " + gameType);
    }

    // Request a list of all online players
    public void getPlayerList() { commandQueue.add("get playerlist"); }

    // Request a list of all available games
    public void getGameList() {
        commandQueue.add("get gamelist");
    }

    /**
     * Challenge another player
     * @param player: The player to be challenged
     * @param game: The game to be played
     */
    public void challenge(String player, String game){
        commandQueue.add("challenge \"" + player + "\" \"" + game + "\"");
    }

    /**
     * Accept a challenge
     * @param id: the ID of the match
     */
    public void challengeAccept(int id){
        commandQueue.add("challenge accept "+id);
    }

    // Request commandline usages, development only
    public void help() {
        commandQueue.add("help");
    }

    // Request commandline usages, development only
    public void help(String s) {
        commandQueue.add("help "+s);
    }

    /**
     * Send the made Move to the server
     * @param cell: the coordinate of the Move
     */
    public void move(int cell) {
        commandQueue.add("move "+ cell);
    }

    // Tell the server to give up the current game. Player loses
    public void forfeit(){
        commandQueue.add("forfeit");
    }

    // Add Observers (Classes that process the data returned by the server)
    public void addObserver(Observer o) {
        if (serverCommunicator != null) {
            serverCommunicator.addObserver(o);
        }
    }

    // Remove Observers (Classes that process the data returned by the server)
    public void removeObserver(Observer o) {
        if (serverCommunicator != null) {
            serverCommunicator.removeObserver(o);
        }
    }
}
