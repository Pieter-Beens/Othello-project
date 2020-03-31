package nl.hanze.game.client.scenes.games.othello;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.Field;
import nl.hanze.game.client.scenes.games.GameModel;

import java.util.Stack;

public class OthelloModel extends GameModel {
    public OthelloModel(int boardSize) {
        super(boardSize);
    }

    @Override
    public void setup() {
        // sets up the opening state of the game with two stones of each color in the middle of the board
        getField(getBoardSize()/2, getBoardSize()/2).setOwner(getPlayer(1));
        getField(getBoardSize()/2 - 1, getBoardSize()/2 - 1).setOwner(getPlayer(1));
        getField(getBoardSize()/2 - 1, getBoardSize()/2).setOwner(getPlayer(0));
        getField(getBoardSize()/2, getBoardSize()/2 - 1).setOwner(getPlayer(0));

        players[0].changeScore(2);
        players[1].changeScore(2);

        //TODO: get starting player from server in online games

        super.setup();
    }

    public void placeStone(Move move) {
        board[move.getRow()][move.getColumn()].setOwner(move.getPlayer());
        activePlayer.changeScore(1);

        int captureTally = enactCaptures();
        activePlayer.changeScore(captureTally);
        players[(turnCounter+1)%2].changeScore(-captureTally);

        nextTurn(false);
    }

    // Note this is not an override! The parent class' nextTurn() accepts no parameters...
    public void nextTurn(boolean lastTurnWasSkipped) {
        super.nextTurn();

        updateFieldValidity();

        if (!turnHasMoves() && lastTurnWasSkipped) { //TODO: turnHasMoves should only be a check, not a setter!
            endGame();
            return;
        }

        if (!turnHasMoves()) {
            System.out.println(activePlayer.getName() + " was unable to move, and had to skip their turn!");
            this.nextTurn(true);
        }
    }

    public boolean turnHasMoves() {
        for (Field[] row : board) {
            for (Field field : row) {
                if (field.getValidity()) return true;
            }
        }
        return false;
    }

    public void updateFieldValidity() {
        for (Field[] row : board) {
            for (Field field : row) {
                if (getCaptures(field).isEmpty()) field.setValidity(false);
                else field.setValidity(true);
            }
        }
    }

    public Stack<Field> checkForCaptures() {
        Stack<Field> captureStack = new Stack<>();

        for (Field[] row : board) {
            for (Field field : row) {
                Stack<Field> fieldCaptures = getCaptures(field);
                while (!fieldCaptures.isEmpty()) {
                    captureStack.push(fieldCaptures.pop());
                }
            }
        }
        return captureStack;
    }

    public Stack<Field> getCaptures(Field field) {
        // occupied Fields are never a valid move!
        if (field.getOwner() != null) {
            return new Stack<>();
        }

        for (int[] direction : GameModel.DIRECTIONS) {

            Stack<Field> potentialCaptures = new Stack<>();

            int currentRowID = field.getRowID();
            int currentColumnID = field.getColumnID();

            while (true) {
                int nextRowID = currentRowID + direction[0];
                int nextColumnID = currentColumnID + direction[1];

                // start with all cases where a direction will never make a move valid, so no further checks are necessary
                // directly borders a place outside the board > NO CAPTURES HERE
                if (nextRowID >= board.length || nextColumnID >= board.length || nextRowID < 0 || nextColumnID < 0)
                    return new Stack<>();
                Field nextField = board[nextRowID][nextColumnID];

                // directly borders an empty Field > NO CAPTURES HERE
                if (nextField.getOwner() == null) return new Stack<>();
                // directly borders a Field with the same owner > NO CAPTURES HERE
                if (nextField.getOwner() == activePlayer && potentialCaptures.size() == 0) return new Stack<>();

                // success condition: if the loop has looped at least once and the next Field has the same owner, a full list of captures is returned
                if (nextField.getOwner() == activePlayer) {
                    return potentialCaptures;
                }

                // if the next Field has another owner, this might be a capture!
                if (nextField.getOwner() != activePlayer) {
                    currentRowID = nextRowID;
                    currentColumnID = nextColumnID;
                    potentialCaptures.add(nextField);
                }
                // if the loop got this far, it means there is potential for a capture; the next iteration will check one Field further...
            }
        }
        // this return statement should be impossible to reach
        return new Stack<>();
    }

    public int enactCaptures() {
        Stack<Field> captures = checkForCaptures();
        for (Field field : captures) field.setOwner(activePlayer);
        System.out.println(activePlayer.getName() + " captured " + captures.size() + " Fields!");
        return captures.size();
    }
}
