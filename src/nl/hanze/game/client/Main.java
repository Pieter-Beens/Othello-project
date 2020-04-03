package nl.hanze.game.client;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.server.ServerConnection;

/**
 * @author Roy Voetman
 */
public class Main extends Application {
    public static Stage primaryStage;
    public static ServerConnection serverConnection;

    public static void main(String[] args) {
        serverConnection = new ServerConnection();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(350);
        primaryStage.setTitle("OtHello World");

        Main.primaryStage = primaryStage;

        Controller.loadScene("start/start.fxml");
    }
}
