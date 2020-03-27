package nl.hanze.game.client.views.boards.othello;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import nl.hanze.game.client.controllers.OthelloController;
import nl.hanze.game.client.games.players.othello.OthelloManualPlayer;
import nl.hanze.game.client.games.players.othello.OthelloPlayer;
import nl.hanze.game.client.games.utils.Field;
import nl.hanze.game.client.util.Move;
import nl.hanze.game.client.views.boards.Board;

public class OthelloBoard extends GridPane implements Board {

    private OthelloController controller;
    private int boardSize;
    private OthelloPlayer player1;
    private OthelloPlayer player2;

    public OthelloBoard(OthelloController controller, int boardSize, OthelloPlayer player1, OthelloPlayer player2) {
        this.controller = controller;
        this.boardSize = boardSize;
        this.player1 = player1;
        this.player2 = player2;

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                this.add(new FieldButton(r,c), r, c);
            }
        }

        setPadding(new Insets(5, 5, 5, 5));
    }

    public void setValidMoves() {
        int rowID = 0;
        int columnID = 0;
        for (Field[] row : controller.getGame().getBoard()) {
            for (Field field : row) {
                if (field.checkValidity()) {
                    getChildren().get(rowID * boardSize + columnID).setDisable(false);
                } else {
                    getChildren().get(rowID * boardSize + columnID).setDisable(true);
                }
                rowID++;
            }
            columnID++;
        }
    }

    public void scale() { // called by listener when resizing
        double smallestDimension = controller.getStageWidth();
        if (controller.getStageHeight() - 100 < smallestDimension) smallestDimension = controller.getStageHeight();

        double fieldSize = (smallestDimension / boardSize) * 0.90;
        for (Node fieldButton : getChildren()) {
            FieldButton fb = (FieldButton) fieldButton;
            fb.setMinSize(fieldSize, fieldSize);
        }

        double fieldSpacing = (smallestDimension / boardSize) * 0.05;
        setVgap(fieldSpacing);
        setHgap(fieldSpacing);
    }

    @Override
    public void update(Field[][] board, Move move) {
        // update colors
        for (Node fieldNode : this.getChildren()) {
            FieldButton fieldButton = (FieldButton) fieldNode;
            try {
                String[] fieldColors = controller.getGame().getField(fieldButton.getRowID(), fieldButton.getColumnID()).getOwner().getColors();
                fieldButton.setStyle("-fx-background-color: " + fieldColors[0] + "; -fx-text-fill: " + fieldColors[1]);
            } catch (NullPointerException npe) {
                //TODO: is this bad practice?
            }
        }

        //TODO: update recentmove signifiers

        if (controller.getActivePlayer() instanceof OthelloManualPlayer) setValidMoves();
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