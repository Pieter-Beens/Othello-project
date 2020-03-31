package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.Field;
import nl.hanze.game.client.scenes.games.tictactoe.TicTacToeModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TicTacToeAI implements AIStrategy {
    private static final int SCORE = 0;
    private static final int ROW = 1;
    private static final int COLUMN = 2;

    private Map<TicTacToeModel.State, Integer> stateToScore = new HashMap<TicTacToeModel.State, Integer>() {{
        put(TicTacToeModel.State.DRAW, 0);
    }};

    private Player maximizingPlayer;
    private Player minimizingPlayer;

    public Move determineNextMove(Field[][] board, Player player, Player opponent) {
        if (player.getSign().equals("O")) {
            stateToScore.put(TicTacToeModel.State.O_WINS, 10);
            stateToScore.put(TicTacToeModel.State.X_WINS, -10);
        } else {
            stateToScore.put(TicTacToeModel.State.O_WINS, -10);
            stateToScore.put(TicTacToeModel.State.X_WINS, 10);
        }

        this.maximizingPlayer = player;
        this.minimizingPlayer = opponent;

        Field[][] cloneBoard = Arrays.stream(board).map(Field[]::clone).toArray(Field[][]::new);

        if (TicTacToeModel.state(cloneBoard) != TicTacToeModel.State.IN_PROGRESS) {
            return null;
        }

        int[] move = minimax(cloneBoard, true);

        return new Move(maximizingPlayer, move[ROW], move[COLUMN]);
    }

    private int[] minimax(Field[][] board, boolean isMaximizing) {
        // Base case
        TicTacToeModel.State curState = TicTacToeModel.state(board);
        if (curState != TicTacToeModel.State.IN_PROGRESS) {
            return new int[]{stateToScore.get(curState)};
        }

        int bestRow = -1;
        int bestColumn = -1;
        int bestScore;
        int[] move;

        // Building the tree
        bestScore = (isMaximizing) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (board[row][column].getOwner() != null) continue;

                Field field = new Field(row, column);

                if (isMaximizing) {
                    field.setOwner(maximizingPlayer);
                } else {
                    field.setOwner(minimizingPlayer);
                }

                board[row][column] = field;

                move = minimax(board, !isMaximizing);

                board[row][column].setOwner(null);

                // If it is an AI move maximize the score
                // Or if it is not an AI move minimize the score
                if ((isMaximizing && move[SCORE] > bestScore) || (!isMaximizing && move[SCORE] < bestScore)) {
                    bestScore = move[SCORE];
                    bestRow = row;
                    bestColumn = column;
                }
            }
        }

        return new int[]{bestScore, bestRow, bestColumn};
    }
}