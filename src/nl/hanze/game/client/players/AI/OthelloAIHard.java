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
 * @version 1.4
 * @since 13-4-2020
 */
public class OthelloAIHard implements AIStrategy {

    private static final int SCORE = 0;
    private static final int ROW = 1;
    private static final int COLUMN = 2;

    private static final int MAXDEPTH = 5;
    private static final int LASTPHASE = 0;

    private static final int CORNERSCORE = 20;
    private static final int BORDERSCORE = 5;
    private static final int XCROSSSCORE = -20;

    private static int[][] scoreBoard = null;

    /**
     * Main method for this class.
     * @param board the board for which a move needs to be determined. A double array of Fields.
     * @param player the ai (Julius).
     * @param opponent it's opponent.
     * @return a valid move on the board, which will be the best move according to this class.
     */
    @Override
    public Move determineNextMove(Field[][] board, Player player, Player opponent) {
        System.out.println("Julius is thinking...");
        board = cloneBoard(board);

        int[] move;
        if (countEmptyFields(board) < LASTPHASE) { //if the amount of empty fields left is less than the lastphase field
            System.out.println(">USING LASTPHASE AI<");
            move = minMaxNotHeuristic(board, true, player, opponent);
        } //then we've reached the final phase and can calculate a win all the way
        else {
            move = minMax(board, 0, player, opponent); //otherwise, just use the regular minmax
        }
        return new Move(player, move[ROW], move[COLUMN]);
    }

    /**
     * The meat of this class. This minMax algorithm recursively calculates the best move for whichever side is calling
     * it, by attempting every move, and backtracking out to see which one is best.
     * @param board The Othello board on which we're playing. A double array of Fields.
     * @param depth The depth of the iteration. Vital due to the recursive nature of this method.
     * @param activePlayer The player for which the best score is calculated.
     * @param inactivePlayer The opponent of the active player.
     * @return Returns an int array containing the score of the best move, the row of the best move, and the column of
     * the best move.
     */
    private int[] minMax(Field[][] board, int depth, Player activePlayer, Player inactivePlayer) {

        int score; //the score of a move.
        int opponentScore; //used to calculate the opponent's response.
        int bestScore = -5000; //the best score, set to an arbitrarily low amount so that at least one move is always returned
        int opponentBestScore = -5000; //same as bestScore, but for the opponent
        int bestRow = -1; //the row associated with the best move.
        int bestCol = -1; //the column associated with the best move.
        HashMap<Integer, Field> opponentResponse = new HashMap<>();

        if (depth >= MAXDEPTH) { //base case for the recursive call
            if (MAXDEPTH % 2 == 0) {
                score = OthelloModel.getBoardScore(board, activePlayer, inactivePlayer); //calculate the value of this current board
                //score = calculateScore(board, activePlayer, inactivePlayer);
            }
            else { //if the max depth is an odd number, Julius is the inactive player instead of the active player
                score = OthelloModel.getBoardScore(board, inactivePlayer, activePlayer);
                //score = calculateScore(board, inactivePlayer, activePlayer);
            }
            return new int[]{score}; //the score is returned
        }

        //valid moves for the active player are put in an array.
        ArrayList<Field> validMoves = checkFieldValidity(board, activePlayer, inactivePlayer);

        //for every valid move, a stone is put, and the appropriate captures are enacted.
        for (Field cell : validMoves) {
            Field[][] boardCopy = enactCaptures(cell, board, activePlayer, inactivePlayer);
            boardCopy[cell.getRowID()][cell.getColumnID()].setOwner(activePlayer);
            if (activePlayer.getPlayerType() == PlayerType.AI) { //if Julius is playing
                score = minMax(boardCopy, depth + 1, inactivePlayer, activePlayer)[SCORE]; //we continue as normal
            }
            else { //if Julius's opponent is playing
                opponentScore = OthelloModel.getBoardScore(boardCopy, activePlayer, inactivePlayer); //we want to know the score
                opponentResponse.put(opponentScore, cell); //and we put it in a scoremap
                score = -5001; //this move won't be used tho
            }

            if (score > bestScore) { //if the score we have so far is better than previous ones, we use this move.
                bestScore = score;
                if (depth == 0) { //and if the depth is 0, the row and column are also relevant.
                    bestRow = cell.getRowID();
                    bestCol = cell.getColumnID();
                }
            }
        }
        if (!opponentResponse.isEmpty()) { //if the non-julius is playing
            for(int key : opponentResponse.keySet()) {
                if (key > opponentBestScore) {
                    opponentBestScore = key; //we grab the highest-scoring key
                }
            }
            Field[][] boardCopy = enactCaptures(opponentResponse.get(opponentBestScore), board, activePlayer, inactivePlayer);
            boardCopy[opponentResponse.get(opponentBestScore).getRowID()][opponentResponse.get(opponentBestScore).getColumnID()].setOwner(activePlayer);
            bestScore = minMax(boardCopy, depth +1, inactivePlayer, activePlayer)[SCORE]; // we know this is the
            // best move for the non-Julius, so no need to compare them, this is the best score
        }
        //If the player had to skip a turn, the opponent might still be able to play, if not this method keeps being
        //invoked anyway until we reach the max depth.
        if (validMoves.isEmpty()) bestScore = minMax(board, depth + 1, inactivePlayer, activePlayer)[SCORE];

        return new int[]{bestScore, bestRow, bestCol}; //the best move is returned!
    }

