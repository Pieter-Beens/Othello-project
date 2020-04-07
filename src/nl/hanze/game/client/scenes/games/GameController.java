package nl.hanze.game.client.scenes.games;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
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
    @FXML
    public Button forfeitButton;

    @FXML
    public HBox boardContainer;

    protected GameModel model;

    protected GameController(GameModel model) {
        this.model = model;
    }

    public Player getActivePlayer() {
        return model.getActivePlayer();
    }

    public void initialize(URL location, ResourceBundle resources) {
        forfeitButton.setOnAction(this::forfeit);
    }

    public void setup() { }

    public void goBack() throws IOException {
        if (Main.serverConnection.hasConnection()) {
            Main.serverConnection.forfeit();
        }

        super.goBack();
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
            getBoardPane().enableAllFields();
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
