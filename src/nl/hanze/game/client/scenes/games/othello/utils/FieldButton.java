package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.scene.control.Button;
import nl.hanze.game.client.Application;
import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.scenes.utils.Colors;

public class FieldButton extends Button {

    private final int rowID;
    private final int columnID;

    public FieldButton (int rowID, int columnID) {
        this.rowID = rowID;
        this.columnID = columnID;

        setStyle("-fx-background-color: " + Colors.BG_COLOR);

        setOnMouseClicked(e -> System.out.println("clickety"));
    }

    public void setOwner(Player player) {
        setStyle("-fx-background-color: " + player.getColors()[0]);
    }

    public int getRowID() {
        return rowID;
    }

    public int getColumnID() {
        return columnID;
    }
}
