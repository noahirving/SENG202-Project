package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Describes the functionality required for getting
 * a new airline record.
 */
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

    /**
     * Gets the content of the text fields in the scene.
     * @return a string array containing each of the text fields' content.
     */
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
