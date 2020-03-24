package sample;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class CommonButton extends Button {

    Game game;

    public CommonButton(Game game, boolean isDefault, String text) {
        super(text);
        this.setMinSize(100, 100);

        this.game = game;

        if (isDefault) this.setStyle("-fx-background-color: " + game.player2colorcode + "; -fx-text-fill: " + game.player2colorcodetext);
        else this.setStyle("-fx-background-color: " + game.player1colorcode + "; -fx-text-fill: " + game.player1colorcodetext);
    }

    public void select() {
        this.setStyle("-fx-background-color: " + game.player2colorcode + "; -fx-text-fill: " + game.player2colorcodetext);
    }

    public void deselect() {
        this.setStyle("-fx-background-color: " + game.player1colorcode + "; -fx-text-fill: " + game.player1colorcodetext);
    }
}