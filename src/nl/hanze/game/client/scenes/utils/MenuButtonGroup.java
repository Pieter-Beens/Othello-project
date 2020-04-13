package nl.hanze.game.client.scenes.utils;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * @author Roy Voetman
 */
public class MenuButtonGroup extends HBox {
    private List<MenuButton> buttons;

    /**
     * Constructs a group of buttons
     *
     * @author Roy Voetman
     * @param buttons List of Menu Buttons that will be grouped
     */
    public MenuButtonGroup(List<MenuButton> buttons) {
        this.buttons = buttons;

        this.buttons.forEach((button) -> {
            this.getChildren().add(button);

            button.setOnMouseClicked(e -> select(button));
        });

        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
    }

    /**
     * Select the given btn object.
     *
     * @author Roy Voetman
     * @param btn The button to be selected.
     */
    public void select(MenuButton btn) {
        this.buttons.forEach(MenuButton::deselect);

        btn.select();
    }

    /**
     * Retrieve the currently selected button in the group.
     *
     * @return Selected MenuButton in the group.
     */
    public MenuButton getActive() {
        for (MenuButton btn : buttons) {
            if (btn.isActive()) {
                return btn;
            }
        }

        return null;
    }
}