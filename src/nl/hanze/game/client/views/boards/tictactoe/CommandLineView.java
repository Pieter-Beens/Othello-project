package nl.hanze.game.client.views.boards.tictactoe;

import nl.hanze.game.client.games.utils.Field;
import nl.hanze.game.client.util.Move;
import nl.hanze.game.client.views.boards.Board;

public class CommandLineView implements Board {
    @Override
    public void update(Field[][] board, Move move) {
        StringBuilder string = new StringBuilder();

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                Field currentField = board[row][column];

                //string.append(board[row][column] == '\u0000' ? "-" : currentField).append(" ");
                //TODO: gebruik Field.getOwner() om de char te bepalen: null = -, player1 = X, player2 = O
            }

            string.append("\n");
        }

        System.out.println(string.toString());
    }
}
