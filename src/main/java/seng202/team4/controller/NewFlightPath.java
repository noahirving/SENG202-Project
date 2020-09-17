package seng202.team4.controller;

import javafx.scene.control.TextField;

public class NewFlightPath extends NewRecord{
    public TextField typeField;
    public TextField idField;
    public TextField altitudeField;
    public TextField latitudeField;
    public TextField longitudeField;

    @Override
    public String[] getRecordData() {
        String type = typeField.getText().trim();
        String id = idField.getText().trim();
        String altitude = altitudeField.getText().trim();
        String latitude = latitudeField.getText().trim();
        String longitude = longitudeField.getText().trim();
        String[] recordData = {type, id, altitude, latitude, longitude};
        return recordData;
    }
}
