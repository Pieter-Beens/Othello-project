package nl.hanze.game.client.scenes.utils;

import javafx.beans.NamedArg;
import javafx.scene.control.Button;

/**
 * @author Roy Voetman
 */
public class MenuButton extends Button {
    protected boolean isActive = false;
    protected String value;

    /**
     * Constructs the MenuButton
     *
     * @author Roy Voetman
     * @param text Text of the button
     * @param value User provided data of the button
     */
    public MenuButton(@NamedArg("btn-text") String text, @NamedArg("btn-value") String value) {
        super(text);
        this.setMinSize(100, 100);
        this.value = value;

        deselect();
    }

    /**
     * Constructs the MenuButton with active param
     *
     * @author Roy Voetman
     * @param text Text of the button
     * @param value User provided data of the button
     * @param isActive Boolean stating the button is active
     */
    public MenuButton(@NamedArg("btn-text") String text, @NamedArg("btn-value") String value, @NamedArg("is-active") boolean isActive) {
        this(text, value);

        if (isActive) {
            select();
        }
    }

    /**
     * Select this button (active)
     *
     * @author Roy Voetman
     */
    public void select() {
        isActive = true;
        this.setStyle("-fx-background-color: " + Colors.BTN_ACTIVE_COLOR + "; -fx-text-fill: " + Colors.BTN_ACTIVE_TEXT_COLOR);
    }

    /**
     * Deselect this button (in-active)
     *
     * @author Roy Voetman
     */
    public void deselect() {
        isActive = false;
        this.setStyle("-fx-background-color: " + Colors.BTN_COLOR + "; -fx-text-fill: " + Colors.BTN_TEXT_COLOR);
    }

    /**
     * Getter for isActive field
     *
     * @author Roy Voetman
     * @return the current value in the isActive field
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Getter for value field
     *
     * @author Roy Voetman
     * @return the current value in the value field
     */
    public String getValue() {
        return value;
    }
}