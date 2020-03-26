package nl.hanze.game.client.games;

import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.util.Move;
import nl.hanze.game.client.games.players.tictactoe.TicTacToePlayer;

public class TicTacToeGame extends Game {



    public TicTacToeGame(TicTacToePlayer... players) {
        super(3, players);
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
        //board[move.getX()][move.getY()] = 'X';
        board[move.getX()][move.getY()].setOwner(activePlayer);

        notifyObservers(move);

        Player activePlayer = players.get(0);  // uh ja ik wil dat de andere player de activePlayer wordt, maar misschien moet dat...
        // ...in een aparte functie genaamd nextTurn()?
    }
}
