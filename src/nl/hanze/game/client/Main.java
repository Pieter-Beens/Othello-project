package nl.hanze.game.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.hanze.game.client.server.Client;

public class Main extends Application {
    public static Stage primaryStage;
    public static Client client;

    public static void main(String[] args) {
        client = new Client();

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
