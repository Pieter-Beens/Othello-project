package nl.hanze.game.client.scenes.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import nl.hanze.game.client.scenes.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController extends Controller implements Initializable {
    @FXML
    public HBox mode;

    @FXML
    public ImageView online;

    @FXML
    public ImageView offline;

    @FXML
    public ImageView logo;

    public void btnOnline(ActionEvent event) throws IOException {
        loadScene("menu/online/online.fxml");
    }

    public void btnOffline(ActionEvent event) throws IOException {
        loadScene("menu/offline/offline.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image;

        try {
            image = new Image(new FileInputStream("src/resources/online.png"));
            online.setImage(image);

            image = new Image(new FileInputStream("src/resources/offline.png"));
            offline.setImage(image);

            image = new Image(new FileInputStream("src/resources/logo.png"));
            logo.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
