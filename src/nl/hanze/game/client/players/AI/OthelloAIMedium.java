package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;
import nl.hanze.game.client.scenes.games.utils.Field;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Pieter Beens
 */

public class OthelloAIMedium implements AIStrategy {

    /**
     * Implementation of AIStrategy interface method. For every valid move in the current turn, this AI generates a new
     * board which is the result of that move, and picks the move which led to the board with the highest score (as
     * calculated by the getBoardScore() score evaluation method).
     * @author Pieter Beens
     * @param board An Othello gameboard on which the AI is asked to make a move.
     * @param player Represents the AI player.
     * @param opponent Represents the AI player's opponent.
     * @return Returns a move selected according to this AI's algorithm.
     */
    @Override
    public Move determineNextMove(Field[][] board, Player player, Player opponent) {
        System.out.println("RoboPieter is thinking...");

        Field[][] boardCopy = board.clone();

        ArrayList<Field> validMoves = new ArrayList<>();
        Move chosenMove = null;
        int bestScore = -50000;

        for (Field[] row : boardCopy) {
            for (Field field : row) {
                if (field.getValidity()) validMoves.add(field);
            }
        }

        for (Field field : validMoves) {
            Field[][] boardPostMove = enactCaptures(field, boardCopy, player, opponent);
            boardPostMove[field.getRowID()][field.getColumnID()].setOwner(player);
            int boardScore = OthelloModel.getBoardScore(boardPostMove, player, opponent);
            if (boardScore > bestScore) {
                bestScore = boardScore;
                chosenMove = new Move(player, field.getRowID(), field.getColumnID());
            }
        }
        return chosenMove;
    }

    /**
     * Creates a proper copy of the given board array, containing copies of the contained Fields rather than providing a
     * pointer to the existing Fields (which is what board.clone() does).
     * @author Pieter Beens
     * @param board An Othello gameboard (data model).
     * @return Returns a new board with the same values as the old.
     */
    private Field[][] cloneBoard(Field[][] board) {
        Field[][] boardCopy = new Field[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                boardCopy[r][c] = new Field(r,c);
                boardCopy[r][c].setOwner(board[r][c].getOwner());
            }
        }
        return boardCopy;
    }

    /**
     * Flips the owner property for all fields captured by the player moving to the given Field.
     * @author Pieter Beens
     * @param field A Field on which a stone has been or will be placed, leading to the captures found via getCaptures().
     * @param board An Othello gameboard on which the captures
     * @param player The active player placing the stone leading to the captures.
     * @param opponent The opponent whose stones may be flipped by this method.
     * @return Returns a proper copy of the given board with the move's captures enacted.
     */
    private Field[][] enactCaptures(Field field, Field[][] board, Player player, Player opponent) {
        Field[][] boardCopy = cloneBoard(board);
        Stack<Field> captures = getCaptures(field, boardCopy, player, opponent);
        for (Field capturedField : captures) {
            capturedField.setOwner(player);
        }
        return boardCopy;
    }

    /**
     * Finds all fields that would be captured if the active player placed a stone on the given Field (potentially zero).
     * @author Pieter Beens
     * @param field The Field where a stone may be placed by the player, leading to the captures found via this method.
     * @param board The Othello gameboard where captures would be taking place.
     * @param player The active player who would be placing a stone, leading to the captures found via this method.
     * @param opponent The opponent whose stones would be turned in the vent of a capture.
     * @return Returns a Stack containing all Fields that would be captured in the event of the active player placing a
     * stone on the given Field. The Stack will be empty if there are no captures possible, meaning the hypothetical move is
     * invalid.
     */
    private Stack<Field> getCaptures(Field field, Field[][] board, Player player, Player opponent) {
        Stack<Field> allCaptures = new Stack<>();

        // occupied Fields are never a valid move - immediately return empty Stack
        if (field.getOwner() != null) {
            return allCaptures;
        }

        for (int[] direction : GameModel.DIRECTIONS) {
            Stack<Field> capturesInThisDirection = new Stack<>();

            int currentRowID = field.getRowID();
            int currentColumnID = field.getColumnID();

            while (true) {
                int nextRowID = currentRowID + direction[0];
                int nextColumnID = currentColumnID + direction[1];

                // start with all cases where a direction will never make a move valid, so no further checks are necessary
                // directly borders a place outside the board > NO CAPTURES HERE
                if (nextRowID >= board.length || nextColumnID >= board.length || nextRowID < 0 || nextColumnID < 0) break;
                Field nextField = board[nextRowID][nextColumnID];

                // directly borders an empty Field > NO CAPTURES HERE
                if (nextField.getOwner() == null) break;
                // directly borders a Field with the same owner > NO CAPTURES HERE
                if (nextField.getOwner() == player && capturesInThisDirection.size() == 0) break;

                // success condition: if the loop has looped at least once and the next Field has the same owner, the captures are added to allCaptures
                if (nextField.getOwner() == player) {
                    while (!capturesInThisDirection.isEmpty()) {
                        allCaptures.push(capturesInThisDirection.pop());
                    }
                    break;
                }

                // if the next Field has another owner, this might be a capture!
                if (nextField.getOwner() == opponent) {
                    currentRowID = nextRowID;
                    currentColumnID = nextColumnID;
                    capturesInThisDirection.push(nextField);
                }
                // if the loop got this far, it means there is potential for a capture; the next iteration will check one Field further...
            }
        }
        // returns captures in all directions
        return allCaptures;
    }

    /**
     * Simple helper method which counts how many empty fields are left on a given board.
     * @author Pieter Beens
     * @param board A double array of Fields for which the number of unoccupied cells will be counted.
     * @return an int, representing the number of unoccupied fields on the given board.
     */
    public int countEmptyFields(Field[][] board) {
        int returnInt = 0;
        for (Field[] row : board) {
            for (Field field : row) {
                if (field.getOwner() == null) {
                    returnInt++;
                }
            }
        }
        return returnInt;
    }
}