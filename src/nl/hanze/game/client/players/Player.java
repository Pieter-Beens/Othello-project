package nl.hanze.game.client.players;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.Field;
import nl.hanze.game.client.scenes.utils.Colors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Player {
    public static int counter = 0;

    private PlayerType playerType;
    private String name;
    private int score;
    private String color;
    private String sign;
    private String textcolor;
    private Image reversiImage;
    private Image tictactoeImage;

    public Player(String ign, PlayerType playerType) {
        Player.counter++;

        if (ign.length() > 10) ign = ign.substring(0,11);
        else if (ign.length() == 0) ign = "player" + counter;

        this.name = ign;
        score = 0;
        color = Colors.BTN_COLOR;
        textcolor = Colors.BTN_TEXT_COLOR;
        this.playerType = playerType;
        try {
            reversiImage = new Image(new FileInputStream("src/resources/whitestone.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Image getReversiImage() {
        return reversiImage;
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
        try {
            reversiImage = new Image(new FileInputStream("src/resources/blackstone.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public Move calculateMove(Field[][] board, Player opponent) {
        throw new UnsupportedOperationException("Move calculation requested from non-AI player.");
    }

    @Override
    public String toString() {
        return "Player{" + "playerType=" + playerType + ", name='" + name + '\'' + ", score=" + score + '}';
    }
}
