package nl.hanze.game.client.scenes.menu.offline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OfflineMenuController extends Controller implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /*
        playersMenu = new MenuButtonGroup(Arrays.asList(
                new MenuButton("P1 vs. AI", "single-player", true),
                new MenuButton("P1 vs. P2", "multi-player"))
        );
        players.getChildren().add(playersMenu);

        gamesMenu = new MenuButtonGroup(Arrays.asList(
                new MenuButton("TicTacToe", "tictactoe", true),
                new MenuButton("Othello", "othello"))
        );
        games.getChildren().add(gamesMenu);

        // Styling
        start.setStyle("-fx-background-color: " + Colors.BTN_COLOR + "; -fx-text-fill: " + Colors.BTN_TEXT_COLOR);
        container.setStyle("-fx-background-color: " + Colors.BG_COLOR);

         */
    }

    public void startBtnClicked(ActionEvent event) throws IOException {
        ToggleButton selectedGame = (ToggleButton) games.getSelectedToggle();
        String game = (String) selectedGame.getUserData();

        ToggleButton selectedGameMode = (ToggleButton) gameModes.getSelectedToggle();
        boolean isMultiplayer = selectedGameMode.getUserData().equals("multi-player");

       GameController.start(player1.getText(), player2.getText(), game, fullscreen.isSelected(), isMultiplayer);
    }
}
