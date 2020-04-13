package nl.hanze.game.client.scenes.games.utils;

import nl.hanze.game.client.players.Player;
import nl.hanze.game.client.players.PlayerType;

/**
 * @author Pieter Beens
 */
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

    public boolean getRecentMove() { return recentMove; }

    /**
     * toString functioned, mostly used for debugging purposes.
     * @return A string containing a cell that, depending on the owner, is either empty, marked C for Computer, or H for
     * human.
     * @author Nick
     */
    @Override
    public String toString() {
        if (owner == null) {
            return "[ ]";
        }
        else {
            if (owner.getPlayerType().equals(PlayerType.AI)) {
                return "[C]";
            }
            else {
                return "[H]";
            }
        }
    }
}
