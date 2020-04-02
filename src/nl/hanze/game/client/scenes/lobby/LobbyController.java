package nl.hanze.game.client.scenes.lobby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController extends Controller implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.client.getGameList();
        Main.client.getPlayerList();
    }

    @FXML
    private void btnLogout(ActionEvent event) throws IOException {
        Main.client.logout();

        goBack();
    }

    @Override
    public void update(String response) {
        super.update(response);
    }
}
