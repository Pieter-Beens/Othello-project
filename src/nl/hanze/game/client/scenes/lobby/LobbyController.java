package nl.hanze.game.client.scenes.lobby;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.utils.PlayerRow;
import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyController extends Controller implements Initializable {

    @FXML
    public ToggleButton btnPlayAsAI;

    @FXML
    public ToggleButton btnPlayAsManual;

    @FXML
    public Button btnFullscreen;

    @FXML
    public HBox gameBtnHBox;

    //@FXML
    //public GridPane gameBtnGrid;

    @FXML
    public TableView<PlayerRow> playersTable;

    @FXML
    public TableColumn<PlayerRow, String> nameColumn;

    @FXML
    public TableColumn<PlayerRow, String> gamesColumn;

    private String gameListString = "";

    private TableUpdater tableUpdater;

    // Contains either 'AI' or 'Manual' to indicate as whom the user wants to play as
    private String playAs = "";

    // Indicates whether the user wants to play fullscreen, default is 'false'
    private Boolean fullscreen = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.client.getGameList();
        Main.client.getPlayerList();

        tableUpdater = new TableUpdater();
        new Thread(tableUpdater).start();

        nameColumn.prefWidthProperty().bind(playersTable.widthProperty().multiply(0.8));
        gamesColumn.prefWidthProperty().bind(playersTable.widthProperty().multiply(0.2));
    }

    /**
     * @author Jasper van Dijken
     */
    private void updateGameBtnGroup() {
        //Split gameListString into a list
        List<String> games = new ArrayList<String>(Arrays.asList(gameListString.split(", ")));
        //ArrayList which will contain the buttons
        ArrayList<Button> buttons = new ArrayList<>();

        //For each game, create button and add to buttons array
        for (String game : games) {
            buttons.add(new Button(game));
        }

        //For each button in buttons, add to gameBtnHbox
        for (Button btn : buttons) {
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    btn.setStyle("-fx-background-color: #ACACAC;; -fx-text-fill: #FFFF;");
                    btn.setPrefHeight(30);
                    btn.setPrefWidth(100);
                    gameBtnHBox.getChildren().add(btn);
                }
            });


        }

    }

    @FXML
    private void clickedBtnAsAI(ActionEvent event) throws IOException {
        //Set playAs to 'AI', make playAsAI button active, make playAsManual inactive
        playAs = "AI";
        btnPlayAsAI.setStyle("-fx-background-color: #46AF4E; -fx-text-fill: #FFFF;");
        btnPlayAsManual.setStyle("-fx-background-color: #ACACAC; -fx-text-fill: #FFFF;");
    }

    @FXML
    private void clickedBtnAsManual(ActionEvent event) throws IOException {
        //Set playAs to 'Manual', make playAsManual button active, make playAsAI inactive
        playAs = "Manual";
        btnPlayAsManual.setStyle("-fx-background-color: #46AF4E; -fx-text-fill: #FFFF;");
        btnPlayAsAI.setStyle("-fx-background-color: #ACACAC; -fx-text-fill: #FFFF;");
    }

    @FXML
    private void clickedBtnFullscreen(ActionEvent event) throws IOException {
        //Set true to false, false to true when bntFullscreen is clicked, alter text in btnFullscreen
        if (fullscreen) {
            fullscreen = false;
            btnFullscreen.setText("fullscreen: off");
        } else {
            fullscreen = true;
            btnFullscreen.setText("fullscreen: on");
        }
    }
    /**
     * END @author Jasper van Dijken
     */



    @FXML
    private void btnLogout(ActionEvent event) throws IOException {
        Main.client.logout();

        goBack();
    }

    @Override
    public void update(String response) {
        super.update(response);
    }

    @Override
    public void updateGameList(List<String> list) {
        super.updateGameList(list);
        gameListString = String.join(", ", list);

        //method that makes sure the game buttons are created
        updateGameBtnGroup();
    }

    @Override
    public void updatePlayerList(List<String> list) {
        super.updatePlayerList(list);

        playersTable.setItems(FXCollections.observableArrayList());

        for (String player : list) {
            playersTable.getItems().add(new PlayerRow(player));
        }
    }

    public void btnStart(ActionEvent event) {
        //playAs contains whom the user wants to play as (String 'AI' or 'Manual')
        //fullscreen contains if the user wants fullscreen, (Boolean 'true' or 'false')
        Platform.runLater(() -> System.out.println(playersTable.getSelectionModel().getSelectedItem().getName()));
    }

    @Override
    public void changeScene() {
        super.changeScene();

        tableUpdater.stop();
    }

    private static class TableUpdater implements Runnable {
        volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(2000);
                    Main.client.getPlayerList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            running = false;
        }
    }
}
