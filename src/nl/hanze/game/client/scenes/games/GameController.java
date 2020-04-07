package nl.hanze.game.client.scenes.games;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.utils.BoardPane;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Roy Voetman
 */
public abstract class GameController extends Controller {
    @FXML
    public Button forfeitButton;

    public Player getActivePlayer() {
        return getModel().getActivePlayer();
    }

    public void setup() { }

    public void goBack() throws IOException {
        if (Main.serverConnection.hasConnection()) {
            Main.serverConnection.forfeit();
        }

        super.goBack();
    }

    public abstract void updateViews();

    public abstract void move(Move move);

    public abstract GameModel getModel();

    public abstract BoardPane getBoardPane();

    public abstract void acceptNewMoves();

    @Override
    public void gameYourTurn(Map<String, String> map) {
        super.gameYourTurn(map);

        if (getModel().getActivePlayer().getPlayerType() == PlayerType.AI) {
            Move move = getModel().getActivePlayer().calculateMove(getModel().getBoard(), getModel().getInactivePlayer());
            move(move);

            if (Main.serverConnection.hasConnection())
                Main.serverConnection.move(Move.cordsToCell(move.getRow(), move.getColumn(), getModel().getBoardSize()));
        } else {
            forfeitButton.setDisable(false);
            getBoardPane().enableAllFields();
        }
    }

    @Override
    public void gameMove(Map<String, String> map) {
        super.gameMove(map);

        int cell = Integer.parseInt(map.get("MOVE"));
        int[] cords = Move.cellToCords(cell, getModel().getBoardSize());

        System.out.println(Arrays.toString(cords) + "----------------------------");
        move(new Move(getModel().getPlayerByName(map.get("PLAYER")), cords[0], cords[1]));
    }

    public void gameWin(Map<String, String> map) {
        getModel().endGame();
    }

    public void gameLoss(Map<String, String> map) {
        getModel().endGame();
    }

    public void gameDraw(Map<String, String> map) {
        getModel().endGame();
    }
}
