package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import seng202.team4.model.DataType;
import seng202.team4.model.Route;

public class RouteDetailsController extends Details {

    @FXML private Text airlineText;
    @FXML private Text sourceText;
    @FXML private Text destinationText;
    @FXML private Text codeShareText;
    @FXML private Text stopsText;
    @FXML private Text equipmentText;

    @Override
    public void setData(DataType data) {
        Route route = (Route) data;
        airlineText.setText(route.getAirlineCode());
        sourceText.setText(route.getSourceAirportCode());
        destinationText.setText(route.getDestinationAirportCode());
        if (route.isCodeshare()) {
            codeShareText.setText("Y");
        } else {
            codeShareText.setText("N");
        }
        stopsText.setText(Integer.toString(route.getNumStops()));
        equipmentText.setText(route.getPlaneTypeCode());
    }
}
