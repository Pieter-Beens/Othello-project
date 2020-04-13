package nl.hanze.game.client.scenes.menu.online;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import nl.hanze.game.client.Main;
import nl.hanze.game.client.scenes.Controller;
import nl.hanze.game.client.scenes.games.GameModel;
import nl.hanze.game.client.scenes.lobby.LobbyController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OnlineMenuController extends Controller {
    @FXML
    private TextField name;

    @FXML
    private TextField ip;

    @FXML
    private TextField port;

    @FXML
    private TextField turnTimeField;

    @FXML
    private Text errorMsg;

    /**
     * Button that starts the login process
     *
     * @author Jasper van Dijken
     * @param event the event mouse-click
     */
    @FXML
    private void connect(ActionEvent event) throws IOException {

        //error indicator
        boolean error = false;

        //Check if all fields are filled in
        if (name.getText().isEmpty() || ip.getText().isEmpty() || port.getText().isEmpty() || turnTimeField.getText().isEmpty()) {
            errorMsg.setText("Please fill in all fields");
            error = true;
        }
        //if all fields are filled in, check for valid input
        else {

            //check for valid ip
            if (!checkIP(ip.getText())) {
                errorMsg.setText("Invalid IP-Address");
                error = true;
            }

            //check for valid port number
            if (!port.getText().matches("^[0-9]*$")) {
                errorMsg.setText("Invalid Port Number");
                error = true;
            }

            // check turn time
            if (!turnTimeField.getText().matches("\\d*")) {
                errorMsg.setText("Turn time is not a number");
                error = true;
            }
        }

        // no errors? try to connect to the server
        if (!error) {
            if (!Main.serverConnection.connect(ip.getText(), Integer.parseInt(port.getText()))) {
                errorMsg.setText("Connection refused");
                return;
            }
            Main.serverConnection.addObserver(this);
            Main.serverConnection.login(name.getText());
        }
    }

    /**
     * Method that checks if the passed IP-address is valid
     *
     * @author Jasper van Dijken
     * @param ip the ip-address that will be checked for validity
     * @return true if ip is valid, false if ip is invalid
     */
    public static boolean checkIP (String ip) {

        //checking the length, split at each dot
        String[] split = ip.split("\\.");
        if (split.length != 4) {
            return false;
        }

        //check if the numbers are within the boundaries
        for (String element : split) {
            int i = Integer.parseInt(element);
            if ((i < 0) || (i > 255)) {
                return false;
            }
        }

        if (ip.startsWith(".")) {
            return false;
        }
        if (ip.endsWith(".")) {
            return false;
        }
        return true;
    }

    /**
     * This method is called whenever the game server send a response.
     *
     * @author Roy Voetman
     * @param response The response from the server
     */
    @Override
    public void update(String response) {
        super.update(response);

        // The only command that is sent in this controller is the "login" command
        // When an "OK" response is received it should correspond the login (i.e. Login successful).
        if (response.equals("OK")) {

            // Save the settings to the GameModel static fields.
            GameModel.serverName = name.getText();
            GameModel.serverTurnTime = Integer.parseInt(turnTimeField.getText());

            // Load the lobby
            Platform.runLater(() -> {
                try {
                    loadScene("lobby/lobby.fxml");
                } catch (IOException ignore) { }
            });
        }
    }

    /**
     * Redirect to the previous scene when go back button is clicked.
     *
     * @author Roy Voetman
     * @param event Action event of the button click.
     * @throws IOException When previous scene FXML can not be found.
     */
    @FXML
    private void btnGoBack(ActionEvent event) throws IOException {
        goBack();
    }
}
