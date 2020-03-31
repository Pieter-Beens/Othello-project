package nl.hanze.game.client.scenes.games;

import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;

import java.util.Stack;

public class Field {
    Field[][] board;
    int rowID;
    int columnID;
    Player owner = null;
    boolean validity = false;
    boolean recentMove = false;

    public Field(int rowID, int columnID) {

        this.rowID = rowID;
        this.columnID = columnID;
    }

    //TODO: there has to be a better way to access activePlayer and board than passing a reference for every check... maybe Field should not be a class?
    public void updateValidity(Field[][] boardState, Player activePlayer) {

        validity = false;

        // occupied Fields are never a valid move!
        if (owner != null) {
            return;
        }

        // empty Fields will be checked for possible captures in eight directions
        for (int[] direction : OthelloModel.DIRECTIONS) {
            if (!checkForCaptures(direction, boardState, activePlayer).isEmpty()) {
                validity = true;
                return;
            }
        }
    }

    public Stack<Field> checkForCaptures(int[] direction, Field[][] boardState, Player activePlayer) {
        Stack<Field> potentialCaptures = new Stack<>();

        int currentRowID = rowID;
        int currentColumnID = columnID;

        while (true) {
            int nextRowID = currentRowID + direction[0];
            int nextColumnID = currentColumnID + direction[1];

            // start with all cases where a direction will never make a move valid, so no further checks are necessary
            // directly borders a place outside the board > NO CAPTURES HERE
            if (nextRowID >= boardState.length || nextColumnID >= boardState.length || nextRowID < 0 || nextColumnID < 0) return new Stack<>();
            Field nextField = boardState[nextRowID][nextColumnID];

            // directly borders an empty Field > NO CAPTURES HERE
            if (nextField.getOwner() == null) return new Stack<>();
            // directly borders a Field with the same owner > NO CAPTURES HERE
            if (nextField.getOwner() == activePlayer && potentialCaptures.size() == 0) return new Stack<>();

            // success condition: if the loop has looped at least once and the next Field has the same owner, a full list of captures is returned
            if (nextField.getOwner() == activePlayer) { return potentialCaptures; }

            // if the next Field has another owner, this might be a capture!
            if (nextField.getOwner() != activePlayer) {
                currentRowID = nextRowID;
                currentColumnID = nextColumnID;
                potentialCaptures.add(nextField);
            }
            // if the loop got this far, it means there is potential for a capture. the next iteration will check one Field further...
        }
    }

    public int enactCaptures(Field[][] board, Player activePlayer) {
        int captureTally = 0;

        for (int[] direction : OthelloModel.DIRECTIONS) {
            for (Field field : checkForCaptures(direction, board, activePlayer)) {
                field.setOwner(getOwner());
                captureTally++;
            }
        }
        System.out.println(activePlayer.getName() + " captured " + captureTally + " Fields!");
        return captureTally;
    }

    public void setOwner(Player player) {
        owner = player;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean getValidity() {
        return validity;
    }
}
