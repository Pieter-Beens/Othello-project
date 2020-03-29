package nl.hanze.game.client.server;

interface Observer{
    default void update(String s){
        System.out.println(s);
    }
    //TODO: implement response handling mechanism
}
