package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;
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

    private static final int MAXDEPTH = 6;

    private static final int CORNERSCORE = 20;
    private static final int BORDERSCORE = 5;
    private static final int XCROSSSCORE = -20;

    private static int[][] scoreBoard;

    @Override
    public Move determineNextMove(Field[][] board, Player player, Player opponent) {
        Field[][] boardCopy = board.clone();
        setupScoreBoard(boardCopy);

        int[] move = minMax(boardCopy, 0, player, opponent);
        return new Move(player, move[ROW], move[COLUMN]);
    }

    private void setupScoreBoard(Field[][] board) {
        scoreBoard = new int[board.length][board.length];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                if ((c == 0 && r == 0) ||
                        (c == 7 && r == 0) ||
                        (c == 0 && r == 7) ||
                        (c == 7 && r == 7)) {
                    scoreBoard[r][c] = CORNERSCORE;
                } else if (c == 0 || c == 7
                        || r == 0 || r == 7) {
                    scoreBoard[r][c] = BORDERSCORE;
                } else if ((c == 1 && r == 1) ||
                        (c == 6 && r == 1) ||
                        (c == 1 && r == 6) ||
                        (c == 6 && r == 6)) {
                    scoreBoard[r][c] = XCROSSSCORE;
                } else {
                    scoreBoard[r][c] = 1;
                }
            }
        }
    }

    private int[] minMax(Field[][] board, int depth, Player player, Player opponent) {
        int score;
        int bestScore = -50000;
        int bestRow = -1;
        int bestCol = -1;

        if (depth >= MAXDEPTH) {
            score = 0;
            for (Field[] row : board) {
                for (Field field : row) {
                    if (field.getOwner() == player) {
                        score += scoreBoard[field.getColumnID()][field.getRowID()];
                        if ((field.getColumnID() == 1 && field.getRowID() == 1) &&
                                (board[0][0].getOwner() == player)) {
                            score -= XCROSSSCORE; //it's okay to have the XCross if we also have the associated corner
                        }
                        else if ((field.getColumnID() == 6 && field.getRowID() == 1) &&
                                (board[7][0].getOwner() == player)) {
                            score -= XCROSSSCORE;
                        }
                        else if ((field.getColumnID() == 1 && field.getRowID() == 6) &&
                                (board[0][7].getOwner() == player)) {
                            score -= XCROSSSCORE;
                        }
                        else if ((field.getColumnID() == 6 && field.getRowID() == 6) &&
                                (board[7][7].getOwner() == player)) {
                            score -= XCROSSSCORE;
                        }
                    }
                    else if (field.getOwner() == opponent) {
                        score -= scoreBoard[field.getColumnID()][field.getRowID()];
                        if ((field.getColumnID() == 1 && field.getRowID() == 1) &&
                                (board[0][0].getOwner() == opponent)) {
                            score += XCROSSSCORE; //same as above, but mirrored for the opponent
                        }
                        else if ((field.getColumnID() == 6 && field.getRowID() == 1) &&
                                (board[7][0].getOwner() == opponent)) {
                            score += XCROSSSCORE;
                        }
                        else if ((field.getColumnID() == 1 && field.getRowID() == 6) &&
                                (board[0][7].getOwner() == opponent)) {
                            score += XCROSSSCORE;
                        }
                        else if ((field.getColumnID() == 6 && field.getRowID() == 6) &&
                                (board[7][7].getOwner() == opponent)) {
                            score += XCROSSSCORE;
                        }
                    }
                }
            }
            //=========================DEBUG
            String rt = "";
            for (Field[] row : board) {
                for (Field field : row) {
                    rt += field;
                }
                rt += "\n";
            }
            String type;
            if (player.getPlayerType() == PlayerType.AI) {
                type = "an AI";
            }
            else {
                type = "a human person";
            }
            System.out.println("SCORE OF THIS BOARD IS: " + score + ", calculated for " + type);
            System.out.println(rt);
            //=========================DEBUG
            return new int[]{score};
        }

        ArrayList<Field> validMoves = checkFieldValidity(board, player, opponent);

        for (Field cell : validMoves) {
            cell.setOwner(player);

            //=========================DEBUG
//            String rt = "";
//            for (Field[] row : board) {
//                for (Field field : row) {
//                    rt += field;
//                }
//                rt += "\n";
//            }
//            System.out.println("BEFORE FLIPS:");
//            System.out.println(rt);
            //=========================DEBUG

            Field[][] boardCopy = enactCaptures(cell, board, player, opponent);
//            rt = "";
//            for (Field[] row : boardCopy) {
//                for (Field field : row) {
//                    rt += field;
//                }
//                rt += "\n";
//            }
//            System.out.println("AFTER FLIPS:");
//            System.out.println(rt);
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

        if (validMoves.isEmpty()) bestScore = minMax(board, depth + 1, opponent, player)[SCORE];

        return new int[]{bestScore, bestRow, bestCol};
    }

    private Field[][] enactCaptures(Field field, Field[][] board, Player player, Player opponent) {
        Field[][] boardCopy = cloneBoard(board);
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

}
