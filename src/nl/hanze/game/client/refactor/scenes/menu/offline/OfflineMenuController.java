package nl.hanze.game.client.refactor.scenes.menu.offline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.hanze.game.client.Application;
import nl.hanze.game.client.refactor.players.AIPlayer;
import nl.hanze.game.client.refactor.players.ManualPlayer;
import nl.hanze.game.client.refactor.players.Player;
import nl.hanze.game.client.refactor.scenes.Controller;
import nl.hanze.game.client.refactor.scenes.utils.Colors;
import nl.hanze.game.client.refactor.scenes.utils.MenuButton;
import nl.hanze.game.client.refactor.scenes.utils.MenuButtonGroup;
import nl.hanze.game.client.refactor.scenes.utils.MenuToggleButton;

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
        MenuButton size6 = new MenuButton("6x6", "6");
        MenuButton size8 = new MenuButton("8x8", "8",true);
        MenuButton size10 = new MenuButton("10x10", "10");
        MenuButton size12 = new MenuButton("12x12", "12");
        sizeMenu = new MenuButtonGroup(Arrays.asList(size6, size8, size10, size12));
        sizes.getChildren().add(sizeMenu);

        MenuButton singlePlayer = new MenuButton("P1 vs. AI", "single-player", true);
        MenuButton multiPlayer = new MenuButton("P1 vs. P2", "multi-player");
        playersMenu = new MenuButtonGroup(Arrays.asList(singlePlayer, multiPlayer));
        players.getChildren().add(playersMenu);

        start.setStyle("-fx-background-color: " + Application.BTN_COLOR + "; -fx-text-fill: " + Application.BTN_TEXT_COLOR);

        container.setStyle("-fx-background-color: " + Colors.BG_COLOR);
    }

    public void startBtnClicked(ActionEvent event) {
        int size = Integer.parseInt(sizeMenu.getActive().getValue());
        boolean isMultiPlayer = playersMenu.getActive().getValue().equals("multi-player");

        start(player1.getText(), player2.getText(), size, fullscreen.getStatus(), isMultiPlayer);
    }

    public void start(String ignPlayer1, String ignPlayer2, int boardSize, boolean fullscreen, boolean isMultiPlayer) {
        // corrects overlong and empty playernames
        if (ignPlayer1.length() > 10) ignPlayer1 = ignPlayer1.substring(0,11);
        else if (ignPlayer1.length() == 0) ignPlayer1 = "player1";
        if (ignPlayer2.length() > 10) ignPlayer2 = ignPlayer2.substring(0,11);
        else if (ignPlayer2.length() == 0) ignPlayer2 = "player2";

        Player player1 = new ManualPlayer(ignPlayer1);
        Player player2 = (isMultiPlayer) ? new ManualPlayer(ignPlayer2) : new AIPlayer(ignPlayer2);;

        System.out.println(ignPlayer1 + " " + ignPlayer2 + " " + boardSize + " " + fullscreen + " " + isMultiPlayer);
//        OthelloController othelloController = new OthelloController(Main.primaryStage, new OthelloGame(boardSize, player1, player2));
//
//        OthelloView othelloView = new OthelloView(othelloController, boardSize, fullscreen, player1, player2);
//
//        othelloController.setView(othelloView);
//
//        Main.primaryStage.setScene(new Scene(othelloView));
//        Main.primaryStage.setFullScreen(fullscreen);
    }
}
