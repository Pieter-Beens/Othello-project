package nl.hanze.game.client.scenes.lobby;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameFacade;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.menu.offline.OfflineMenuModel;
import nl.hanze.game.client.scenes.utils.PlayerRow;
import nl.hanze.game.client.scenes.utils.Popup;
import nl.hanze.game.client.scenes.utils.RequestRow;
import nl.hanze.game.client.scenes.utils.SelectButton;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LobbyController extends Controller implements Initializable {

    @FXML
    public HBox gameBtnHBox;

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

    private String gameListString;

    private static Timer playersTableUpdater;

    // Contains either 'ai' or 'manual' to indicate as whom the user wants to play as
    @FXML private ToggleGroup playerMode;

    // Contains the name of the game the user wants to play
    @FXML private ToggleGroup selectedGame;

    // Indicates whether the user wants to play fullscreen, default is 'false'
    @FXML private CheckBox fullscreen;

    @FXML
    public Text lastGameResult;

    //Receives the result-message that should be displayed
    public static String lastGameResultMsg = "";

    private ObservableList<PlayerRow> tableList = FXCollections.observableArrayList();

    // Save the game match data, when you are the starting player.
    private Map<String, String> gameMatchBuffer;

    private LobbyModel model = new LobbyModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playersTable.setItems(tableList);
        selectedGame = new ToggleGroup();
        Main.serverConnection.getGameList();
        Main.serverConnection.getPlayerList();

        if (playersTableUpdater != null)
            playersTableUpdater.cancel();

        playersTableUpdater = new Timer();
        playersTableUpdater.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Main.serverConnection.getPlayerList();
            }
        }, 5000,5000);

        nameColumn.prefWidthProperty().bind(playersTable.widthProperty().multiply(0.8));

        //Show the result of the last game
        //lastGameResult.setStyle("-fx-text-fill: #46AF4E;");
        lastGameResult.setStyle("-fx-text-fill: white;");
        lastGameResult.setText("\n" + lastGameResultMsg);

        /*
        if (lastGameResultMsg.equals("Result of last game:\\nYou won")) {
            lastGameResult.setStyle("fx-text-fill: #46AF4E;");
        }
         */

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
        List<String> games = new ArrayList<>(Arrays.asList(gameListString.split(", ")));
        //ArrayList which will contain the buttons
        ArrayList<SelectButton> buttons = new ArrayList<>();

        //For each game, create button and add to buttons array
        for (String game : games) {
            SelectButton btn = new SelectButton();
            btn.setText(game);
            btn.setUserData(game);
            btn.setOnMouseClicked(event -> {
                model.setGame(game);
            });
            buttons.add(btn);
        }

        //For each button in buttons, add to gameBtnHbox
        for (SelectButton btn : buttons) {
            gameBtnHBox.getChildren().add(btn);
            selectedGame.getToggles().add(btn);
        }
    }

    @FXML
    private void btnAcceptChallenge(ActionEvent event) throws IOException {
        Main.serverConnection.challengeAccept(Integer.parseInt(requestTable.getSelectionModel().getSelectedItem().getChallengeID()));
    }

    //Sets gameMode in model
    @FXML
    private void gameModeChanged() {
        model.setGameMode((String) playerMode.getSelectedToggle().getUserData());
    }

    //Sets fullscreen in model
    @FXML
    public void fullscreenReleased(MouseEvent mouseEvent) {
        model.setFullscreen(fullscreen.isSelected());
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

        //When the match-requesting player is not on server, remove any match-requests from that player
        requestTable.getItems().removeIf(r -> !list.contains(r.getName()));
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
                PlayerType playerType = model.getGameMode().equals("ai") ? PlayerType.AI : PlayerType.LOCAL;
                GameFacade.startOnline(map, model.getFullscreen(), playerType, 10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void gameChallenge(Map<String, String> map) {
        //Author: Jasper van Dijken
        RequestRow row = new RequestRow(map.get("CHALLENGER"), map.get("CHALLENGENUMBER"), map.get("GAMETYPE"));

        //When there are multiple requests from the same player, show only the latest one, delete older ones
        if (!requestTable.getItems().isEmpty()) {
            for (RequestRow r : requestTable.getItems()) {
                if (r.getName().equals(map.get("CHALLENGER"))) {
                    requestTable.getItems().remove(r);
                    requestTable.getItems().add(row);
                }
            }
        } else {
            requestTable.getItems().add(row);
        }
        //End Author
    }

    @Override
    public void gameYourTurn(Map<String, String> map) {
        // If you are the player with the first move, start the game board
        Platform.runLater(() -> {
            try {
                //TODO:toggleGroup="$selectedGame"
                PlayerType playerType = model.getGameMode().equals("ai") ? PlayerType.AI : PlayerType.LOCAL;
                GameController controller = GameFacade.startOnline(gameMatchBuffer, model.getFullscreen(), playerType, 10);
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
        Main.serverConnection.subscribe(model.getGame());

        System.out.println("Play as: " + model.getGameMode() + ", Fullscreen: " + model.getFullscreen() + ", Game: " + model.getGame());
    }



    public void btnMatchRequest(ActionEvent event) {
        if (playersTable.getSelectionModel().getSelectedItem() == null) {
            Platform.runLater(() -> Popup.display("First select an opponent"));
            return;
        }

        Main.serverConnection.challenge(playersTable.getSelectionModel().getSelectedItem().getName(), model.getGame());

    }

    @Override
    public void changeScene() {
        super.changeScene();
        playersTableUpdater.cancel();
    }
}
