package nl.hanze.game.client;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.hanze.game.client.controllers.Controller;
import nl.hanze.game.client.controllers.MenuController;
import nl.hanze.game.client.views.MenuView;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Stage primaryStage;

    public static final String BG_COLOR = "#f5e5ae";
    public static final String BTN_COLOR = "#940a0a";
    public static final String BTN_TEXT_COLOR = "white";
    public static final String BTN_ACTIVE_COLOR = "#050561";
    public static final String BTN_ACTIVE_TEXT_COLOR = "white";

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        MenuController menuController = new MenuController(primaryStage);
        VBox menu = new MenuView(menuController);

        primaryStage.setTitle("OtHello World");
        this.primaryStage.setScene(new Scene(menu));
        primaryStage.show();
    }
}
