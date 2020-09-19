package seng202.team4.controller;

import javafx.scene.control.TextField;

public class NewAirline extends NewRecord{
    public TextField nameField;
    public TextField codeField;
    public TextField iataField;
    public TextField icaoField;
    public TextField callSignField;
    public TextField countryField;
    public TextField recentlyActiveField;

    @Override
    public String[] getRecordData() {
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
