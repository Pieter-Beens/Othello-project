package nl.hanze.game.client.server.interpreter.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Roy Voetman
 */
public class ListParser implements ParseStrategy<List<String>> {
    @Override
    public List<String> parse(String response, String command) {
        String string = response.replace("SVR " + command + " ", "")
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "");

        return new ArrayList<>(Arrays.asList(string.split(", ")));
    }
}
