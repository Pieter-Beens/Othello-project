package nl.hanze.game.client.scenes.utils;

import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

/**
 * Class representing rows in the Player Table from the lobby.
 *
 * @author Roy Voetman
 */
public class PlayerRow {
    private final SimpleStringProperty name;

    /**
     * Zero argument constructor required for FXML use.
     *
     * @author Roy Voetman
     */
    public PlayerRow() {
        this("");
    }

    /**
     * Construct a PlayerRow object
     *
     * @author Roy Voetman
     * @param name Name of the player that should be in the row.
     */
    public PlayerRow(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * Getter for the player property value.
     *
     * @return The player name.
     */
    public String getNameValue() {
        return name.getValue();
    }

    /**
     * Getter for the player property data.
     *
     * @return The player name.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Setter for the player name property
     *
     * @param name Name of the player
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Objects are equal if the have the same Player name.
     *
     * @param o Object to compare
     * @return boolean indicating if object are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerRow playerRow = (PlayerRow) o;
        return this.getNameValue().equals(playerRow.getNameValue());
    }

    /**
     * Hash the player name.
     *
     * @return a hash value of the player name.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
