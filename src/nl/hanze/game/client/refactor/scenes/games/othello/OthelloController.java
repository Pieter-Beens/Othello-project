package nl.hanze.game.client.refactor.scenes.games.othello;

import javafx.event.ActionEvent;
import nl.hanze.game.client.refactor.scenes.games.GameController;

import java.io.IOException;

public class OthelloController extends GameController {
    public void move(ActionEvent event) {
    }

    public void btnGoBack(ActionEvent event) throws IOException {
        loadScene("menu/offline/offline.fxml");
    }
}
