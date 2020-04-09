package nl.hanze.game.client.scenes.games.othello;

import javafx.application.Platform;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.utils.Field;

import java.util.Stack;

/**
 * @author Pieter Beens
 */

public class OthelloModel extends GameModel {
    public OthelloModel(int boardSize) {
        super(boardSize);
    }

    @Override
    public void setup() {
        // sets up the opening state of the game with two stones of each color in the middle of the board
        getField(getBoardSize()/2, getBoardSize()/2).setOwner(getPlayer(0));
        getField(getBoardSize()/2 - 1, getBoardSize()/2 - 1).setOwner(getPlayer(0));
        getField(getBoardSize()/2 - 1, getBoardSize()/2).setOwner(getPlayer(1));
        getField(getBoardSize()/2, getBoardSize()/2 - 1).setOwner(getPlayer(1));

        players[0].changeScore(2);
        players[1].changeScore(2);

        //TODO: get starting player from server in online games

        super.setup();
    }

    public void recordMove(Move move) {
        Field targetField = board[move.getRow()][move.getColumn()];

        // stones are captured first...
        int captureTally = enactCaptures(targetField);
        getActivePlayer().changeScore(captureTally);
        players[(turnCounter+1)%2].changeScore(-captureTally);

        // ...before the stone is placed: this is because placing the stone first would affect enactCaptures>getCaptures calculation
        targetField.setOwner(move.getPlayer());
        getActivePlayer().changeScore(1);

        System.out.println(getActivePlayer().getName() + " captured " + captureTally + " Field(s)!");

        nextTurn(false);
    }

    // Note this is not an override! The parent class' nextTurn() accepts no parameters...
    public void nextTurn(boolean lastTurnWasSkipped) {
        super.nextTurn();

        updateFieldValidity();

        if (!turnHasMoves() && lastTurnWasSkipped) { //TODO: turnHasMoves should only be a check, not a setter!
            if (!Main.serverConnection.hasConnection()) {
                Platform.runLater(this::endGame);
            }
            return;
        }

        if (!turnHasMoves()) {
            System.out.println(getActivePlayer().getName() + " was unable to move, and had to skip their turn!");
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

    public Stack<Field> getCaptures(Field field) {
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
                if (nextField.getOwner() == getActivePlayer() && capturesInThisDirection.size() == 0) break;

                // success condition: if the loop has looped at least once and the next Field has the same owner, the captures are added to allCaptures
                if (nextField.getOwner() == getActivePlayer()) {
                    while (!capturesInThisDirection.isEmpty()) {
                        allCaptures.push(capturesInThisDirection.pop());
                    }
                    break;
                }

                // if the next Field has another owner, this might be a capture!
                if (nextField.getOwner() == getInactivePlayer()) {
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

    public int enactCaptures(Field field) {
        int captureTally = 0;
        Stack<Field> captures = getCaptures(field);
        for (Field capturedField : captures) {
            capturedField.setOwner(getActivePlayer());
            captureTally++;
        }
        return captureTally;
    }

    public boolean isValidMove(Move move) {
        if(move == null) return false;
        Field field = this.board[move.getRow()][move.getColumn()];
        return field.getValidity();
    }
}
