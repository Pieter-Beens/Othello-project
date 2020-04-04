package nl.hanze.game.client.scenes.games.tictactoe.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.tictactoe.TicTacToeController;
import nl.hanze.game.client.scenes.games.tictactoe.TicTacToeModel;

public class TicTacToeBoard extends GridPane {
    TicTacToeModel model;
    TicTacToeController controller;

    public TicTacToeBoard(TicTacToeModel model, TicTacToeController controller) {
        this.model = model;
        this.controller = controller;

        for (int r = 0; r < model.getBoardSize(); r++) {
            for (int c = 0; c < model.getBoardSize(); c++) {
                FieldButton button = new FieldButton(r,c);
                this.add(button, r, c);
            }
        }

        disableAllFields();

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

    public void update() {
        // Update color/ sign of fields
        for (Node fieldNode : this.getChildren()) {
            FieldButton fieldButton = (FieldButton) fieldNode;
            try {
                Player fieldOwner  = model.getField(fieldButton.getRowID(), fieldButton.getColumnID()).getOwner();
                String[] fieldColors = fieldOwner.getColors();
                fieldButton.setStyle("-fx-background-color: " + fieldColors[0] + "; -fx-text-fill: " + fieldColors[1]);
                fieldButton.setText(fieldOwner.getSign());
            } catch (NullPointerException ignore) {}
        }

        disableAllFields();
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

    public TicTacToeController getController() { return controller; }
}
