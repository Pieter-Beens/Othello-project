package nl.hanze.game.client.scenes.utils;

import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class PlayerRow {
    private final SimpleStringProperty name;

    public PlayerRow() {
        this("");
    }

    public PlayerRow(String name) {
        this.name = new SimpleStringProperty(name);
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
        return name.getValue().equals(playerRow.name.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
