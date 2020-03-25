package nl.hanze.game.client.views.utils;

import javafx.scene.control.Button;
import nl.hanze.game.client.views.MenuView;

public class MenuButton extends Button {
    protected boolean isActive = false;
    protected String value;

    public MenuButton(String text, String value) {
        super(text);
        this.setMinSize(100, 100);
        this.value = value;

        deselect();
    }

    public MenuButton(String text, String value, boolean isActive) {
        this(text, value);

        if (isActive) {
            select();
        }
    }

    public void select() {
        isActive = true;
        this.setStyle("-fx-background-color: " + MenuView.BTN_ACTIVE_COLOR + "; -fx-text-fill: " + MenuView.BTN_ACTIVE_TEXT_COLOR);
    }

    public void deselect() {
        isActive = false;
        this.setStyle("-fx-background-color: " + MenuView.BTN_COLOR + "; -fx-text-fill: " + MenuView.BTN_TEXT_COLOR);
    }

    public boolean isActive() {
        return isActive;
    }

    public String getValue() {
        return value;
    }
}