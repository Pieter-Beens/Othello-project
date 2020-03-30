package nl.hanze.game.client.refactor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage primaryStage;

    public static void main(String[] args) {
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
