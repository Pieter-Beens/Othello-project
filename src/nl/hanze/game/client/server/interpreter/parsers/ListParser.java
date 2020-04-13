package nl.hanze.game.client.server.interpreter.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parser to parse lists (i.e. Items between '[' ']')
 *
 * @author Roy Voetman
 */
public class ListParser implements ParseStrategy<List<String>> {
    /**
     * Parses all the items between '[' ']' into a list.
     *
     * @author Roy Voetman
     * @param response The response from the server.
     * @param command The matched command.
     * @return A list of all the elements in the list.
     */
    @Override
    public List<String> parse(String response, String command) {
        String string = response.replace("SVR " + command + " ", "")
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "");

        return new ArrayList<>(Arrays.asList(string.split(", ")));
    }
}
