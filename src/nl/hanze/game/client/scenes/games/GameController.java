package nl.hanze.game.client.scenes.games;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
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
    @FXML protected Text skippedTurnText;
    @FXML protected Label timerLabel;

    protected BoardPane gameBoard;
    protected GameModel model;
    protected Timer timer;

    protected GameController(GameModel model, int turnTime) {
        this.model = model;

        model.setTurnTime(turnTime);
    }

    public Player getActivePlayer() {
        return model.getActivePlayer();
    }

    public void initialize(URL location, ResourceBundle resources) {
        forfeitButton.setOnAction(this::forfeit);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                int time = model.getElapsedTime();
                Platform.runLater(() -> timerLabel.setText("Timer:" + time));

                if(time > 0) {
                    model.decreaseElapsedTime();
                } else {
                    timer.cancel();

                    if (!Main.serverConnection.hasConnection() && !model.hasGameEnded())
                        Platform.runLater(() -> model.endGame());
                }
            }
        }, 0,1000);
    }

    public void setup() {
        model.setup();
        updateViews();

        if (!Main.serverConnection.hasConnection())
            acceptNewMoves();
    }

    public void goBack() throws IOException {
        if (Main.serverConnection.hasConnection()) {
            Main.serverConnection.forfeit();
        }

        super.goBack();
    }
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
    @Override
    public void gameYourTurn(Map<String, String> map) {
        super.gameYourTurn(map);

        if (model.getActivePlayer().getPlayerType() == PlayerType.AI) {
            Move move = model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer());
            move(move);

            if (Main.serverConnection.hasConnection())
                Main.serverConnection.move(Move.cordsToCell(move.getRow(), move.getColumn(), model.getBoardSize()));
        } else {
            forfeitButton.setDisable(false);
            getBoardPane().enableValidFields();
        }
    }

    @Override
    public void gameMove(Map<String, String> map) {
        super.gameMove(map);

        int cell = Integer.parseInt(map.get("MOVE"));
        int[] cords = Move.cellToCords(cell, model.getBoardSize());

        System.out.println(Arrays.toString(cords) + "----------------------------");
        move(new Move(model.getPlayerByName(map.get("PLAYER")), cords[0], cords[1]));
    }
    
    public GameModel getModel() {
        return model;
    }

    /**
     * @author Pieter Beens
     */
    public void forfeit(ActionEvent e) {
        model.forfeitGame(model.getActivePlayer());
        if (Main.serverConnection.hasConnection()) {
            Main.serverConnection.forfeit();
        }
    }

    /**
     * @author Pieter Beens
     */
    public void acceptNewMoves() {
        // check if the next turn belongs to an AIPlayer and if so, request a move
        if (model.getActivePlayer().getPlayerType() == PlayerType.AI && !model.hasGameEnded()) {
            //move(model.getActivePlayer().calculateMove(model.getBoard(), model.getInactivePlayer()));

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

    public BoardPane getBoardPane() {
        return gameBoard;
    }

    public void updateTurnLabel(){
        String player = model.getActivePlayer().getName();

        if(player.equals("You")) turnLabel.setText("Your turn");
        else turnLabel.setText(player+"'s turn");

        skippedTurnText.setText(GameModel.getSkippedTurnText());
    }

    @Override
    public void gameWin(Map<String, String> map) {
        String msg = "You won";
        if (!map.get("COMMENT").equals("Turn time limit reached")) {
            msg += " with " + model.getPlayerByName(GameModel.serverName).getScore() + " points!";
        }

        goToLobby(map, msg);
    }

    @Override
    public void gameLoss(Map<String, String> map) {
        String msg = "You lost";

        if (!map.get("COMMENT").equals("Turn time limit reached")) {
            msg += " with " + model.getPlayerByName(GameModel.serverName).getScore() + " points!";
        }

        goToLobby(map, msg);
    }

    @Override
    public void gameDraw(Map<String, String> map) {
        goToLobby(map, "You came to a draw at "+ model.getPlayerByName(GameModel.serverName).getScore() + " points!");
    }

    private void goToLobby(Map<String, String> map, String msg) {
        String serverComment = map.get("COMMENT");
        if (!serverComment.equals("")) {
            msg += "\n" + serverComment;
        }

        LobbyController.lastGameResultMsg = "Result of last game:\n" + msg;
        goToLobby();
    }

    private void goToLobby() {
        try {
            Controller.loadScene("lobby/lobby.fxml");
        } catch (IOException ignore) {}
    }

    public void updateViews() {
        gameBoard.update();
        updateTurnLabel();
    }

    @Override
    public void changeScene() {
        timer.cancel();

        super.changeScene();
    }

    public boolean move(Move move) {
        if (model.isValidMove(move)) {
            forfeitButton.setDisable(true);
            model.recordMove(move);
            updateViews();

            return true;
        }
        return false;
    }
}
