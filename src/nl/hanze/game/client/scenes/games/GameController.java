package nl.hanze.game.client.scenes.games;

import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.AIStrategy;
import nl.hanze.game.client.players.AI.OthelloAI;
import nl.hanze.game.client.players.AI.TicTacToeAI;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.AIPlayer;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.Controller;

import java.io.IOException;
import java.util.List;

public abstract class GameController extends Controller {
    protected Player player1;
    protected Player player2;
    public List<Move> moveHistory;

    public static void start(String ignPlayer1, String ignPlayer2, String game, boolean fullscreen, boolean isMultiPlayer) throws IOException {
        // corrects overlong and empty playernames
        if (ignPlayer1.length() > 10) ignPlayer1 = ignPlayer1.substring(0,11);
        else if (ignPlayer1.length() == 0) ignPlayer1 = "player1";
        if (ignPlayer2.length() > 10) ignPlayer2 = ignPlayer2.substring(0,11);
        else if (ignPlayer2.length() == 0) ignPlayer2 = "player2";

        AIStrategy aiStrategy = null; //TODO: support multiple AI's/difficulties per game
        switch (game) {
            case "tictactoe":
                aiStrategy = new TicTacToeAI();
                break;
            case "othello":
                aiStrategy = new OthelloAI();
                break;
        }

        Player player1 = new Player(ignPlayer1, PlayerType.LOCAL);
        Player player2 = (isMultiPlayer) ? new Player(ignPlayer2, PlayerType.LOCAL) : new AIPlayer(ignPlayer2, PlayerType.AI, aiStrategy);

        GameController controller = (GameController) loadScene("games/" + game + "/" + game + ".fxml");

        GameModel model = controller.getModel();
        model.setPlayer1(player1);
        model.setPlayer2(player2);

        controller.setup();

        Main.primaryStage.setFullScreen(fullscreen);
    }

    public Player getActivePlayer() {
        return getModel().getActivePlayer();
    }

    public void setup() {}

    public abstract void updateViews();

    public void move(Move move) {
        moveHistory.add(move);
        // handling of moves is defined in inheriting classes
    }

    protected abstract GameModel getModel();
}
