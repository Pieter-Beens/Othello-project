package nl.hanze.game.client.scenes.games.tictactoe;

import javafx.application.Platform;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.utils.Field;
import nl.hanze.game.client.scenes.menu.offline.OfflineMenuModel;
import nl.hanze.game.client.scenes.utils.Popup;

import java.io.IOException;

/**
 * @author Roy Voetman
 */
public class TicTacToeModel extends GameModel {
    private State currentState;

    public TicTacToeModel() {
        super(3);

        currentState = State.IN_PROGRESS;
    }

    @Override
    public void setup() {
        super.setup();

        players[0].setSign("O");
        players[1].setSign("X");
    }

    @Override
    public void recordMove(Move move) {
        board[move.getRow()][move.getColumn()].setOwner(move.getPlayer());

        nextTurn();
    }

    @Override
    public void nextTurn() {
        currentState = state();

        if (currentState != State.IN_PROGRESS) {
            gameHasEnded = true;
        }

        super.nextTurn();

        if (!Main.serverConnection.hasConnection() && hasGameEnded()) {
            Platform.runLater(this::endGame);
        }
    }

    @Override
    public void updateFieldValidity() {
        for (Field[] row : board) {
            for (Field field : row) {
                if (field.getOwner() == null) {
                    field.setValidity(true);
                } else {
                    field.setValidity(false);
                }
            }
        }
    }

    public State state() {
        return state(this.board);
    }

    /**
     * @author Roy Voetman
     */
    public static State state(Field[][] board) {
        State state = null;

        // Check horizontal lines
        for (int row = 0; row < board.length; row++) {
            if (board[row][0].getOwner() == null || board[row][1].getOwner() == null || board[row][2].getOwner() == null) continue;

            if (board[row][0].getOwner().equals(board[row][1].getOwner()) &&
                    board[row][1].getOwner().equals(board[row][2].getOwner())) {
                state = (board[row][0].getOwner().getSign().equals("O")) ? State.O_WINS : State.X_WINS;
            }
        }

        // Check vertical lines
        for (int column = 0; column < board.length; column++) {
            if (board[0][column].getOwner() == null || board[1][column].getOwner() == null || board[2][column].getOwner() == null) continue;

            if (board[0][column].getOwner().equals(board[1][column].getOwner()) && board[1][column].getOwner().equals(board[2][column].getOwner())) {
                state = (board[0][column].getOwner().getSign().equals("O")) ? State.O_WINS : State.X_WINS;
            }
        }

        // Check diagonal lines
        if (board[1][1].getOwner() != null) {

            if (board[0][0].getOwner() != null && board[2][2].getOwner() != null) {
                if (board[0][0].getOwner().equals(board[1][1].getOwner()) &&
                        board[1][1].getOwner().equals(board[2][2].getOwner())) {
                    state = (board[1][1].getOwner().getSign().equals("O")) ? State.O_WINS : State.X_WINS;
                }
            }

            if (board[2][0].getOwner() != null && board[0][2].getOwner() != null) {
                if (board[2][0].getOwner().equals(board[1][1].getOwner()) &&
                        board[1][1].getOwner().equals(board[0][2].getOwner())) {
                    state = (board[1][1].getOwner().getSign().equals("O")) ? State.O_WINS : State.X_WINS;
                }
            }
        }

        int emptyCells = 0;
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (board[row][column].getOwner() == null) {
                    emptyCells++;
                }
            }
        }

        if (state == null && emptyCells == 0) {
            return State.DRAW;
        } else if (state != null) {
            return state;
        } else {
            return State.IN_PROGRESS;
        }
    }

    @Override
    public void endGame() {
        String msg;
        if (currentState == State.O_WINS) {
            players[0].changeScore(1);
            players[1].changeScore(0);
            msg = players[0].getName() + " has won!";
        } else if (currentState == State.X_WINS) {
            players[0].changeScore(0);
            players[1].changeScore(1);
            msg = players[1].getName() + " has won!";
        } else {
            msg = players[1].getName() + " and " + players[0].getName() + " have tied for second place!";
        }

        //Popup.display(msg, "GAME END", 300, 200);

        /**
         * @author Jasper van Dijken
         */
        //Send the result of the game, redirect to lobby
        try {
            if (!Main.serverConnection.hasConnection()) {
                OfflineMenuModel.setResultMessage(msg);
                Controller.loadScene("menu/offline/offline.fxml");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public enum State {
        IN_PROGRESS,
        DRAW,
        X_WINS,
        O_WINS
    }
}
