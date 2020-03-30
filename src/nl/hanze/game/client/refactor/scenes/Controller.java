package nl.hanze.game.client.refactor.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nl.hanze.game.client.refactor.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class Controller {
    public static Controller loadScene(String fxml) throws IOException {
        fxml = "src/nl/hanze/game/client/refactor/scenes/" + fxml;

        FileInputStream fileInputStream = new FileInputStream(new File(fxml));
        FXMLLoader loader = new FXMLLoader();
        Parent parent = loader.load(fileInputStream);

        Scene scene = new Scene(parent);

        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();

        return loader.getController();
    }
}
