package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.utils.Field;
import nl.hanze.game.client.scenes.games.GameModel;

import java.util.ArrayList;
import java.util.HashMap;

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

    private Player humanPlayer;
    private Player aiPlayer;

    @Override
    public Move determineNextMove(Field[][] board, Player player, Player opponent) {

        this.humanPlayer= opponent;
        this.aiPlayer = player;

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

        int[] move = minMax(board,0,true);
        return new Move(aiPlayer,move[ROW], move[COLUMN]);
    }

    private int[] minMax(Field[][] board, int depth, boolean isItJulius) {
        int score;
        int bestScore;
        int bestRow = -1;
        int bestCol = -1;

        if (depth >= MAXDEPTH) {
            score = scoreMap.get(lastMove);
            return new int[] {score};
        }



        ArrayList<Field> validMoves = new ArrayList<>();
        for (Field[] row : board) {
            for (Field field : row) {
                if (field.getValidity()) validMoves.add(field);
            }
        }

        if (isItJulius) { //ai is playing
            bestScore = -500000;
            for (Field cell : validMoves) {
                cell.setOwner(aiPlayer);
                lastMove = cell;
                score = minMax(board, depth + 1, false)[SCORE];
                cell.setOwner(null);

                if (score > bestScore) {
                    bestScore = score;
                    bestRow = cell.getRowID();
                    bestCol = cell.getColumnID();
                }
            }
        } else { //human is playing
            bestScore = 500000;
            for (Field cell : validMoves) {
                cell.setOwner(humanPlayer);
                score = minMax(board, depth + 1, true)[SCORE] * -1;
                cell.setOwner(null);

                if (score < bestScore) {
                    bestScore = score;
                    bestRow = cell.getRowID();
                    bestCol = cell.getColumnID();
                }
            }
        }
        return new int[]{bestScore, bestRow, bestCol};
    }

}
