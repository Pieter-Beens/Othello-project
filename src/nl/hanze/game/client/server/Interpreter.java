package nl.hanze.game.client.server;

import java.util.*;

/**
 * @author Roy Voetman
 */
public class Interpreter {
    public static final String[] LIST_COMMANDS = {"GAMELIST", "PLAYERLIST"};
    public static final String[] MAP_COMMANDS = {"GAME CHALLENGE", "GAME MATCH", "GAME YOURTURN", "GAME CHALLENGE CANCELLED", "SVR GAME MOVE"};

    public static ServerResponse parse(String response) {
        for (String command : LIST_COMMANDS) {
            if (response.contains(command)) {
                return new ServerResponse(command, parseList(response, command));
            }
        }

        for (String command : MAP_COMMANDS) {
            if (response.contains(command)) {
                return new ServerResponse(command, parseMap(response, command));
            }
        }

        return new ServerResponse(response);
    }

    private static List<String> parseList(String response, String command) {
        String string = response.replace("SVR " + command + " ", "")
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "");

        return new ArrayList<>(Arrays.asList(string.split(", ")));
    }

    private static Map<String, String> parseMap(String response, String command) {
        String string = response.replace("SVR " + command + " ", "")
                .replace("{", "")
                .replace("}", "")
                .replace("\"", "");

        String[] pairs = string.split(", ");

        Map<String, String> map = new HashMap<>();

        for (String pair : pairs) {
            String[] segments = pair.split(": ");
            System.out.println(Arrays.toString(segments));

            map.put(segments[0], segments.length == 2 ? segments[1] : "");
        }

        return map;
    }
}
