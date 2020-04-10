package nl.hanze.game.client.scenes.games.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.othello.OthelloController;

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

        setVgap(0);
        setHgap(0);
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
    public GameController getController() { return controller; }

    public abstract void enableValidFields();

    public abstract void update();

}
