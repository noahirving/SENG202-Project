package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NewAirline extends NewRecord {
    @FXML
    private TextField nameField;
    @FXML
    private TextField codeField;
    @FXML
    private TextField iataField;
    @FXML
    private TextField icaoField;
    @FXML
    private TextField callSignField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField recentlyActiveField;

    @Override
    String[] getRecordData() {
        String name = nameField.getText().trim();
        String code = codeField.getText().trim();
        String iata = iataField.getText().trim();
        String icao = icaoField.getText().trim();
        String callSign = callSignField.getText().trim();
        String country = countryField.getText().trim();
        String recentlyActive = recentlyActiveField.getText().trim();
        String[] recordData = {name, code, iata, icao, callSign, country, recentlyActive};
        return recordData;
    }
}
