package nl.hanze.game.client.scenes;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.utils.Popup;
import nl.hanze.game.client.server.interpreter.Interpreter;
import nl.hanze.game.client.server.Observer;
import nl.hanze.game.client.server.interpreter.ServerResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Abstract controller that all controllers in the MVC pattern should extend.
 *
 * @author Roy Voetman
 */
public abstract class Controller implements Observer {
    public static Stack<String> sceneHistory = new Stack<>();
    private static Controller currentController;

    /**
     * Constructs a Controller and adds itself as an observer to the GameServer events.
     *
     * @author Roy Voetman
     */
    public Controller() {
        Main.serverConnection.addObserver(this);
    }

    /**
     * Load a FXML with a default FXMLLoader.
     *
     * @author Roy Voetman
     * @param fxml Path to the fxml file based in the scenes folder.
     * @throws IOException When previous scene FXML can not be found.
     */
    public static void loadScene(String fxml) throws IOException {
        loadScene(fxml, new FXMLLoader());
    }

    /**
     * Load a FXML with a injected FXMLLoader param.
     *
     * @author Roy Voetman
     * @param fxml Path to the fxml file based in the scenes folder.
     * @param loader A FXML loader instance to be used to parse the FXML file.
     * @return The controller associated with this scene.
     * @throws IOException When previous scene FXML can not be found.
     */
    public static Controller loadScene(String fxml, FXMLLoader loader) throws IOException {
        // Try to create an file input stream based on the provided fxml path.
        FileInputStream fileInputStream = new FileInputStream(new File("src/nl/hanze/game/client/scenes/" + fxml));
        Parent parent = loader.load(fileInputStream);

        // Call change scene on the previous controller.
        if (currentController != null)
            currentController.changeScene();

        // Override the previous controller with the new controller.
        currentController = loader.getController();

        // Don't push game scenes to the history stack
        // And don't push the same scene twice
        if (!(currentController instanceof GameController) && (sceneHistory.isEmpty() || !fxml.equals(sceneHistory.peek()))) {
            sceneHistory.push(fxml);
        }

        // Set new scene as the new root scene.
        Main.primaryStage.getScene().setRoot(parent);
        Main.primaryStage.show();

        // Return the controller
        return loader.getController();
    }

    /**
     * When the scene is changed, remove the associated controller from the observer list.
     *
     * @author Roy Voetman
     */
    public void changeScene() {
        Main.serverConnection.removeObserver(this);
    }

    /**
     * Load the previous scene.
     *
     * @throws IOException When previous scene FXML can not be found.
     */
    public void goBack() throws IOException {
        // Pop current scene
        sceneHistory.pop();

        // Pop previous scene
        loadScene(sceneHistory.pop());
    }

    /**
     * This method is called whenever the game server send a response.
     *
     * @param response The response from the server
     */
    public void update(String response) {
        // When it is an error show a popup window.
        if (response.contains("ERR")) {
            Platform.runLater(() -> Popup.display(response));
        }

        // Parse the response
        ServerResponse resp = Interpreter.parse(response);

        // Determine which method to call.
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
                // Do not notify illegal moves.
                if (!resp.getMap().get("DETAILS").equals("Illegal move"))
                    Platform.runLater(() -> gameMove(resp.getMap()));
                break;
            case "GAME WIN":
                Platform.runLater(() -> gameWin(resp.getMap()));
                break;
            case "GAME LOSS":
                Platform.runLater(() -> gameLoss(resp.getMap()));
                break;
            case "GAME DRAW":
                Platform.runLater(() -> gameDraw(resp.getMap()));
                break;
        }

        // Log command being send.
        System.err.println(response);
    }

    /**
     * A game list is received from the server.
     * Child classes are open to override this method.
     *
     * @param list  A list of all the supported games.
     */
    public void updateGameList(List<String> list) { }

    /**
     * A player list is received from the server.
     * Child classes are open to override this method.
     *
     * @param list A list of all the players currently in the Lobby.
     */
    public void updatePlayerList(List<String> list) { }

    /**
     * A challenge request is received from the server.
     * Child classes are open to override this method.
     *
     * @param map A map with all the argument from this response.
     */
    public void gameChallenge(Map<String, String> map) { }

    /**
     * A game match is received from the server.
     * Child classes are open to override this method.
     *
     * @param map A map with all the argument from this response.
     */
    public void gameMatch(Map<String, String> map) { }

    /**
     * The your turn event is received from the server.
     * Child classes are open to override this method.
     *
     * @param map A map with all the argument from this response.
     */
    public void gameYourTurn(Map<String, String> map) { }

    /**
     * A move from either your or your opponent is received from the server.
     * Child classes are open to override this method.
     *
     * @param map A map with all the argument from this response.
     */
    public void gameMove(Map<String, String> map) { }

    /**
     * The game win event is received from the server.
     * Child classes are open to override this method.
     *
     * @param map A map with all the argument from this response.
     */
    public void gameWin(Map<String, String> map) { }

    /**
     * The game loss event is received from the server.
     * Child classes are open to override this method.
     *
     * @param map A map with all the argument from this response.
     */
    public void gameLoss(Map<String, String> map) { }

    /**
     * The game draw event is received from the server.
     * Child classes are open to override this method.
     *
     * @param map A map with all the argument from this response.
     */
    public void gameDraw(Map<String, String> map) { }
}
