package nl.hanze.game.client.players;

import nl.hanze.game.client.Application;
import nl.hanze.game.client.scenes.utils.Colors;

public class Player {

    private String name;
    private int score;
    private String color;
    private String textcolor;

    public Player(String ign) {
        this.name = ign;
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
        color = Colors.BTN_ACTIVE_COLOR;
        textcolor = Colors.BTN_ACTIVE_TEXT_COLOR;
    }

    public String getName() {
        return name;
    }
}
