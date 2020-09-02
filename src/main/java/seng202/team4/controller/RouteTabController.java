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

import java.sql.*;

public class RouteTabController extends DataController{

    public Route dataType = new Route();
    @FXML private TableView<Route> routeDataTable;
    @FXML private TableColumn<Route, String> routeTabAirlineColumn;
    @FXML private TableColumn<Route, String> routeTabDepartureAirportColumn;
    @FXML private TableColumn<Route, String> routeTabDestinationAirportColumn;
    @FXML private TableColumn<Route, Integer> routeTabNumStopsColumn;
    @FXML private TableColumn<Route, String> routeTabPlaneTypeColumn;
    @FXML private TableColumn<Route, Integer> routeTabCarbonEmissionsColumn;
    @FXML private TableColumn<Route, Boolean> routeTabSelectedRoute;

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

    public void initialize() {
        // Initialise columns to data type attributes
        routeTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCode"));
        routeTabDepartureAirportColumn.setCellValueFactory(new PropertyValueFactory<>("sourceAirportCode"));
        routeTabDestinationAirportColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAirportCode"));
        routeTabNumStopsColumn.setCellValueFactory(new PropertyValueFactory<>("numStops"));
        routeTabPlaneTypeColumn.setCellValueFactory(new PropertyValueFactory<>("planeTypeCode"));
        routeTabCarbonEmissionsColumn.setCellValueFactory(new PropertyValueFactory<>("carbonEmissions"));
        routeTabSelectedRoute.setCellValueFactory(new PropertyValueFactory<>("select"));

        // Connect sliders to labels indicating their value
        routeStopsFilterSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> stopsLabel.textProperty().setValue(
                String.valueOf(newValue.intValue())));

        routeEmissionsFilterSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> emissionsLabel.textProperty().setValue(
                String.valueOf(newValue.intValue())));

        try {
            setTable(); // Super class method which calls setTableData
            initialiseComboBoxes();
            filterData();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    @Override
    public void setTableData(ResultSet rs) throws Exception {
        while (rs.next()) {
            CheckBox check = new CheckBox();
            Route route = new Route();
            String airline = rs.getString("Airline");
            String sourceAirport = rs.getString("SourceAirport");
            String destinationAirport = rs.getString("DestinationAirport");
            String planeType = rs.getString("Equipment");

            route.setAirlineCode(airline);
            route.setSourceAirportCode(sourceAirport);
            route.setDestinationAirportCode(destinationAirport);
            route.setNumStops(rs.getInt("Stops"));
            route.setPlaneTypeCode(planeType);
            route.setCarbonEmissions(rs.getDouble("CarbonEmissions"));
            route.setSelect(check);
            routes.add(route);

            addToComboBoxList(airlineCodes, airline);
            addToComboBoxList(departureCountries, sourceAirport);
            addToComboBoxList(destinationCountries, destinationAirport);
            addToComboBoxList(planeTypes, planeType);

        }
        routeDataTable.setItems(routes);

    }

    @Override
    public void initialiseComboBoxes() {
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

    @Override
    public void filterData() {
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
        return "SELECT * FROM Route";
    }


}
