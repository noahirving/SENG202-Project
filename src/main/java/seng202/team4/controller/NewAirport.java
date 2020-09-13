package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seng202.team4.model.Airport;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class NewAirport {

    public TextField nameField;
    public TextField cityField;
    public TextField countryField;
    public TextField iataField;
    public TextField icaoField;
    public TextField latitudeField;
    public TextField longitudeField;
    public TextField altitudeField;
    public TextField timeZoneField;
    public TextField dstField;
    public TextField tzDatabaseField;
    public ComboBox setComboBox;
    public Stage stage;
    public DataController controller;
    public void setUp(Stage stage, DataController controller) {
        this.controller = controller;
        this.stage = stage;
        try {
            setDataSetComboBox(setComboBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: Essentially the same as the function from DataController, a more efficient implementation would be preferred
    public void setDataSetComboBox(ComboBox comboBox) throws Exception{
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet rs = stmt.executeQuery("Select Name from AirportSet");
        ObservableList<String> dataSetNames = FXCollections.observableArrayList();
        while (rs.next()) {
            dataSetNames.add(rs.getString("Name"));
        }
        comboBox.setItems(dataSetNames);
        rs.close();
        stmt.close();
        DatabaseManager.disconnect(c);
    }

    public void confirm(ActionEvent actionEvent) throws Exception {
        ArrayList<String> errorMessage = new ArrayList<String>();
        String name = nameField.getText().trim();
        String city = nameField.getText().trim();
        String country = countryField.getText().trim();
        String iata = iataField.getText().trim();
        String icao = icaoField.getText().trim();
        String latitude = latitudeField.getText().trim();
        String longitude = longitudeField.getText().trim();
        String altitude = altitudeField.getText().trim();
        String timeZone = timeZoneField.getText().trim();
        String dst = dstField.getText().trim();
        String tzDatabase = tzDatabaseField.getText().trim();

        Airport airport = Airport.getValid(errorMessage, name, city, country, iata, icao, latitude, longitude, altitude, timeZone, dst, tzDatabase);
        if (airport == null) {
            for (String s: errorMessage) {
                System.out.println(s);
            }
        }
        else {
            String setName = setComboBox.getValue().toString();
            DataLoader.addNewRecord(airport, setName);
            controller.setTable();
            stage.close();
        }
    }

    public void cancel(ActionEvent actionEvent) {
        stage.close();
    }
}
