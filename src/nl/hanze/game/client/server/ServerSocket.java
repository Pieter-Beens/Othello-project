package nl.hanze.game.client.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ServerSocket {
    private Socket socket;
    private Scanner scanner;

    public ServerSocket(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.scanner = new Scanner(System.in);
    }

    public boolean login(String ign) throws IOException {
        String result = sendCommand("login " + ign);

        return result.equals("OK");
    }

    public boolean subscribe(String gameType) throws IOException {
        String result = sendCommand("subscribe " + gameType);

        return result.equals("OK");
    }

    public List<String> getPlayerList() {
        return null;
    }

    public List<String> getGameList() {
        return null;
    }

    private String sendCommand(String command) throws IOException {
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
        out.println(command);
        out.flush();

        return scanner.nextLine();
    }
}
