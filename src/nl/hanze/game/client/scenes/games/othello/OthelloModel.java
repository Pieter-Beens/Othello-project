package nl.hanze.game.client.scenes.games.othello;

import javafx.application.Platform;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.utils.Field;

import java.util.Stack;

/**
 * @author Pieter Beens
 */

public class OthelloModel extends GameModel {
    public OthelloModel() {
        super(8);
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

        super.setup();
    }

    @Override
    public void recordMove(Move move) {
        Field targetField = board[move.getRow()][move.getColumn()];

        // stones are captured first...
        int captureTally = enactCaptures(targetField);
        getActivePlayer().changeScore(captureTally);
        players[(turnCounter+1)%2].changeScore(-captureTally);

        // ...before the stone is placed: this is because placing the stone first would affect enactCaptures() > getCaptures() calculation
        targetField.setOwner(move.getPlayer());
        updateRecentMove(targetField);
        getActivePlayer().changeScore(1);


        System.out.println(getActivePlayer().getName() + " moved to " + Main.alphabet[move.getRow()] + move.getColumn() + ", capturing " + captureTally + " Field(s)!");

        nextTurn(false);
    }

    @Override
    public void updateFieldValidity() {
        for (Field[] row : board) {
            for (Field field : row) {
                if (getCaptures(field).isEmpty()) field.setValidity(false);
                else field.setValidity(true);
            }
        }
    }

    // Note this is not an override! The parent class' nextTurn() accepts no parameters...
    public void nextTurn(boolean lastTurnWasSkipped) {
        super.nextTurn();

        GameModel.skippedTurnText = "";
        if (lastTurnWasSkipped) GameModel.skippedTurnText = getInactivePlayer().getName() + " skipped a turn!";

        if (!turnHasMoves() && lastTurnWasSkipped) {
            if (!Main.serverConnection.hasConnection()) {
                System.out.println("Neither player was able to move, so the game has ended!");
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

    public void updateRecentMove(Field recentMove) {
        for (Field[] row : board) {
            for (Field field : row) {
                field.unsetRecentMove();
            }
        }
        recentMove.setRecentMove();
    }

    public static int getBoardScore(Field[][] board, Player player, Player opponent) {
        int score = 0;

        // SOURCE 1: number of valid moves ==========================================================================
        for (Field[] row : board) {
            for (Field field : row) {
                if (field.getValidity()) score += 1;
            }
        }

        //TODO: implement negative score for opponent's valid moves on this board

        // SOURCE 2: stable stones ================================================================================

        //TODO: score per stable stone: 10

        for (Field[] row : board) {
            for (Field field : row) {
                if (isStable(field)) {
                    if (field.getOwner() == player) score += 0;
                    else score -= 0;
                }
            }
        }

        // SOURCE 3: corners and x-corners ========================================================================

        // cornerscore = 15
        //TODO: cornerscore = 10 for every distance from stable stones (yours or opponent's)
        // amazing depending on empty fields to build with...

        // x-cornerscore = -10 unless associated corner is occupied

        if (board[0][0].getOwner() == null) {
            if (board[1][1].getOwner() != null) {
                if (board[1][1].getOwner() == player) score += -10;
                else score += +10;
            }
        }
        else if (board[0][0].getOwner() == player) score += 15;
        else score += -15;

        if (board[7][0].getOwner() == null) {
            if (board[6][1].getOwner() != null) {
                if (board[6][1].getOwner() == player) score += -10;
                else score += +10;
            }
        }
        else if (board[7][0].getOwner() == player) score += 10;
        else score += -10;

        if (board[0][7].getOwner() == null) {
            if (board[1][6].getOwner() != null) {
                if (board[1][6].getOwner() == player) score += -10;
                else if (board[1][6].getOwner() == opponent) score += +10;
            }
        }
        else if (board[0][7].getOwner() == player) score += 10;
        else score += -10;

        if (board[7][7].getOwner() == null) {
            if (board[6][6].getOwner() != null) {
                if (board[6][6].getOwner() == player) score += -10;
                else score += +10;
            }
        }
        else if (board[7][7].getOwner() == player) score += 10;
        else score += -10;

        // SOURCE 4: edge patterns (hardcoded) =====================================================================

        for (int i = 2; i < 6; i++) {
            if (board[0][i].getOwner() != null) {
                if (board[0][i].getOwner() == player) score += 2;
                else score += -2;
            }
        }

        String string = "";
        for (int i = 0; i < 8; i++) {
            try { string += board[0][i].getOwner().getSign(); } catch (NullPointerException e) { string += "-"; }
        }

        //if (string.equals("-O-OO-O-")) score += 10;
        //TODO: USE REGEX FOR PATTERNS

        // SOURCE 5: ?????????????? ===============================================================================

        return score;
    }

    public static boolean isStable(Field field) {
        boolean result = true;

        //TODO: check all directions in sets of two opposites:
        // if no empty fields on either side, or only same color (and no empty) on one side, field is stable

        return result;
    }
}
