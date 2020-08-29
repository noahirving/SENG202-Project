package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import seng202.team4.Path;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.DatabaseManager;
import seng202.team4.model.Route;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class routeTabController extends DataController{

    public Route dataType = new Route();
    @FXML private TableView<Route> routeDataTable;
    @FXML private TableColumn<Route, String> routeTabAirlineColumn;
    @FXML private TableColumn<Route, String> routeTabDepartureAirportColumn;
    @FXML private TableColumn<Route, String> routeTabDestinationAirportColumn;
    @FXML private TableColumn<Route, Integer> routeTabNumStopsColumn;
    @FXML private TableColumn<Route, String> routeTabPlaneTypeColumn;
    @FXML private TableColumn<Route, Integer> routeTabCarbonEmissionsColumn;

    @FXML private ComboBox<String> routeAirlineFilterCombobox;
    @FXML private ComboBox<String> routeDepartureFilterCombobox;
    @FXML private ComboBox<String> routeDestinationFilterCombobox;
    @FXML private Slider routeStopsFilterSlider;
    @FXML private Label stopsLabel;
    @FXML private ComboBox<String> routePlaneTypeFilterCombobox;
    @FXML private Slider routeEmissionsFilterSlider;
    @FXML private Label emissionsLabel;


    @FXML private TextField routeSearchField;

    private ObservableList<Route> routes = FXCollections.observableArrayList();
    private ObservableList<String> airlineCodes = FXCollections.observableArrayList();
    private ObservableList<String> departureCountries = FXCollections.observableArrayList();
    private ObservableList<String> destinationCountries = FXCollections.observableArrayList();
    private ObservableList<String> planeTypes = FXCollections.observableArrayList();

    private Connection conn;

    public void initialize() {
        routeTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCode"));
        routeTabDepartureAirportColumn.setCellValueFactory(new PropertyValueFactory<>("sourceAirportCode"));
        routeTabDestinationAirportColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAirportCode"));
        routeTabNumStopsColumn.setCellValueFactory(new PropertyValueFactory<>("numStops"));
        routeTabPlaneTypeColumn.setCellValueFactory(new PropertyValueFactory<>("planeTypeCode"));
        routeTabCarbonEmissionsColumn.setCellValueFactory(new PropertyValueFactory<>("carbonEmissions"));


        // Connect sliders to labels indicating their value
        routeStopsFilterSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> stopsLabel.textProperty().setValue(
                String.valueOf(newValue.intValue())));

        routeEmissionsFilterSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> emissionsLabel.textProperty().setValue(
                String.valueOf(newValue.intValue())));

        try {
            setTable();
            initialiseComboBoxes();
            filterData();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }


    }



    private void filterData() {

        airlineCodes.add("---");
        departureCountries.add("---");
        destinationCountries.add("---");
        planeTypes.add("---");

        // Connect combobox and slider filters to table
        FilteredList<Route> airlinesFilter = addFilter(new FilteredList<>(routes, p -> true), routeAirlineFilterCombobox, "Airline");
        FilteredList<Route> sourceFilter = addFilter(airlinesFilter, routeDepartureFilterCombobox, "Source");
        FilteredList<Route> stopSliderFilter = new FilteredList<>(sourceFilter, p -> true);
        stopsLabel.textProperty().addListener((observableValue, oldValue, newValue) ->
                stopSliderFilter.setPredicate((route -> (Integer.parseInt(newValue) == route.getNumStops()))));

        FilteredList<Route> destinationFilter = addFilter(stopSliderFilter, routeDestinationFilterCombobox, "Destination");
        FilteredList<Route> planeFilter = addFilter(destinationFilter, routePlaneTypeFilterCombobox, "Plane");

        FilteredList<Route> emissionsSliderFilter = new FilteredList<>(planeFilter, p -> true);
        emissionsLabel.textProperty().addListener((observableValue, oldValue, newValue) ->
                emissionsSliderFilter.setPredicate((route -> (Double.parseDouble(newValue) == route.getCarbonEmissions()))));

        // Add search bar filter
        FilteredList<Route> searchFilter = searchBarFilter(emissionsSliderFilter);
        SortedList<Route> sortedRoute = new SortedList<>(searchFilter);
        sortedRoute.comparatorProperty().bind(routeDataTable.comparatorProperty());

        routeDataTable.setItems(sortedRoute);

    }

    private void addToComboBoxes(String airline, String sourceAirport, String destinationAirport, String planeType) {
        if (!airlineCodes.contains(airline)) {
            airlineCodes.add(airline);
        }
        if (!departureCountries.contains(sourceAirport)) {
            departureCountries.add(sourceAirport);
        }
        if (!destinationCountries.contains(destinationAirport)) {
            destinationCountries.add(destinationAirport);
        }
        if (!planeTypes.contains(planeType)) {
            planeTypes.add(planeType);
        }


    }
    private void initialiseComboBoxes() {
        // Sort and set combobox items
        FXCollections.sort(airlineCodes); routeAirlineFilterCombobox.setItems(airlineCodes);
        FXCollections.sort(departureCountries); routeDepartureFilterCombobox.setItems(departureCountries);
        FXCollections.sort(destinationCountries); routeDestinationFilterCombobox.setItems(destinationCountries);
        FXCollections.sort(planeTypes); routePlaneTypeFilterCombobox.setItems(planeTypes);

        // Make combobox searching autocomplete
        new AutoCompleteComboBoxListener<>(routeAirlineFilterCombobox);
        new AutoCompleteComboBoxListener<>(routeDepartureFilterCombobox);
        new AutoCompleteComboBoxListener<>(routeDestinationFilterCombobox);
        new AutoCompleteComboBoxListener<>(routePlaneTypeFilterCombobox);
    }


    private FilteredList<Route> searchBarFilter(FilteredList<Route> emissionsSliderFilter) {
        FilteredList<Route> searchFilter = new FilteredList<>(emissionsSliderFilter, p -> true);
        routeSearchField.textProperty().addListener((observable, oldValue, newValue) ->
                searchFilter.setPredicate(route -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (route.getAirlineCode().toLowerCase().contains(lower)) {
                        return true;
                    } else if (route.getSourceAirportCode().toLowerCase().contains(lower)) {
                        return true;
                    } else if (route.getDestinationAirportCode().toLowerCase().contains(lower)){
                        return true;
                    } else {
                        return (route.getPlaneTypeCode().toLowerCase().contains(lower));
                    }
                }));
        return searchFilter;
    }

    public FilteredList<Route> addFilter(FilteredList<Route> filteredList, ComboBox<String> comboBox, String filter) {
        FilteredList<Route> newFilter = new FilteredList<>(filteredList, p -> true);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                newFilter.setPredicate(route -> {
                    if (newValue == null || newValue.equals("---")) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();

                    if (filter.equals("Airline")) {
                        return route.getAirlineCode().toLowerCase().contains(lower);
                    } else if (filter.equals("Source")) {
                        return route.getSourceAirportCode().toLowerCase().contains(lower);
                    } else if (filter.equals("Destination")) {
                        return route.getDestinationAirportCode().toLowerCase().contains(lower);
                    } else {
                        return route.getPlaneTypeCode().toLowerCase().contains(lower);
                    }

                }));
        return newFilter;
    }

    @Override
    public DataType getDataType() {
        return new Route();
    }

    @Override
    public String getTableQuery() {
        return "SELECT * FROM Routes";
    }

    @Override
    public void setTableData(ResultSet rs) throws Exception {
//        String airportNameQuery = "SELECT Name FROM Airport WHERE IATA = '%s'";
//        String airlineNameQuery = "SELECT Name FROM Airlines WHERE IATA = '%s'";
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        while (rs.next()) {
            Route route = new Route();
            String airline = rs.getString("Airline");
            String sourceAirport = rs.getString("SourceAirport");
            String destinationAirport = rs.getString("DestinationAirport");
            String planeType = rs.getString("Equipment");

            // Add names to airline and airport codes

            /*
            ResultSet airlineQuery = stmt.executeQuery(String.format(airlineNameQuery, airline));
            ResultSet sourceAirportQuery = stmt.executeQuery(String.format(airportNameQuery, sourceAirport));
            ResultSet destAirportQuery = stmt.executeQuery(String.format(airportNameQuery, destinationAirport));

            if (airlineQuery.next()) {
                airline += String.format(" (%s)", airlineQuery.getString("Name"));
            }
            if (sourceAirportQuery.next()) {
                sourceAirport += String.format(" (%s)", sourceAirportQuery.getString("Name"));
            }
            if (destAirportQuery.next()) {
                destinationAirport += String.format(" (%s)", destAirportQuery.getString("Name"));
            }
            airlineQuery.close();
            sourceAirportQuery.close();
            destAirportQuery.close();
             */

            route.setAirlineCode(airline);
            route.setSourceAirportCode(sourceAirport);
            route.setDestinationAirportCode(destinationAirport);
            route.setNumStops(rs.getInt("Stops"));
            route.setPlaneTypeCode(planeType);
            route.setCarbonEmissions(rs.getDouble("CarbonEmissions"));
            routes.add(route);

            addToComboBoxes(airline, sourceAirport, destinationAirport, planeType);

        }
        stmt.close();
        DatabaseManager.disconnect(c);

    }
}
