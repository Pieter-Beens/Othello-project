package nl.hanze.game.client.scenes.utils;

import javafx.beans.property.SimpleStringProperty;

public class PlayerRow {
    private final SimpleStringProperty name;
    private final SimpleStringProperty games;

    public PlayerRow() {
        this("", "");
    }

    public PlayerRow(String name, String games) {
        this.name = new SimpleStringProperty(name);
        this.games = new SimpleStringProperty(games);
    }

    public String getName() {
        return name.get();
    }

    public String getGames() {
        return games.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setGames(String games) {
        this.games.set(games);
    }
}
