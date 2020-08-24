package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team4.model.Route;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Filter;

public class routeTabController {

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
    @FXML private ComboBox<String> routePlaneTypeFilterCombobox;
    @FXML private Slider routeEmissionsFilterSlider;

    @FXML private TextField routeSearchField;

    private ObservableList<Route> routes = FXCollections.observableArrayList();
    private ObservableList<String> airlineCodes = FXCollections.observableArrayList();
    private ObservableList<String> departureCountries = FXCollections.observableArrayList();
    private ObservableList<String> destinationCountries = FXCollections.observableArrayList();
    private ObservableList<String> planeTypes = FXCollections.observableArrayList();

    private Connection conn;


    @FXML
    public void pressHomeButton(ActionEvent buttonPress) throws IOException {
        Parent homeView = FXMLLoader.load(getClass().getResource("/seng202.team4/homePage.fxml"));

        Scene homeScene = new Scene(homeView);

        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }

    public void initialize() {
        routeTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCode"));
        routeTabDepartureAirportColumn.setCellValueFactory(new PropertyValueFactory<>("sourceAirportCode"));
        routeTabDestinationAirportColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAirportCode"));
        routeTabNumStopsColumn.setCellValueFactory(new PropertyValueFactory<>("numStops"));
        routeTabPlaneTypeColumn.setCellValueFactory(new PropertyValueFactory<>("planeTypeCode"));
        routeTabCarbonEmissionsColumn.setCellValueFactory(new PropertyValueFactory<>("carbonEmissions"));

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/initialised_database.db");
            getSQLData();
            filterData();
            conn.close();
        } catch (Exception ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            System.exit(0);
        }


    }



    public void getSQLData() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Routes");
        while (rs.next()) {
            Route route = new Route();
            String airlineCode = rs.getString("Airline");
            String sourceAirport = rs.getString("SourceAirport");
            String destinationAirport = rs.getString("DestinationAirport");
            String planeType = rs.getString("Equipment");
            route.setAirlineCode(airlineCode);
            route.setSourceAirportCode(sourceAirport);
            route.setDestinationAirportCode(destinationAirport);
            route.setNumStops(rs.getString("Stops"));
            route.setPlaneTypeCode(planeType);
            route.setCarbonEmissions(rs.getDouble("CarbonEmissions"));
            routes.add(route);

            if (!airlineCodes.contains(airlineCode)) {
                airlineCodes.add(airlineCode);
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

        routeDataTable.setItems(routes);

    }

    private void filterData() {
        airlineCodes.add("---"); departureCountries.add("---"); destinationCountries.add("---"); planeTypes.add("---");
        FXCollections.sort(airlineCodes); FXCollections.sort(departureCountries);
        FXCollections.sort(destinationCountries); FXCollections.sort(planeTypes);

        routeAirlineFilterCombobox.setItems(airlineCodes);
        routeDepartureFilterCombobox.setItems(departureCountries);
        routeDestinationFilterCombobox.setItems(destinationCountries);
        routePlaneTypeFilterCombobox.setItems(planeTypes);

        FilteredList<Route> airlinesFilter = addFilter(new FilteredList<>(routes, p -> true), routeAirlineFilterCombobox, "Airline");
        FilteredList<Route> sourceFilter = addFilter(airlinesFilter, routeDepartureFilterCombobox, "Source");
        FilteredList<Route> destinationFilter = addFilter(sourceFilter, routeDestinationFilterCombobox, "Destination");
        FilteredList<Route> planeFilter = addFilter(destinationFilter, routePlaneTypeFilterCombobox, "Plane");

        // Add search bar filter
        FilteredList<Route> searchFilter = new FilteredList<>(planeFilter, p -> true);
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
        SortedList<Route> sortedRoute = new SortedList<>(searchFilter);
        sortedRoute.comparatorProperty().bind(routeDataTable.comparatorProperty());

        routeDataTable.setItems(sortedRoute);

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


}
