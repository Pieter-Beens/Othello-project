package nl.hanze.game.client.scenes.menu.offline;

public class OfflineMenuModel {

    private String game;
    private String gameMode;
    private int difficulty = 1; //difficulty defaults to Medium
    private boolean fullscreen;

    //Game
    public void setGame(String game) { this.game = game; }

    public String getGame() { return game; }

    //GameMode
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameMode() {
        return gameMode;
    }

    //Difficulty
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    //Fullscreen
    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }

    public boolean getFullscreen() { return fullscreen; }

}
