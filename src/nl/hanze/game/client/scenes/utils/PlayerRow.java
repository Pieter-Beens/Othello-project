package nl.hanze.game.client.scenes.utils;

import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

/**
 * @author Roy Voetman
 */
public class PlayerRow {
    private final SimpleStringProperty name;

    public PlayerRow() {
        this("");
    }

    public PlayerRow(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getNameValue() {
        return name.getValue();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerRow playerRow = (PlayerRow) o;
        return this.getNameValue().equals(playerRow.getNameValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
