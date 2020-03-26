package nl.hanze.game.client.games;

import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.games.players.othello.OthelloPlayer;
import nl.hanze.game.client.games.utils.Field;
import nl.hanze.game.client.util.Move;
import nl.hanze.game.client.util.GameObservable;
import nl.hanze.game.client.util.GameObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Game implements GameObservable {

    private ArrayList<GameObserver> observers;

    protected ArrayList<Player> players;

    protected Player activePlayer;

    protected int currentPlayerIndex = 0;

    protected int boardSize;
    protected Field[][] board;

    protected Game(int boardSize, Player... players) {
        if (players.length < getMinAmountOfPlayers() || players.length > getMaxAmountOfPlayers()) {
            throw new IllegalArgumentException("An invalid amount of nl.hanze.group1.framework.players was given, " + players.length +
                    " should be between min: " + getMinAmountOfPlayers() + " max: " + getMaxAmountOfPlayers());
        }

        this.boardSize = boardSize;
        this.board = new Field[getBoardSize()][getBoardSize()];
        this.players = new ArrayList<>(Arrays.asList(players));
        this.observers = new ArrayList<>();
    }

    public Player getNextPlayer() {
        Player nextPlayer = players.get(currentPlayerIndex);

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        return nextPlayer;
    }

    public void register(GameObserver o) {
        this.observers.add(o);
    }

    public void unregister(GameObserver o) {
        this.observers.remove(o);
    }

    public void notifyObservers(Move move) {
        for (GameObserver observer : observers) {
            observer.update(board, move);
        }
    }

    public Field[][] getBoard() {
        return board;
    }

    abstract public boolean isComplete();
    abstract public boolean validMove(Move move);
    public abstract void recordMove(Move move);
    abstract public Player getWinner();
    abstract public int getMinAmountOfPlayers();
    abstract public int getMaxAmountOfPlayers();
    abstract public int getBoardSize();
}
