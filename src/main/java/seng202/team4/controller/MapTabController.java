package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.team4.Path;
import seng202.team4.model.DatabaseManager;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MapTabController implements Initializable {
    @FXML private WebView googleMapView;
    @FXML private TextField mapSearchField;
    @FXML private TextField mapTabLatitudeField;
    @FXML private TextField mapTabLongitudeField;
    @FXML private Button showAllAirportsButton;
    @FXML private Button selectedRoutesButton;
    @FXML private Button clearMapButton;

    private WebEngine webEngine;
    private static String airportCoordQuery = "SELECT Longitude, Latitude, Name FROM Airport WHERE IATA = '%s'";

    public MapTabController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initMap();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void initMap() throws SQLException {
        webEngine = googleMapView.getEngine();
        webEngine.load(getClass().getResource(Path.mapRsc).toExternalForm());
    }

    @FXML
    private void showSelectedRoutes() throws SQLException {
        resetMap();
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet routesResultSet = stmt.executeQuery("SELECT SourceAirport, DestinationAirport FROM Routes");
        int count = 0;
        while (routesResultSet.next() && count <= 400) {
            showOneRoute(routesResultSet, c);
            count++;
        }
        stmt.close();
        DatabaseManager.disconnect(c);
    }

    private void showOneRoute(ResultSet routesResultSet, Connection c) throws SQLException {


        String sourceAirportIATA = routesResultSet.getString("SourceAirport");
        String destinationAirportIATA = routesResultSet.getString("DestinationAirport");
        ResultSet sourceAirportQuery = c.createStatement().executeQuery(String.format(airportCoordQuery, sourceAirportIATA));
        ResultSet destAirportQuery = c.createStatement().executeQuery(String.format(airportCoordQuery, destinationAirportIATA));

        if (sourceAirportQuery.next() && destAirportQuery.next()) {

            double sourceLatitude = (sourceAirportQuery.getDouble("Latitude"));
            double sourceLongitude = (sourceAirportQuery.getDouble("Longitude"));
            String sourceName = (sourceAirportQuery.getString("Name"));

            double destLatitude = (destAirportQuery.getDouble("Latitude"));
            double destLongitude = (destAirportQuery.getDouble("Longitude"));
            String destName = (destAirportQuery.getString("Name"));

            String routePoints = String.format("[{lat: %f, lng: %f}, {lat: %f, lng: %f}, ]",
                    sourceLatitude, sourceLongitude, destLatitude, destLongitude);

            String scriptToExecute = "addRoute(" + routePoints + ", '" + sourceName + "', '" + destName + "');";
            executeScript(scriptToExecute);


        }


    }
    @FXML
    public void showAllAirports() throws SQLException {
        resetMap();
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet airportResultSet = stmt.executeQuery("SELECT Longitude, Latitude, Name FROM Airport");

        while (airportResultSet.next()) {
            double sourceLatitude = (airportResultSet.getDouble("Latitude"));
            double sourceLongitude = (airportResultSet.getDouble("Longitude"));
            String sourceName = (airportResultSet.getString("Name"));
            String airportPoint = String.format("{lat: %f, lng: %f}", sourceLatitude, sourceLongitude);
            String scriptToExecute = "addAirport(" + airportPoint + ", \"" + sourceName + "\");";
            executeScript(scriptToExecute);
        }
        stmt.close();
        DatabaseManager.disconnect(c);

    }

    @FXML
    private void clearMap() {
        resetMap();
    }

    private void resetMap() {
        String scriptToExecute = "resetMap();";
        executeScript(scriptToExecute);
    }



    public void executeScript(String scriptToExecute) {
        webEngine.executeScript(scriptToExecute);

    }



}
