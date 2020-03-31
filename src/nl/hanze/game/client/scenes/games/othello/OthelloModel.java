package nl.hanze.game.client.scenes.games.othello;

import nl.hanze.game.client.players.AI.utils.Move;
import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;
import nl.hanze.game.client.scenes.games.Field;
import nl.hanze.game.client.scenes.games.GameModel;

public class OthelloModel extends GameModel {

    public OthelloModel() {
        super();

        boardSize = 8;

        board = new Field[getBoardSize()][getBoardSize()];
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                board[r][c] = new Field(r,c);
            }
        }
    }

    @Override
    public void setup() {
        // sets up the opening state of the game with two stones of each color in the middle of the board
        getField(getBoardSize()/2, getBoardSize()/2).setOwner(getPlayer(1));
        getField(getBoardSize()/2 - 1, getBoardSize()/2 - 1).setOwner(getPlayer(1));
        getField(getBoardSize()/2 - 1, getBoardSize()/2).setOwner(getPlayer(0));
        getField(getBoardSize()/2, getBoardSize()/2 - 1).setOwner(getPlayer(0));

        players[0].changeScore(2);
        players[1].changeScore(2);

        //TODO: get starting player from server in online games
        activePlayer = players[turnCounter%2];
        activePlayer.setStartingColors();
    }

    public void placeStone(Move move) {
        board[move.getRow()][move.getColumn()].setOwner(move.getPlayer());
        activePlayer.changeScore(1);

        int captures = board[move.getRow()][move.getColumn()].enactCaptures(board, activePlayer);
        activePlayer.changeScore(captures);
        players[(turnCounter+1)%2].changeScore(-captures);

        nextTurn(false);
    }

    // Note this is not an override! The parent class' nextTurn() accepts no parameters...
    public void nextTurn(boolean lastTurnWasSkipped) {
        super.nextTurn();

        if (!turnHasMoves(board, activePlayer) && lastTurnWasSkipped) { //TODO: turnHasMoves should only be a check, not a setter!
            endGame();
            return;
        }

        if (!turnHasMoves(board, activePlayer)) {
            System.out.println(activePlayer.getName() + " was unable to move, and had to skip their turn!");
            this.nextTurn(true);
        }
    }

    public boolean turnHasMoves(Field[][] board, Player activePlayer) {
        boolean turnHasMoves = false;

        for (Field[] row : board) {
            for (Field field : row) {
                field.updateValidity(board, activePlayer);
                if (field.getValidity()) turnHasMoves = true;
            }
        }
        return turnHasMoves;
    }
}
