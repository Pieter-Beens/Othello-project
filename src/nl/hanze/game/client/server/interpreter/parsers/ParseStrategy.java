package nl.hanze.game.client.server.interpreter.parsers;

/**
 * An interface that all command parsers should implement. This interface has only
 * one method which is used parse the given server response. Classes are open to define
 * their return type which is represented by E. Using this interface assures that the Interpreter is open
 * for extensibility (Strategy Pattern).
 *
 * @author Roy Voetman
 */
public interface ParseStrategy<E> {
    public E parse(String response, String command);
}
