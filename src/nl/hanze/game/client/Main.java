package nl.hanze.game.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.server.ServerConnection;

import java.io.File;
import java.io.FileInputStream;

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


    //Setup Stage and its attributes
    @Override
    public void start(Stage primaryStage) throws Exception {
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


        //Close background process on close
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

}
