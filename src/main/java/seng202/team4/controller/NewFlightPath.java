package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NewFlightPath extends NewRecord {
    @FXML
    private TextField typeField;
    @FXML
    private TextField idField;
    @FXML
    private TextField altitudeField;
    @FXML
    private TextField latitudeField;
    @FXML
    private TextField longitudeField;

    @Override
    String[] getRecordData() {
        String type = typeField.getText().trim();
        String id = idField.getText().trim();
        String altitude = altitudeField.getText().trim();
        String latitude = latitudeField.getText().trim();
        String longitude = longitudeField.getText().trim();
        String[] recordData = {type, id, altitude, latitude, longitude};
        return recordData;
    }
}
