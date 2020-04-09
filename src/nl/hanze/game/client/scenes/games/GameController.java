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
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.utils.BoardPane;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Roy Voetman
 */
public abstract class GameController extends Controller implements Initializable {

    //@FXML public HBox boardContainer;
    @FXML protected Pane gameBoardPane;
    @FXML protected BoardPane boardPane;
    @FXML protected GridPane infoPanel;
    @FXML protected Label turnLabel;
    @FXML protected Button forfeitButton;
    @FXML protected Label gameTitle;
    @FXML protected HBox topFieldId;
    @FXML protected HBox bottomFieldId;
    @FXML protected VBox leftFieldId;
    @FXML protected VBox rightFieldId;


    protected GameModel model;

    protected GameController(GameModel model) {
        this.model = model;
    }

    public Player getActivePlayer() {
        return model.getActivePlayer();
    }

    public void initialize(URL location, ResourceBundle resources) {
        forfeitButton.setOnAction(this::forfeit);
        Font font = new Font("System Bold",24);
        Label turn = new Label("Turn: ");
        turn.setFont(font);
        turnLabel.setGraphic(turn);
        drawFieldIds();
    }

    abstract public void setup();

    public void goBack() throws IOException {
        if (Main.serverConnection.hasConnection()) {
            Main.serverConnection.forfeit();
        }

        super.goBack();
    }
    private void drawFieldIds(){
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

    public void gameWin(Map<String, String> map) {
        model.endGame();
    }

    public void gameLoss(Map<String, String> map) {
        model.endGame();
    }

    public void gameDraw(Map<String, String> map) {
        model.endGame();
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

    public abstract void updateViews();

    public abstract void move(Move move);

    public abstract BoardPane getBoardPane();
}
