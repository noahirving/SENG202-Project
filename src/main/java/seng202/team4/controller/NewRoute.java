package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Describes the functionality required for getting
 * a new route record.
 */
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

    /**
     * Gets the content of the text fields in the scene.
     * @return a string array containing each of the text fields' content.
     */
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
