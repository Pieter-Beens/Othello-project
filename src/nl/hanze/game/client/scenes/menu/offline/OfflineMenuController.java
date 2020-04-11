/*
 * @author Bart van Poele
 */
package nl.hanze.game.client.scenes.menu.offline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameFacade;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Bart van Poele
 */

public class OfflineMenuController extends Controller implements Initializable {
    @FXML private VBox container;
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
        container.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> enablePlayButton());

        StringConverter tickLabelFormatter = new StringConverter<Double>() {
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
                if (string.equals("Easy")) {
                    return 0d;
                } else if (string.equals("Normal")) {
                    return 1d;
                } else if (string.equals("Hard")) {
                    return 2d;
                }
                return null;
            }
        };
        difficultySlider.setLabelFormatter(tickLabelFormatter);
        difficultySlider.setShowTickLabels(true);
        setDifficultyVisibility(false);
        setPlayernamesVisibility(false);
            // Styling
        //start.setStyle("-fx-background-color: " + Colors.BTN_COLOR + "; -fx-text-fill: " + Colors.BTN_TEXT_COLOR);
        //container.setStyle("-fx-background-color: " + Colors.BG_COLOR);


    }

    public void setDifficultyVisibility(boolean b){
        if(b) {
            difficulty.managedProperty().bind(container.visibleProperty());
        }else{
            difficulty.managedProperty().unbind();
            difficulty.setManaged(false);
        }
        difficulty.setVisible(b);
    }

    public void setPlayernamesVisibility(boolean b){

        if(b) {
            playernames.managedProperty().bind(container.visibleProperty());
        }else{
            playernames.managedProperty().unbind();
            playernames.setManaged(false);
        }
        playernames.setVisible(b);
    }

    //Sets selectedGame in model when altered
    @FXML
    public void gameChanged() {
        model.setGame((String) selectedGame.getSelectedToggle().getUserData());
    }

    //Sets gameMode in model, shows or hides difficulty slider accordingly
    @FXML
    public void gameModeChanged() {
        model.setGameMode((String) selectedGameMode.getSelectedToggle().getUserData());

        if(model.getGameMode().equals("single-player")){
            setDifficultyVisibility(true);
            setPlayernamesVisibility(false);
        } else if(model.getGameMode().equals("multi-player")){
            setDifficultyVisibility(false);
            setPlayernamesVisibility(true);
        }
    }

    //Sets difficulty in model when altered
    @FXML
    public void onDifficultyChanged(MouseEvent mouseEvent) {
        model.setDifficulty((int) difficultySlider.getValue());
    }

    //Sets fullscreen in model when altered
    @FXML
    public void fullscreenReleased(MouseEvent mouseEvent) {
        model.setFullscreen(fullscreen.isSelected());
    }

    public void enablePlayButton(){
        try{
            selectedGameMode.getSelectedToggle().getUserData();
            selectedGame.getSelectedToggle().getUserData();
            start.setDisable(false);
        }catch(NullPointerException ignored){}

    }

    public void startBtnClicked(ActionEvent event) throws IOException {
        boolean multiplayer = model.getGameMode().equals("multi-player");

        GameFacade.startOffline(
                player1.getText(),
                player2.getText(),
                model.getGame(),
                model.getFullscreen(),
                multiplayer,
                model.getDifficulty()
        );
        System.out.println(selectedGame.getSelectedToggle());
    }

    public void btnGoBack(ActionEvent actionEvent) throws IOException {
        goBack();
    }
}
