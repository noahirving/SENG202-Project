package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import seng202.team4.model.Main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Performs logic for the 'Home' tab of the application
 * Responsible for giving instructions to the user and in
 * a later release will be responsible for global searching
 * functionality. Allows user to load the default database
 */
public class HomePageController {

    /**
     * Label indicating internet and therefore maps is inaccessible
     */
    @FXML private Label internetWarningLabel;
    /**
     * ImageView that stores the home page image
     */
    @FXML private ImageView homeImage;
    /**
     * GridPane that holds all the controls for the homepage
     */
    @FXML private GridPane gridPane;
    @FXML private VBox vBox;

    /**
     * Initialise the homepage by testing internet connection and displaying error message is internet is not available
     */
    @FXML
    public void initialize() {
        if (!getInternetAccess()) {
            internetWarningLabel.setVisible(true);
        }

    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    /**
     * Uses isHostAvailable to check if one or more of google.com, amazon.com, facebook.com, or apple.com is available.
     * The likelihood of all these websites being down is extremely low.
     * @return true or false on whether or not internet is available
     */
    private Boolean getInternetAccess() {
        return isHostAvailable("google.com") || isHostAvailable("amazon.com")
                || isHostAvailable("facebook.com")|| isHostAvailable("apple.com");
    }

    /**
     * Opens a socket to a given host and tests if this host is available attempting to connect, returns the result of
     * this attempt (true or false). Timeout for connection is 3000 milliseconds.
     * @param hostName host to connect to (e.g. google.com)
     * @return true or false on whether connection attempt was successful
     */
    private static boolean isHostAvailable(String hostName) {
        Socket socket = new Socket();
        try {
            int port = 80;
            InetSocketAddress socketAddress = new InetSocketAddress(hostName, port);
            socket.connect(socketAddress, 3000);

            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

}
