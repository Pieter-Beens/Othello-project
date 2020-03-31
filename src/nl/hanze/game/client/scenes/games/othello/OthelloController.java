package nl.hanze.game.client.scenes.games.othello;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.GameController;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.games.othello.utils.InfoBox;
import nl.hanze.game.client.scenes.games.othello.utils.OthelloBoard;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OthelloController extends GameController implements Initializable {
    @FXML
    public HBox boardContainer;

    @FXML
    public HBox info;

    private OthelloBoard boardPane;

    private InfoBox infoBox;

    private OthelloModel model = new OthelloModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardPane = new OthelloBoard(model, this);
        infoBox = new InfoBox(model);

        boardContainer.getChildren().add(boardPane);
        info.getChildren().add(infoBox);
    }

    @Override
    public void setup() {
        model.setup();
        boardPane.disableAllFields();
        model.turnHasMoves(model.getBoard(), getActivePlayer());
        updateViews();
    }

    @Override
    public void updateViews() {
        boardPane.update();
        infoBox.update();
    }

    @Override
    public void move(Move move) {
        model.placeStone(move);
        updateViews();

        // check if the next turn belongs to an AIPlayer and if so, request a move
        if (model.getActivePlayer().getPlayerType() == PlayerType.AI) model.getActivePlayer().move(model.getBoard());
    }

    public void btnGoBack(ActionEvent event) throws IOException {
        loadScene("menu/offline/offline.fxml");
    }

    @Override
    protected GameModel getModel() {
        return model;
    }
}
