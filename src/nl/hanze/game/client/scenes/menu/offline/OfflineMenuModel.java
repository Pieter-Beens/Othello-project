package nl.hanze.game.client.scenes.menu.offline;

public class OfflineMenuModel {

    private String game;
    private String gameMode;
    private int difficulty = 1; //difficulty defaults to Medium
    private int turnTime = 10; //difficulty defaults to Medium
    private boolean fullscreen;
    private static String resultMessage;

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

    // Turn time
    public int getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(int turnTime) {
        this.turnTime = turnTime;
    }

    //Fullscreen
    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }

    public boolean getFullscreen() { return fullscreen; }

    public static void setResultMessage(String msg) { resultMessage = msg; }

    public String getResultMessage() { return resultMessage; }
}
