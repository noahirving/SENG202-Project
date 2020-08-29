package seng202.team4.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.team4.Path;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MapTabController implements Initializable {
    @FXML private WebView googleMapView;
    @FXML private TextField mapSearchField;
    @FXML private TextField mapTabLatitudeField;
    @FXML private TextField mapTabLongitudeField;

    private WebEngine webEngine;
    private Connection conn;

    public MapTabController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(Path.databaseConnection);
            initMap();
            conn.close();
        } catch (Exception ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            System.exit(0);
        }


    }

    private void initMap() throws SQLException {
        webEngine = googleMapView.getEngine();

        webEngine.load(getClass().getResource(Path.mapRsc).toExternalForm());
        showAllRoutes();
    }

    private void showAllRoutes() throws SQLException {
        ResultSet routesResultSet = conn.createStatement().executeQuery("SELECT SourceAirport, DestinationAirport FROM Routes");
        String airportCoordQuery = "SELECT Longitude, Latitude FROM Airport WHERE IATA = '%s'";
        int count = 0;
        while (routesResultSet.next() && count <= 400) {
            showOneRoute(routesResultSet, airportCoordQuery);
            count++;
        }
    }

    private void showOneRoute(ResultSet routesResultSet, String airportCoordQuery) throws SQLException {

        String sourceAirportIATA = routesResultSet.getString("SourceAirport");
        String destinationAirportIATA = routesResultSet.getString("DestinationAirport");
        ResultSet sourceAirportQuery = conn.createStatement().executeQuery(String.format(airportCoordQuery, sourceAirportIATA));
        ResultSet destAirportQuery = conn.createStatement().executeQuery(String.format(airportCoordQuery, destinationAirportIATA));

        if (sourceAirportQuery.next() && destAirportQuery.next()) {

            double sourceLatitude = (sourceAirportQuery.getDouble("Latitude"));
            double sourceLongitude = (sourceAirportQuery.getDouble("Longitude"));

            double destLatitude = (destAirportQuery.getDouble("Latitude"));
            double destLongitude = (destAirportQuery.getDouble("Longitude"));

            String routePoints = String.format("[{lat: %f, lng: %f}, {lat: %f, lng: %f}, ]",
                    sourceLatitude, sourceLongitude, destLatitude, destLongitude);

            String scriptToExecute = "addRoute(" + routePoints + ");";

            webEngine.getLoadWorker().stateProperty().addListener(
                    (ov, oldState, newState) -> {
                        if (newState == Worker.State.SUCCEEDED) {
                            webEngine.executeScript(scriptToExecute);
                        }
                    });

        }


    }


}
