package nl.hanze.game.client.refactor.server;

public interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String s);
}
