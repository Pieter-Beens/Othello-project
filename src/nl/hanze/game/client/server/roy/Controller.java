package nl.hanze.game.client.server.roy;

public class Controller implements Observer {
    public Controller() {
        Main.client.addObserver(this);
    }

    @Override
    public void commandResponse(String response) {
        System.out.println(response);
    }
}
