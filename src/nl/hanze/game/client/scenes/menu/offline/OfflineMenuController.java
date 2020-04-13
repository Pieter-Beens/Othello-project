package nl.hanze.game.client.scenes.menu.offline;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameFacade;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Bart van Poele
 * This Controller handles user input from the Offline game menu
 */
public class OfflineMenuController extends Controller implements Initializable {
    @FXML private ImageView resultIconLeft;
    @FXML private ImageView resultIconRight;
    @FXML private TextField turnTimeField;
    @FXML private VBox container;
    @FXML private Text resultMessage;
    @FXML public HBox players;
    @FXML public GridPane playernames;
    @FXML public TextField player1;
    @FXML public TextField player2;
    @FXML public CheckBox fullscreen;
    @FXML public GridPane difficulty;
    @FXML public Slider difficultySlider;
    @FXML public Button start;
    @FXML public ToggleGroup selectedGame;
    @FXML public ToggleGroup selectedGameMode;

    private OfflineMenuModel model = new OfflineMenuModel();
    public String resultIconPath = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Get the resultMessage from the model, save as the variable ResultMsg
        String resultMsg = model.getResultMessage();

        StringConverter<Double> tickLabelFormatter = new StringConverter<Double>() {
            @Override
            public String toString(Double tickLabel) {
                if (tickLabel == 0) {
                    return "Easy";
                } else if (tickLabel == 1) {
                    return "Normal";
                } else if (tickLabel == 2) {
                    return "Hard";
                }
                return null;
            }

            @Override
            public Double fromString(String string) {
                switch (string) {
                    case "Easy":
                        return 0d;
                    case "Normal":
                        return 1d;
                    case "Hard":
                        return 2d;
                }
                return null;
            }
        };
        difficultySlider.setLabelFormatter(tickLabelFormatter);
        difficultySlider.setShowTickLabels(true);
        setPlayernamesVisibility(false);

        turnTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) newValue = newValue.replaceAll("[^\\d]", "");

            int turnTime = Integer.parseInt(newValue);
            model.setTurnTime(turnTime);
            turnTimeField.setText(String.valueOf(model.getTurnTime()));
        });


        /** @author Jasper van Dijken */
        //Game result messages
        //If there is no result to be displayed, set a title
        if (resultMsg == null) {
            resultMessage.setText("Offline\nGame Settings:");
        } else {
            //Else, set display the resultMsg
            resultMessage.setText(resultMsg);
            //If the AI won, set gif path to lost.gif
            if (resultMsg.contains("Julius")) {
                resultIconPath = "src/resources/lost.gif";
            }
            //If the AI didn't win, set gif path to win.gif
            if (!resultMsg.contains("Julius")) {
                resultIconPath = "src/resources/win.gif";
            }
            //If the game resulted in a tie, set gif path to tie.gif
            if (resultMsg.contains("tied")) {
                resultIconPath = "src/resources/tie.gif";
            }

            //If the gif path has been set
            if (!resultIconPath.equals("")) {
                //Inserting the gifs
                Image image;
                try {
                    image = new Image(new FileInputStream(resultIconPath));
                    resultIconLeft.setImage(image);
                    resultIconRight.setImage(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        /** end @author Jasper van Dijken */
    }

    /**
     * Display or hide the difficulty slider
     * @param b: show or hide the difficulty slider
     */
    public void setDifficultyVisibility(boolean b){
        if(b) {
            difficulty.managedProperty().bind(container.visibleProperty());
        }else{
            difficulty.managedProperty().unbind();
            difficulty.setManaged(false);
        }
        difficulty.setVisible(b);
    }

    /**
     * Display or hide the player name input fields
     * @param b: show or hide the player name input fields
     */
    public void setPlayernamesVisibility(boolean b){

        if(b) {
            playernames.managedProperty().bind(container.visibleProperty());
        }else{
            playernames.managedProperty().unbind();
            playernames.setManaged(false);
        }
        playernames.setVisible(b);
    }


    //Updates selected game & gameMode in model, shows or hides difficulty slider accordingly
    public void gameChanged() {
        model.setGame((String) selectedGame.getSelectedToggle().getUserData());
        model.setGameMode((String) selectedGameMode.getSelectedToggle().getUserData());

        if(model.getGameMode().equals("single-player")){
            setPlayernamesVisibility(false);
            setDifficultyVisibility(model.getGame().equals("othello"));
        } else if(model.getGameMode().equals("multi-player")){
            setDifficultyVisibility(false);
            setPlayernamesVisibility(true);
        }
    }

    //Sets difficulty in model
    @FXML public void onDifficultyChanged() {
        model.setDifficulty((int) difficultySlider.getValue());
    }

    //Sets fullscreen in model when altered
    @FXML public void fullscreenReleased() {
        model.setFullscreen(fullscreen.isSelected());
    }

    //Play button clicked -> start game
    public void startBtnClicked() throws IOException {
        gameChanged();
        onDifficultyChanged();
        fullscreenReleased();

        boolean multiplayer = model.getGameMode().equals("multi-player");

        if(multiplayer){
            GameFacade.startOffline(
                    player1.getText(),
                    player2.getText(),
                    model.getGame(),
                    model.getFullscreen(),
                    model.getTurnTime()
            );
        } else {
            GameFacade.startOffline(
                    model.getGame(),
                    model.getDifficulty(),
                    model.getFullscreen(),
                    model.getTurnTime()
            );
        }
    }

    /**
     * Redirect to the previous scene when go back button is clicked.
     *
     * @author Roy Voetman
     * @throws IOException When previous scene FXML can not be found.
     */
    public void btnGoBack() throws IOException {
        goBack();
    }
}
