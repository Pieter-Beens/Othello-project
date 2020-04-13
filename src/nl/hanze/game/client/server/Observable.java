package nl.hanze.game.client.server;

/**
 * @author Bart van Poele
 * @description Implemented by the class that receives server data
 * Based on the Observer Pattern
 */

interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String s);
}
