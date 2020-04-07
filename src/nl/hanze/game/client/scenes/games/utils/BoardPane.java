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
    protected FieldButton[][] fieldButtons;

    protected BoardPane(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;

        int boardSize = model.getBoardSize();
        int fieldSize = 680/boardSize;
        fieldButtons = new FieldButton[boardSize][boardSize];

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                FieldButton newField = new FieldButton(r, c);
                newField.setPrefSize(fieldSize,fieldSize);
                fieldButtons[r][c] = newField;
                this.add(newField, c, r);
            }
        }

        scale();
        //setPadding(new Insets(5, 5, 5, 5));
        setVgap(0);
        setHgap(0);
        setGridLinesVisible(true);
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
        for (FieldButton[] row : fieldButtons) for(Node node : row){
            FieldButton button = (FieldButton) node;
            button.setDisable(disable);
        }
    }

    public abstract void enableValidFields();

    public abstract GameController getController();
}
