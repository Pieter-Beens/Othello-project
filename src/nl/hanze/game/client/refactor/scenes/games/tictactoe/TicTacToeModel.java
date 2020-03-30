package nl.hanze.game.client.refactor.scenes.games.tictactoe;

import nl.hanze.game.client.refactor.scenes.games.GameModel;

public class TicTacToeModel extends GameModel {
    @Override
    public int getBoardSize() {
        return 3;
    }
}
