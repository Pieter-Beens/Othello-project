package nl.hanze.game.client.server.interpreter.parsers;

import java.util.*;

/**
 * @author Roy Voetman
 */
public class MapParser implements ParseStrategy<Map<String, String>> {
    @Override
    public Map<String, String> parse(String response, String command) {
        String string = response.replace("SVR " + command + " ", "")
                .replace("{", "")
                .replace("}", "")
                .replace("\"", "");

        String[] pairs = string.split(", ");

        Map<String, String> map = new HashMap<>();

        for (String pair : pairs) {
            String[] segments = pair.split(": ");
            map.put(segments[0], segments.length == 2 ? segments[1] : "");
        }

        // Fix typo from server response
        String comment = map.get("COMMENT");
        if (comment != null && comment.equals("Turn timelimit reached")) {
            map.put("COMMENT", "Turn time limit reached");
        }

        return map;
    }
}
