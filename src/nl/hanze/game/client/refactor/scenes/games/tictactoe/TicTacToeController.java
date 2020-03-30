package nl.hanze.game.client.refactor.scenes.games.tictactoe;

import javafx.event.ActionEvent;
import nl.hanze.game.client.refactor.scenes.games.GameController;

import java.io.IOException;

public class TicTacToeController extends GameController {

    public void move(ActionEvent event) {
    }

    public void btnGoBack(ActionEvent event) throws IOException {
        loadScene("menu/offline/offline.fxml");
    }
}
