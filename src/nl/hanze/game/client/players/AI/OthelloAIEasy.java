package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.Field;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Pieter Beens
 */

public class OthelloAIEasy implements AIStrategy {

    @Override
    public Move determineNextMove(Field[][] board, Player player, Player opponent) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignore) {}

        ArrayList<Field> validMoves = new ArrayList<>();

        for (Field[] row : board) {
            for (Field field : row) {
                if (field.getValidity()) validMoves.add(field);
            }
        }

        int i = new Random().nextInt(validMoves.size());
        Field chosenMove = validMoves.get(i);
        return new Move(player, chosenMove.getRowID(), chosenMove.getColumnID());
    }
}