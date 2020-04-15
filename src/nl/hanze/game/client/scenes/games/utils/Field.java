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

    /**
     * Sets the owner of a Field.
     * @param player The player who is now occupying this field.
     */
    public void setOwner(Player player) {
        owner = player;
    }

    /**
     * @return Returns the owner of a Field.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the validity of a Field.
     * @param validity True if this Field has been found to be a legal move for the active player in the GameModel.
     */
    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    /**
     * @return Returns information on this Field's validity.
     */
    public boolean getValidity() {
        return validity;
    }

    /**
     * @return Returns this Field's row number (denoted by alphabetical letters in-game).
     */
    public int getRowID() {
        return rowID;
    }

    /**
     * @return Returns this Field's column number (denoted by the same number + 1 in-game).
     */
    public int getColumnID() {
        return columnID;
    }

    /**
     * Sets this Field as the subject of the most recent move, meaning it will be highlighted on the Othello game board.
     */
    public void setRecentMove() {
        recentMove = true;
    }

    /**
     * Sets this Field as not being the most recent move (even if it wasn't in the first place).
     */
    public void unsetRecentMove() {
        recentMove = false;
    }

    /**
     * @return Returns an answer to the question if this Field was subject to the most recent Move.
     */
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
