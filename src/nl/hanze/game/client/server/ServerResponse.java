package nl.hanze.game.client.server;

import java.util.List;
import java.util.Map;

/**
 * @author Roy Voetman
 */
public class ServerResponse {
    private Map<String, String> map;
    private List<String> list;
    private String command;

    public ServerResponse(String command) {
        this.command = command;
    }

    public ServerResponse(String command, Map<String, String> map) {
        this.map = map;
        this.command = command;
    }

    public ServerResponse(String command, List<String> list) {
        this.list = list;
        this.command = command;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public List<String> getList() {
        return list;
    }

    public String getCommand() {
        return command;
    }
}
