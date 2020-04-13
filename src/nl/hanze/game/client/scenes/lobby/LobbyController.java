package nl.hanze.game.client.scenes.lobby;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LobbyController extends Controller implements Initializable {

    @FXML
    public ImageView resultIconLeft;

    @FXML
    public ImageView resultIconRight;

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

    @FXML
    public Text challengeErrorText;

    private List<String> gameList;

    private static Timer playersTableUpdater;


    // Contains either 'ai' or 'manual' to indicate as whom the user wants to play as
    @FXML private ToggleGroup playerMode;

    // Contains the name of the game the user wants to play
    @FXML private ToggleGroup selectedGame;


    // Indicates whether the user wants to play fullscreen, default is 'false'
    @FXML private CheckBox fullscreen;



    @FXML
    public Text lastGameResult;

    public String resultIconPath;

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
        lastGameResult.setText("\nLobby\nSubscribe for a game:");

        /** @author Roy Voetman */
        // Retrieve game list and player list
        Main.serverConnection.getGameList();
        Main.serverConnection.getPlayerList();

        // Cancel player table updater Task if it was still running
        // Note: this is only a precaution the timer should already be canceled at this point
        if (playersTableUpdater != null)
            playersTableUpdater.cancel();

        // Start a TimerTask that will retrieve the player list every 5 seconds
        playersTableUpdater = new Timer();
        playersTableUpdater.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Main.serverConnection.getPlayerList();
            }
        }, 5000,5000);
        /** end Roy Voetman */

        nameColumn.prefWidthProperty().bind(playersTable.widthProperty().multiply(0.8));

        /** @author Jasper van Dijken */
        //If there is a result to be displayed
        if (!lastGameResultMsg.isEmpty()) {
            //Show the result of the last game
            lastGameResult.setText("\n" + lastGameResultMsg);

            //If player won, icon = win
            if (lastGameResultMsg.contains("You won")) {
                resultIconPath = "src/resources/win.gif";
            }

            //If player lost, icon = lost
            if (lastGameResultMsg.contains("You lost")) {
                resultIconPath = "src/resources/lost.gif";
            }

            //If the game resulted in a draw, icon = tie
            if (lastGameResultMsg.contains("draw")) {
                resultIconPath = "src/resources/tie.gif";
            }

            //Inserting icon
            Image image;
            try {
                image = new Image(new FileInputStream(resultIconPath));
                resultIconLeft.setImage(image);
                resultIconRight.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        //Preparing the requests-table
        challengerColumn.setCellValueFactory(new PropertyValueFactory<RequestRow, String>("name"));
        challengeNumberColumn.setCellValueFactory(new PropertyValueFactory<RequestRow, String>("challengeID"));
        gameColumn.setCellValueFactory(new PropertyValueFactory<RequestRow, String>("game"));

    }

    /**
     * Takes all the games from the gameList and displays them as buttons
     *
     * @author Jasper van Dijken
     */
    private void updateGameBtnGroup() {
        //Split gameListString into a list
        List<String> games = gameList;
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

    /**
     * Handles the process of accepting a challenge
     *
     * @author Jasper van Dijken
     * @param event is the mouse-click action
     */
    @FXML
    private void btnAcceptChallenge(ActionEvent event) throws IOException {
        try { Main.serverConnection.challengeAccept(Integer.parseInt(requestTable.getSelectionModel().getSelectedItem().getChallengeID())); }
        catch (NullPointerException e) { challengeErrorText.setText("ERROR: SELECT A CHALLENGE FIRST!");}
    }

    /**
     * Changes the selected game mode in the LobbyModel
     *
     * @author Jasper van Dijken
     */
    @FXML
    private void gameModeChanged() {
        model.setGameMode((String) playerMode.getSelectedToggle().getUserData());
    }

    /**
     * Changes the preference of fullscreen in the LobbyModel
     *
     * @author Jasper van Dijken
     * @param mouseEvent is the mouse-click action
     */
    @FXML
    public void fullscreenReleased(MouseEvent mouseEvent) {

        model.setFullscreen(fullscreen.isSelected());
    }
    /** end @author Jasper van Dijken */


    /**
     * Logout of the game server by sending the "logout" command and
     * go back to the previous scene.
     *
     * @author Roy Voetman
     * @param event Action event of the button click.
     * @throws IOException When previous scene FXML can not be found.
     */
    @FXML
    private void btnLogout(ActionEvent event) throws IOException {
        GameModel.serverName = null;
        Main.serverConnection.logout();
        goBack();
    }


    /**
     * Received the game list from the Game Server.
     *
     * @author Roy Voetman
     * @param list A list of all the supported games.
     */
    @Override
    public void updateGameList(List<String> list) {
        super.updateGameList(list);

        // Save the game list
        gameList = list;

        // Create the game buttons in the GUI
        updateGameBtnGroup();
    }

    /**
     * Received the player list from the Game Server.
     * Update the player table in the scene.
     *
     * @author Roy Voetman
     * @param list A list of all the players currently in the Lobby.
     */
    @Override
    public void updatePlayerList(List<String> list) {
        super.updatePlayerList(list);

        // Declare a player row object.
        PlayerRow playerRow;

        // A set that will contain all indexes that a currently in the table
        // and correspond to a player in the received list.
        Set<Integer> foundIndexes = new HashSet<>();
        // A set with a range from 0 to the size of the current list (exclusive). (i.e. 0, 1, 2, 3 .... list.size() - 1)
        Set<Integer> allIndexes = IntStream.range(0, tableList.size()).boxed().collect(Collectors.toSet());

        // Loop trough all the currently online players
        for (String player : list) {
            // Create a player row object for this player.
            playerRow = new PlayerRow(player);
            // Determine if this player is already in the table.
            int indexOf = tableList.indexOf(playerRow);

            // Ignore yourself
            if (!GameModel.serverName.equals(player)) {
                // If player is not in table add it.
                if (indexOf == -1) {
                    tableList.add(new PlayerRow(player));
                } else {
                    // If player is already in the table save it.
                    foundIndexes.add(indexOf);
                }
            }
        }

        // Subtract sets to get all players that "left" to lobby
        allIndexes.removeAll(foundIndexes);
        // Remove all left players
        for (int index : allIndexes)
            tableList.remove(index);

        // When the match-requesting player is not on server, remove any match-requests from that player
        requestTable.getItems().removeIf(r -> !list.contains(r.getName()));
    }

    /**
     * When the server sends a Game Match start the game when you are not the starting player.
     * Starting players will start the game when the "your turn" command is send.
     *
     * @author Roy Voetman
     * @see #gameYourTurn
     * @param map Map containing all the argument that came with this command.
     */
    @Override
    public void gameMatch(Map<String, String> map) {
        // If you are the starting player wait for the Your Turn Command
        if(map.get("PLAYERTOMOVE").equals(GameModel.serverName)) {
            // Buffer this map so it can be used in your turn.
            gameMatchBuffer = map;
            return;
        }

        // Start the requested game based on the currently applied settings.
        Platform.runLater(() -> {
            try {
                PlayerType playerType = model.getGameMode().equals("ai") ? PlayerType.AI : PlayerType.LOCAL;
                GameFacade.startOnline(map, model.getFullscreen(), playerType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Handles incoming game-requests, adds them to the requests-table
     *
     * @author Jasper van Dijken
     * @param map Map containing all the arguments that came with this command.
     */
    @Override
    public void gameChallenge(Map<String, String> map) {
        //Create new row for the incoming request
        RequestRow row = new RequestRow(map.get("CHALLENGER"), map.get("CHALLENGENUMBER"), map.get("GAMETYPE"));

        //When there are multiple requests from the same player, show only the latest one, delete older ones
        if (!requestTable.getItems().isEmpty()) {
            //For every row
            for (RequestRow r : requestTable.getItems()) {
                //If there are any old requests from the challenger
                if (r.getName().equals(map.get("CHALLENGER"))) {
                    //Remove the old request
                    requestTable.getItems().remove(r);
                    //Add the new request
                    requestTable.getItems().add(row);
                }
            }
        } else {
            //If the table is empty, simply add the request
            requestTable.getItems().add(row);
        }
    }

    /**
     * When the server informs you it is your turn, start the game.
     * Game is started here because the GameController can not
     * be created in time to receive the "your turn" command.
     *
     * @author Roy Voetman
     * @see #gameMatch
     * @param map Map containing all the arguments that came with this command.
     */
    @Override
    public void gameYourTurn(Map<String, String> map) {
        // If you are the player with the first move, start the game.
        Platform.runLater(() -> {
            try {
                // Determine player type
                PlayerType playerType = model.getGameMode().equals("ai") ? PlayerType.AI : PlayerType.LOCAL;

                // Create the game
                GameController controller = GameFacade.startOnline(gameMatchBuffer, model.getFullscreen(), playerType);

                // Call your turn method on the GameController.
                controller.gameYourTurn(map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Button that subscribes the player for a certain game, with certain preferences
     *
     * @author Jasper van Dijken
     * @param event the event of mouse-click
     */
    public void btnStart(ActionEvent event) {

        //Subscribe for game
        Main.serverConnection.subscribe(model.getGame());

        System.out.println("Play as: " + model.getGameMode() + ", Fullscreen: " + model.getFullscreen() + ", Game: " + model.getGame());
    }

    public void btnMatchRequest(ActionEvent event) {
        if (playersTable.getSelectionModel().getSelectedItem() == null) {
            challengeErrorText.setText("ERROR: SELECT AN OPPONENT FIRST!");
            return;
        }
        Main.serverConnection.challenge(playersTable.getSelectionModel().getSelectedItem().getName(), model.getGame());
    }

    /**
     * When scene is changed cancel the playersTableUpdater Task
     *
     * @author Roy Voetman
     */
    @Override
    public void changeScene() {
        super.changeScene();
        playersTableUpdater.cancel();
    }
}
