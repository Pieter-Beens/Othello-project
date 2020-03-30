package nl.hanze.game.client.refactor.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nl.hanze.game.client.refactor.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class Controller {
    protected void loadScene(String fxml) throws IOException {
        loadScene(fxml, new FXMLLoader());
    }

    protected void loadScene(String fxml, FXMLLoader loader) throws IOException {
        fxml = "src/nl/hanze/game/client/refactor/scenes/" + fxml;

        FileInputStream fileInputStream = new FileInputStream(new File(fxml));
        Parent parent = loader.load(fileInputStream);

        Scene scene = new Scene(parent);

        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();
    }
}
