package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

public class Game extends Application { //TODO: port to Android: https://stackoverflow.com/questions/9832052/port-java-application-to-android

    private int BOARDSIZE = 8;
    private int FIELDSIZE = 95;
    private int FIELDSPACING = 5;

    private boolean fullScreen = false;

    private boolean vsAI = true;
    private AI roboPieter = new AI(2000);

    private ArrayList<ArrayList<Field>> fields; // contains all BOARDSIZE^2 field nodes // TODO: turn into separate CLASS

    public int turn = 1;

    public Field mostRecentMove;

    public boolean gameFinished = false;

    private String player1 = "RED"; // == 1
    private String player2 = "BLUE"; // == -1

    private boolean turnHasNoValidMoves = true;

    private GridPane grid = new GridPane();
    private HBox infoBar = new HBox();

    private Integer turncounter = 1;
    private Label turnLabel;

    public String boardcolorcode = "#f5e5ae";
    public String boardcolorcodetext = "black";
    public String player1colorcode = "#940a0a";
    public String player1colorcodetext = "white";
    public String player2colorcode = "#050561";
    public String player2colorcodetext = "white";


    private Label player1ScoreBar;
    private Label player2ScoreBar;

//    private Image player1img = new Image(getClass().getResourceAsStream("x.gif"));
//    private Image player2img = new Image(getClass().getResourceAsStream("o.gif"));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox startScreen = new VBox();

        startScreen.setMinSize(500, 500);

        HBox titleRow = new HBox(); // <---------------------------------------------------------------------------

  //      titleRow.getChildren().add(new ImageView(new Image("logo.png")));
  //      titleRow.setAlignment(Pos.CENTER);

        Label boardsizeLabel = new Label("CHOOSE NUMBER OF FIELDS");

        HBox boardsizeRow = new HBox(); // <---------------------------------------------------------------------------

        CommonButton size6 = new CommonButton(this, false, "6x6");
        size6.deselect();

