package nl.hanze.game.client.server;

public interface Observable {
    void addObserver(Object o);
    void removeObserver(Object o);
    void notifyObservers();
}
