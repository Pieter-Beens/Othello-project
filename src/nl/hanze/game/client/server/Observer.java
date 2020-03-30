package nl.hanze.game.client.server;

import java.util.HashMap;

interface Observer{
    HashMap<String, String> serverResponses = new HashMap<>();

    default void update(String s) {
        System.out.println(s);
        //TODO: implement response handling mechanism
    }

}
