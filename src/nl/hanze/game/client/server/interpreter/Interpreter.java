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

    public static ServerResponse parse(String response) {
        for (String command : LIST_COMMANDS) {
            if (response.contains(command)) {
                ParseStrategy<List<String>> parser = new ListParser();

                return new ServerResponse(command, parser.parse(response, command));
            }
        }

        for (String command : MAP_COMMANDS) {
            if (response.contains(command)) {
                ParseStrategy<Map<String, String>> parser = new MapParser();

                return new ServerResponse(command, parser.parse(response, command));
            }
        }

        return new ServerResponse(response);
    }
}
