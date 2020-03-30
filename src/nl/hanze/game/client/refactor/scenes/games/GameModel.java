package nl.hanze.game.client.refactor.scenes.games;

import nl.hanze.game.client.refactor.players.Player;

public abstract class GameModel {
    Player player1;
    Player player2;
    Player activePlayer;

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public abstract int getBoardSize();
}
