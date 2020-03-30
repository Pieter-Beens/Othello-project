package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.AIPlayer;
import nl.hanze.game.client.scenes.games.Cell;
import nl.hanze.game.client.scenes.games.othello.OthelloModel;

public class OthelloBoard extends GridPane {
    OthelloModel model;

    public OthelloBoard(OthelloModel model) {
        this.model = model;

        for (int r = 0; r < model.getBoardSize(); r++) {
            for (int c = 0; c < model.getBoardSize(); c++) {
                this.add(new FieldButton(r,c), r, c);
            }
        }

        scale();
        setPadding(new Insets(5, 5, 5, 5));
    }

    public void setValidMoves() {
        int rowID = 0;
        int columnID = 0;
        for (Cell[] row : model.getBoard()) {
            for (Cell field : row) {
                if (field.checkValidity()) {
                    getChildren().get(rowID * model.getBoardSize() + columnID).setDisable(false);
                } else {
                    getChildren().get(rowID * model.getBoardSize() + columnID).setDisable(true);
                }
                rowID++;
            }
            columnID++;
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

    public void update(Cell[][] board, Move move) {
        // update colors
        for (Node fieldNode : this.getChildren()) {
            FieldButton fieldButton = (FieldButton) fieldNode;
            try {
                String[] fieldColors = model.getCell(fieldButton.getRowID(), fieldButton.getColumnID()).getOwner().getColors();
                fieldButton.setStyle("-fx-background-color: " + fieldColors[0] + "; -fx-text-fill: " + fieldColors[1]);
            } catch (NullPointerException ignore) {}
        }

        //TODO: update recentmove signifiers

        if ( !(model.getActivePlayer() instanceof AIPlayer) ) setValidMoves();
    }
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