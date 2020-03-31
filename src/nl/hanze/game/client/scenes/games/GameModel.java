package nl.hanze.game.client.scenes.games;

import nl.hanze.game.client.players.Player;

public abstract class GameModel {
    final static int MAX_PLAYERS = 2;
    protected Player[] players = new Player[MAX_PLAYERS];
    protected Player activePlayer;
    protected int boardSize;
    protected int turnCounter = 1;
    protected Field[][] board;
    public static final int[][] DIRECTIONS = {{1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}};

    public GameModel(int boardSize) {
        this.boardSize = boardSize;

        this.board = new Field[boardSize][boardSize];
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                this.board[r][c] = new Field(r,c);
            }
        }
    }

    public void nextTurn() {
        turnCounter++;
        activePlayer = players[turnCounter%2];
    }

    public abstract void setup();

    public Player getPlayer(int i) {
        return players[i];
    }

    public void setPlayer1(Player player1) {
        this.players[1] = player1;
    }

    public void setPlayer2(Player player2) {
        this.players[0] = player2;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getTurnCount() {
        return turnCounter;
    }

    public Field getField(int columnID, int rowID) {
        return board[columnID][rowID];
    }

    public Field[][] getBoard() {
        return board;
    }

    public void endGame() { // TODO: make this a visual message/design results screen
        System.out.println("Neither player was able to move, so the game has ended!");
        if (players[0].getScore() > players[1].getScore()) System.out.print(players[0].getName() + " has won!");
        if (players[0].getScore() < players[1].getScore()) System.out.print(players[1].getName() + " has won!");
        if (players[0].getScore() == players[1].getScore()) System.out.print(players[0].getName() + " and " + players[1] + " have tied for second place!");
    }

}
