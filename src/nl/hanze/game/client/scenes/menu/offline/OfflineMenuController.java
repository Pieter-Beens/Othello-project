package nl.hanze.game.client.scenes.menu.offline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.utils.Colors;
import nl.hanze.game.client.scenes.utils.MenuButton;
import nl.hanze.game.client.scenes.utils.MenuButtonGroup;
import nl.hanze.game.client.scenes.utils.MenuToggleButton;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class OfflineMenuController extends Controller implements Initializable {
    @FXML
    private VBox container;

    @FXML
    public HBox players;
    public MenuButtonGroup playersMenu;

    @FXML
    public HBox games;
    public MenuButtonGroup gamesMenu;

    @FXML
    public TextField player1;

    @FXML
    public TextField player2;

    @FXML
    public HBox bottomRow;

    @FXML
    public MenuToggleButton fullscreen;

    @FXML
    public Button start;

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
        String game = gamesMenu.getActive().getValue();
        boolean isMultiPlayer = playersMenu.getActive().getValue().equals("multi-player");

        GameController.start(player1.getText(), player2.getText(), game, fullscreen.getStatus(), isMultiPlayer);
    }
}
