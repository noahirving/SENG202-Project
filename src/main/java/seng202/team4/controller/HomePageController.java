package seng202.team4.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng202.team4.Path;

import java.io.IOException;

public class HomePageController {


    @FXML
    public void searchButtonPressed() {
    }

    @FXML
    public void viewAllTabs(ActionEvent buttonPress) throws IOException {
        Parent mainView = FXMLLoader.load(getClass().getResource(Path.mainSceneFXML));

        Scene mainScene = new Scene(mainView);

        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    @FXML
    public void viewAirlinesButtonPress(ActionEvent buttonPress) throws IOException {
        Parent airlineView = FXMLLoader.load(getClass().getResource(Path.airlineSceneFXML));

        Scene airlineScene = new Scene(airlineView);


        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(airlineScene);
        window.show();
    }

    @FXML
    public void viewAirportsButtonPress(ActionEvent buttonPress) throws IOException {
        Parent airportView = FXMLLoader.load(getClass().getResource(Path.airportSceneFXML));

        Scene airportScene = new Scene(airportView);

        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(airportScene);
        window.show();
    }

    @FXML
    public void viewRoutesButtonPress(ActionEvent buttonPress) throws IOException {
        Parent routeView = FXMLLoader.load(getClass().getResource(Path.routeSceneFXML));

        Scene routesScene = new Scene(routeView);

        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(routesScene);
        window.show();
    }

    @FXML
    public void viewMapButtonPressed(ActionEvent buttonPress) throws IOException {
        Parent mapView = FXMLLoader.load(getClass().getResource(Path.mapSceneFXML));

        Scene mapScene = new Scene(mapView);

        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(mapScene);
        window.show();
    }

    @FXML
    public void viewEmissionsButtonPressed(ActionEvent buttonPress) throws IOException {
        Parent emissionView = FXMLLoader.load(getClass().getResource(Path.emissionsSceneFXML));

        Scene emissionScene = new Scene(emissionView);

        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(emissionScene);
        window.show();
    }

    @FXML
    public void modifyDataButtonPressed() {

    }


}
