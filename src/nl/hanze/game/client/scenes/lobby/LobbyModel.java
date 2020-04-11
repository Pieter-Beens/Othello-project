package nl.hanze.game.client.scenes.lobby;

public class LobbyModel {

    private String gameMode;
    private boolean fullscreen;

    //GameMode
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameMode() {
        return gameMode;
    }

    //Fullscreen
    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }

    public boolean getFullscreen() { return fullscreen; }

}
