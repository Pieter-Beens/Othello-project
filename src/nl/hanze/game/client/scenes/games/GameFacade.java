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

    public static GameController startOnline(Map<String, String> args, boolean fullscreen, PlayerType playerType) throws IOException {
        String game = args.get("GAMETYPE").toLowerCase().replace("-", "");
        game = game.equals("reversi") ? "othello" : game;

        Player player1;
        if (playerType == PlayerType.AI) {
            player1 = new AIPlayer(GameModel.serverName, playerType, aiFactory.create(game, 2));
        } else {
            player1 = new Player(GameModel.serverName, playerType);
        }

        Player player2 = new Player(args.get("OPPONENT"), PlayerType.REMOTE);

        GameController controller = getController(game);
        GameModel model = controller.getModel();

        // Determine which player should begin (model.player1 always starts)
        model.setPlayer(0, args.get("PLAYERTOMOVE").equals(GameModel.serverName) ? player2 : player1);
        model.setPlayer(1, args.get("PLAYERTOMOVE").equals(GameModel.serverName) ? player1 : player2);

        return start(controller, fullscreen);
    }

    public static void startOffline(String ignPlayer1, String ignPlayer2, String game, boolean fullscreen) throws IOException {
        game = game.toLowerCase().replace("-", "");
        
        GameController controller = getController(game);
        GameModel model = controller.getModel();

        Player player1 = new Player(ignPlayer1, PlayerType.LOCAL);
        Player player2 = new Player(ignPlayer2, PlayerType.LOCAL);
        model.setPlayer(0, player1);
        model.setPlayer(1, player2);

        start(controller, fullscreen);
    }

    public static void startOffline(String game, boolean fullscreen, int difficulty) throws IOException {
        game = game.toLowerCase().replace("-", "");

        Player player1 = new Player("You", PlayerType.LOCAL);
        Player player2 = new AIPlayer("Computer", PlayerType.AI, aiFactory.create(game, SimpleAIFactory.HARD));

        GameController controller = getController(game);
        GameModel model = controller.getModel();

        model.setPlayer(0, player1);
        model.setPlayer(1, player2);

        start(controller, fullscreen);
    }

    private static GameController start(GameController controller, boolean fullscreen) {
        controller.setup();

        Main.primaryStage.setFullScreen(fullscreen);

        return controller;
    }

    private static GameController getController(String game) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        switch (game) {
            case "tictactoe":
                loader.setController(new TicTacToeController(
                        new TicTacToeModel()
                ));
                break;
            case "reversi":
            case "othello":
                loader.setController(new OthelloController(
                        new OthelloModel()
                ));
                break;
        }

        return (GameController) Controller.loadScene("games/game.fxml", loader);
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

        public  AIStrategy create(String game, int difficulty) {
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
