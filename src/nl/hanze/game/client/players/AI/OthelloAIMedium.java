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

    private Field[][] enactCaptures(Field field, Field[][] board, Player player, Player opponent) {
        Field[][] boardCopy = cloneBoard(board);
        Stack<Field> captures = getCaptures(field, boardCopy, player, opponent);
        for (Field capturedField : captures) {
            capturedField.setOwner(player);
        }
        return boardCopy;
    }

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
}