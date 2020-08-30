package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seng202.team4.Path;
import seng202.team4.model.DatabaseManager;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MapTabController implements Initializable {
    @FXML private WebView googleMapView;
    @FXML private RadioButton routeFilterRadio;
    @FXML private RadioButton airportFilterRadio;
    @FXML private RadioButton airlineFilterRadio;
    @FXML private ToggleGroup filterSelection;
    @FXML private GridPane routeFilterGrid;
    @FXML private GridPane airportAirlineFilterGrid;

    @FXML private ComboBox routeAirlineFilterCombobox;
    @FXML private ComboBox routeAirportFilterCombobox;
    @FXML private ComboBox routePlaneTypeFilterCombobox;

    @FXML private ComboBox airportCountryFilterCombobox;

    private ObservableList<String> airlineCodes = FXCollections.observableArrayList();
    private ObservableList<String> departureCountries = FXCollections.observableArrayList();
    private ObservableList<String> planeTypes = FXCollections.observableArrayList();

    private ObservableList<String> airportCountries = FXCollections.observableArrayList();
    private ObservableList<String> airlineCountries = FXCollections.observableArrayList();

    private static final int ROUTELIMIT = 400;



    private WebEngine webEngine;
    private static String airportCoordQuery = "SELECT Longitude, Latitude, Name FROM Airport WHERE IATA = '%s'";

    public MapTabController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            initMap();
            initialiseRadioButtons();
            initialiseComboboxes();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void initialiseRadioButtons() {
        filterSelection.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == routeFilterRadio) {
                displayRouteFilters();
            } else {
                displayAirportAirlineFilters();
            }

        });
    }

    private void initialiseComboboxes() throws SQLException {
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        initialiseRouteComboboxes(stmt);

        ResultSet airportResultSet = stmt.executeQuery("SELECT Country FROM Airport");
        while (airportResultSet.next()) {
            String country = airportResultSet.getString("Country");
            if (!airportCountries.contains(country)) {
                airportCountries.add(country);
            }
        }
        ResultSet airlineResultSet = stmt.executeQuery("SELECT Country FROM Airlines");
        while (airlineResultSet.next()) {
            String country = airlineResultSet.getString("Country");
            if (!airlineCountries.contains(country)) {
                airlineCountries.add(country);
            }
        }
        FXCollections.sort(airportCountries); airportCountryFilterCombobox.setItems(airportCountries);
        new AutoCompleteComboBoxListener<>(airportCountryFilterCombobox);


    }

    private void initialiseRouteComboboxes(Statement stmt) throws SQLException {
        ResultSet routesResultSet = stmt.executeQuery("SELECT Airline, SourceAirport, Equipment FROM Routes");
        while (routesResultSet.next()) {
            String airline = routesResultSet.getString("Airline");
            String sourceAirport = routesResultSet.getString("SourceAirport");
            String planeType = routesResultSet.getString("Equipment");

            if (!airlineCodes.contains(airline)) {
                airlineCodes.add(airline);
            }
            if (!departureCountries.contains(sourceAirport)) {
                departureCountries.add(sourceAirport);
            }
            if (!planeTypes.contains(planeType)) {
                planeTypes.add(planeType);
            }
        }
        FXCollections.sort(airlineCodes); routeAirlineFilterCombobox.setItems(airlineCodes);
        FXCollections.sort(departureCountries); routeAirportFilterCombobox.setItems(departureCountries);
        FXCollections.sort(planeTypes); routePlaneTypeFilterCombobox.setItems(planeTypes);
        // Make combobox searching autocomplete
        new AutoCompleteComboBoxListener<>(routeAirlineFilterCombobox);
        new AutoCompleteComboBoxListener<>(routeAirportFilterCombobox);
        new AutoCompleteComboBoxListener<>(routePlaneTypeFilterCombobox);
    }


    private void displayRouteFilters() {
        routeFilterGrid.setVisible(true);
        airportAirlineFilterGrid.setVisible(false);
    }

    private void displayAirportAirlineFilters() {
        routeFilterGrid.setVisible(false);
        airportAirlineFilterGrid.setVisible(true);
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
        while (routesResultSet.next() && count <= ROUTELIMIT) {
            showOneRoute(routesResultSet);
            count++;
        }
        stmt.close();
        DatabaseManager.disconnect(c);
    }

    private void showOneRoute(ResultSet routesResultSet) throws SQLException {
        Connection c = DatabaseManager.connect();

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

            sourceName = sourceName.replaceAll("'", "");
            destName = destName.replaceAll("'", "");

            String scriptToExecute = "addRoute(" + routePoints + ", '" + sourceName + "', '" + destName + "');";

            executeScript(scriptToExecute);

        }

    }

    @FXML
    public void showSelectedAirports() throws SQLException {
        resetMap();
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet airportResultSet = stmt.executeQuery("SELECT Longitude, Latitude, Name FROM Airport");

        while (airportResultSet.next()) {
            addOneAirport(airportResultSet);
        }
        stmt.close();
        DatabaseManager.disconnect(c);

    }

    private void addOneAirport(ResultSet airportResultSet) throws SQLException {
        double sourceLatitude = (airportResultSet.getDouble("Latitude"));
        double sourceLongitude = (airportResultSet.getDouble("Longitude"));
        String sourceName = (airportResultSet.getString("Name"));
        String airportPoint = String.format("{lat: %f, lng: %f}", sourceLatitude, sourceLongitude);
        String scriptToExecute = "addAirport(" + airportPoint + ", \"" + sourceName + "\");";
        executeScript(scriptToExecute);
    }

    @FXML
    private void clearMap() {
        resetMap();
    }

    @FXML
    private void routeApplyFilter() throws SQLException {
        resetMap();
        String airline = (String) routeAirlineFilterCombobox.getValue();
        String airport = (String) routeAirportFilterCombobox.getValue();
        String planeType = (String) routePlaneTypeFilterCombobox.getValue();

        /* If airline/airport/planeType == "null" this mean that no value is entered in the combobox(es) so
        it means 'all' of that type are chosen, so in SQL this means 'is not null' for getting every value.
         */
        airline = checkValidInput(airline);
        airport = checkValidInput(airport);

        planeType = checkValidInput(planeType);

        String query = String.format("SELECT SourceAirport, DestinationAirport FROM Routes WHERE Airline is %s and SourceAirport is %s and Equipment is %s",
                airline, airport, planeType);
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet filteredResultSet = stmt.executeQuery(query);
        int count = 0;
        while (filteredResultSet.next() && count < ROUTELIMIT) {
            showOneRoute(filteredResultSet);
            count++;
        }

    }

    @FXML
    private void airportApplyFilter() throws SQLException {
        resetMap();
        String country = (String) airportCountryFilterCombobox.getValue();
        country = checkValidInput(country);
        String query = String.format("SELECT Longitude, Latitude, Name FROM Airport WHERE Country is %s", country);
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet airportResultSet = stmt.executeQuery(query);

        while (airportResultSet.next()) {
            addOneAirport(airportResultSet);
        }
    }


    private String checkValidInput(String inputString) {
        if (inputString == null || inputString.length() == 0) {
            return "not null";
        } else {
            return String.format("'%s'", inputString);
        }
    }

    private void resetMap() {
        String scriptToExecute = "resetMap();";
        executeScript(scriptToExecute);
    }

    public void executeScript(String scriptToExecute) {
        webEngine.executeScript(scriptToExecute);

    }



}
