package nl.hanze.game.client.scenes.games.othello.utils;

import javafx.scene.control.Button;
import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.scenes.games.othello.OthelloController;
import nl.hanze.game.client.scenes.utils.Colors;

public class FieldButton extends Button {

    private final int rowID;
    private final int columnID;

    public FieldButton (int rowID, int columnID) {
        this.rowID = rowID;
        this.columnID = columnID;

        setStyle("-fx-background-color: " + Colors.BG_COLOR);

        setOnMouseClicked(e -> {
            OthelloBoard board = (OthelloBoard) getParent();
            Move move = new Move(board.getController().getActivePlayer(), getRowID(), getColumnID());
            board.getController().move(move);
        });
    }

    public int getRowID() {
        return rowID;
    }

    public int getColumnID() {
        return columnID;
    }
}
