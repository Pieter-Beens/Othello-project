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
        String string = response
                .replace("[", "")
                .replace("]", "");

        List<String> list = new ArrayList<>();
        boolean foundString = false;
        int index = 0;

        // Loop trough all characters
        for (char charater : string.toCharArray()) {
            // If a string starts or ends
            if ( charater == '"') {
                // Toggle boolean
                foundString = !foundString;

                // Increase list index if a string ending is found
                if (!foundString) index++;
            } else if (foundString) {
                // Create new empty string if index does not exist in list.
                if (list.size() == index) list.add("");

                // Append character to current String at: index
                list.set(index, list.get(index) + charater);
            }
        }

        return list;
    }
}
