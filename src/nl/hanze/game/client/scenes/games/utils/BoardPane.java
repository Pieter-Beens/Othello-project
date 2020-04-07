package nl.hanze.game.client.scenes.games.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;

public abstract class BoardPane extends GridPane {
    protected GameModel model;
    protected GameController controller;

    protected BoardPane(GameModel model, GameController controller, String color) {
        this.model = model;
        this.controller = controller;

        for (int r = 0; r < model.getBoardSize(); r++) {
            for (int c = 0; c < model.getBoardSize(); c++) {
                this.add(new FieldButton(r, c, color), r, c);
            }
        }

        scale();
        setPadding(new Insets(5, 5, 5, 5));
    }

    // called by listener when resizing
    public void scale() { //TODO: fix this
        double smallestDimension = Main.primaryStage.getWidth();
        if (Main.primaryStage.getHeight() < smallestDimension) smallestDimension = Main.primaryStage.getHeight();

        double fieldSize = (smallestDimension / model.getBoardSize()) * 0.90;
        for (Node fieldButton : getChildren()) {
            FieldButton fb = (FieldButton) fieldButton;
            fb.setMinSize(fieldSize, fieldSize);
        }

        double fieldSpacing = (smallestDimension / model.getBoardSize()) * 0.05;
        setVgap(fieldSpacing);
        setHgap(fieldSpacing);
    }

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

    public abstract void enableValidFields();

    public abstract GameController getController();
}
