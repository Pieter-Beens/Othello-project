package nl.hanze.game.client;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.hanze.game.client.controllers.MenuController;
import nl.hanze.game.client.views.MenuView;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox menu = new MenuView(
                new MenuController()
        );

        primaryStage.setTitle("OtHello World");
        primaryStage.setScene(new Scene(menu));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
