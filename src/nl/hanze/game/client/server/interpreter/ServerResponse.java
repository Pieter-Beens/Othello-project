package nl.hanze.game.client.server.interpreter;

import java.util.List;
import java.util.Map;

/**
 * Class representing a response from the server.
 *
 * @author Roy Voetman
 */
public class ServerResponse {
    private Map<String, String> map;
    private List<String> list;
    private String command;

    /**
     * Constructs a ServerResponse object.
     *
     * @author Roy Voetman
     * @param command Command received from the server.
     */
    public ServerResponse(String command) {
        this.command = command;
    }

    /**
     * Constructs a ServerResponse object with a map of arguments.
     *
     * @author Roy Voetman
     * @param command Command received from the server.
     * @param map Arguments associated with this command.
     */
    public ServerResponse(String command, Map<String, String> map) {
        this.map = map;
        this.command = command;
    }

    /**
     * Constructs a ServerResponse object with a list of arguments.
     *
     * @author Roy Voetman
     * @param command Command received from the server.
     * @param list Arguments associated with this command.
     */
    public ServerResponse(String command, List<String> list) {
        this.list = list;
        this.command = command;
    }

    /**
     * Getter for the map arguments.
     *
     * @author Roy Voetman
     * @return Map of arguments
     */
    public Map<String, String> getMap() {
        return map;
    }

    /**
     * Getter for the list arguments.
     *
     * @author Roy Voetman
     * @return List of arguments
     */
    public List<String> getList() {
        return list;
    }

    /**
     * Getter for the command itself
     *
     * @author Roy Voetman
     * @return The command as a String
     */
    public String getCommand() {
        return command;
    }
}
