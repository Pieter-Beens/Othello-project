package nl.hanze.game.client.players;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.Field;
import nl.hanze.game.client.scenes.utils.Colors;

public class Player {

    private PlayerType playerType;
    private String name;
    private int score;
    private String color;
    private String textcolor;

    public Player(String ign, PlayerType playerType) {
        this.name = ign;
        score = 0;
        color = Colors.BTN_COLOR;
        textcolor = Colors.BTN_TEXT_COLOR;
        this.playerType = playerType;
    }

    public void changeScore(int score) {
        this.score += score;
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

    public PlayerType getPlayerType() {
        return playerType;
    }

    public Move move(Field[][] board) {
        System.out.println("ERROR: Move calculation requested from non-AI player.");
        return null;
    }
}
