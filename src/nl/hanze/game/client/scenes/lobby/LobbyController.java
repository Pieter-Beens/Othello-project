package nl.hanze.game.client.scenes.lobby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.Controller;

import java.io.IOException;

public class LobbyController extends Controller {
    @FXML
    private void btnLogout(ActionEvent event) throws IOException {
        Main.client.logout();

        goBack();
    }
}
