package seng202.team4.controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import seng202.team4.Path;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.DatabaseManager;
import seng202.team4.model.Route;

import java.sql.*;
import java.util.ArrayList;

public class RouteTabController extends DataController{

    public Route dataType = new Route();
    @FXML private TableView<Route> routeDataTable;
    @FXML private TableColumn<Route, String> routeTabAirlineColumn;
    @FXML private TableColumn<Route, String> routeTabDepartureAirportColumn;
    @FXML private TableColumn<Route, String> routeTabDestinationAirportColumn;
    @FXML private TableColumn<Route, Integer> routeTabNumStopsColumn;
    @FXML private TableColumn<Route, String> routeTabPlaneTypeColumn;
    @FXML private TableColumn<Route, Integer> routeTabDistanceColumn;
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
    private ObservableList<Route> selectedRoutes = FXCollections.observableArrayList();
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
        routeTabDistanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        //routeTabSelectedRoute.setCellValueFactory(new PropertyValueFactory<>("select"));
        routeDataTable.setEditable(true);
        routeTabSelectedRoute.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Integer param) {
                ObservableValue<Boolean> current = routes.get(param).selectedProperty();
                //routes.get(param).setSelected(!routes.get(param).isSelected());
                selectedRoutes.add(routes.get(param));
                //System.out.println(selectedRoutes);
                return current;
            }
        }));
        // Connect sliders to labels indicating their value
        routeStopsFilterSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> stopsLabel.textProperty().setValue(
                String.valueOf(newValue.intValue())));

        routeEmissionsFilterSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> emissionsLabel.textProperty().setValue(
                String.valueOf(newValue.intValue())));

        try {
            getMaxStops();
            setTable(); // Super class method which calls setTableData
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    @Override
    public void setTableData(ResultSet rs) throws Exception {
        routes = FXCollections.observableArrayList();
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
            route.setDistance(0);
//            if(numStops == 0){
//                route.setSelect(check);
//            }
            //route.setSelect(check);
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

        filterData();

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
                emissionsSliderFilter.setPredicate((route -> (Double.parseDouble(newValue) == route.getDistance()))));

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

    public void carbonEmissions(){

    }

    public double calculateDistance(String airportCodeOne, String airportCodeTwo) throws SQLException {
        Double lat1;
        Double lat2;
        Double long1;
        Double long2;

        String query = "SELECT Latitude,Longitude from Airport WHERE IATA = '" + airportCodeOne + "' OR IATA = '" + airportCodeTwo + "'";
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        ResultSet result = stmt.executeQuery(query);
        ArrayList<Double> lats = new ArrayList<Double>();
        ArrayList<Double> longs = new ArrayList<Double>();
        while (result.next()){
            lats.add(result.getDouble("Latitude"));
            longs.add(result.getDouble("Longitude"));
        }
        stmt.close();
        //Get latitude and longitude of airports in route
        try{
            lat1 = Math.toRadians(lats.get(0));
            lat2 = Math.toRadians(lats.get(1));
            long1 = Math.toRadians(longs.get(0));
            long2 = Math.toRadians(longs.get(1));
        } catch(Exception e){
            return 0.0;
        }


        //Calculate distance between airports
        // Haversine formula
        double dlon = long2 - long1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));
        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;
        //return distance to be stored in DB
        DatabaseManager.disconnect(con);
        return(r * c);
    }

    public void getMaxStops() throws Exception {
        String query = "SELECT max(STOPS) FROM Route";
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet result = stmt.executeQuery(query);
        Integer maxStop = result.getInt("max(STOPS)");
        routeStopsFilterSlider.setMax(maxStop);
        stmt.close();
        DatabaseManager.disconnect(c);
    }

    @Override
    public DataType getDataType() {
        return new Route();
    }

    @Override
    public String getTableQuery() {
        return "SELECT * FROM Route";
    }


    public void carbonEmissions(ActionEvent actionEvent) {
        // To implement
    }
}
