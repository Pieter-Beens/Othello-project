package nl.hanze.game.client.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nl.hanze.game.client.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import nl.hanze.game.client.server.Observer;

public abstract class Controller implements Observer {
    public static Controller loadScene(String fxml) throws IOException {
        return loadScene(fxml, new FXMLLoader());
    }

    public static Controller loadScene(String fxml, FXMLLoader loader) throws IOException {
        fxml = "src/nl/hanze/game/client/refactor/scenes/" + fxml;

        FileInputStream fileInputStream = new FileInputStream(new File(fxml));
        Parent parent = loader.load(fileInputStream);

        Scene scene = new Scene(parent);

        Main.primaryStage.setScene(scene);
        Main.primaryStage.show();

        return loader.getController();
    }

    public void update(String s){
        System.out.println("Controller sees: " + s);
    }
}
