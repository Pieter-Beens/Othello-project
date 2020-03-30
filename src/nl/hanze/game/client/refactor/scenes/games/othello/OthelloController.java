package nl.hanze.game.client.refactor.scenes.games.othello;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.refactor.scenes.games.GameController;
import nl.hanze.game.client.refactor.scenes.games.GameModel;
import nl.hanze.game.client.refactor.scenes.games.othello.utils.InfoBox;
import nl.hanze.game.client.refactor.scenes.games.othello.utils.OthelloBoard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OthelloController extends GameController implements Initializable {
    @FXML
    public HBox boardContainer;

    @FXML
    public HBox info;

    private OthelloBoard board;

    private InfoBox infoBox;

    private OthelloModel model = new OthelloModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board = new OthelloBoard(model);
        infoBox = new InfoBox(model);

        boardContainer.getChildren().add(board);
        info.getChildren().add(infoBox);
    }

    @Override
    public void setup() {
        model.setup();
        infoBox.updateScore();
    }

    public void move(ActionEvent event) {
    }

    public void btnGoBack(ActionEvent event) throws IOException {
        loadScene("menu/offline/offline.fxml");
    }

    @Override
    protected GameModel getModel() {
        return model;
    }
}
