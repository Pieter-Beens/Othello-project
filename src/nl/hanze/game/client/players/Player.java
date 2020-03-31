package nl.hanze.game.client.players;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.Field;
import nl.hanze.game.client.scenes.utils.Colors;

public class Player {
    public static int counter = 0;

    private PlayerType playerType;
    private String name;
    private int score;
    private String color;
    private String sign;
    private String textcolor;

    public Player(String ign, PlayerType playerType) {
        Player.counter++;

        if (ign.length() > 10) ign = ign.substring(0,11);
        else if (ign.length() == 0) ign = "player" + counter;

        this.name = ign;
        score = 0;
        color = Colors.BTN_COLOR;
        textcolor = Colors.BTN_TEXT_COLOR;
        this.playerType = playerType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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
        throw new UnsupportedOperationException("Move calculation requested from non-AI player.");
    }

    @Override
    public String toString() {
        return "Player{" + "playerType=" + playerType + ", name='" + name + '\'' + ", score=" + score + '}';
    }
}
