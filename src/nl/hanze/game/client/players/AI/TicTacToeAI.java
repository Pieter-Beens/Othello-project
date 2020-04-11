package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.tictactoe.TicTacToeModel;
import nl.hanze.game.client.scenes.games.utils.Field;

/**
 *This class implements the AI for the tic-tac-toe singleplayer game.
 *
 * Much of this code was inspired by the tic-tac-toe minmax video by CodeTrain
 * https://www.youtube.com/watch?v=trKjYdBASyQ
 * @author Nick
 * @version 1.1
 * @since 9-4-2020
 */
public class TicTacToeAI implements AIStrategy {

    private Player humanPlayer;
    private Player aiPlayer;

    /**
     * This interface method uses the minimax method to detirmine the best next move.
     * @param board the tic-tac-toe board being played on, which is a double array of the Field class.
     * @param player the AI player, which is an instance of the Player class.
     * @param opponent the Human player, which is an instance of the Player class.
     *
     * @return this method returns an instance of the Move class, which will contain the coordinates for the best move
     *         for the ai player.
     */
    public Move determineNextMove(Field[][] board, Player player, Player opponent) {
        humanPlayer = opponent;
        aiPlayer = player;

        int[] move = minimax(cloneBoard(board), true);

        return new Move(aiPlayer, move[1], move[2]);
    }

    /**
     * this method uses the minimax algorithm to recursively determine the best move for the AI player. Unlike other
     * boardgames that utilize the minimax algorithm, tic-tac-toe is simple enough to allow the computer to fully calculate every
     * possible move, thus we are left not with a heuristic guess to the best move, but instead the absolute best
     * move available.
     * @param board the tic-tac-toe board being played on, which is a double array of the Field class.
     * @param aiPlaying a boolean to determine whether or not it is the AI trying to decide the best move, in which
     *                  case the method will try and obtain the highest possible score. If this is not the case the
     *                  method will instead try to obtain the lowest possible score.
     *
     * @return an int array containing the score for the move, the row for that move, and the column for that move,
     *         respectively.
     */
    private int[] minimax(Field[][] board, boolean aiPlaying) {
        int score; //our movescore
        int bestScore; //the score of the best move so far
        int bestRow = -1; //the best row, set to a negative int because it will be overwritten
        int bestCol = -1; //the best column, set to a negative int because it will be overwritten

        //stop condition for the recursive call
        if (TicTacToeModel.state(board) != TicTacToeModel.State.IN_PROGRESS) { //if the game on the board is finished:
            if (TicTacToeModel.state(board) == TicTacToeModel.State.O_WINS) { //and O has won:
                if (humanPlayer.getSign().equals(("O"))) { //and if O is the human player's sign:
                    return new int[]{-1}; //we get a negative score, because the human won
                }
                else { //and if O is the computer player's sign:
                    return new int[]{1}; //we get a positive score, because the human lost
                }
            }
            else if (TicTacToeModel.state(board) == TicTacToeModel.State.X_WINS) { //same as above, but inverted for X
                if (humanPlayer.getSign().equals(("O"))) {
                    return new int[]{1};
                }
                else {
                    return new int[]{-1};
                }
            }
            else { //if there's a draw
                return new int[]{0}; //we get a neutral score
            }
        }

        //the score is set as an arbitrarily high or low number for the initial best score, so that even a guaranteed
        //loss or draw overrides it
        if (aiPlaying) {
            bestScore = -500;
        }
        else {
            bestScore = 500;
        }
        for (int i = 0; i < 9; i++) { //for every cell in the board
            if(board[i/3][i%3].getOwner() == null) { //if that cell is free
                Field field = new Field(i/3,i%3);
                //for either player, a field is made and placed on the board, and a recursive call is than made for the
                //opposing player
                if (aiPlaying) {
                    field.setOwner(aiPlayer);
                    board[i/3][i%3] = field;
                    score = minimax(board, false)[0];
                }
                else {
                    field.setOwner(humanPlayer);
                    board[i/3][i%3] = field;
                    score = minimax(board, true)[0];
                }
                board[i/3][i%3].setOwner(null); //the cell becomes free again, so the board is kept clear
                if ((score > bestScore && aiPlaying) || (score < bestScore && !aiPlaying)) { //if this move is better
                    bestScore = score; //than this score becomes the best score
                    bestRow = i/3;
                    bestCol = i%3; //and the row and column are saved
                }
            }
        }
        return new int[]{bestScore, bestRow, bestCol}; //the best move is returned at the end of the for loop
    }

    /**
     * This function copies every field in a given board. Used by tic-tac-toe so that any edits made to the board for the
     * minmax calculations are made on cloned fields.
     * @param board a double array of Fields that needs to be cloned.
     * @return a double array of with cloned Fields
     */
    private Field[][] cloneBoard(Field[][] board) {
        Field[][] boardCopy = new Field[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                boardCopy[r][c] = new Field(r,c);
                boardCopy[r][c].setOwner(board[r][c].getOwner());
            }
        }
        return boardCopy;
    }
}