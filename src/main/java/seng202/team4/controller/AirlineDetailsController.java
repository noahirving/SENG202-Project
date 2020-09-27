package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import seng202.team4.model.*;

public class AirlineDetailsController extends Details {

    @FXML private Text nameText;
    @FXML private Text countryText;
    @FXML private Text codeText;
    @FXML private Text IATAText;
    @FXML private Text ICAOText;
    @FXML private Text CSText;
    @FXML private Text activeText;

    public void setData(DataType data) {
        Airline airline = (Airline) data;
        nameText.setText(airline.getName());
        countryText.setText(airline.getCountry());
        codeText.setText(airline.getAlias());
        IATAText.setText(airline.getIata());
        ICAOText.setText(airline.getIcao());
        CSText.setText(airline.getCallSign());
        if (airline.isRecentlyActive()) {
            activeText.setText("True");
        } else {
            activeText.setText("False");
        }
    }
}