        Button size8 = new Button("8x8");
        size8.setMinSize(100, 100);
        size8.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);

        Button size10 = new Button("10x10");
        size10.setMinSize(100, 100);
        size10.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);

        Button size12 = new Button("12x12");
        size12.setMinSize(100, 100);
        size12.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);

        size6.setOnMouseClicked(e -> {
            size6.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
            size8.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            size10.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            size12.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
        });

        size8.setOnMouseClicked(e -> {
            this.BOARDSIZE = 8;
            size6.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            size8.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
            size10.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            size12.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
        });

        size10.setOnMouseClicked(e -> {
            this.BOARDSIZE = 10;
            size6.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            size8.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            size10.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
            size12.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
        });

        size12.setOnMouseClicked(e -> {
            this.BOARDSIZE = 12;
            size6.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            size8.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            size10.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            size12.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
        });

        boardsizeRow.getChildren().add(size6);
        boardsizeRow.getChildren().add(size8);
        boardsizeRow.getChildren().add(size10);
        boardsizeRow.getChildren().add(size12);
        boardsizeRow.setAlignment(Pos.CENTER);
        boardsizeRow.setSpacing(30);

        Label playersLabel = new Label("CHOOSE NUMBER OF PLAYERS");

        HBox playersRow = new HBox(); // <---------------------------------------------------------------------------

        Button oneplayer = new Button("P1 vs. AI");
        oneplayer.setMinSize(100, 100);
        oneplayer.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);

        Button twoplayers = new Button("P1 vs. P2");
        twoplayers.setMinSize(100, 100);
        twoplayers.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);

        oneplayer.setOnMouseClicked(e -> {
            this.vsAI = true;
            oneplayer.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
            twoplayers.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
        });

        twoplayers.setOnMouseClicked(e -> {
            this.vsAI = false;
            oneplayer.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            twoplayers.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
        });

        playersRow.getChildren().add(oneplayer);
        playersRow.getChildren().add(twoplayers);
        playersRow.setAlignment(Pos.CENTER);
        playersRow.setSpacing(30);

        Label colorLabel = new Label("CHOOSE COLOR SCHEME");

        HBox colorRow = new HBox(); // <---------------------------------------------------------------------------

        Button pieter = new Button("PIETER");
        pieter.setMinSize(100, 100);
        pieter.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);

        Button anna = new Button("ANNA");
        anna.setMinSize(100, 100);
        anna.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);

        Button hummer = new Button("HUMMER");
        hummer.setMinSize(100, 100);
        hummer.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);


        pieter.setOnMouseClicked(e -> {
            this.boardcolorcode = "#f5e5ae";
            this.boardcolorcodetext = "black";
            this.player1colorcode = "#940a0a";
            this.player1colorcodetext = "white";
            this.player2colorcode = "#050561";
            this.player2colorcodetext = "white";
            updateStartView();
            pieter.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
            anna.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            hummer.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
        });

        anna.setOnMouseClicked(e -> {
            this.boardcolorcode = "#000000";
            this.boardcolorcodetext = "white";
            this.player1colorcode = "#852e00"; // dark orange red
            this.player1colorcodetext = "black";
            this.player2colorcode = "#0c4f00"; // dark green
            this.player2colorcodetext = "white";
            updateStartView();
            pieter.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            anna.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
            hummer.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
        });

        hummer.setOnMouseClicked(e -> {
            this.boardcolorcode = "#75551d";
            this.player2colorcodetext = "white";
            this.player1colorcode = "#e0deda";
            this.player2colorcodetext = "black";
            this.player2colorcode = "#2b2a2a";
            this.player2colorcodetext = "white";
            updateStartView();
            pieter.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            anna.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            hummer.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
        });

        colorRow.getChildren().add(pieter);
        colorRow.getChildren().add(anna);
        colorRow.getChildren().add(hummer);
        colorRow.setAlignment(Pos.CENTER);
        colorRow.setSpacing(30);

        Label namesLabel = new Label("CHANGE PLAYER NAMES");

        HBox namesRow = new HBox(); // <---------------------------------------------------------------------------

        TextField player1Input = new TextField("player1");
        player1Input.setMaxSize(150, 35);

        TextField player2Input = new TextField("player2");
        player1Input.setMaxSize(150, 35);

        namesRow.getChildren().add(player1Input);
        namesRow.getChildren().add(player2Input);
        namesRow.setAlignment(Pos.TOP_CENTER);
        namesRow.setSpacing(30);

        HBox bottomRow = new HBox(); // <---------------------------------------------------------------------------

        Button fullscreenToggle = new Button();
        fullscreenToggle.setText("Fullscreen OFF");
        fullscreenToggle.setMinSize(300, 50);
        fullscreenToggle.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
        fullscreenToggle.setOnMouseClicked(e -> {
            fullScreen = !fullScreen;
            if (fullScreen) {
                fullscreenToggle.setText("Fullscreen ON");
                fullscreenToggle.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
            }
            else {
                fullscreenToggle.setText("Fullscreen OFF");
                fullscreenToggle.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
            }
        });

        Button startButton = new Button();
        startButton.setText("START");
        startButton.setMinSize(100, 100);
        startButton.setStyle("-fx-background-color: " + player1colorcode + "; -fx-text-fill: " + player1colorcodetext);
        startButton.setOnMouseClicked(e -> startGame(primaryStage, player1Input.getText(), player2Input.getText()));

        bottomRow.getChildren().add(fullscreenToggle);
        bottomRow.getChildren().add(startButton);
        bottomRow.setAlignment(Pos.CENTER_RIGHT);
        bottomRow.setSpacing(10);


        startScreen.getChildren().add(titleRow);
        startScreen.getChildren().add(boardsizeLabel);
        startScreen.getChildren().add(boardsizeRow);
        startScreen.getChildren().add(playersLabel);
        startScreen.getChildren().add(playersRow);
        startScreen.getChildren().add(colorLabel);
        startScreen.getChildren().add(colorRow);
        startScreen.getChildren().add(namesLabel);
        startScreen.getChildren().add(namesRow);
        startScreen.getChildren().add(bottomRow);

        startScreen.setSpacing(20);
        startScreen.setStyle("-fx-background-color: " + boardcolorcode);

        primaryStage.setTitle("OtHello World");
        primaryStage.setFullScreen(this.fullScreen);
        primaryStage.setScene(new Scene(startScreen));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void updateStartView() {
        //TODO: recolor all elements of the start screen using this method
    }

    public void startGame(Stage primaryStage, String player1name, String player2name) {
        if (player1name.length() > 11) player1name = player1name.substring(0,11);
        else if (player1name.length() == 0) player1name = "player1";
        if (player2name.length() > 11) player2name = player2name.substring(0,11);
        else if (player2name.length() == 0) player2name = "player2";

        this.player1 = player1name;
        this.player2 = player2name;

        VBox gameScreen = new VBox();

        gameScreen.getChildren().add(this.grid);
        gameScreen.getChildren().add(this.infoBar);
        gameScreen.setAlignment(Pos.TOP_CENTER);

        setupBoard();
        setupStartingPositions();

        updateBoardView();
        updateInfoBarView();

        primaryStage.setScene(new Scene(gameScreen));
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
        if (!fullScreen) roomPerField *= 0.80;
        this.FIELDSIZE = (int) (roomPerField * 0.95);
        this.FIELDSPACING = (int) (roomPerField * 0.05);

        //grid.setPadding(new Insets(10, 10, 10, 10));
        this.grid.setMinSize(FIELDSIZE, FIELDSIZE);
        this.grid.setVgap(FIELDSPACING);
        this.grid.setHgap(FIELDSPACING);
        this.grid.setAlignment(Pos.CENTER);

        this.fields = new ArrayList<>();
        for (int i = 0; i < BOARDSIZE; i++) {
            this.fields.add(new ArrayList<>());
            for (int j = 0; j < BOARDSIZE; j++) {
                Field field = new Field(i, j);
                field.setMinSize(FIELDSIZE, FIELDSIZE);
                field.setStyle("-fx-background-color: " + boardcolorcode);
                grid.add(field, i, j);

                field.setOnMouseClicked(e -> { // set up listener
                    new Thread(() -> {
                        try {
                            makeAMove(field);
                        } catch (InterruptedException ex) {
                            System.out.print("Yes, it happened, the Thread was interrupted! No move for you!;");
                            ex.printStackTrace();
                        }
                    }).start();});

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

        this.infoBar.getChildren().add(this.turnLabel);
        this.infoBar.getChildren().add(this.player1ScoreBar);
        this.infoBar.getChildren().add(this.player2ScoreBar);
        this.infoBar.setAlignment(Pos.CENTER);
    }

    public void makeAMove(Field field) throws InterruptedException {
        lockBoard(); // locks the board to prevent players from immediately pressing another field and crashing the app
        if (this.mostRecentMove != null) {
            Field fieldToUnmark = this.mostRecentMove;
            Platform.runLater(() -> fieldToUnmark.unmark());
        }
        placeStone(field);
        this.mostRecentMove = field;
        Platform.runLater(() -> this.mostRecentMove.mark());
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
                if (field.getColor() == 0) checkAllDirections(field); // only check for validity if the field is empty
                dimensions[1]++;
            }
            dimensions[1] = 0;
            dimensions[0]++;
        }
        if (!(vsAI && this.turn == -1)) lockBoard();
//        System.out.println("SET VALID MOVES");
    }

    public void checkAllDirections(Field field) {
        loopCheck(field, 1, 1, new Stack<>());
        loopCheck(field, 1, 0, new Stack<>());
        loopCheck(field, 1, -1, new Stack<>());
        loopCheck(field, 0, 1, new Stack<>());
        loopCheck(field, 0, -1, new Stack<>());
        loopCheck(field, -1, 1, new Stack<>());
        loopCheck(field, -1, 0, new Stack<>());
        loopCheck(field, -1, -1, new Stack<>());
    }

    public void loopCheck(Field originalField, int columndir, int rowdir, Stack<int[]> potentialCaptureData) {
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
            updateBoardView();
            lockBoard();
            Thread.sleep(200);
            int[] dimensions = field.getCaptureData().pop();
            this.fields.get(dimensions[0]).get(dimensions[1]).switchColor();
            stonesCaptured++;
            playSound(stonesCaptured);
        }
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

        for (ArrayList<Field> row : this.fields) {
            for (Field field : row) {
                field.resetCaptureData();
            }
        }

        setValidMoves();


        if (turnHasNoValidMoves && !skippedTurn) {
            System.out.println(getPlayerName(this.turn) + " has no valid moves and must skip a turn");
            nextTurn(true);
        }

        if (turnHasNoValidMoves && skippedTurn) {
            System.out.println(getPlayerName(this.turn) + " was not able to move either, ending the game");
            gameFinished = true;
        }

        Platform.runLater(() -> {
            updateBoardView();
            updateInfoBarView();
        });

        if (!gameFinished && vsAI && this.turn == -1) {
            Thread.sleep(2000);
            makeAMove(roboPieter.getMove(this.fields));
        }
    }

    public String getPlayerName(int player) {
        if (player == 1) return player1;
        if (player == -1) return player2;
        return "MISSINGNO";
    }

    public void updateInfoBarView() {
        this.turnLabel.setText(" TURN " + this.turncounter.toString() + ": " + getPlayerName(this.turn) + "'s move");
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

        this.player1ScoreBar.setText(" " + getPlayerName(1) + ": " + player1score);
        this.player2ScoreBar.setText(" " + getPlayerName(-1) + ": " + player2score);

        if (gameFinished) {
            int winSum = 0;
            for (ArrayList<Field> row : fields) {
                for (Field field : row) {
                    winSum += field.getColor();
                }
            }

            String resultString;

            if (winSum > 0) resultString = "GAME END: " + getPlayerName(1) + " has won!";
            else if (winSum < 0) resultString = "GAME END: " + getPlayerName(-1) + " has won!";
            else resultString = "It's a draw!";

            System.out.println(resultString);
            this.turnLabel.setText(resultString);
            this.turnLabel.setTextFill(Color.BLACK);
        }
    }

    public void updateBoardView() { // TODO: divide into multiple parts relevant to respective calls; this will become a Class!
        if (!(vsAI && this.turn == -1)) {
            for (ArrayList<Field> row : this.fields) {
                for (Field field : row) {
                    if (!field.getCaptureData().isEmpty()) {
                        field.setDisable(false);
                    }
                    else field.setDisable(true);
                }
            }
        }

        //TODO: known bug: sometimes after a capture the background color is not changed, and can never be changed again?

        for (ArrayList<Field> row : this.fields) {
            for (Field field : row) {
                //ImageView iv = new ImageView(this.x);
                //iv.setFitHeight(FIELDSIZE);

                //if (field.getColor() > 0) field.setGraphic(new ImageView(this.x));
                //else if (field.getColor() < 0) field.setGraphic(new ImageView(this.o));

                if (field.getColor() > 0) field.setStyle("-fx-background-color: " + player1colorcode);
                else if (field.getColor() < 0) field.setStyle("-fx-background-color: " + player2colorcode);
            }
        }
    }

    public void playSound(int i) {
        //TODO: fix issue where sometimes sound effects fail to play

        Media sound = new Media(new File(i%4 + ".wav").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
    }
}