package nl.hanze.game.client.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.hanze.game.client.Application;
import nl.hanze.game.client.controllers.Controller;
import nl.hanze.game.client.controllers.MenuController;
import nl.hanze.game.client.views.utils.MenuButton;
import nl.hanze.game.client.views.utils.MenuButtonGroup;
import nl.hanze.game.client.views.utils.MenuToggleButton;

import java.util.Arrays;

public class MenuView extends VBox implements View {

    private MenuController controller;

    public MenuView(MenuController controller) {
        this.controller = controller;

        this.setMinSize(500, 500);

        HBox titleRow = new HBox();

        Label boardsizeLabel = new Label("CHOOSE NUMBER OF FIELDS");
        MenuButton size6 = new MenuButton("6x6", "6");
        MenuButton size8 = new MenuButton("8x8", "8",true);
        MenuButton size10 = new MenuButton("10x10", "10");
        MenuButton size12 = new MenuButton("12x12", "12");
        MenuButtonGroup sizeMenu = new MenuButtonGroup(Arrays.asList(size6, size8, size10, size12));

        Label playersLabel = new Label("CHOOSE NUMBER OF PLAYERS");
        MenuButton singlePlayer = new MenuButton("P1 vs. AI", "single-player", true);
        MenuButton multiPlayer = new MenuButton("P1 vs. P2", "multi-player");
        MenuButtonGroup playersRow = new MenuButtonGroup(Arrays.asList(singlePlayer, multiPlayer));

        Label namesLabel = new Label("CHANGE PLAYER NAMES");
        HBox namesRow = new HBox();
        TextField player1Input = new TextField("player1");
        player1Input.setMaxSize(150, 35);
        TextField player2Input = new TextField("player2");
        player1Input.setMaxSize(150, 35);
        namesRow.getChildren().add(player1Input);
        namesRow.getChildren().add(player2Input);
        namesRow.setAlignment(Pos.TOP_CENTER);
        namesRow.setSpacing(30);

        HBox bottomRow = new HBox();
        MenuToggleButton fullscreenToggle = new MenuToggleButton("Fullscreen OFF", "Fullscreen ON");

        Button startButton = new Button();
        startButton.setText("START");
        startButton.setMinSize(100, 100);
        startButton.setStyle("-fx-background-color: " + Application.BTN_COLOR + "; -fx-text-fill: " + Application.BTN_TEXT_COLOR);
        startButton.setOnMouseClicked(e -> {
            int size = Integer.parseInt(sizeMenu.getActive().getValue());
            boolean IsMultiPlayer = playersRow.getActive().getValue().equals("multi-player");

            controller.startBtnClicked(player1Input.getText(), player2Input.getText(), size, fullscreenToggle.getStatus(), IsMultiPlayer);
        });

        bottomRow.getChildren().add(fullscreenToggle);
        bottomRow.getChildren().add(startButton);
        bottomRow.setAlignment(Pos.CENTER_RIGHT);
        bottomRow.setSpacing(10);


        this.getChildren().add(titleRow);
        this.getChildren().add(boardsizeLabel);
        this.getChildren().add(sizeMenu);
        this.getChildren().add(playersLabel);
        this.getChildren().add(playersRow);
        this.getChildren().add(namesLabel);
        this.getChildren().add(namesRow);
        this.getChildren().add(bottomRow);

        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + Application.BG_COLOR);
    }
}
