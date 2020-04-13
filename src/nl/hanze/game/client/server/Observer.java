package nl.hanze.game.client.server;

/**
 * @author Bart van Poele
 * @description Implemented by classes that process relevant server data
 * Based on the Observer Pattern
 */

public interface Observer {
    void update(String s);
}
