package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.utils.Field;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;
import nl.hanze.game.client.scenes.games.GameModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * This AI uses primitive min-maxing to make at least some intelligent decisions.
 *
 * Its name is Julius.
 *
 * @Author Nick
 * @version 1.0
 * @since 9-4-2020
 */
public class OthelloAIHard implements AIStrategy {

    private static final int SCORE = 0;
    private static final int ROW = 1;
    private static final int COLUMN = 2;

    private static final int MAXDEPTH = 5;

    private static final int CORNERSCORE = 20;
    private static final int BORDERSCORE = 5;
    private static final int XCROSSSCORE = -20;

    private static HashMap<Field, Integer> scoreMap = new HashMap<>();
    private Field lastMove;

    @Override
    public Move determineNextMove(Field[][] board, Player player, Player opponent) {

        for(Field[] col : board) {
            for(Field cell : col) {
                if ((cell.getColumnID() == 0 && cell.getRowID() == 0) ||
                    (cell.getColumnID() ==7 && cell.getRowID() == 0) ||
                    (cell.getColumnID() == 0 && cell.getRowID() == 7) ||
                    (cell.getColumnID() == 7 && cell.getRowID() == 7)) {
                    scoreMap.put(cell, CORNERSCORE);
                }
                else if (cell.getColumnID() == 0 || cell.getColumnID() == 7
                        || cell.getRowID() == 0 || cell.getRowID() == 7) {
                    scoreMap.put(cell, BORDERSCORE);
                }
                else if ((cell.getColumnID() == 1 && cell.getRowID() == 1) ||
                        (cell.getColumnID() == 6 && cell.getRowID() == 1) ||
                        (cell.getColumnID() == 1 && cell.getRowID() == 6) ||
                        (cell.getColumnID() == 6 && cell.getRowID() == 6)) {
                    scoreMap.put(cell, XCROSSSCORE);
                }
                else {
                    scoreMap.put(cell, 1);
                }
            }
        }

        Field[][] boardCopy = board.clone();
        int[] move = minMax(boardCopy,0, player, opponent);
        return new Move(player ,move[ROW], move[COLUMN]);
    }

    private int[] minMax(Field[][] board, int depth, Player player, Player opponent) {
        int score;
        int bestScore = -50000;
        int bestRow = -1;
        int bestCol = -1;
        Field[][] boardCopy;

        if (depth >= MAXDEPTH) {
            score = 0;
            for (Field[] row : board) {
                for (Field field : row) {
                    if (field.getOwner() == player) {
                        score += scoreMap.get(field);
                    }
                    else if (field.getOwner() == opponent) {
                        score -= scoreMap.get(field);
                    }
                }
            }
            return new int[]{score};
        }

        //updateFieldValidity(board, player, opponent);
        ArrayList<Field> validMoves = checkFieldValidity(board, player, opponent);//new ArrayList<>();
//        for (Field[] row : board) {
//            for (Field field : row) {
//                if (field.getValidity()) validMoves.add(field);
//            }
//        }

        for (Field cell : validMoves) {
            cell.setOwner(player);
            String rt = "";
            for (Field[] row : board) {
                for (Field field : row) {
                    rt += field;
                }
                rt += "\n";
            }
            System.out.println("BEFORE FLIPS:");
            System.out.println(rt);
            boardCopy = enactCaptures(cell, board, player, opponent);
            rt = "";
            for (Field[] row : boardCopy) {
                for (Field field : row) {
                    rt += field;
                }
                rt += "\n";
            }
            System.out.println("AFTER FLIPS:");
            System.out.println(rt);
            lastMove = cell;
            score = minMax(boardCopy, depth + 1, opponent, player)[SCORE];
            cell.setOwner(null);

            if (score > bestScore) {
                bestScore = score;
                if (depth == 0) {
                    bestRow = cell.getRowID();
                    bestCol = cell.getColumnID();
                }
            }
        }

        return new int[]{bestScore, bestRow, bestCol};
    }

    private Field[][] enactCaptures(Field field, Field[][] board, Player player, Player opponent) {
        Field[][] boardCopy = board.clone();
        Stack<Field> captures = getCaptures(field, boardCopy, player, opponent, true);
        for (Field capturedField : captures) {
            capturedField.setOwner(player);

        }
        return boardCopy;
    }

    private Stack<Field> getCaptures(Field field, Field[][] board, Player player, Player opponent, Boolean capturing) {
        Stack<Field> allCaptures = new Stack<>();

        // occupied Fields are never a valid move - immediately return empty Stack
        if (field.getOwner() != null && !capturing) {
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

    private ArrayList<Field> checkFieldValidity(Field[][] board, Player player, Player opponent) {
        ArrayList<Field> returnArray = new ArrayList<>();
        for (Field[] row : board) {
            for (Field field : row) {
                if (!getCaptures(field, board, player, opponent, false).isEmpty()) {
                    returnArray.add(field);
                }//field.setValidity(false);
                //else field.setValidity(true);
            }
        }
        return returnArray;
    }

}
