package nl.hanze.game.client.players.AI;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.Cell;

public class TicTacToeAI implements AIStrategy {

    @Override
    public Move determineNextMove(Cell[][] board, Player player) {
        return null;
    }
}

/*
    private Time maxTurnTime; //TODO: use .now() comparison check as condition for continued calculation

    public AI(int maxTurnTime) {
        this.maxTurnTime = new Time(maxTurnTime);
    }

    public Field getMove(ArrayList<ArrayList<Field>> fields) {
        //TODO: use own version of loopCheck, use recursively for up to 4 moves, store results in temp field and choose
        //      the best one (only consider valid moves, and drop the worst half every step!)
        int topCaptures = 0;
        Field chosenMove = null;
        for (ArrayList<Field> row : fields) {
            for (Field field : row) {
                if (field.getCaptureData().size() > topCaptures) {
                    chosenMove = field;
                    topCaptures = field.getCaptureData().size();
                }
            }
        }
        return chosenMove;
    }

    public void decideMove() {

    }
    //TODO: make multiple threads, one for every initial valid move, use temp arrays and store to shared array at the end (synchronized)
// (or maybe just let the main thread handle this part)

 */