    /**
     * Alternative min-max method, which calculates the best move for the ai based on what will win them the game,
     * without bothering with any of that heurism nonsense. This method should not be used unless there are only a few
     * moves left, due to time constraints!
     * @param board A double array of Fields, on which the othello game is played.
     * @param lastMoveWasValid A boolean that keeps track on whether or not the previous player could play their last
     *                         move. 2 players unable to play in a row constitutes a game end.
     * @param player An instance of the Player class, representing the current player, for which the best score will be
     *               calculated.
     * @param opponent An instance of the Player class, representing the opponent of the player.
     * @return Returns an int array containing the score of the best move, the row of the best move, and the column of
     * the best move.
     */
    public int[] minMaxNotHeuristic(Field[][] board, boolean lastMoveWasValid, Player player, Player opponent) {
        int score;
        int bestScore = -5000;
        int bestRow = -1;
        int bestCol = -1;

        ArrayList<Field> validMoves = checkFieldValidity(board, player, opponent);

        for(Field cell : validMoves) {
            cell.setOwner(player);
            Field[][] boardCopy = enactCaptures(cell, board, player, opponent);
            score = minMaxNotHeuristic(boardCopy, true, opponent, player)[SCORE];
            cell.setOwner(null);
            if ((score > bestScore && player.getPlayerType() == PlayerType.AI) ||
                    (score < bestScore && player.getPlayerType() != PlayerType.AI)) {
                bestScore = score;
                bestRow = cell.getRowID();
                bestCol = cell.getRowID();
            }
        }

        if (validMoves.isEmpty()) {
            if (lastMoveWasValid) {
                bestScore = minMaxNotHeuristic(board, false, opponent, player)[SCORE];
            }
            else { //2 invalid moves in a row? Game has ended!
                String boardWinCondition = didPlayerWin(board, player);
                if (boardWinCondition.equals("win")) {
                    if (player.getPlayerType() == PlayerType.AI) {
                        return new int[] {1}; //Julius has won
                    }
                    else {
                        return new int[] {-1}; //Julius has lost
                    }
                }
                else if (boardWinCondition.equals("lose")) {
                    if (player.getPlayerType() == PlayerType.AI) {
                        return new int[] {-1}; //Julius has lost
                    }
                    else {
                        return new int[] {1}; //Julius has won
                    }
                }
                else {//draw
                    return new int[] {0};
                }
            }
        }
        return new int[]{bestScore, bestRow, bestCol};
    }

    /**
     * Simple helper method which counts how many empty fields are left on a given board.
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

    /**
     * Method which looks at the given board and calculates if the given player has won, lost, or if the match resulted
     * in a draw.
     * @param board A double array of Fields representing the Othello board that we're going to look at to see who won.
     *              This board should contain a game that has finished.
     * @param player An instance of the player class, which should be a player that's on the given board.
     * @return A string consisting of "win", if the given player has won on that board; "loss" if the given player has
     * lost; or "draw" if neither player won.
     */
    private String didPlayerWin(Field[][] board, Player player) {
        int playerScore = 0;
        int opponentScore = 0;
        for (Field[] row : board) {
            for (Field field : row) {
                if (field.getOwner() == player) {
                    playerScore++;
                }
                else if (field.getOwner() != player && field.getOwner() != null) {
                    opponentScore++;
                }
            }
        }
        if (playerScore > opponentScore) {
            return "win";
        }
        else if (playerScore == opponentScore) {
            return "draw";
        }
        else {
            return "loss";
        }
    }


    /**
     * This method makes a copy of the given board, and flips all stones captured on it.
     * Copied from the eponymous method made by Pieter Beens in OthelloModel, and slightly edited by me.
     * @param field The field on which a move was just made.
     * @param board The board on which the move was made. A double array of Fields.
     * @param player The player that just made the move.
     * @param opponent The opponent.
     * @return A double array of the Field class, which is a copy of the board given in the parameters, but with the
     * appropriate captured stones flipped.
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
     * Method which checks all the possible captures from a given position.
     * Copied from the eponymous method made by Pieter Beens in OthelloModel.
     * @author Pieter Beens
     * @param field The position from which captures are checked.
     * @param board The othello board on which we're playing. A double array of Fields.
     * @param player The player for which we're trying to find captures.
     * @param opponent The player's opponent.
     * @return A stack containing instances of the Field class, which are positions on which captures are possible.
     */
    public static Stack<Field> getCaptures(Field field, Field[][] board, Player player, Player opponent) {
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
     * This method is a modified version of the eponymous method used in OthelloModel. It finds all the valid moves
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
                if (!getCaptures(field, board, player, opponent).isEmpty()) {
                    returnArray.add(field);
                }
            }
        }
        return returnArray;
    }

    /**
     * Method which properly clones the board array by also cloning all the Field classes inside it.
     * @param board A double array of Field instances, which needs to be cloned.
     * @return A double array of Field instances, every field instance a copy of the ones in the given board.
     */
    public Field[][] cloneBoard(Field[][] board) {
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