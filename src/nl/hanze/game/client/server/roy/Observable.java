package nl.hanze.game.client.server.roy;

interface Observable {
    public void addObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObservers(String s);
}
