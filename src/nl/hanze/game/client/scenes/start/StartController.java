package nl.hanze.game.client.scenes.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.scenes.Controller;

import java.io.IOException;

public class StartController extends Controller {
    @FXML
    public HBox mode;

    public void btnOnline(ActionEvent event) throws IOException {
        loadScene("menu/online/online.fxml");
    }

    public void btnOffline(ActionEvent event) throws IOException {
        loadScene("menu/offline/offline.fxml");
    }
}
