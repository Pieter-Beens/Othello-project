package nl.hanze.game.client.views.utils;

import javafx.scene.control.Button;
import nl.hanze.game.client.Application;

public class MenuToggleButton extends Button {
    protected boolean toggle = false;

    public MenuToggleButton(String text1, String text2) {
        this.setMinSize(300, 50);
        this.setStyle("-fx-background-color: " + Application.BTN_COLOR + "; -fx-text-fill: " + Application.BTN_TEXT_COLOR);
        this.setText(text1);

        this.setOnMouseClicked(e -> {
            if (this.toggle) {
                this.setText(text1);
                this.setStyle("-fx-background-color: " + Application.BTN_COLOR + "; -fx-text-fill: " + Application.BTN_TEXT_COLOR);
            }
            else {
                this.setText(text2);
                this.setStyle("-fx-background-color: " + Application.BTN_ACTIVE_COLOR + "; -fx-text-fill: " + Application.BTN_ACTIVE_TEXT_COLOR);
            }

            this.toggle = !this.toggle;
        });
    }

    public boolean getStatus() {
        return this.toggle;
    }
}