package nl.hanze.game.client.server;

/**
 * Implemented by the class that receives server data
 * Based on the Observer Pattern
 * @author Bart van Poele
 */

interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String s);
}
