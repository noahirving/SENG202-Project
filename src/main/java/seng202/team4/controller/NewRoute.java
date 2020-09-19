package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NewRoute extends NewRecord{
    @FXML
    private TextField airlineField;
    @FXML
    private TextField srcAirportField;
    @FXML
    private TextField dstAirportField;
    @FXML
    private TextField codeshareField;
    @FXML
    private TextField stopsField;
    @FXML
    private TextField equipmentField;

    @Override
    String[] getRecordData() {
        String airline = airlineField.getText().trim();
        String srcAirport = srcAirportField.getText().trim();
        String dstAirport = dstAirportField.getText().trim();
        String codeshare = codeshareField.getText().trim();
        String stops = stopsField.getText().trim();
        String equipment = equipmentField.getText().trim();

        String[] recordData = {airline, srcAirport, dstAirport, codeshare, stops, equipment};
        return recordData;
    }
}
