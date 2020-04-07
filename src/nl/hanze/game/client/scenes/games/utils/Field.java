package nl.hanze.game.client.scenes.games.utils;

import nl.hanze.game.client.players.Player;

public class Field {
    int rowID;
    int columnID;
    Player owner = null;
    boolean validity = false;
    boolean recentMove = false;

    public Field(int rowID, int columnID) {

        this.rowID = rowID;
        this.columnID = columnID;
    }

    public void setOwner(Player player) {
        owner = player;
    }

    public Player getOwner() {
        return owner;
    }

    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    public boolean getValidity() {
        return validity;
    }

    public int getRowID() {
        return rowID;
    }

    public int getColumnID() {
        return columnID;
    }

    public void setRecentMove() {
        recentMove = true;
    }

    public void unsetRecentMove() {
        recentMove = false;
    }
}
