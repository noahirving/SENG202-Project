package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import seng202.team4.model.Airport;
import seng202.team4.model.DataType;

/**
 * Performs all operations done in the airport details window
 * that is opened when an airport data row from the tableview is double clicked.
 */
public class AirportDetailsController extends Details {

    @FXML private Text nameText;
    @FXML private Text cityText;
    @FXML private Text countryText;
    @FXML private Text iataText;
    @FXML private Text icaoText;
    @FXML private Text latitudeText;
    @FXML private Text longitudeText;
    @FXML private Text altitudeText;
    @FXML private Text timezoneText;
    @FXML private Text dstText;
    @FXML private Text tzText;

    /**
     * Sets the values of the airport data in their corresponding text attribute.
     * @param data data that will be displayed.
     */
    @Override
    public void setData(DataType data) {
        Airport airport = (Airport) data;
        nameText.setText(airport.getName());
        cityText.setText(airport.getCity());
        countryText.setText(airport.getCountry());
        iataText.setText(airport.getIata());
        icaoText.setText(airport.getIcao());
        latitudeText.setText(Double.toString(airport.getLatitude()));
        longitudeText.setText(Double.toString(airport.getLongitude()));
        altitudeText.setText(Double.toString(airport.getAltitude()));
        timezoneText.setText(Double.toString(airport.getTimezone()));
        dstText.setText(Character.toString(airport.getDst()));
        tzText.setText(airport.getTzDatabase());
    }

}
