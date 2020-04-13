package nl.hanze.game.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.server.ServerConnection;

import java.io.IOException;

/**
 * @author Roy Voetman
 */
public class Main extends Application {
    public static Stage primaryStage;
    public static ServerConnection serverConnection;

    /**
     * Hanzehogeschool Groningen University of Applied Sciences HBO-ICT
     * Project Software Engineering 2.3
     * Game Framework
     *
     * @author Roy Voetman
     * @param args Arguments passed with the command line.
     */
    public static void main(String[] args) {
        serverConnection = new ServerConnection();

        launch(args);
    }

    /**
     * Starts the JavaFX application
     *
     * @author Roy Voetman
     * @param primaryStage The created primary stage.
     * @throws IOException When the start.fxml file is not found
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(350);
        primaryStage.setTitle("Two Player Games");
        primaryStage.setResizable(true);

        Main.primaryStage = primaryStage;
        Scene scene = new Scene(new Pane());
        scene.getStylesheets().add("/resources/style.css");
        Main.primaryStage.setScene(scene);
        Main.primaryStage.setMinHeight(800);
        Main.primaryStage.setMinWidth(1265);
        Controller.loadScene("start/start.fxml");


        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

}
