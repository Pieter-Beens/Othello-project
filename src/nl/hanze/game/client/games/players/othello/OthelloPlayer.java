package nl.hanze.game.client.games.players.othello;

import nl.hanze.game.client.Application;
import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.util.Move;

public abstract class OthelloPlayer extends Player {

    private int score;
    private String color;
    private String textcolor;

    public OthelloPlayer(String ign) {
        super(ign);
        score = 0;
        color = Application.BTN_COLOR;
        textcolor = Application.BTN_TEXT_COLOR;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String[] getColors() {
        return new String[]{color, textcolor};
    }

    public void setStartingColors() {
        color = Application.BTN_ACTIVE_COLOR;
        textcolor = Application.BTN_ACTIVE_TEXT_COLOR;
    }
}
