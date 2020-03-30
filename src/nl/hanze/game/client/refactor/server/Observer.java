package nl.hanze.game.client.refactor.server;

public interface Observer {
    default void update(String s){
        System.out.println(s);
    }
    //TODO: implement response handling mechanism
}
