package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import seng202.team4.model.DataType;
import seng202.team4.model.FlightPath;

public class FlightPathDetailsController extends Details {

    @FXML private Text typeText;
    @FXML private Text flightPathIdText;
    @FXML private Text latitudeText;
    @FXML private Text longitudeText;
    @FXML private Text altitudeText;


    @Override
    public void setData(DataType data) {
        FlightPath path = (FlightPath) data;
        flightPathIdText.setText(path.getFlightPathId());
        latitudeText.setText(Double.toString(path.getLatitude()));
        longitudeText.setText(Double.toString(path.getLongitude()));
        altitudeText.setText(Double.toString(path.getAltitude()));

        switch (path.getType()) {
            case "APT": typeText.setText("Airport"); break;
            case "NDB": typeText.setText("Non-directional beacon"); break;
            case "VOR": typeText.setText("VHF omni-directional range"); break;
            case "FIX": typeText.setText("Navigational fix"); break;
            case "DME": typeText.setText("Distance measuring equipment"); break;
            case "LATLON": typeText.setText("Latitude/Longitude point"); break;
            default: typeText.setText("Unknown"); break;
        }
    }
}
