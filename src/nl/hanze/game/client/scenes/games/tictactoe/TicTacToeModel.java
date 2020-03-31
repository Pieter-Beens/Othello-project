package nl.hanze.game.client.scenes.games.tictactoe;

import nl.hanze.game.client.scenes.games.GameModel;

public class TicTacToeModel extends GameModel {
    public TicTacToeModel(int boardSize) {
        super(boardSize);
    }

    @Override
    public void setup() {
        activePlayer = players[turnCounter%2];
    }
}
