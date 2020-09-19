package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Describes the functionality required for getting
 * a new airport record.
 */
public class NewAirport extends NewRecord {

    @FXML
    private TextField nameField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField iataField;
    @FXML
    private TextField icaoField;
    @FXML
    private TextField latitudeField;
    @FXML
    private TextField longitudeField;
    @FXML
    private TextField altitudeField;
    @FXML
    private TextField timeZoneField;
    @FXML
    private TextField dstField;
    @FXML
    private TextField tzDatabaseField;

    /**
     * Gets the content of the text fields in the scene.
     * @return a string array containing each of the text fields' content.
     */
    @Override
    String[] getRecordData() {
        String name = nameField.getText().trim();
        String city = cityField.getText().trim();
        String country = countryField.getText().trim();
        String iata = iataField.getText().trim();
        String icao = icaoField.getText().trim();
        String latitude = latitudeField.getText().trim();
        String longitude = longitudeField.getText().trim();
        String altitude = altitudeField.getText().trim();
        String timeZone = timeZoneField.getText().trim();
        String dst = dstField.getText().trim();
        String tzDatabase = tzDatabaseField.getText().trim();
        String[] recordData = {name, city, country, iata, icao, latitude, longitude, altitude, timeZone, dst, tzDatabase};
        return recordData;
    }
}
