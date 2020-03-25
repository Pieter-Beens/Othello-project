package nl.hanze.game.client.games;

import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.util.Move;
import nl.hanze.game.client.games.players.tictactoe.TicTacToePlayer;

public class TicTacToe extends Game {

    public TicTacToe(TicTacToePlayer... players) {
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
        return 3;
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
    public Player getWinner() {
        return null;
    }

    public void recordMove(Move move) {
        board[move.getX()][move.getY()] = 'X';

        notifyObservers(move);
    }
}
