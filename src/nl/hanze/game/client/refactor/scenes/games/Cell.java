package nl.hanze.game.client.refactor.scenes.games;

import nl.hanze.game.client.games.OthelloGame;
import nl.hanze.game.client.refactor.players.Player;

public class Cell {
    Player owner = null;
    boolean valid = false;
    boolean recentMove = false;

    public boolean checkValidity() {
        for (int[] direction : OthelloGame.directions) {
            valid = checkForCaptures(direction) > 0;
            if (valid) return true;
        }
        return false;
    }

    public int checkForCaptures(int[] direction) {
        int captureTally = 0;

        //MAGIC
        //wait I need access to the board here

        return captureTally;
    }

    //    public void loopCheck(Field originalField, int columndir, int rowdir, Stack<int[]> potentialCaptureData) {
//        int loopNumber = 1;
//        while (true) {
//
//            int[] nextField = new int[]{originalField.columnindex + (loopNumber * columndir), originalField.rowindex + (loopNumber * rowdir)};
//
//            // start with all cases where a direction will never make a move valid, so no further checks are necessary
//            if (nextField[0] < 0 || nextField[0] >= BOARDSIZE || nextField[1] < 0 || nextField[1] >= BOARDSIZE)
//                break; // breaks loop if next field is out of board range
//            if (this.fields.get(nextField[0]).get(nextField[1]).getColor() == 0)
//                break; // breaks loop if next field is empty
//
//            // success condition: next field is of your color, and this is not the first loop
//            if (this.fields.get(nextField[0]).get(nextField[1]).getColor() == this.turn && loopNumber > 1) {
//                while (!potentialCaptureData.empty()) {
//                    originalField.addToCaptureData(potentialCaptureData.pop());
//                }
//                this.turnHasNoValidMoves = false;
//                break;
//            }
//
//            if (this.fields.get(nextField[0]).get(nextField[1]).getColor() == this.turn && loopNumber == 1) break;
//
//            // if next field has the opposing color, there is a possibility a move here is valid
//            if (this.fields.get(nextField[0]).get(nextField[1]).getColor() == this.turn * -1) {
//                potentialCaptureData.push(nextField);
//                loopNumber++;
//            }
//        }
//    }

    public void setOwner(Player player) {
        owner = player;
    }

    public Player getOwner() {
        return owner;
    }
}
