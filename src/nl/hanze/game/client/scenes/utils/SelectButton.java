package nl.hanze.game.client.scenes.utils;

/**
 * @author Bart van Poele
 * This is a normal button with the behaviour of a  RadioButton
 */

public class SelectButton extends javafx.scene.control.RadioButton {

    public SelectButton() {
        getStyleClass().remove("radio-button");
        getStyleClass().add("toggle-button");
    }
}
