package nl.hanze.game.client.scenes.games.tictactoe;

import nl.hanze.game.client.scenes.games.GameModel;

public class TicTacToeModel extends GameModel {
    public TicTacToeModel(int boardSize) {
        super(boardSize);
    }

    // note to Roy: turncounter and activePlayer setup now happens in concrete method GameModel.setup()
}
