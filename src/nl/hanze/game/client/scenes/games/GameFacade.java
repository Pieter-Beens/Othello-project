package nl.hanze.game.client.scenes.games;

import javafx.fxml.FXMLLoader;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.*;
import nl.hanze.game.client.players.AIPlayer;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.othello.OthelloController;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;
import nl.hanze.game.client.scenes.games.tictactoe.TicTacToeController;
import nl.hanze.game.client.scenes.games.tictactoe.TicTacToeModel;

import java.io.IOException;
import java.util.Map;

/**
 * This class masks the more complex underlying logic of starting a Game.
 * (Facade Pattern)
 *
 * @author Roy Voetman
 */
public class GameFacade {
    private static final SimpleAIFactory aiFactory = new SimpleAIFactory();
    private static final SimpleGameSceneFactory gameSceneFactory = new SimpleGameSceneFactory();

    /**
     * Starts an offline game for two manual players.
     *
     * @author Roy Voetman
     * @author Roy Voetman
     * @param ignPlayer1 In game name of player 1.
     * @param ignPlayer2 In game name of player 2.
     * @param game The requested game to play.
     * @param fullscreen If the game should play in fullscreen.
     * @param turnTime The time players have to do a move.
     * @throws IOException When the game.fxml file could not be found.
     */
    public static void startOffline(String ignPlayer1, String ignPlayer2, String game, boolean fullscreen, int turnTime) throws IOException {
        game = game.toLowerCase().replace("-", "");

        // Create to local players.
        Player player1 = new Player(ignPlayer1, PlayerType.LOCAL);
        Player player2 = new Player(ignPlayer2, PlayerType.LOCAL);

        startOffline(player1, player2, game, fullscreen, turnTime);
    }

    /**
     * Starts an offline game for a manual player against an AI.
     *
     * @author Roy Voetman
     * @param game The requested game to play.
     * @param difficulty The difficulty level of the AI.
     * @param fullscreen If the game should play in fullscreen.
     * @param turnTime The time players have to do a move.
     * @throws IOException When the game.fxml file could not be found.
     */
    public static void startOffline(String game, int difficulty, boolean fullscreen, int turnTime) throws IOException {
        game = game.toLowerCase().replace("-", "");

        // Create an AIPlayer and a local player.
        Player player1 = new AIPlayer("Julius", PlayerType.AI, aiFactory.create(game, difficulty));
        //Player player2 = new AIPlayer("RoboPieter", PlayerType.AI, aiFactory.create(game, 1)); // USE TO TEST AGAINST MEDIUM AI
        Player player2 = new Player("You", PlayerType.LOCAL);

        startOffline(player1, player2, game, fullscreen, turnTime);
    }

    /**
     * Generic version of creating an offline game.
     *
     * @author Roy Voetman
     * @param player1 Player object representing Player 1
     * @param player2 Player object representing Player 2
     * @param game The requested game to play.
     * @param fullscreen If the game should play in fullscreen.
     * @param turnTime The time players have to do a move.
     * @throws IOException When the game.fxml file could not be found.
     */
    private static void startOffline(Player player1, Player player2, String game, boolean fullscreen, int turnTime) throws IOException {
        GameController controller = gameSceneFactory.create(game, turnTime);
        GameModel model = controller.getModel();

        // Set players in the models.
        model.setPlayer(0, player1);
        model.setPlayer(1, player2);

        start(controller, fullscreen);
    }

    /**
     * Starts an online game.
     *
     * @author Roy Voetman
     * @param args Arguments from the "game match" command.
     * @param fullscreen If the game should play in fullscreen.
     * @param playerType playerType enum of the logged in player.
     * @return The GameController associated with the created game.
     * @throws IOException When the game.fxml file could not be found.
     */
    public static GameController startOnline(Map<String, String> args, boolean fullscreen, PlayerType playerType) throws IOException {
        String game = args.get("GAMETYPE").toLowerCase().replace("-", "");

        GameController controller = gameSceneFactory.create(game, GameModel.serverTurnTime);
        GameModel model = controller.getModel();

        // Create the first player based on the provided params.
        Player player1;
        if (playerType == PlayerType.AI) {
            player1 = new AIPlayer(GameModel.serverName, playerType, aiFactory.create(game, 2));
        } else {
            player1 = new Player(GameModel.serverName, playerType);
        }

        Player player2 = new Player(args.get("OPPONENT"), PlayerType.REMOTE);

        // Determine which player should begin
        model.setPlayer(0, args.get("PLAYERTOMOVE").equals(GameModel.serverName) ? player2 : player1);
        model.setPlayer(1, args.get("PLAYERTOMOVE").equals(GameModel.serverName) ? player1 : player2);

        return start(controller, fullscreen);
    }

    /**
     * Generic version of creating a game.
     *
     * @author Roy Voetman
     * @param controller The GameController associated with the created game.
     * @param fullscreen If the game should play in fullscreen.
     * @return The GameController associated with the created game.
     */
    private static GameController start(GameController controller, boolean fullscreen) {
        // Inform controller players are now set in the models.
        controller.setup();

        Main.primaryStage.setFullScreen(fullscreen);

        return controller;
    }

    /**
     * This SimpleGameControllerFactory class serves as a Simple GameController Factory.
     * Game scenes are also automatically generated and loaded into the current stage.
     * (Simple Factory)
     *
     * @author Roy Voetman
     */
    private static class SimpleGameSceneFactory {
        /**
         * Automatically generates a game scene loads it into the current stage.
         *
         * @author Roy Voetman
         * @param game The requested game to play.
         * @param turnTime The time players have to do a move.
         * @return A GameController associated with the create game scene.
         * @throws IOException When the game.fxml file could not be found.
         */
        public GameController create(String game, int turnTime) throws IOException {
            FXMLLoader loader = new FXMLLoader();

            switch (game) {
                case "tictactoe":
                    loader.setController(new TicTacToeController(
                            new TicTacToeModel(), turnTime
                    ));
                    break;
                case "reversi":
                case "othello":
                    loader.setController(new OthelloController(
                            new OthelloModel(), turnTime
                    ));
                    break;
            }

            return (GameController) Controller.loadScene("games/game.fxml", loader);
        }
    }

    /**
     * This SimpleAIFactory class serves as a Simple AIStrategy Factory.
     * Providing different AIs based on the given game and difficulty
     * (Simple Factory)
     *
     * @author Roy Voetman
     */
    private static class SimpleAIFactory {
        public static final int EASY = 0;
        public static final int MEDIUM = 1;
        public static final int HARD = 2;

        /**
         * Creates an AIStrategy based on the given parameters
         *
         * @author Roy Voetman
         * @param game The requested game to play.
         * @param difficulty The difficulty level of the AI.
         * @return A concrete implementation of the AIStrategy interface.
         */
        public AIStrategy create(String game, int difficulty) {
            AIStrategy aiStrategy = null;

            switch (game) {
                case "tictactoe":
                    aiStrategy = new TicTacToeAI();
                    break;
                case "reversi":
                case "othello":
                    if (difficulty == EASY) aiStrategy = new OthelloAIEasy();
                    if (difficulty == MEDIUM) aiStrategy = new OthelloAIMedium();
                    if (difficulty == HARD) aiStrategy = new OthelloAIHard();
                    break;
            }

            return aiStrategy;
        }
    }
}
