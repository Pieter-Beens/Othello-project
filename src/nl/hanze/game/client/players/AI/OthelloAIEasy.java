package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.utils.Field;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Pieter Beens
 */

public class OthelloAIEasy implements AIStrategy {

    private Random rand = new Random();

    /**
     * Easy AI's implementation of AIStrategy interface method. This AI randomly selects a Move from a list of valid moves.
     * @author Pieter Beens
     * @param board An Othello gameboard on which the AI is asked to make a move.
     * @param player Represents the AI player.
     * @param opponent Represents the AI player's opponent.
     * @return Returns a move selected according to this AI's algorithm.
     */
    @Override
    public Move determineNextMove(Field[][] board, Player player, Player opponent) {
        System.out.println("RNGesus is blessing us with His wisdom...");
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignore) {}

        ArrayList<Field> validMoves = new ArrayList<>();

        for (Field[] row : board) {
            for (Field field : row) {
                if (field.getValidity()) validMoves.add(field);
            }
        }
        if (validMoves.size() > 0) {
            int i = rand.nextInt(validMoves.size());
            Field chosenMove = validMoves.get(i);
            return new Move(player, chosenMove.getRowID(), chosenMove.getColumnID());
        }
        System.out.println("RNGesus decided the only valid move was to die for our sins");
        return null;
    }
}