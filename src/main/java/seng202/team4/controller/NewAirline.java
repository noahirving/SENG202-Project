package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Describes the functionality required for getting
 * a new airline record.
 */
public class NewAirline extends NewRecord {
    @FXML
    private TextField nameField;
    @FXML
    private TextField codeField;
    @FXML
    private TextField iataField;
    @FXML
    private TextField icaoField;
    @FXML
    private TextField callSignField;
    @FXML
    private TextField countryField;
    @FXML
    private CheckBox recentlyActiveField;

    @FXML
    private ImageView aliasToolTip;
    @FXML
    private ImageView iataToolTip;
    @FXML
    private ImageView icaoToolTip;
    @FXML
    private ImageView callSignToolTip;

    @FXML
    public void initialize() {
        createToolTip(aliasToolTip, "For example, All Nippon Airways is commonly known as \"ANA\"");
    }

    private void createToolTip(ImageView image, String tooltip) {
        Label label = new Label();
        label.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        label.setGraphic(image);
        label.setTooltip(new Tooltip(tooltip));
    }

    /**
     * Gets the content of the text fields in the scene.
     * @return a string array containing each of the text fields' content.
     */
    @Override
    String[] getRecordData() {
        String name = nameField.getText().trim();
        String code = codeField.getText().trim();
        String iata = iataField.getText().trim();
        String icao = icaoField.getText().trim();
        String callSign = callSignField.getText().trim();
        String country = countryField.getText().trim();
        String recentlyActive;
        if (recentlyActiveField.isSelected()) {
            recentlyActive = "Y";
        } else {
            recentlyActive = "N";
        }
        String[] recordData = {name, code, iata, icao, callSign, country, recentlyActive};
        return recordData;
    }
}
