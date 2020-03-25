/*
package sample;

import javafx.application.Application;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        //back-end
        Game game = new Game(); // includes new Board -> new Field but they don't extend JavaFX stuff!
        // during enactCaptures(), return every game state to Model

        //front-end MVC
        Controller controller = new Controller(); // translate (and validate) requests to commands between Model and View
        Model model = new Model(game, controller); // contains data, upon get() will query Game (maybe not always??)
        View view = new View(stage, controller); // has exclusive access to all JavaFX elements
                                                // sets up listeners - listeners create new Threads to query Controller
                                                // after handling placeStone(), Platform.runLater(() -> checkNextCapture
                                                // updateView based on getController()>getModel()>getGame()>getBoard()
        stage.show();
    }

}
*/