package nl.hanze.game.client.scenes.menu.offline;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameFacade;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Bart van Poele
 * This Controller handles use input from the Offline game menu
 */
public class OfflineMenuController extends Controller implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        //Set resultMessage
        resultMessage.setText(model.getResultMessage());
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
