package nl.hanze.game.client.scenes.games.tictactoe;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.Field;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.utils.Popup;

/**
 * @author Roy Voetman
 */
public class TicTacToeModel extends GameModel {
    private State currentState;

    public TicTacToeModel(int boardSize) {
        super(boardSize);

        currentState = State.IN_PROGRESS;
    }

    public void setup() {
        super.setup();

        players[0].setSign("O");
        players[1].setSign("X");
    }

    public boolean isValidMove(Move move) {
        Field field = this.board[move.getRow()][move.getColumn()];

        return (field.getOwner() == null);
    }

    public void recordMove(Move move) {
        board[move.getRow()][move.getColumn()].setOwner(move.getPlayer());

        nextTurn();
    }

    public void nextTurn() {
        currentState = state();

        super.nextTurn();
    }

    public State state() {
        return state(this.board);
    }

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

    public State getCurrentState() {
        return currentState;
    }

    public void endGameIfFinished() {
        if(currentState != TicTacToeModel.State.IN_PROGRESS) {
            String msg;
            if (currentState == State.O_WINS) {
                msg = players[0].getName() + " has won!";
            } else if (currentState == State.X_WINS) {
                msg = players[1].getName() + " has won!";
            } else {
                msg = players[1].getName() + " and " + players[0].getName() + " have tied for second place!";
            }

            Popup.display(msg, "GAME END", 300, 200);
        }
    }

    public enum State {
        IN_PROGRESS,
        DRAW,
        X_WINS,
        O_WINS
    }
}
