package sample;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;

public class AI {

    private Time maxTurnTime; //TODO: use .now() comparison check as condition for continued calculation
    private static final int IQ = 9001;

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
}
