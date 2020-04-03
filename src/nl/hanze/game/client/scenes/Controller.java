package nl.hanze.game.client.scenes;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.utils.Popup;
import nl.hanze.game.client.server.Observer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * @author Roy Voetman
 */
public abstract class Controller implements Observer {
    public static Stack<String> sceneHistory = new Stack<>();
    private static Controller currentController;

    public Controller() {
        Main.client.addObserver(this);
    }

    public static Controller loadScene(String fxml) throws IOException {
        return loadScene(fxml, new FXMLLoader());
    }

    public static Controller loadScene(String fxml, FXMLLoader loader) throws IOException {
        sceneHistory.add(fxml);

        fxml = "src/nl/hanze/game/client/scenes/" + fxml;

        FileInputStream fileInputStream = new FileInputStream(new File(fxml));
        Parent parent = loader.load(fileInputStream);

        if (currentController != null)
            currentController.changeScene();

        currentController = loader.getController();

        Scene scene = new Scene(parent);
        scene.getStylesheets().add("/resources/menu.css");

        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();

        return loader.getController();
    }

    public void changeScene() {
        Main.client.removeObserver(this);
    }

    public void goBack() throws IOException {
        // Pop current scene
        sceneHistory.pop();

        // Pop previous scene
        loadScene(sceneHistory.pop());
    }

    public void update(String response) {
        if (response.contains("ERR")) {
            Platform.runLater(() -> Popup.display(response));
        }

        String[] listCommands = {"GAMELIST", "PLAYERLIST"};

        for (String command : listCommands) {
            if (response.contains(command)) {
                String string = response.replace("SVR " + command + " ", "")
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "");

                List<String> list = new ArrayList<>(Arrays.asList(string.split(", ")));

                switch (command) {
                    case "GAMELIST":
                        updateGameList(list);
                    case "PLAYERLIST":
                        updatePlayerList(list);
                }
            }
        }

        System.out.println("Controller sees: " + response);
    }

    public void updateGameList(List<String> list) { }

    public void updatePlayerList(List<String> list) { }
}
