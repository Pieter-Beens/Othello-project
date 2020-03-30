package nl.hanze.game.client.server;

import java.util.HashMap;

public interface Observer{
    //HashMap<String, String> serverResponses = new HashMap<>();

    default void update(String s) {
        System.out.println("Observer: "+s);
        //TODO: implement response handling mechanism
    }

}
