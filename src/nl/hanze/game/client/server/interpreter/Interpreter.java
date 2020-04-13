package nl.hanze.game.client.server.interpreter;

import nl.hanze.game.client.server.interpreter.parsers.ListParser;
import nl.hanze.game.client.server.interpreter.parsers.MapParser;
import nl.hanze.game.client.server.interpreter.parsers.ParseStrategy;

import java.util.*;

/**
 * This Interpreter class serves as a Simple ServerResponse Factory.
 * Using different parsing strategies based on the send command
 * (Simple Factory)
 *
 * @author Roy Voetman
 */
public class Interpreter {
    public static final String[] LIST_COMMANDS = {"GAMELIST", "PLAYERLIST"};
    public static final String[] MAP_COMMANDS = {"GAME CHALLENGE", "GAME MATCH", "GAME YOURTURN", "GAME CHALLENGE CANCELLED", "GAME MOVE", "GAME WIN", "GAME LOSS", "GAME DRAW"};

    /**
     * Parses the provided command (i.e. parse the list, dict's, etc. present in the command)
     *
     * @author Roy Voetman
     * @param response The response to be parsed.
     * @return The constructed ServerResponse object.
     */
    public static ServerResponse parse(String response) {
        for (String command : LIST_COMMANDS) {
            // Parse the list if present.
            if (response.contains(command)) {
                // Create a list parser.
                ParseStrategy<List<String>> parser = new ListParser();

                return new ServerResponse(command, parser.parse(response, command));
            }
        }

        for (String command : MAP_COMMANDS) {
            // Parse the map if present.
            if (response.contains(command)) {
                // Create a map parser.
                ParseStrategy<Map<String, String>> parser = new MapParser();

                return new ServerResponse(command, parser.parse(response, command));
            }
        }

        // If no arguments are supplied create a default ServerResponse.
        return new ServerResponse(response);
    }
}
