package nl.hanze.game.client.scenes.utils;

import javafx.beans.NamedArg;
import javafx.scene.control.Button;

public class MenuToggleButton extends Button {
    protected boolean toggle = false;

    public MenuToggleButton(@NamedArg("text1") String text1, @NamedArg("text2") String text2) {
        this.setMinSize(300, 50);
        this.setStyle("-fx-background-color: " + Colors.BTN_COLOR + "; -fx-text-fill: " + Colors.BTN_TEXT_COLOR);
        this.setText(text1);

        this.setOnMouseClicked(e -> {
            if (this.toggle) {
                this.setText(text1);
                this.setStyle("-fx-background-color: " + Colors.BTN_COLOR + "; -fx-text-fill: " + Colors.BTN_TEXT_COLOR);
            }
            else {
                this.setText(text2);
                this.setStyle("-fx-background-color: " + Colors.BTN_ACTIVE_COLOR + "; -fx-text-fill: " + Colors.BTN_ACTIVE_TEXT_COLOR);
            }

            this.toggle = !this.toggle;
        });
    }

    public boolean getStatus() {
        return this.toggle;
    }
}