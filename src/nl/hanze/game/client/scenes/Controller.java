package nl.hanze.game.client.scenes;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nl.hanze.game.client.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Stack;

import nl.hanze.game.client.scenes.utils.Popup;
import nl.hanze.game.client.server.Observer;

public abstract class Controller implements Observer {
    public static Stack<String> sceneHistory = new Stack<>();

    public Controller() {
        Main.client.setController(this);
    }

    public static Controller loadScene(String fxml) throws IOException {
        return loadScene(fxml, new FXMLLoader());
    }

    public static Controller loadScene(String fxml, FXMLLoader loader) throws IOException {
        sceneHistory.add(fxml);

        fxml = "src/nl/hanze/game/client/scenes/" + fxml;

        FileInputStream fileInputStream = new FileInputStream(new File(fxml));
        Parent parent = loader.load(fileInputStream);

        Scene scene = new Scene(parent);

        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();

        return loader.getController();
    }

    public void goBack() throws IOException {
        // Pop current scene
        sceneHistory.pop();

        // Pop previous scene
        loadScene(sceneHistory.pop());
    }

    public void update(String response) {
        if(response.contains("ERR")) {
            Platform.runLater(() -> Popup.display(response));
        }

        System.out.println("Controller sees: " + response);
    }
}
