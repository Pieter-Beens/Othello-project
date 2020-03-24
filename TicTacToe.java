package nl.hanze.roy.ads.week4.minimax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TicTacToe {
    public static final int SCORE = 0;
    public static final int ROW = 1;
    public static final int COLUMN = 2;

    private static final Map<String, Integer> stateToScore = new HashMap<String, Integer>() {{
        put("O wins", 10);
        put("X wins", -10);
        put("draw", 0);
    }};

    private char[][] board;

    public TicTacToe() {
        this.board = new char[3][3];
    }

    public boolean recordMove(int row, int column, char player) {
        if (this.board[row][column] == '\u0000') {
            this.board[row][column] = player;

            return true;
        }

        return false;
    }

    private void AIMove() {
        char[][] board = Arrays.stream(this.board).map(char[]::clone).toArray(char[][]::new);

        int[] move = minimax(board, true);

        this.board[move[ROW]][move[COLUMN]] = 'O';
    }

    private int[] minimax(char[][] board, boolean isAIMove) {
        // Base case
        String curState = state(board);
        if (!curState.equals("In progress")) {
            return new int[]{stateToScore.get(curState)};
        }

        int bestRow = -1;
        int bestColumn = -1;
        int bestScore;
        int[] move;

        // Building the tree
        bestScore = (isAIMove) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (board[row][column] != '\u0000') continue;

                board[row][column] = (isAIMove) ? 'O' : 'X';
                move = minimax(board, !isAIMove);
                board[row][column] = '\u0000';

                // If it is an AI move maximize the score
                // Or if it is not an AI move minimize the score
                if ((isAIMove && move[SCORE] > bestScore) || (!isAIMove && move[SCORE] < bestScore)) {
                    bestScore = move[SCORE];
                    bestRow = row;
                    bestColumn = column;
                }
            }
        }

        return new int[]{bestScore, bestRow, bestColumn};
    }

    public String state() {
        return state(this.board);
    }

    public String state(char[][] board) {
        String state = null;

        // Check horizontal lines
        for (int row = 0; row < board.length; row++) {
            if (board[row][0] == '\u0000') continue;

            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                state = board[row][0] + " wins";
            }
        }

        // Check vertical lines
        for (int column = 0; column < board.length; column++) {
            if (board[0][column] == '\u0000') continue;

            if (board[0][column] == board[1][column] && board[1][column] == board[2][column]) {
                state = board[0][column] + " wins";
            }
        }

        // Check diagonal lines
        if (board[1][1] != '\u0000') {
            if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                state = board[1][1] + " wins";
            }

            if (board[2][0] == board[1][1] && board[1][1] == board[0][2]) {
                state = board[1][1] + " wins";
            }
        }

        int emptyCells = 0;
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (board[row][column] == '\u0000') {
                    emptyCells++;
                }
            }
        }

        if (state == null && emptyCells == 0) {
            return "draw";
        } else if (state != null) {
            return state;
        } else {
            return "In progress";
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                char current = board[row][column];

                string.append(current == '\u0000' ? "-" : current).append(" ");
            }

            string.append("\n");
        }

        return string.toString();
    }

    public static void main(String[] args) throws IOException {
        TicTacToe game = new TicTacToe();

        String state;

        do {
            System.out.println(game);
            boolean success;

            do {
                int[] coordinates = askUserForMove();

                success = game.recordMove(coordinates[0], coordinates[1], 'X');
            } while(!success);

            game.AIMove();

            state = game.state();
        } while(state.equals("In progress"));

        System.out.println(game);
        System.out.println(state);
    }

    public static int[] askUserForMove() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter your move (row, column): ");

        int[] coordinates = Arrays.stream(in.readLine().split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt).toArray();

        if (coordinates.length != 2) {
            System.out.println("Format: row, column");

            return askUserForMove();
        }

        if (coordinates[0] >= 3 || coordinates[1] >= 3) {
            System.out.println("Invalid coordinates");

            return askUserForMove();
        }

        return coordinates;
    }
}
