package nl.hanze.game.client.server;

interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String s);
}
