package seng202.team4.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class emissionsTabController {

    @FXML
    public void pressHomeButton(ActionEvent buttonPress) throws IOException {
        Parent homeView = FXMLLoader.load(getClass().getResource("/seng202.team4/homePage.fxml"));

        Scene homeScene = new Scene(homeView);

        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }

    @FXML
    public void pressEnvironmentalDonationButton(ActionEvent buttonPress) {
        double tempDonationAmount = 10;
        String donationDollarsCents = String.format("%.2f", tempDonationAmount);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Environmental Donation Equivalent");
        alert.setHeaderText("Donation Equivalent: $" + donationDollarsCents);
        alert.setContentText("Donations for offsetting carbon emissions can be made on the following sites...");
        alert.show();
    }

    @FXML
    public void pressTreesEquivalentButton(ActionEvent buttonPress) {
        int tempTreesAmount = 10;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Trees Equivalent");
        alert.setHeaderText("Trees Equivalent: " + tempTreesAmount);
        alert.setContentText("Donations for planting trees can be made on the following sites...");
        alert.show();
    }

    @FXML
    public void pressAlternativeRoutesButton(ActionEvent buttonPress) {
        // To implement
    }

    @FXML
    public void pressAlternativeAircraftTypeButton(ActionEvent buttonPress) {
        // To implement
    }

}
