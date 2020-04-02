package nl.hanze.game.client.scenes.menu.offline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameController;

import java.io.IOException;

public class OfflineMenuController extends Controller {
    @FXML
    public TextField player1;

    @FXML
    public TextField player2;

    @FXML
    public Button start;

    @FXML
    public ToggleGroup games;

    @FXML
    public ToggleGroup gameModes;

    @FXML
    public CheckBox fullscreen;

    public void startBtnClicked(ActionEvent event) throws IOException {
        ToggleButton selectedGame = (ToggleButton) games.getSelectedToggle();
        String game = (String) selectedGame.getUserData();

        ToggleButton selectedGameMode = (ToggleButton) gameModes.getSelectedToggle();
        boolean isMultiplayer = selectedGameMode.getUserData().equals("multi-player");

       GameController.start(player1.getText(), player2.getText(), game, fullscreen.isSelected(), isMultiplayer);
    }

    public void btnGoBack(ActionEvent event) throws IOException {
        goBack();
    }
}
