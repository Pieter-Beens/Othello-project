package nl.hanze.game.client.players;

import javafx.scene.image.Image;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.scenes.games.utils.Field;
import nl.hanze.game.client.scenes.utils.Colors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Player {
    public static int counter = 0;

    private final PlayerType playerType;
    private final String name;
    private int score;
    private String sign;
    private Image reversiImage;
    private Image tictactoeImage;

    public Player(String ign, PlayerType playerType) {
        Player.counter++;

        if (ign.length() > 10) ign = ign.substring(0,11);
        else if (ign.length() == 0) ign = "player" + counter;

        this.name = ign;
        score = 0;

        this.playerType = playerType;
        try {
            reversiImage = new Image(new FileInputStream("src/resources/whitestone.png"));
            tictactoeImage = new Image(new FileInputStream("src/resources/O.png"));
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

    public Image getTictactoeImage() { return tictactoeImage; }

    public void changeScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void setStartingColors() {
        try {
            reversiImage = new Image(new FileInputStream("src/resources/blackstone.png"));
            tictactoeImage = new Image(new FileInputStream("src/resources/X.png"));
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
