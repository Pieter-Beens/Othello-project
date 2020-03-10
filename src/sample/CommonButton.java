package sample;

import javafx.scene.control.Button;

public class CommonButton extends Button {

    int groupID; // used for handling exclusivity relationships between buttons in the same group (maybe make this another class)

    public CommonButton(int rowID) {
        super();
        this.setMinSize(100, 100);
        //this.setStyle("-fx-background-color: " + player2colorcode + "; -fx-text-fill: " + player2colorcodetext);
        this.groupID = rowID;
    }
}
