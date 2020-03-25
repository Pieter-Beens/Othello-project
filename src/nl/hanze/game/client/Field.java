package nl.hanze.game.client;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Stack;

public class Field extends Button {

    public int columnindex;
    public int rowindex;

    private int color;
    private Boolean validity;

    private Stack<int[]> captureData;

    public Field(int columnindex, int rowindex) {
        super();
        this.columnindex = columnindex;
        this.rowindex = rowindex;
        this.color = 0;
        this.captureData = new Stack<>();
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void switchColor() {
        this.color = this.color * -1;
    }

    public void addToCaptureData(int[] dimensions) {
        this.captureData.add(dimensions);
    }

    public void resetCaptureData() {
        this.captureData = new Stack<>();
    }

    public void mark() {
        this.setText("X");
        this.setTextFill(Color.WHITE);
        this.setFont(new Font("Sans", 20));
        this.setStyle("-fx-font-weight: bold");
    }

    public void unmark() {
        this.setText("");
    }

    public int getColor() {
        return this.color;
    }

    public boolean getValidity() {
        return this.validity;
    }

    public Stack<int[]> getCaptureData() {
        return this.captureData;
    }
}
