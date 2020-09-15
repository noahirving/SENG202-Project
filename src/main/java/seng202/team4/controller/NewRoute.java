package seng202.team4.controller;

import javafx.scene.control.TextField;

import java.sql.Struct;

public class NewRoute extends NewRecord{
    public TextField airlineField;
    public TextField srcAirportField;
    public TextField dstAirportField;
    public TextField codeshareField;
    public TextField stopsField;
    public TextField equipmentField;

    @Override
    public String[] getRecordData() {
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
