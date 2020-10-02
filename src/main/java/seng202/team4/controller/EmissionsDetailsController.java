package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import seng202.team4.model.DataType;
import seng202.team4.model.Route;

/**
 * Performs all operations done in the emissions details window
 * that is opened when an emission data row from the tableview is double clicked.
 */
public class EmissionsDetailsController extends Details {

    @FXML private Text airlineText;
    @FXML private Text sourceText;
    @FXML private Text destinationText;
    @FXML private Text planeText;
    @FXML private Text distanceText;
    @FXML private Text CO2Text;

    /**
     * Sets the values of the emissions data in their corresponding text attribute.
     * @param data data that will be displayed.
     */
    @Override
    public void setData(DataType data) {
        Route route = (Route) data;
        airlineText.setText(route.getAirlineCode());
        sourceText.setText(route.getSourceAirportCode());
        destinationText.setText(route.getDestinationAirportCode());
        planeText.setText(route.getPlaneTypeCode());
        distanceText.setText(Double.toString(route.getDistance()));
        CO2Text.setText(Double.toString(route.getCarbonEmissions()));
    }
}
