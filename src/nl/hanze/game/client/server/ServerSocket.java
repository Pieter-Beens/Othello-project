package nl.hanze.game.client.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerSocket implements Runnable {
    private Socket socket;
    private Scanner scanner;

    public ServerSocket(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.scanner = new Scanner(System.in);
    }

    public String login(String ign) throws IOException {
        String result = sendCommand("login " + ign);

        System.out.println("x");//result.equals("OK"));
        return result;//.equals("OK");
    }
    public String logout() throws IOException {
        String result = sendCommand("logout");

        System.out.println("x");//result.equals("OK"));
        return result;//.equals("OK");
    }

    public boolean subscribe(String gameType) throws IOException {
        String result = sendCommand("subscribe " + gameType);
        return result.equals("OK");
    }

    public List<String> getPlayerList() {
        return null;
    }

    public List<String> getGameList() throws IOException {
        String result = sendCommand("get gamelist'");
        List<String> gamelist = new ArrayList<>();
        gamelist.add(result);
        return gamelist;
        //return result.equals("OK");
    }

    private String sendCommand(String command) throws IOException {
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
        out.println(command);
        out.flush();

        return scanner.nextLine();
    }
    @Override
    public void run(){
        try {
            this.login("user1");
            //System.out.println(this.getGameList());
            //socket.sendCommand("login user1");
            //System.out.println(this.sendCommand("get gamelist"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        try{
            ServerSocket socket = new ServerSocket("127.0.0.1",7789);
            Thread t = new Thread(socket);
            t.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
