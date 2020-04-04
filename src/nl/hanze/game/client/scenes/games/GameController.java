package nl.hanze.game.client.scenes.games;

import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.AIStrategy;
import nl.hanze.game.client.players.AI.OthelloAIEasy;
import nl.hanze.game.client.players.AI.TicTacToeAI;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.AIPlayer;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.Controller;

import java.io.IOException;
import java.util.Map;

public abstract class GameController extends Controller {
    // If the your turn command was received in the lobby controller
    // it will be buffered in this field
    public static Map<String, String> YOUR_TURN_COMMAND_BUFFER = null;

    /**
     * @author Roy Voetman
     */
    public static void startOnline(Map<String, String> args, boolean fullscreen, PlayerType playerType) throws IOException {
        String game = args.get("GAMETYPE").toLowerCase().replace("-", "");

        Player player1;
        if (playerType == PlayerType.AI) {
            AIStrategy aiStrategy = determineAIStrategy(game);

            player1 = new AIPlayer(GameModel.serverName, playerType, aiStrategy);
        } else {
            player1 = new Player(GameModel.serverName, playerType);
        }

        Player player2 = new Player(args.get("OPPONENT"), PlayerType.REMOTE);

        GameController controller = (GameController) loadScene("games/" + game + "/" + game + ".fxml");

        GameModel model = controller.getModel();

        // Determine which player should begin (model.player1 always starts)
        model.setPlayer1(args.get("PLAYERTOMOVE").equals(GameModel.serverName) ? player1 : player2);
        model.setPlayer2(args.get("PLAYERTOMOVE").equals(GameModel.serverName) ? player2 : player1);

        start(controller, fullscreen);
    }

    /**
     * @author Roy Voetman
     */
    public static void startOffline(String ignPlayer1, String ignPlayer2, String game, boolean fullscreen, boolean isMultiPlayer) throws IOException {
        game = game.toLowerCase().replace("-", "");

        AIStrategy aiStrategy = determineAIStrategy(game);

        Player player1 = new Player(ignPlayer1, PlayerType.LOCAL);
        Player player2 = (isMultiPlayer) ? new Player(ignPlayer2, PlayerType.LOCAL) : new AIPlayer(ignPlayer2, PlayerType.AI, aiStrategy);

        GameController controller = (GameController) loadScene("games/" + game + "/" + game + ".fxml");

        GameModel model = controller.getModel();
        model.setPlayer1(player1);
        model.setPlayer2(player2);

        start(controller, fullscreen);
    }

    private static void start(GameController controller, boolean fullscreen) {
        controller.setup();

        Main.primaryStage.setFullScreen(fullscreen);
    }

    private static AIStrategy determineAIStrategy(String game) {
        AIStrategy aiStrategy = null; //TODO: support multiple AI's/difficulties per game
        switch (game) {
            case "tictactoe":
                aiStrategy = new TicTacToeAI();
                break;
            case "othello":
                aiStrategy = new OthelloAIEasy();
                break;
        }

        return aiStrategy;
    }

    public Player getActivePlayer() {
        return getModel().getActivePlayer();
    }

    public void setup() {
        if (YOUR_TURN_COMMAND_BUFFER != null) {
            gameYourTurn(YOUR_TURN_COMMAND_BUFFER);
            YOUR_TURN_COMMAND_BUFFER = null;
        }
    }

    public void goBack() throws IOException {
        if (Main.serverConnection.hasConnection()) {
            Main.serverConnection.forfeit();
        }

        super.goBack();
    }

    public abstract void updateViews();

    public abstract void move(Move move);

    public abstract GameModel getModel();
}
