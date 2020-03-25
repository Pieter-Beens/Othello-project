package nl.hanze.game.client.util;

public interface GameObservable {
    public void register(GameObserver game);
    public void unregister(GameObserver game);
    public void notifyObservers(Move move);
}
