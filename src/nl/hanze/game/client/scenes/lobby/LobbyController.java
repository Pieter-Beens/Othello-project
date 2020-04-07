package nl.hanze.game.client.scenes.lobby;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.utils.PlayerRow;
import nl.hanze.game.client.scenes.utils.Popup;
import nl.hanze.game.client.scenes.utils.RequestRow;

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
    public ButtonBar gamesBar;

    @FXML
    public TableView<PlayerRow> playersTable;

    @FXML
    public TableColumn<PlayerRow, String> nameColumn;

    @FXML
    public TableView<RequestRow> requestTable;

    @FXML
    public TableColumn<RequestRow, String> challengerColumn;

    @FXML
    public TableColumn<RequestRow, String> gameColumn;

    @FXML
    public TableColumn<RequestRow, String> challengeNumberColumn;

    private String gameListString = "";

    private static TableUpdater tableUpdater;

    // Contains either 'AI' or 'Manual' to indicate as whom the user wants to play as
    public String playAs = "AI";

    // Contains the name of the game the user wants to play
    public String selectedGame = "";

    // Indicates whether the user wants to play fullscreen, default is 'false'
    private Boolean fullscreen = false;

    private ObservableList<PlayerRow> tableList = FXCollections.observableArrayList();

    // Save the game match data, when you are the starting player.
    private Map<String, String> gameMatchBuffer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playersTable.setItems(tableList);

        Main.serverConnection.getGameList();
        Main.serverConnection.getPlayerList();

        if (tableUpdater != null)
            tableUpdater.stop();

        tableUpdater = new TableUpdater();
        new Thread(tableUpdater).start();

        nameColumn.prefWidthProperty().bind(playersTable.widthProperty().multiply(0.8));


        /**
         * @author Jasper van Dijken
         */
        challengerColumn.setCellValueFactory(new PropertyValueFactory<RequestRow, String>("name"));
        challengeNumberColumn.setCellValueFactory(new PropertyValueFactory<RequestRow, String>("challengeID"));
        gameColumn.setCellValueFactory(new PropertyValueFactory<RequestRow, String>("game"));

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
    private void clickedGameBtn(Button btn) {
        selectedGame = btn.getText();
        ObservableList<Node> b = gamesBar.getButtons();

        for (Node bt : b ) {
            if(bt != btn) {
                bt.setStyle("");
                bt.setStyle("-fx-background-color: #ACACAC; -fx-text-fill: #FFFF");
                //bt.getStyleClass().add();
                bt.applyCss();
            }else{
                bt.setStyle("");
                bt.applyCss();
                //bt.setStyle("-fx-background-color: #46AF4E; -fx-text-fill: #FFFF;");
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

    @FXML
    private void btnAcceptChallenge(ActionEvent event) throws IOException {
        Main.serverConnection.challengeAccept(Integer.parseInt(requestTable.getSelectionModel().getSelectedItem().getChallengeID()));
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
    public void updateGameList(List<String> list) {
        super.updateGameList(list);

        gameListString = String.join(", ", list);
        //method that makes sure the game buttons are created
        updateGameBtnGroup();
    }

    @Override
    public void updatePlayerList(List<String> list) {
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
    public void gameMatch(Map<String, String> map) {
        // If you are the starting player wait for the Your Turn Command
        if(map.get("PLAYERTOMOVE").equals(GameModel.serverName)) {
            gameMatchBuffer = map;
            return;
        }

        Platform.runLater(() -> {
            try {
                PlayerType playerType = playAs.equals("AI") ? PlayerType.AI : PlayerType.LOCAL;
                GameController.startOnline(map, fullscreen, playerType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void gameChallenge(Map<String, String> map) {
        //Platform.runLater(() -> Popup.display("Match from " + map.get("CHALLENGER") + " for a game of " + map.get("GAMETYPE")));

        //Author: Jasper van Dijken
        RequestRow row = new RequestRow(map.get("CHALLENGER"), map.get("CHALLENGENUMBER"), map.get("GAMETYPE"));

        requestTable.getItems().add(row);
        //End Author

    }

    @Override
    public void gameYourTurn(Map<String, String> map) {
        // If you are the player with the first move, start the game board
        Platform.runLater(() -> {
            try {
                PlayerType playerType = playAs.equals("AI") ? PlayerType.AI : PlayerType.LOCAL;
                GameController controller = GameController.startOnline(gameMatchBuffer, fullscreen, playerType);
                controller.gameYourTurn(map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void btnStart(ActionEvent event) {
        //playAs contains whom the user wants to play as, (String 'AI' or 'Manual')
        //fullscreen contains if the user wants fullscreen, (Boolean 'true' or 'false')
        //selectedGame contains the game the user wants to play, (String 'Reversi' or 'Tic-tac-toe')

        //Subscribe for game
        Main.serverConnection.subscribe(selectedGame);

        System.out.println("Play as: " + playAs + ", Fullscreen: " + fullscreen + ", Game: " + selectedGame);
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

    @Override
    protected void finalize() throws Throwable {
        tableUpdater.stop();

        super.finalize();
    }

    private static class TableUpdater implements Runnable {
        volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(5000);
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
