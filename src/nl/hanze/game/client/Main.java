package nl.hanze.game.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
        primaryStage.setTitle("Two Player Games");

        Main.primaryStage = primaryStage;

        primaryStage.setResizable(false);

        Controller.loadScene("start/start.fxml");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

}
