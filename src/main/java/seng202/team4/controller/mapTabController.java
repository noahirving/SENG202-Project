package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.team4.Path;

import java.net.URL;
import java.util.ResourceBundle;

public class mapTabController implements Initializable {
    @FXML private WebView googleMapView;
    @FXML private TextField mapSearchField;
    @FXML private TextField mapTabLatitudeField;
    @FXML private TextField mapTabLongitudeField;

    private WebEngine webEngine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initMap();
    }

    private void initMap() {
        googleMapView.getEngine().load(getClass().getResource(Path.mapRsc).toString());

    }



}
