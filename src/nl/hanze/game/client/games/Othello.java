package nl.hanze.game.client.games;

import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.util.Move;
import nl.hanze.game.client.games.players.othello.OthelloPlayer;

public class Othello extends Game {

    public Othello(OthelloPlayer... players) {
        super(players);
    }

    @Override
    public int getMinAmountOfPlayers() {
        return 2;
    }

    @Override
    public int getMaxAmountOfPlayers() {
        return 2;
    }

    @Override
    public int getBoardSize() {
        return 4;
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean validMove(Move move) {
        return true;
    }

    @Override
    public void recordMove(Move move) {
        board[move.getX()][move.getY()] = 'X';

        notifyObservers(move);
    }

    @Override
    public Player getWinner() {
        return null;
    }
}
