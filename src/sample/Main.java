package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Stack;

public class Main extends Application {

    private static final int BOARDSIZE = 19;
    private int FIELDSIZE = 95;
    private int FIELDSPACING = 5;
    private static final boolean fullScreen = false;

    private static final boolean vsAI = true;
    private AI roboPieter = new AI(2000);

    private ArrayList<ArrayList<Field>> fields; // contains all BOARDSIZE^2 field nodes // TODO: turn into separate CLASS

    public int turn = 1;

    public Field mostRecentMove;

    public boolean gameFinished = false;

    private String player1 = "RED"; // == 1
    private String player2 = "BLUE"; // == -1
    // TODO: make opening screen to set boardsize, colorScheme (Pieter, Anna, Hummer), player names and vsAI/2 player game

    private boolean turnHasNoValidMoves = true;

    private GridPane grid = new GridPane();
    private VBox vbox = new VBox();

    private Integer turncounter = 1;
    private Label turnLabel;

    private static final String boardcolorcode = "#f5e5ae";
    private static final String player1colorcode = "#940a0a";
    private static final String player2colorcode = "#050561";

    private Label player1ScoreBar;
    private Label player2ScoreBar;

//    private Image player1img = new Image(getClass().getResourceAsStream("x.gif"));
//    private Image player2img = new Image(getClass().getResourceAsStream("o.gif"));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupBoard();
        setupStartingPositions();

        updateView();

