package nl.hanze.game.client.views.boards.tictactoe;

import nl.hanze.game.client.util.Move;
import nl.hanze.game.client.views.boards.BoardView;

public class CommandLineView implements BoardView {
    @Override
    public void update(char[][] board, Move move) {
        StringBuilder string = new StringBuilder();

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                char current = board[row][column];

                string.append(board[row][column] == '\u0000' ? "-" : current).append(" ");
            }

            string.append("\n");
        }

        System.out.println(string.toString());
    }
}
