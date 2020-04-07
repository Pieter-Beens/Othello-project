package nl.hanze.game.client.scenes.games.utils;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import nl.hanze.game.client.scenes.games.GameController;

public abstract class BoardPane extends GridPane {
    public void disableAllFields() {
        setDisablePropertyOnAllFields(true);
    }

    public void enableAllFields() {
        setDisablePropertyOnAllFields(false);
    }

    private void setDisablePropertyOnAllFields(boolean disable) {
        for (Node node : getChildren()) {
            FieldButton button = (FieldButton) node;
            button.setDisable(disable);
        }
    }

    public abstract GameController getController();
}
