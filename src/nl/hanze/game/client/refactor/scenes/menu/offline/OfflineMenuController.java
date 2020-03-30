package nl.hanze.game.client.refactor.scenes.menu.offline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.hanze.game.client.Application;
import nl.hanze.game.client.refactor.Main;
import nl.hanze.game.client.refactor.players.AI.AIStrategy;
import nl.hanze.game.client.refactor.players.AI.OthelloAI;
import nl.hanze.game.client.refactor.players.AI.TicTacToeAI;
import nl.hanze.game.client.refactor.players.AIPlayer;
import nl.hanze.game.client.refactor.players.Player;
import nl.hanze.game.client.refactor.scenes.Controller;
import nl.hanze.game.client.refactor.scenes.games.GameController;
import nl.hanze.game.client.refactor.scenes.games.othello.OthelloController;
import nl.hanze.game.client.refactor.scenes.games.tictactoe.TicTacToeController;
import nl.hanze.game.client.refactor.scenes.utils.Colors;
import nl.hanze.game.client.refactor.scenes.utils.MenuButton;
import nl.hanze.game.client.refactor.scenes.utils.MenuButtonGroup;
import nl.hanze.game.client.refactor.scenes.utils.MenuToggleButton;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class OfflineMenuController extends Controller implements Initializable {
    @FXML
    private VBox container;

    @FXML
    public HBox sizes;
    public MenuButtonGroup sizeMenu;

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
        sizeMenu = new MenuButtonGroup(Arrays.asList(
                new MenuButton("6x6", "6"),
                new MenuButton("8x8", "8", true),
                new MenuButton("10x10", "10"),
                new MenuButton("12x12", "12"))
        );
        sizes.getChildren().add(sizeMenu);

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
    }

    public void startBtnClicked(ActionEvent event) throws IOException {
        int size = Integer.parseInt(sizeMenu.getActive().getValue());
        String game = gamesMenu.getActive().getValue();
        boolean isMultiPlayer = playersMenu.getActive().getValue().equals("multi-player");

        GameController.start(player1.getText(), player2.getText(), size, game, fullscreen.getStatus(), isMultiPlayer);
    }
}
