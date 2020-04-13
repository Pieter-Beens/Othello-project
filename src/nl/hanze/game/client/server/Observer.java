package nl.hanze.game.client.server;

/**
 * Implemented by classes that process relevant server data
 * Based on the Observer Pattern
 * @author Bart van Poele
 */

public interface Observer {
    void update(String s);
}
