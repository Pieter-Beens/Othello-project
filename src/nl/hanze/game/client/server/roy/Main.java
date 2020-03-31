package nl.hanze.game.client.server.roy;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static BlockingQueue<String> commandQueue;
    public static SocketClient client;

    public static void main(String[] args) throws IOException, InterruptedException {
        commandQueue = new LinkedBlockingQueue<>();

        client = new SocketClient(commandQueue);
        client.setIp("127.0.0.1");
        client.setPort(7789);

        new Thread(client).start();

        Thread.sleep(1000);

        Controller controller = new Controller();

        commandQueue.put("login roy");
    }
}
