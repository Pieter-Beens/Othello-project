package nl.hanze.game.client.scenes.menu.offline;

public class OfflineMenuModel {
    private String gameMode;

    private int difficulty;

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
