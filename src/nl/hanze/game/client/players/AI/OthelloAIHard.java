package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.utils.Field;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;
import nl.hanze.game.client.scenes.games.GameModel;

import java.util.ArrayList;
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

    /**
     * Main method for this class.
     * @param board the board for which a move needs to be determined. A double array of Fields.
     * @param player the ai (Julius).
     * @param opponent it's opponent.
     * @return a valid move on the board, which will be the best move according to this class.
     */
    @Override
    public Move determineNextMove(Field[][] board, Player player, Player opponent) {
        Field[][] boardCopy = board.clone();
        setupScoreBoard(boardCopy);

        int[] move = minMax(boardCopy, 0, player, opponent);
        return new Move(player, move[ROW], move[COLUMN]);
    }

    /**
     * This method creates a score based on the squares of an othello board
     * @param board the board for which a score needs to be made. Only relevant for it's length. A double array of
     *              Fields.
     */
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

        //TODO see if you can't make this method more efficient using model commented below

//        scoreBoard[0][0] = CORNERSCORE;
//        scoreBoard[7][0] = CORNERSCORE;
//        scoreBoard[0][7] = CORNERSCORE;
//        scoreBoard[7][7] = CORNERSCORE;
//
//        scoreBoard[1][1] = XCROSSSCORE;
//        scoreBoard[6][1] = XCROSSSCORE;
//        scoreBoard[1][6] = XCROSSSCORE;
//        scoreBoard[6][6] = XCROSSSCORE;
    }

    /**
     * The meat of this class. This minMax algorithm recursively calculates the best move for whichever side is calling
     * it, by attempting every move, and backtracking out to see which one is best.
     * @param board The Othello board on which we're playing. A double array of Fields.
     * @param depth The depth of the iteration. Vital due to the recursive nature of this method.
     * @param player The player for which the best score is calculated.
     * @param opponent The opponent of the player.
     * @return Returns an int array containing the score of the best move, the row of the best move, and the column of
     * the best move.
     */
    private int[] minMax(Field[][] board, int depth, Player player, Player opponent) {
        int score; //the score of a move.
        int humanScore; //used to calculate the opponent's response.
        int bestScore = -5000; //the best score, set to an arbitrarily low amount so that at least one move is always returned
        int humanBestScore = -5000; //same as bestScore, but for the opponent
        int bestRow = -1; //the row associated with the best move.
        int bestCol = -1; //the column associated with the best move.

        if (depth >= MAXDEPTH) { //base case for the recursive call
            //TODO score = calculateScore(board, player, opponent); //calculate the value of this current board
            score = OthelloModel.getBoardScore(board);
            return new int[]{score}; //the score is returned
        }

        //valid moves for the active player are put in an array.
        ArrayList<Field> validMoves = checkFieldValidity(board, player, opponent);

        //for every valid move, a stone is put, and the appropriate captures are enacted.
        for (Field cell : validMoves) {
            cell.setOwner(player);
            Field[][] boardCopy = enactCaptures(cell, board, player, opponent);
            if (player.getPlayerType() == PlayerType.AI) { //if Julius is playing
                score = minMax(boardCopy, depth + 1, opponent, player)[SCORE]; //we continue as normal
            }
            else { //if Julius's opponent is playing
                //TODO humanScore = calculateScore(boardCopy, player, opponent); //we want to know the score
                humanScore = OthelloModel.getBoardScore(boardCopy);
                if (humanScore > humanBestScore) { //and we check if it's better than previous non-Julius player moves.
                    humanBestScore = humanScore;
                    score = minMax(boardCopy, depth + 1, opponent, player)[SCORE]; //this is a proper human move,
                    //so it will be used to calculate the next move.
                }
                else {
                    score = -5001; //this human move is trash, and so it won't be used.
                }
            }
            cell.setOwner(null); //the stone is removed again, so we have a clear board again.

            if (score > bestScore) { //if the score we have so far is better than previous ones, we use this move.
                bestScore = score;
                if (depth == 0) { //and if the depth is 0, the row and column are also relevant.
                    bestRow = cell.getRowID();
                    bestCol = cell.getColumnID();
                }
            }
        }

        //If the player had to skip a turn, the opponent might still be able to play, if not this method keeps being
        //invoked anyway until we reach the max depth.
        if (validMoves.isEmpty()) bestScore = minMax(board, depth + 1, opponent, player)[SCORE];

        return new int[]{bestScore, bestRow, bestCol}; //the best move is returned!
    }

    /**
     * Method which calculates the score on a given board.
     * @param board The Othello board for which the score needs to be calculated. A double array of Fields.
     * @param player The Player for which we're calculating the score.
     * @param opponent An instance of the Player class representing the player's opponent.
     * @return The score on the board, calculated for the player.
     */
    private int calculateScore(Field[][] board, Player player, Player opponent) {
        int score = 0;
        for (Field[] row : board) {
            for (Field field : row) {
                if (field.getOwner() == player) {
                    score += scoreBoard[field.getColumnID()][field.getRowID()];
                    if ((field.getColumnID() == 1 && field.getRowID() == 1) &&
                            (board[0][0].getOwner() == player)) {
                        score -= XCROSSSCORE; //it's okay to have the XCross if we also have the associated corner
                    } else if ((field.getColumnID() == 6 && field.getRowID() == 1) &&
                            (board[7][0].getOwner() == player)) {
                        score -= XCROSSSCORE;
                    } else if ((field.getColumnID() == 1 && field.getRowID() == 6) &&
                            (board[0][7].getOwner() == player)) {
                        score -= XCROSSSCORE;
                    } else if ((field.getColumnID() == 6 && field.getRowID() == 6) &&
                            (board[7][7].getOwner() == player)) {
                        score -= XCROSSSCORE;
                    }
                } else if (field.getOwner() == opponent) {
                    score -= scoreBoard[field.getColumnID()][field.getRowID()];
                    if ((field.getColumnID() == 1 && field.getRowID() == 1) &&
                            (board[0][0].getOwner() == opponent)) {
                        score += XCROSSSCORE; //same as above, but mirrored for the opponent
                    } else if ((field.getColumnID() == 6 && field.getRowID() == 1) &&
                            (board[7][0].getOwner() == opponent)) {
                        score += XCROSSSCORE;
                    } else if ((field.getColumnID() == 1 && field.getRowID() == 6) &&
                            (board[0][7].getOwner() == opponent)) {
                        score += XCROSSSCORE;
                    } else if ((field.getColumnID() == 6 && field.getRowID() == 6) &&
                            (board[7][7].getOwner() == opponent)) {
                        score += XCROSSSCORE;
                    }
                }
            }
        }
        //=========================DEBUG
//        String rt = "";
//        for (Field[] row : board) {
//            for (Field field : row) {
//                rt += field;
//            }
//            rt += "\n";
//        }
//        String type;
//        if (player.getPlayerType() == PlayerType.AI) {
//            type = "an AI";
//        }
//        else {
//            type = "a human person";
//        }
//        System.out.println("SCORE OF THIS BOARD IS: " + score + ", calculated for " + type);
//        System.out.println(rt);
        //=========================DEBUG
        return score;
    }


    /**
     * This method makes a copy of the given board, and flips all stones captured on it.
     * Copied from the homonymous method made by Pieter Beens in OthelloModel, and slightly edited by me.
     * @param field The field on which a move was just made.
     * @param board The board on which the move was made. A double array of Fields.
     * @param player The player that just made the move.
     * @param opponent The opponent.
     * @return A double array of the Field class, which is a copy of the board given in the parameters, but with the
     * appropriate captured stones flipped.
     */
    private Field[][] enactCaptures(Field field, Field[][] board, Player player, Player opponent) {
        Field[][] boardCopy = cloneBoard(board);
        Stack<Field> captures = getCaptures(field, boardCopy, player, opponent, true);
        for (Field capturedField : captures) {
            capturedField.setOwner(player);

        }
        return boardCopy;
    }

    /**
     * Method which checks all the possible captures from a given position.
     * Copied from the homonymous method made by Pieter Beens in OthelloModel, and slightly edited by me.
     * @param field The position from which captures are checked.
     * @param board The othello board on which we're playing. A double array of Fields.
     * @param player The player for which we're trying to find captures.
     * @param opponent The player's opponent.
     * @param capturing boolean used to see if we're actually trying to flip stones here or are just trying to see
     *                  if it's possible to flip some stones from a given position.
     * @return A stack containing instances of the Field class, which are positions on which captures are possible.
     */
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

    /**
     * This method is a modified version of the homonymous method used in OthelloModel. It finds all the valid moves
     * on a field, by seeing from which field(s) captures are possible. Any field from which you can capture some of
     * your opponent's stones is a valid move in Othello.
     * @param board The othello board on which we're playing. A double array of Fields.
     * @param player The Player for which we're trying to find valid moves.
     * @param opponent An instance of the Player class representing the player's opponent.
     * @return An ArrayList containing instances of the Field class, which are all valid moves for the player.
     */
    private ArrayList<Field> checkFieldValidity(Field[][] board, Player player, Player opponent) {
        ArrayList<Field> returnArray = new ArrayList<>();
        for (Field[] row : board) {
            for (Field field : row) {
                if (!getCaptures(field, board, player, opponent, false).isEmpty()) {
                    returnArray.add(field);
                }
            }
        }
        return returnArray;
    }

    /**
     * Method which properly clones the board by cloning all the Field classes inside it.
     * @param board A double array of Field instances, which needs to be cloned.
     * @return A double array of Field instances, every field instance a copy of the ones in the given board.
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

}