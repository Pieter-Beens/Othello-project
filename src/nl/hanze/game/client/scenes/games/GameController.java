package nl.hanze.game.client.scenes.games;

import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.AIStrategy;
import nl.hanze.game.client.players.AI.OthelloAI;
import nl.hanze.game.client.players.AI.TicTacToeAI;
import nl.hanze.game.client.players.AIPlayer;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.Controller;

import java.io.IOException;

public abstract class GameController extends Controller {
    protected Player player1;
    protected Player player2;
    protected Player currentPlayer;

    public static void start(String ignPlayer1, String ignPlayer2, String game, boolean fullscreen, boolean isMultiPlayer) throws IOException {
        // corrects overlong and empty playernames
        if (ignPlayer1.length() > 10) ignPlayer1 = ignPlayer1.substring(0,11);
        else if (ignPlayer1.length() == 0) ignPlayer1 = "player1";
        if (ignPlayer2.length() > 10) ignPlayer2 = ignPlayer2.substring(0,11);
        else if (ignPlayer2.length() == 0) ignPlayer2 = "player2";

        AIStrategy aiStrategy = null;
        switch (game) {
            case "tictactoe":
                aiStrategy = new TicTacToeAI();
                break;
            case "othello":
                aiStrategy = new OthelloAI();
                break;
        }

        Player player1 = new Player(ignPlayer1);
        Player player2 = (isMultiPlayer) ? new Player(ignPlayer2) : new AIPlayer(ignPlayer2, aiStrategy);

        GameController controller = (GameController) loadScene("games/" + game + "/" + game + ".fxml");

        GameModel model = controller.getModel();
        model.setPlayer1(player1);
        model.setPlayer2(player2);

        controller.setup();

        Main.primaryStage.setFullScreen(fullscreen);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setup() {}

    protected abstract GameModel getModel();
}
