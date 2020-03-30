package nl.hanze.game.client.refactor.scenes.utils;

import javafx.beans.NamedArg;
import javafx.scene.control.Button;

public class MenuButton extends Button {
    protected boolean isActive = false;
    protected String value;

    public MenuButton(@NamedArg("btn-text") String text, @NamedArg("btn-value") String value) {
        super(text);
        this.setMinSize(100, 100);
        this.value = value;

        deselect();
    }

    public MenuButton(@NamedArg("btn-text") String text, @NamedArg("btn-value") String value, @NamedArg("is-active") boolean isActive) {
        this(text, value);

        if (isActive) {
            select();
        }
    }

    public void select() {
        isActive = true;
        this.setStyle("-fx-background-color: " + Colors.BTN_ACTIVE_COLOR + "; -fx-text-fill: " + Colors.BTN_ACTIVE_TEXT_COLOR);
    }

    public void deselect() {
        isActive = false;
        this.setStyle("-fx-background-color: " + Colors.BTN_COLOR + "; -fx-text-fill: " + Colors.BTN_TEXT_COLOR);
    }

    public boolean isActive() {
        return isActive;
    }

    public String getValue() {
        return value;
    }
}