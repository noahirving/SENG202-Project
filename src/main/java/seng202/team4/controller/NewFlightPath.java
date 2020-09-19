package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Describes the functionality required for getting
 * a new flight path record.
 */
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

    /**
     * Gets the content of the text fields in the scene.
     * @return a string array containing each of the text fields' content.
     */
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
