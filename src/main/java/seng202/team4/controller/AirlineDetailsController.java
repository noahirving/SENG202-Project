package seng202.team4.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seng202.team4.model.Airline;
import seng202.team4.model.DataType;
import seng202.team4.model.Validate;

/**
 * Performs all operations done in the airline details window
 * that is opened when an airline data row from the tableview is double clicked.
 * This includes viewing and editing all data attribute values.
 */
public class AirlineDetailsController extends Details {

    @FXML private JFXTextField nameText;
    @FXML private JFXTextField countryText;
    @FXML private JFXTextField codeText;
    @FXML private JFXTextField IATAText;
    @FXML private JFXTextField ICAOText;
    @FXML private JFXTextField CSText;
    @FXML private JFXCheckBox activeCheckBox;

    @FXML private Label invalidName;
    @FXML private Label invalidCountry;
    @FXML private Label invalidCode;
    @FXML private Label invalidIATA;
    @FXML private Label invalidICAO;
    @FXML private Label invalidCallSign;

    /**
     * Sets the values of the airline data in their corresponding text attribute.
     * @param data data that will be displayed.
     */
    @Override
    public void setData(DataType data) {
        Airline airline = (Airline) data;
        nameText.setText(airline.getName());
        countryText.setText(airline.getCountry());
        codeText.setText(airline.getAlias());
        IATAText.setText(airline.getIata());
        ICAOText.setText(airline.getIcao());
        CSText.setText(airline.getCallSign());
        activeCheckBox.setSelected(airline.isRecentlyActive());
    }

    @FXML
    private void editAndClose() {
        if (checkValid()) {
            editAirline((Airline) data);
            stage.close();
        }
    }

    private void editAirline(Airline airline) {
        airline.setName(nameText.getText());
        airline.setCountry(countryText.getText());
        airline.setAlias(codeText.getText());
        airline.setIata(IATAText.getText());
        airline.setIcao(ICAOText.getText());
        airline.setCallSign(CSText.getText());
        airline.setRecentlyActive(activeCheckBox.isSelected());
    }

    private boolean checkValid() {
        boolean valid = true;
        setErrorsHide();
        if (!Validate.isAlphaMultiLanguage(nameText.getText())) {
            invalidName.setVisible(true);
            valid = false;
        }
        if (!Validate.isAlphaMultiLanguage(countryText.getText())) {
            invalidCountry.setVisible(true);
            valid = false;
        }
        if (!Validate.isAlphaMultiLanguage(codeText.getText())) {
            invalidCode.setVisible(true);
            valid = false;
        }
        if (!Validate.isAirlineIATA(IATAText.getText())) {
            invalidIATA.setVisible(true);
            valid = false;
        }
        if (!Validate.isAirlineICAO(ICAOText.getText())) {
            invalidICAO.setVisible(true);
            valid = false;
        }
        if (!Validate.isAlphaMultiLanguage(CSText.getText())) {
            invalidCallSign.setVisible(true);
            valid = false;
        }
        return valid;
    }

    private void setErrorsHide() {
        invalidName.setVisible(false);
        invalidCountry.setVisible(false);
        invalidCode.setVisible(false);
        invalidIATA.setVisible(false);
        invalidICAO.setVisible(false);
        invalidCallSign.setVisible(false);
    }
}
