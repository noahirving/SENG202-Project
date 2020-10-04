package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import seng202.team4.model.Path;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.StandardCopyOption;

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
     * Initialise the homepage by testing internet connection and displaying error message is internet is not available
     */
    @FXML
    public void initialize() {
        if (!getInternetAccess()) {
            internetWarningLabel.setVisible(true);
        }
    }

    /**
     * Exits the application after user clicks 'Exit' button
     */
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

    /**
     * Opens the user manual as a pdf if the operating system is windows, otherwise informs the user of where
     * the pdf file is located
     */
    public void userManual() {
        Boolean success = true;
        String operatingSystem = System.getProperty("os.name");

        InputStream initialStream = (this.getClass().getResourceAsStream(Path.USER_MANUAL));
        File targetFile = new File(Path.DIRECTORY + Path.USER_MANUAL);

        try {
            java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            success = false;
        }

        if (operatingSystem.startsWith("Windows")) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(targetFile);
                } catch (IOException e) {
                    success = false;
                }
            } else {
                success = false;
            }

            if (!success) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error: Opening user manual PDF file is not supported by your device.");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("PDF file has been copied to the \"GreenFlights_Resources\" folder in the same " +
                    "location as the running JAR or the current directory.");
            alert.show();
        }
    }

    /**
     * Shows the user information in the form of an alert of how calculations are done and the references used.
     */
    public void howItsDone() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How It's Done");
        alert.setHeaderText("The following sources are used in the emission calculations of GreenFlights.");

        FlowPane fp = new FlowPane();
        Label explainText = new Label("Source of $10 per tonne: https://www.mercycorps.org/blog/how-much-offset-your-carbon\n" +
                "Calculating how much carbon a tree obsorbs: https://www.carbonindependent.org/22.html\n" +
                "CO2 per car: https://www.lightfoot.co.uk/news/2017/10/04/how-much-co2-does-a-car-emit-per-year/\n" +
                "Fuel economy in automobiles: https://en.wikipedia.org/wiki/Fuel_economy_in_automobiles\n" +
                "Fuel consumption and CO2: https://www.nrcan.gc.ca/sites/www.nrcan.gc.ca/files/oee/pdf/transportation/fuel-efficient-technologies/autosmart_factsheet_6_e.pdf");
        fp.getChildren().addAll(explainText);
        alert.getDialogPane().contentProperty().set( fp );
        alert.show();
    }
}
