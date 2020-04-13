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
 * TicTacToe game logic.
 *
 * @author Roy Voetman
 */
public class TicTacToeModel extends GameModel {
    public enum State {
        IN_PROGRESS,
        DRAW,
        X_WINS,
        O_WINS
    }

    private State currentState;

    /**
     * Constructs a TicTacToe model with board size 3.
     *
     * @author Roy Voetman
     */
    public TicTacToeModel() {
        super(3);

        currentState = State.IN_PROGRESS;
    }

    /**
     * Defines which player has which Sign on the board. (i.e. "O" or "X").
     *
     * @author Roy Voetman
     */
    @Override
    public void setup() {
        super.setup();

        players[0].setSign("O");
        players[1].setSign("X");
    }

    /**
     * Record a move by setting the owner of the provided move to the player associated with that move.
     *
     * @author Roy Voetman
     * @param move The move that needs to be recorded.
     */
    @Override
    public void recordMove(Move move) {
        board[move.getRow()][move.getColumn()].setOwner(move.getPlayer());

        nextTurn();
    }

    /**
     * Every turn calculate the new board state which is used to determine if the game has ended.
     *
     * @author Roy Voetman
     */
    @Override
    public void nextTurn() {
        currentState = state();

        if (currentState != State.IN_PROGRESS) {
            gameHasEnded = true;
        }

        super.nextTurn();

        // End the game if state is no longer in progress and game is offline.
        // Otherwise wait for the GAME command from the server.
        if (!Main.serverConnection.hasConnection() && hasGameEnded()) {
            Platform.runLater(() -> endGame(false));
        }
    }

    /**
     * Updates the validity of all the fields by checking if the field is already claimed or not.
     *
     * @author Roy Voetman
     */
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

    /**
     * Determine the state of the current board.
     *
     * @author Roy Voetman
     * @return The state enum of the current board.
     */
    public State state() {
        return state(this.board);
    }

    /**
     * Calculate the state of the given board array.
     *
     * @author Roy Voetman
     * @param board 2D representing a TicTacToe board.
     * @return The state in the form of an enum value.
     */
    public static State state(Field[][] board) {
        State state = null;

        // Check the horizontal lines
        for (int row = 0; row < board.length; row++) {
            if (board[row][0].getOwner() == null || board[row][1].getOwner() == null || board[row][2].getOwner() == null) continue;

            if (board[row][0].getOwner().equals(board[row][1].getOwner()) &&
                    board[row][1].getOwner().equals(board[row][2].getOwner())) {
                state = (board[row][0].getOwner().getSign().equals("O")) ? State.O_WINS : State.X_WINS;
            }
        }

        // Check the vertical lines
        for (int column = 0; column < board.length; column++) {
            if (board[0][column].getOwner() == null || board[1][column].getOwner() == null || board[2][column].getOwner() == null) continue;

            if (board[0][column].getOwner().equals(board[1][column].getOwner()) && board[1][column].getOwner().equals(board[2][column].getOwner())) {
                state = (board[0][column].getOwner().getSign().equals("O")) ? State.O_WINS : State.X_WINS;
            }
        }

        // Check the diagonal lines
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

        // Check the amount of cells that are empty
        int emptyCells = 0;
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (board[row][column].getOwner() == null) {
                    emptyCells++;
                }
            }
        }

        if (state == null && emptyCells == 0) {
            // If no lines could be found and there are no cells left its a draw.
            return State.DRAW;
        } else if (state != null) {
            // If there was a line found its a win for one of the two players.
            return state;
        } else {
            // No lines found and still empty cells, game is in progress.
            return State.IN_PROGRESS;
        }
    }

    /**
     * When the game has ended build the result message.
     *
     * @author Roy Voetman
     * @param timedOut Boolean indicating if the game stopped because of a time out.
     */
    @Override
    public void endGame(boolean timedOut) {
        String msg;

        if (timedOut) msg = "TIME-OUT: " + getInactivePlayer().getName() + " has won!";
        else {
            if (currentState == State.O_WINS) {
                players[0].changeScore(1);
                players[1].changeScore(0);
                msg = players[0].getName() + " won!";
            } else if (currentState == State.X_WINS) {
                players[0].changeScore(0);
                players[1].changeScore(1);
                msg = players[1].getName() + " won!";
            } else {
                msg = players[1].getName() + " and " + players[0].getName() + " have tied for second place!";
            }


        }

        /** @author Jasper van Dijken */
        //If the game did not take place on the server
        //Set the result of the game, redirect to the offline menu
        try {
            if (!Main.serverConnection.hasConnection()) {
                OfflineMenuModel.setResultMessage(msg);
                Controller.loadScene("menu/offline/offline.fxml");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
