package nl.hanze.game.client.server;

/**
 * @author Bart van Poele
 */

interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String s);
}
