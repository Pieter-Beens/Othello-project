package nl.hanze.game.client.scenes.menu.offline;

public class OfflineMenuModel {

    private String game;
    private String gameMode;
    private int difficulty;
    private boolean fullscreen;

    //Game
    public void setGame(String game) { this.game = game; }

    public String getGame() { return game; }

    //GameMode
    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    //Difficulty
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    //Fullscreen
    public boolean getFullscreen() { return fullscreen; }

    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }



}
