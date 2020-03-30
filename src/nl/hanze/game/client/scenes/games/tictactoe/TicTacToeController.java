package nl.hanze.game.client.scenes.games.tictactoe;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;

import java.io.IOException;

public class TicTacToeController extends GameController {
    private TicTacToeModel model = new TicTacToeModel();

    public void move(ActionEvent event) {
        Button btn = (Button) event.getSource();

        int row = GridPane.getRowIndex(btn);
        int column = GridPane.getColumnIndex(btn);

        System.out.println("clicked on:" + row + ", " + column);
    }

    public void btnGoBack(ActionEvent event) throws IOException {
        loadScene("menu/offline/offline.fxml");
    }

    @Override
    protected GameModel getModel() {
        return model;
    }
}
