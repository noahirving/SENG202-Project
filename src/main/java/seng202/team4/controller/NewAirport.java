package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Describes the functionality required for getting
 * a new airport record.
 */
public class NewAirport extends NewRecord {

    @FXML private TextField nameField;
    @FXML private TextField cityField;
    @FXML private TextField countryField;
    @FXML private TextField iataField;
    @FXML private TextField icaoField;
    @FXML private TextField latitudeField;
    @FXML private TextField longitudeField;
    @FXML private TextField altitudeField;
    @FXML private TextField timeZoneField;
    @FXML private TextField dstField;
    @FXML private TextField tzDatabaseField;
    @FXML private Label iataToolTip;
    @FXML private Label icaoToolTip;
    @FXML private Label timeZoneToolTip;
    @FXML private Label dsToolTip;
    @FXML private Label tzDatabaseToolTip;

    /**
     * Initialises the tool tips for the new airline popup
     */
    @FXML
    public void initialize() {
        initialiseToolTips();
    }

    /**
     * Uses the CreateToolTip method from NewRecord to set the tool tips for the different entry fields
     */
    private void initialiseToolTips() {
        createToolTip(iataToolTip, "International Air Transport Association number, usually a 3-letter code", false);
        createToolTip(icaoToolTip, "International Civil Aviation Organization, usually a 4-letter code", false);
        createToolTip(timeZoneToolTip, "Hours offset from UTC. Fractional hours are expressed as decimals, eg. India is 5.5", false);
        createToolTip(dsToolTip, "Daylight savings time. One of E (Europe), A (US/Canada), " +
                "S (South America), O (Australia), Z (New Zealand), N (None) or U (Unknown)", false);
        createToolTip(tzDatabaseToolTip, "Timezone in \"tz\" (Olson) format, eg. \"America/Los_Angeles\"", false);
    }

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
