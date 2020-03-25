package nl.hanze.game.client.controllers;

import nl.hanze.game.client.games.Game;
import nl.hanze.game.client.games.Othello;
import nl.hanze.game.client.games.players.othello.AIPlayer;
import nl.hanze.game.client.games.players.othello.ManualPlayer;
import nl.hanze.game.client.games.players.othello.OthelloPlayer;

public class MenuController implements Controller {
    public void startBtnClicked(String ignPlayer1, String ignPlayer2, int boardSize, boolean fullscreen, boolean IsMultiPlayer) {
        OthelloPlayer player1 = new ManualPlayer(ignPlayer1);
        OthelloPlayer player2 = (IsMultiPlayer) ? new AIPlayer(ignPlayer2) : new ManualPlayer(ignPlayer2);;

        Game game = new Othello(player1, player2);
    }
}
