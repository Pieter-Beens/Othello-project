package nl.hanze.game.client.scenes.lobby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nl.hanze.game.client.scenes.Controller;

import java.io.IOException;

public class LobbyController extends Controller {
    public void btnChangeSceneClick(ActionEvent event) {
    }

    @FXML
    private void btnGoBack(ActionEvent event) throws IOException {
        loadScene("menu/online/online.fxml");
    }

    public void btnClick(ActionEvent event) {
    }
}
