package sample;

import javafx.scene.layout.GridPane;

public class Board extends GridPane {
    public Board(int fieldsize) {

    }

    public Field[] getFields() {

        Field[] fields = new Field[64];
        return fields;
    }
}
