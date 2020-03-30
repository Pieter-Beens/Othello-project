package nl.hanze.game.client.refactor.scenes.menu.online;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import nl.hanze.game.client.refactor.scenes.Controller;

import java.io.IOException;

public class OnlineMenuController extends Controller {
    @FXML
    public TextArea textField;

    private String string;

    @FXML
    private void btnClick(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String value = btn.getText();

        string += value;

        textField.setText(string);
    }

    @FXML
    private void btnGoBack(ActionEvent event) throws IOException {
        loadScene("start/start.fxml");
    }
}
