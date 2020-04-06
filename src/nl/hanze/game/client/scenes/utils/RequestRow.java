package nl.hanze.game.client.scenes.utils;


/**
 * @author Jasper van Dijken
 */
public class RequestRow {
    String name;
    String challengeID;
    String game;

    public RequestRow() {

    }

    public RequestRow(String name, String challengeID, String game) {
        super();
        this.name = name;
        this.challengeID = challengeID;
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public String getChallengeID() {
        return challengeID;
    }

    public String getGame() {
        return game;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChallengeID(String challengeID) {
        this.challengeID = challengeID;
    }

    public void setGame(String game) {
        this.game = game;
    }

}
