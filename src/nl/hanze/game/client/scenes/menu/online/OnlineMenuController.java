package nl.hanze.game.client.scenes.menu.online;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.Controller;

import java.io.IOException;

public class OnlineMenuController extends Controller {
    @FXML
    public TextArea textField;

    @FXML
    private void btnClick(ActionEvent event) {
        String value = textField.getText();

        try {
            Main.client.connect("127.0.0.1", 7789);
            Main.client.setController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Main.client.login(value);
    }

    public void update(String s) {
        if (s.equals("OK")) {
            Platform.runLater(() -> {
                try { loadScene("games/tictactoe/tictactoe.fxml");
                } catch (IOException ignore) {}
            });
        }
    }

    @FXML
    private void btnGoBack(ActionEvent event) throws IOException {
        loadScene("start/start.fxml");
    }
}
