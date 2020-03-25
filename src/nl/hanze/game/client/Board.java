//package sample;
//
//import javafx.geometry.Pos;
//import javafx.geometry.Rectangle2D;
//import javafx.scene.control.Label;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.TextAlignment;
//import javafx.stage.Screen;
//
//import java.util.ArrayList;
//
//public class Board extends GridPane {
//    public Board(int boardsize) {
//        super();
//        Rectangle2D screenBounds = Screen.getPrimary().getBounds(); // get dimensions of screen
//        int smallestDimension = (int) screenBounds.getMaxX();
//        if ((screenBounds.getMaxY() - 50) < smallestDimension) smallestDimension = (int) (screenBounds.getMaxY() - 50);
//        int roomPerField = smallestDimension / BOARDSIZE;
//        this.FIELDSIZE = (int) (roomPerField * 0.95);
//        this.FIELDSPACING = (int) (roomPerField * 0.05);
//
//        //grid.setPadding(new Insets(10, 10, 10, 10));
//        this.grid.setMinSize(FIELDSIZE, FIELDSIZE);
//        this.grid.setVgap(FIELDSPACING);
//        this.grid.setHgap(FIELDSPACING);
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
//                    try {
//                        makeAMove(field);
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
//                });
//
//                this.fields.get(i).add(field);
//            }
//        }
//
//        this.turnLabel = new Label();
//        this.turnLabel.setMinWidth((BOARDSIZE*(FIELDSIZE + FIELDSPACING))/2);
//        this.turnLabel.setTextAlignment(TextAlignment.CENTER);
//        this.turnLabel.setFont(new Font("Sans", 30));
//        this.turnLabel.setStyle("-fx-font-weight: bold");
//
//        this.player1ScoreBar = new Label();
//        this.player1ScoreBar.setMinWidth((BOARDSIZE*(FIELDSIZE + FIELDSPACING))/4);
//        this.player1ScoreBar.setTextAlignment(TextAlignment.RIGHT);
//        this.player1ScoreBar.setFont(new Font("Sans", 30));
//        this.player1ScoreBar.setTextFill(Color.WHITE);
//        this.player1ScoreBar.setStyle("-fx-font-weight: bold");
//        this.player1ScoreBar.setStyle("-fx-background-color: " + player1colorcode);
//
//        this.player2ScoreBar = new Label();
//        this.player2ScoreBar.setMinWidth((BOARDSIZE*(FIELDSIZE + FIELDSPACING))/4);
//        this.player2ScoreBar.setTextAlignment(TextAlignment.RIGHT);
//        this.player2ScoreBar.setFont(new Font("Sans", 30));
//        this.player2ScoreBar.setTextFill(Color.WHITE);
//        this.player2ScoreBar.setStyle("-fx-font-weight: bold");
//        this.player2ScoreBar.setStyle("-fx-background-color: " + player2colorcode);
//
//        HBox infoBar = new HBox();
//        infoBar.getChildren().add(this.turnLabel);
//        infoBar.getChildren().add(this.player1ScoreBar);
//        infoBar.getChildren().add(this.player2ScoreBar);
//
//        this.vbox.getChildren().add(this.grid);
//        this.vbox.getChildren().add(infoBar);
//        this.vbox.setAlignment(Pos.CENTER);
//    }
//
//    public Field[] getFields() {
//
//        Field[] fields = new Field[64];
//        return fields;
//    }
//
//    public void resetCaptureData() {
//        this.getChildren().resetCaptureData();
//    }
//}
