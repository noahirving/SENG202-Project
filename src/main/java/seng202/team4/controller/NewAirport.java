package seng202.team4.controller;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seng202.team4.model.Airport;
import seng202.team4.model.DataType;
import java.util.ArrayList;

public class NewAirport extends NewRecord {

    public TextField nameField;
    public TextField cityField;
    public TextField countryField;
    public TextField iataField;
    public TextField icaoField;
    public TextField latitudeField;
    public TextField longitudeField;
    public TextField altitudeField;
    public TextField timeZoneField;
    public TextField dstField;
    public TextField tzDatabaseField;
    public ComboBox setComboBox;
    public Stage stage;
    public DataController controller;

    @Override
    public String[] getRecordData() {
        String name = nameField.getText().trim();
        String city = nameField.getText().trim();
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
