package nl.hanze.game.client.scenes;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.utils.Popup;
import nl.hanze.game.client.server.Interpreter;
import nl.hanze.game.client.server.Observer;
import nl.hanze.game.client.server.ServerResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Roy Voetman
 */
public abstract class Controller implements Observer {
    public static Stack<String> sceneHistory = new Stack<>();
    private static Controller currentController;

    public Controller() {
        Main.serverConnection.addObserver(this);
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
        //scene.getStylesheets().add("/resources/menu.css");

        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();

        return loader.getController();
    }

    public void changeScene() {
        Main.serverConnection.removeObserver(this);
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

        ServerResponse resp = Interpreter.parse(response);

        switch (resp.getCommand()) {
            case "GAMELIST":
                Platform.runLater(() -> updateGameList(resp.getList()));
                break;
            case "PLAYERLIST":
                Platform.runLater(() -> updatePlayerList(resp.getList()));
                break;
            case "GAME CHALLENGE":
                Platform.runLater(() -> gameChallenge(resp.getMap()));
                break;
            case "GAME MATCH":
                Platform.runLater(() -> gameMatch(resp.getMap()));
                break;
            case "GAME YOURTURN":
                Platform.runLater(() -> gameYourTurn(resp.getMap()));
                break;
            case "GAME MOVE":
                Platform.runLater(() -> gameMove(resp.getMap()));
                break;
        }

        System.err.println(response);
    }

    protected void updateGameList(List<String> list) { }

    protected void updatePlayerList(List<String> list) { }

    protected void gameChallenge(Map<String, String> map) { }

    protected void gameMatch(Map<String, String> map) { }

    protected void gameYourTurn(Map<String, String> map) { }

    protected void gameMove(Map<String, String> map) { }
}
