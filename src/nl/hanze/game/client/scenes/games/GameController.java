package nl.hanze.game.client.scenes.games;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.utils.BoardPane;
import nl.hanze.game.client.scenes.lobby.LobbyController;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Abstract game controller that all concrete games controllers should extend.
 *
 * @author Roy Voetman
 */
public abstract class GameController extends Controller implements Initializable {

    @FXML protected Pane gameBoardPane;
    @FXML protected GridPane boardGridPane;
    @FXML protected Label turnLabel;
    @FXML protected Button forfeitButton;
    @FXML protected Label gameTitle;
    @FXML protected HBox topFieldId;
    @FXML protected HBox bottomFieldId;
    @FXML protected VBox leftFieldId;
    @FXML protected VBox rightFieldId;
    @FXML protected Label timerLabel;
    @FXML protected Text skippedTurnText;
    @FXML protected Label boardScoreLabel;


    protected BoardPane gameBoard;
    protected GameModel model;
    protected Timer timer;

    /**
     * Constructs a GameController.
     *
     * @author Roy Voetman
     * @param model The model for the game.
     * @param turnTime The time players have to do a move.
     */
    protected GameController(GameModel model, int turnTime) {
        this.model = model;

        model.setTurnTime(turnTime);
    }

    /**
     * Getter for the active player in the model.
     *
     * @author Roy Voetman
     * @return Active Player object.
     */
    public Player getActivePlayer() {
        return model.getActivePlayer();
    }

