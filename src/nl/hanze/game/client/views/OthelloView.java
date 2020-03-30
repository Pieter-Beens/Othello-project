package nl.hanze.game.client.views;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import nl.hanze.game.client.controllers.OthelloController;
import nl.hanze.game.client.games.players.Player;
import nl.hanze.game.client.views.boards.othello.InfoBox;
import nl.hanze.game.client.views.boards.othello.OthelloBoard;

public class OthelloView extends VBox implements View {

    private final OthelloBoard board;
    private final InfoBox infoBox;

    public OthelloView(OthelloController controller, int boardSize, boolean fullscreen, Player player1, Player player2) {
        this.board = new OthelloBoard(controller, boardSize, player1, player2);
        this.infoBox = new InfoBox(controller, boardSize, player1, player2);

        this.getChildren().add(board);
        this.getChildren().add(infoBox);
        this.setAlignment(Pos.TOP_CENTER);
    }

    public void scale() {
        this.board.scale();
        this.infoBox.scale();
    }

    public OthelloBoard getBoard() {
        return this.board;
    }

    public InfoBox getInfoBox() {
        return this.infoBox;
    }
}
