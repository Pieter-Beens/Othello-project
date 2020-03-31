package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.AIPlayer;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.Field;
import nl.hanze.game.client.scenes.games.othello.OthelloController;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;

public class OthelloBoard extends GridPane {
    OthelloModel model;
    OthelloController controller;

    public OthelloBoard(OthelloModel model, OthelloController controller) {
        this.model = model;
        this.controller = controller;

        for (int r = 0; r < model.getBoardSize(); r++) {
            for (int c = 0; c < model.getBoardSize(); c++) {
                this.add(new FieldButton(r,c), r, c);
            }
        }

        scale();
        setPadding(new Insets(5, 5, 5, 5));
    }

    public void enableValidFields() {
        int i = 0;
        for (Field[] row : model.getBoard()) {
            for (Field field : row) {
                if (field.getValidity()) {
                    getChildren().get(i).setDisable(false);
                } else {
                    getChildren().get(i).setDisable(true);
                }
                i++;
            }
        }
    }

    public void disableAllFields() {
        for (Node node : getChildren()) {
            FieldButton button = (FieldButton) node;
            button.setDisable(true);
        }
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
        // update colors
        for (Node fieldNode : this.getChildren()) {
            FieldButton fieldButton = (FieldButton) fieldNode;
            try {
                String[] fieldColors = model.getField(fieldButton.getRowID(), fieldButton.getColumnID()).getOwner().getColors();
                fieldButton.setStyle("-fx-background-color: " + fieldColors[0] + "; -fx-text-fill: " + fieldColors[1]);
            } catch (NullPointerException ignore) {}
        }

        //TODO: mark recent moves (and supply model logic in Field)

        // makes buttons representing valid moves clickable
        enableValidFields();
        if (model.getActivePlayer().getPlayerType() != PlayerType.LOCAL) disableAllFields();

    }

    public OthelloController getController() { return controller; }
}

//    public void setupBoard() {
//        Rectangle2D screenBounds = Screen.getPrimary().getBounds(); // get dimensions of screen
//        int smallestDimension = (int) screenBounds.getMaxX();
//        if ((screenBounds.getMaxY() - 50) < smallestDimension) smallestDimension = (int) (screenBounds.getMaxY() - 50);
//        int roomPerField = smallestDimension / BOARDSIZE;
//        if (!fullScreen) roomPerField *= 0.80;
//        this.FIELDSIZE = (int) (roomPerField * 0.95);
//        this.FIELDSPACING = (int) (roomPerField * 0.05);
//
//        //grid.setPadding(new Insets(10, 10, 10, 10));
//        this.grid.setMinSize(FIELDSIZE, FIELDSIZE);
//        this.grid.setVgap(FIELDSPACING);
//        this.grid.setHgap(FIELDSPACING);
//        this.grid.setAlignment(Pos.CENTER);
//
//        this.fields = new ArrayList<>();
//        for (int i = 0; i < BOARDSIZE; i++) {
//            this.fields.add(new ArrayList<>());
//            for (int j = 0; j < BOARDSIZE; j++) {
//                Field field = new Field(i, j);
//                field.setMinSize(FIELDSIZE, FIELDSIZE);
//                field.setStyle("-fx-background-color: " + boardcolorcode);
//                grid.add(field, i, j);
//
//                field.setOnMouseClicked(e -> { // set up listener
//                    new Thread(() -> {
//                        try {
//                            makeAMove(field);
//                        } catch (InterruptedException ex) {
//                            System.out.print("Yes, it happened, the Thread was interrupted! No move for you!;");
//                            ex.printStackTrace();
//                        }
//                    }).start();});
//
//                this.fields.get(i).add(field);
//            }
//        }