    /**
     * When all FXML elements are set in their Fields bootstrap all elements here.
     *
     * @author Roy Voetman
     * @param location Uniform Resource of the FXML
     * @param resources Locale-specific objects
     */
    public void initialize(URL location, ResourceBundle resources) {
        forfeitButton.setOnAction(this::forfeit);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                int time = model.getElapsedTime();
                Platform.runLater(() -> timerLabel.setText("Turn Timer: " + time));

                if(time > 0) {
                    model.decreaseElapsedTime();
                } else {
                    timer.cancel();

                    if (!Main.serverConnection.hasConnection() && !model.hasGameEnded())
                        Platform.runLater(() -> model.endGame(true));
                }
            }
        }, 0,1000);
    }

    /**
     * When players are defined in the model bootstrap the player logic.
     *
     * @author Roy Voetman
     */
    public void setup() {
        model.setup();
        updateViews();

        if (!Main.serverConnection.hasConnection())
            acceptNewMoves();
    }

    /**
     * This draws the field coordinates around the board view
     * @author Bart van Poele
     */
    protected void drawCoordinates(){
        double hPadding = ((680/model.getBoardSize())/2)-6;
        double vPadding = ((680/model.getBoardSize())/2)-12;
        char[] fieldIds = {'A','B','C','D','E','F','G','H'};

        for(int i=1;i<=model.getBoardSize();i++){
            Label label1 = new Label(String.valueOf(i));
            Label label2 = new Label(String.valueOf(i));
            label1.setPadding(new Insets(0,hPadding,0,hPadding));
            label2.setPadding(new Insets(0,hPadding,0,hPadding));
            topFieldId.getChildren().add(label1);
            bottomFieldId.getChildren().add(label2);
        }
        for(int i=0;i<model.getBoardSize();i++){
            Label label1 = new Label(String.valueOf(fieldIds[i]));
            Label label2 = new Label(String.valueOf(fieldIds[i]));
            label1.setPadding(new Insets(vPadding,0,vPadding,0));
            label2.setPadding(new Insets(vPadding,0,vPadding,0));
            leftFieldId.getChildren().add(label1);
            rightFieldId.getChildren().add(label2);
        }
    }

    /**
     * Ask AI to calculate move or enable valid fields so local player is able to click in the GUI.
     *
     * @author Roy Voetman
     * @param map A map with all the argument from this response.
     */
    @Override
    public void gameYourTurn(Map<String, String> map) {
        super.gameYourTurn(map);

        // If player is an AI calculate a new move.
        if (model.getActivePlayer().getPlayerType() == PlayerType.AI) {
            // Calculate the move in a separate thread.
            new Thread(() -> {
                Move move = model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer());

                // Record move in the model and on the game board.
                Platform.runLater(() -> {
                    move(move);

                    // Send move to the server.
                    if (Main.serverConnection.hasConnection())
                        Main.serverConnection.move(Move.cordsToCell(move.getRow(), move.getColumn(), model.getBoardSize()));
                });
            }).start();
        } else {
            // Enable GUI elements to click on valid moves.
            forfeitButton.setDisable(false);
            getBoardPane().enableValidFields();
        }
    }

    /**
     * When an opponent's move is received record it.
     *
     * @author Roy Voetman
     * @param map A map with all the argument from this response.
     */
    @Override
    public void gameMove(Map<String, String> map) {
        // Ignore move of yourself.
        if (map.get("PLAYER").equals(GameModel.serverName)) return;

        super.gameMove(map);

        // Determine coordinates.
        int cell = Integer.parseInt(map.get("MOVE"));
        int[] cords = Move.cellToCords(cell, model.getBoardSize());

        System.out.println(Arrays.toString(cords) + "----------------------------");
        // Record move in the model and on the game board.
        move(new Move(model.getPlayerByName(map.get("PLAYER")), cords[0], cords[1]));
    }

    /**
     * Method called on click of Forfeit Button.
     * @author Pieter Beens
     */
    public void forfeit(ActionEvent e) {
        model.forfeitGame(model.getActivePlayer());
        if (Main.serverConnection.hasConnection()) {
            Main.serverConnection.forfeit();
        }
    }

    /**
     * Calls all possible methods for receiving the next move, depending on the type of player whose turn it is next:
     * enabling buttons for local players and triggering the AI algorithms for AI players. Moves from remote players do
     * not have to be requested.
     * @author Pieter Beens
     */
    public void acceptNewMoves() {
        // check if the next turn belongs to an AIPlayer and if so, request a move
        if (model.getActivePlayer().getPlayerType() == PlayerType.AI && !model.hasGameEnded()) {

            new Thread(() -> {
                Move move = model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer());
                Platform.runLater(() -> {
                    move(move);
                    acceptNewMoves();
                });
            }).start();
        }
        else if (model.getActivePlayer().getPlayerType() == PlayerType.LOCAL && !model.hasGameEnded()) {
            forfeitButton.setDisable(false);
            getBoardPane().enableValidFields();
        }
    }

    /**
     * @author Bart van Poele
     */
    public void updateTurnLabel(){
        String player = model.getActivePlayer().getName();

        if(player.equals("You")) turnLabel.setText("Your turn");
        else turnLabel.setText(player+"'s turn");

        skippedTurnText.setText(GameModel.getSkippedTurnText());
    }

    /**
     * When the scene is changed, cancel the turn timer and empty the skippedTurnText field.
     *
     * @author Roy Voetman
     */
    @Override
    public void changeScene() {
        timer.cancel();
        GameModel.skippedTurnText = "";
        super.changeScene();
    }

    /**
     * Load the previous scene and forfeit the game.
     *
     * @author Roy Voetman
     * @throws IOException When previous scene FXML can not be found.
     */
    @Override
    public void goBack() throws IOException {
        if (Main.serverConnection.hasConnection()) {
            Main.serverConnection.forfeit();
        }

        super.goBack();
    }

    /**
     * When a win occurs go to the lobby with a win message.
     *
     * @author Roy Voetman
     * @param map A map with all the argument from this response.
     */
    @Override
    public void gameWin(Map<String, String> map) {
        String msg = "You won";
        if (!map.get("COMMENT").equals("Turn time limit reached")) {
            msg += " with " + model.getPlayerByName(GameModel.serverName).getScore() + " points!";
        }

        goToLobby(map, msg);
    }

    /**
     * When a loss occurs go to the lobby with a loss message.
     *
     * @author Roy Voetman
     * @param map A map with all the argument from this response.
     */
    @Override
    public void gameLoss(Map<String, String> map) {
        String msg = "You lost";

        if (!map.get("COMMENT").equals("Turn time limit reached")) {
            msg += " with " + model.getPlayerByName(GameModel.serverName).getScore() + " points!";
        }

        goToLobby(map, msg);
    }

    /**
     * When a draw occurs go to the lobby with a draw message.
     *
     * @author Roy Voetman
     * @param map A map with all the argument from this response.
     */
    @Override
    public void gameDraw(Map<String, String> map) {
        goToLobby(map, "You came to a draw at "+ model.getPlayerByName(GameModel.serverName).getScore() + " points!");
    }

    /**
     * Load the lobby scene with a game result message.
     *
     * @author Roy Voetman
     * @param map A map with all the argument from this response.
     * @param msg Message to be shown in the lobby.
     */
    private void goToLobby(Map<String, String> map, String msg) {
        String serverComment = map.get("COMMENT");
        if (!serverComment.equals("")) {
            // Add comment from args map to the message.
            msg += "\n" + serverComment;
        }

        LobbyController.lastGameResultMsg = "Result of last game:\n" + msg;
        goToLobby();
    }

    /**
     * Load the lobby scene.
     *
     * @author Roy Voetman
     */
    private void goToLobby() {
        try {
            Controller.loadScene("lobby/lobby.fxml");
        } catch (IOException ignore) {}
    }

    /**
     * All views that should be updated when a move has been recorded.
     * Child classes are open to extend this method.
     *
     * @author Roy Voetman
     */
    public void updateViews() {
        gameBoard.update();
        updateTurnLabel();
    }

    /**
     * Record the move if it was valid in the model and update all the views.
     *
     * @author Roy Voetman
     * @param move The move that has been done by the active player.
     * @return A boolean indicating if the move was valid.
     */
    public boolean move(Move move) {
        if (model.isValidMove(move)) {
            forfeitButton.setDisable(true);
            model.recordMove(move);
            updateViews();

            return true;
        }
        System.out.println(getActivePlayer().getName() + " made an ILLEGAL MOVE");
        return false;
    }

    /**
     * Getter for the game model
     *
     * @author Roy Voetman
     * @return The game model
     */
    public GameModel getModel() {
        return model;
    }

    /**
     * Getter for the BoardPane
     *
     * @author Roy Voetman
     * @return The BoardPane.
     */
    public BoardPane getBoardPane() {
        return gameBoard;
    }
}
