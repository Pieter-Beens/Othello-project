package nl.hanze.game.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.hanze.game.client.server.roy.SocketClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main extends Application {
    public static Stage primaryStage;
    public static BlockingQueue<String> commandQueue;
    public static SocketClient client;

    public static void main(String[] args) {
        commandQueue = new LinkedBlockingQueue<>();
        client = new SocketClient(commandQueue);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(350);

        Parent root = FXMLLoader.load(getClass().getResource("scenes/start/start.fxml"));
        primaryStage.setTitle("OtHello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Main.primaryStage = primaryStage;
    }
}
