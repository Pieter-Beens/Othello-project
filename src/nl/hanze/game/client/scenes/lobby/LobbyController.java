package nl.hanze.game.client.scenes.lobby;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.utils.PlayerRow;
import nl.hanze.game.client.scenes.utils.Popup;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LobbyController extends Controller implements Initializable {

    @FXML
    public ToggleButton btnPlayAsAI;

    @FXML
    public ToggleButton btnPlayAsManual;

    @FXML
    public Button btnFullscreen;

    @FXML
    public HBox gameBtnHBox;

    @FXML
    public GridPane gameGrid;

    @FXML
    public ButtonBar gamesBar;

    @FXML
    public TableView<PlayerRow> playersTable;

    @FXML
    public TableColumn<PlayerRow, String> nameColumn;

    private String gameListString = "";

    private TableUpdater tableUpdater;

    // Contains either 'AI' or 'Manual' to indicate as whom the user wants to play as
    public String playAs = "";

    // Contains the name of the game the user wants to play
    public String selectedGame = "";

    // Indicates whether the user wants to play fullscreen, default is 'false'
    private Boolean fullscreen = false;

    private ObservableList<PlayerRow> tableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playersTable.setItems(tableList);

        Main.serverConnection.getGameList();
        Main.serverConnection.getPlayerList();

        tableUpdater = new TableUpdater();
        new Thread(tableUpdater).start();

        nameColumn.prefWidthProperty().bind(playersTable.widthProperty().multiply(0.8));


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
            btn.setStyle("-fx-background-color: #ACACAC; -fx-text-fill: #FFFF;");
            btn.setPrefWidth(150);
            btn.setPrefHeight(30);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    gamesBar.getButtons().add(btn);
                    btn.setOnAction(event -> {
                        try {
                            clickedGameBtn(btn);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });

        }
    }

    @FXML
    private void clickedGameBtn(Button btn) throws Exception {
        selectedGame = btn.getText();
        for (Node bt : gamesBar.getButtons()) {
            if(bt != btn) {
                bt.setStyle("-fx-background-color: #ACACAC; -fx-text-fill: #FFFF");
            }else{
                bt.setStyle("-fx-background-color: #46AF4E; -fx-text-fill: #FFFF;");
        }}
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
        GameModel.serverName = null;

        Main.serverConnection.logout();

        goBack();
    }

    @Override
    public void update(String response) {
        super.update(response);
    }

    @Override
    protected void updateGameList(List<String> list) {
        super.updateGameList(list);

        gameListString = String.join(", ", list);
        //method that makes sure the game buttons are created
        updateGameBtnGroup();
    }

    @Override
    protected void gameMatch(Map<String, String> map) {
        Platform.runLater(() -> {
            try {
                GameController.startOnline(map, fullscreen, PlayerType.LOCAL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void updatePlayerList(List<String> list) {
        super.updatePlayerList(list);

        PlayerRow playerRow;

        Set<Integer> foundIndexes = new HashSet<>();
        Set<Integer> allIndexes = IntStream.range(0, tableList.size()).boxed().collect(Collectors.toSet());

        for (String player : list) {
            playerRow = new PlayerRow(player);
            int indexOf = tableList.indexOf(playerRow);

            // Ignore self
            if (!GameModel.serverName.equals(player)) {
                if (indexOf == -1) {
                    tableList.add(new PlayerRow(player));
                } else {
                    foundIndexes.add(indexOf);
                }
            }
        }

        // Subtract sets to get all players that "left" to lobby
        allIndexes.removeAll(foundIndexes);
        for (int index : allIndexes)
            tableList.remove(index);
    }

    @Override
    protected void gameChallenge(Map<String, String> map) {
        Platform.runLater(() -> Popup.display("Match from " + map.get("CHALLENGER") + " for a game of " + map.get("GAMETYPE")));
    }

    public void btnStart(ActionEvent event) {
        //playAs contains whom the user wants to play as, (String 'AI' or 'Manual')
        //fullscreen contains if the user wants fullscreen, (Boolean 'true' or 'false')
        //selectedGame contains the game the user wants to play, (String 'Reversi' or 'Tic-tac-toe')

        Platform.runLater(() -> System.out.println(playersTable.getSelectionModel().getSelectedItem().getName()));

        //Subscribe for game
        Main.serverConnection.subscribe(selectedGame);

        System.out.println("Play as: " + playAs + " fullscreen: " + fullscreen + " game: " + selectedGame);
    }

    public void btnMatchRequest(ActionEvent event) {
        if (playersTable.getSelectionModel().getSelectedItem() == null) {
            Platform.runLater(() -> Popup.display("First select an opponent"));
            return;
        }

        Main.serverConnection.challenge(playersTable.getSelectionModel().getSelectedItem().getName(), "Tic-tac-toe");
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
                    Main.serverConnection.getPlayerList();
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