        primaryStage.setTitle("Othello");
        primaryStage.setScene(new Scene(this.vbox));
        primaryStage.setFullScreen(this.fullScreen);
        primaryStage.show();

//        primaryStage.setOnCloseRequest(e->{
//            Platform.exit();
//            System.exit(0);
//        });

    }

    public void setupBoard() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds(); // get dimensions of screen
        int smallestDimension = (int) screenBounds.getMaxX();
        if ((screenBounds.getMaxY() - 50) < smallestDimension) smallestDimension = (int) (screenBounds.getMaxY() - 50);
        int roomPerField = smallestDimension / BOARDSIZE;
        this.FIELDSIZE = (int) (roomPerField * 0.95);
        this.FIELDSPACING = (int) (roomPerField * 0.05);

        //grid.setPadding(new Insets(10, 10, 10, 10));
        this.grid.setMinSize(FIELDSIZE, FIELDSIZE);
        this.grid.setVgap(FIELDSPACING);
        this.grid.setHgap(FIELDSPACING);

        this.fields = new ArrayList<>();
        for (int i = 0; i < BOARDSIZE; i++) {
            this.fields.add(new ArrayList<>());
            for (int j = 0; j < BOARDSIZE; j++) {
                Field field = new Field(i, j);
                field.setMinSize(FIELDSIZE, FIELDSIZE);
                field.setStyle("-fx-background-color: " + boardcolorcode);
                grid.add(field, i, j);

                field.setOnMouseClicked(e -> { // set up listener
                    try {
                        makeAMove(field);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });

                this.fields.get(i).add(field);
            }
        }

        this.turnLabel = new Label();
        this.turnLabel.setMinWidth((BOARDSIZE*(FIELDSIZE + FIELDSPACING))/2);
        this.turnLabel.setTextAlignment(TextAlignment.CENTER);
        this.turnLabel.setFont(new Font("Sans", 30));
        this.turnLabel.setStyle("-fx-font-weight: bold");

        this.player1ScoreBar = new Label();
        this.player1ScoreBar.setMinWidth((BOARDSIZE*(FIELDSIZE + FIELDSPACING))/4);
        this.player1ScoreBar.setTextAlignment(TextAlignment.RIGHT);
        this.player1ScoreBar.setFont(new Font("Sans", 30));
        this.player1ScoreBar.setTextFill(Color.WHITE);
        this.player1ScoreBar.setStyle("-fx-font-weight: bold");
        this.player1ScoreBar.setStyle("-fx-background-color: " + player1colorcode);

        this.player2ScoreBar = new Label();
        this.player2ScoreBar.setMinWidth((BOARDSIZE*(FIELDSIZE + FIELDSPACING))/4);
        this.player2ScoreBar.setTextAlignment(TextAlignment.RIGHT);
        this.player2ScoreBar.setFont(new Font("Sans", 30));
        this.player2ScoreBar.setTextFill(Color.WHITE);
        this.player2ScoreBar.setStyle("-fx-font-weight: bold");
        this.player2ScoreBar.setStyle("-fx-background-color: " + player2colorcode);

        HBox infoBar = new HBox();
        infoBar.getChildren().add(this.turnLabel);
        infoBar.getChildren().add(this.player1ScoreBar);
        infoBar.getChildren().add(this.player2ScoreBar);

        this.vbox.getChildren().add(this.grid);
        this.vbox.getChildren().add(infoBar);
        this.vbox.setAlignment(Pos.CENTER);
    }

    public void makeAMove(Field field) throws InterruptedException {
        lockBoard(); // locks the board to prevent players from immediately pressing another field and crashing the app
        if (this.mostRecentMove != null) this.mostRecentMove.unmark();
        placeStone(field);
        this.mostRecentMove = field;
        this.mostRecentMove.mark();
        enactCaptures(field);
        nextTurn(false);
    }

    public void setupStartingPositions() { // sets up the opening state of the game with two fields of each color in the middle of the board
        this.fields.get(BOARDSIZE/2).get(BOARDSIZE/2).setColor(1);
        this.fields.get(BOARDSIZE/2 - 1).get(BOARDSIZE/2 - 1).setColor(1);
        this.fields.get(BOARDSIZE/2 - 1).get(BOARDSIZE/2).setColor(-1);
        this.fields.get(BOARDSIZE/2).get(BOARDSIZE/2 - 1).setColor(-1);

        setValidMoves();

//        makeAMove(this.fields.get(#).get(#)); // use this to input starting moves for testing
    }

    public void setValidMoves() {
        int[] dimensions = new int[2];
        for (ArrayList<Field> row : fields) {
            for (Field field : row) {
                field.resetValidity();
                if (field.getColor() == 0) checkAllDirections(dimensions, field); // only check for validity if the field is empty
                dimensions[1]++;
            }
            dimensions[1] = 0;
            dimensions[0]++;
        }
    }

    public void checkAllDirections(int[] initialDimensions, Field field) {
        //TODO: make 8 threads, one for every direction, write temp array to global this.fields in synchronized order at the end
        loopCheck(field, initialDimensions, 1, 1, 0, new Stack<>());
        loopCheck(field, initialDimensions, 1, 0, 0, new Stack<>());
        loopCheck(field, initialDimensions, 1, -1, 0, new Stack<>());
        loopCheck(field, initialDimensions, 0, 1, 0, new Stack<>());
        loopCheck(field, initialDimensions, 0, -1, 0, new Stack<>());
        loopCheck(field, initialDimensions, -1, 1, 0, new Stack<>());
        loopCheck(field, initialDimensions, -1, 0, 0, new Stack<>());
        loopCheck(field, initialDimensions, -1, -1, 0, new Stack<>());
    }

    public void loopCheck(Field originalField, int[] dimensions, int columndir, int rowdir, int numberOfFields, Stack<int[]> potentialCaptureData) {
        //TODO: what about occupied fields??
        int loopNumber = 1;
        while (true) {

            int[] nextField = new int[]{originalField.columnindex + (loopNumber * columndir), originalField.rowindex + (loopNumber * rowdir)};

            // start with all cases where a direction will never make a move valid, so no further checks are necessary
            if (nextField[0] < 0 || nextField[0] >= BOARDSIZE || nextField[1] < 0 || nextField[1] >= BOARDSIZE)
                break; // breaks loop if next field is out of board range
            if (this.fields.get(nextField[0]).get(nextField[1]).getColor() == 0)
                break; // breaks loop if next field is empty

            // success condition: next field is of your color, and this is not the first loop
            if (this.fields.get(nextField[0]).get(nextField[1]).getColor() == this.turn && loopNumber > 1) {
                originalField.setAsValid();
                while (!potentialCaptureData.empty()) {
                    originalField.addToCaptureData(potentialCaptureData.pop());
                }
                this.turnHasNoValidMoves = false;
                break;
            }

            if (this.fields.get(nextField[0]).get(nextField[1]).getColor() == this.turn && loopNumber == 1) break;

            // if next field has the opposing color, there is a possibility a move here is valid
            if (this.fields.get(nextField[0]).get(nextField[1]).getColor() == this.turn * -1) {
                potentialCaptureData.push(nextField);
                loopNumber++;
            }
        }
    }

    public void enactCaptures(Field field) throws InterruptedException {
        int stonesCaptured = 0;
        while (!field.getCaptureData().empty()) {
            int[] dimensions = field.getCaptureData().pop();
            this.fields.get(dimensions[0]).get(dimensions[1]).switchColor();
            stonesCaptured++;
            updateView(); //TODO: MULTITHREAD EVERYTHING THAT CALLS updateView AND WAIT/SLEEP IMMEDIATELY AFTER, OR THE UPDATED VIEW WILL NOT BE SHOWN
        }
        field.resetCaptureData();
        System.out.println("Stones captured: " + stonesCaptured);
    }

    public void lockBoard() {
        for (ArrayList<Field> row : fields) {
            for (Field field : row) {
                field.setDisable(true);
            }
        }
    }

    public int[] translatePixelToField(MouseEvent e) { // in case I want to stop using Buttons...
        int column = (int) e.getSceneX() / (FIELDSIZE + FIELDSPACING);
        int row = (int) e.getSceneY() / (FIELDSIZE + FIELDSPACING);
        return new int[]{column, row};
    }

    public void placeStone(Field field) {
        System.out.println(getPlayerName(this.turn) + " played the following field:");
        System.out.println("Column: " + field.columnindex + ", Row: " + field.rowindex);

        field.setColor(this.turn);
    }

    public void nextTurn(boolean skippedTurn) throws InterruptedException {
        this.turn = this.turn * -1;
        this.turncounter++;
        System.out.println("Next turn: " + this.turncounter);
        this.turnHasNoValidMoves = true;

        setValidMoves();

        if (turnHasNoValidMoves && !skippedTurn) {
            System.out.println(getPlayerName(this.turn) + " has no valid moves and must skip a turn");
            nextTurn(true);
        }

        if (turnHasNoValidMoves && skippedTurn) {
            System.out.println(getPlayerName(this.turn) + " was not able to move either, ending the game");
            endGame();
        }

        updateView(); //TODO: MULTITHREAD EVERYTHING THAT CALLS updateView AND WAIT/SLEEP IMMEDIATELY AFTER, OR THE UPDATED VIEW WILL NOT BE SHOWN

        if (!gameFinished && vsAI && this.turn == -1) {
            Thread.sleep(2000);
            makeAMove(roboPieter.getMove(this.fields));
        }
    }

    public void endGame() {
        int winSum = 0;
        for (ArrayList<Field> row : fields) {
            for (Field field : row) {
                winSum += field.getColor();
            }
        }
        if (winSum > 0) System.out.println(getPlayerName(1) + " has won!");
        else if (winSum < 0) System.out.println(getPlayerName(-1) + " has won!");
        else System.out.println("It's a draw!");
        gameFinished = true;
    }

    public String getPlayerName(int player) {
        if (player == 1) return player1;
        if (player == -1) return player2;
        return "MISSINGNO";
    }

    public void updateView() throws InterruptedException { // make this the only method for handling visual changes!
        this.turnLabel.setText("TURN " + this.turncounter.toString() + ": " + getPlayerName(this.turn) + "'s move");
        if (this.turn > 0) this.turnLabel.setStyle("-fx-text-fill: " + player1colorcode);
        if (this.turn < 0) this.turnLabel.setStyle("-fx-text-fill: " + player2colorcode);

        int player1score = 0;
        int player2score = 0;  // TODO: implement lowering/increasing score in methods so this check is not needed every turn

        for (ArrayList<Field> row : this.fields) {
            for (Field field : row) {
                if (field.getColor() > 0) player1score++;
                if (field.getColor() < 0) player2score++;
            }
        }

        this.player1ScoreBar.setText(getPlayerName(1) + ": " + player1score);
        this.player2ScoreBar.setText(getPlayerName(-1) + ": " + player2score);

        for (ArrayList<Field> row : this.fields) {
            for (Field field : row) {
                if (field.getValidity()) field.setDisable(false);
                else field.setDisable(true);

//                ImageView iv = new ImageView(this.x);
//                iv.setFitHeight(FIELDSIZE);

//                if (field.getColor() > 0) field.setGraphic(new ImageView(this.x));
//                else if (field.getColor() < 0) field.setGraphic(new ImageView(this.o));

                if (field.getColor() > 0) field.setStyle("-fx-background-color: " + player1colorcode);
                else if (field.getColor() < 0) field.setStyle("-fx-background-color: " + player2colorcode);

                String musicFile = "1.wav";

                Media sound = new Media(new File(musicFile).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);

                new Thread(mediaPlayer::play); // TODO: kill this thread when it's done
            }
        }
//        System.out.println("VIEW UPDATED");
    }
}