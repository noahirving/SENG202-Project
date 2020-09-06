package seng202.team4.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import seng202.team4.Path;
import seng202.team4.model.*;
import seng202.team4.controller.CheckBoxCell;

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
    @FXML private TableColumn<Route, Route> routeTabSelectedRoute;

    @FXML private ComboBox<String> routeAirlineFilterCombobox;
    @FXML private ComboBox<String> routeDepartureFilterCombobox;
    @FXML private ComboBox<String> routeDestinationFilterCombobox;
    @FXML private Slider routeStopsFilterSlider;
    @FXML private Label stopsLabel;
    @FXML private ComboBox<String> routePlaneTypeFilterCombobox;
    @FXML private Slider routeEmissionsFilterSlider;
    @FXML private Label emissionsLabel;
    @FXML private Button routeTabDistanceBtn;

    @FXML private TextField routeSearchField;

    private ObservableList<Route> routes = FXCollections.observableArrayList();
    private ObservableSet<Route> selectedRoutes = FXCollections.observableSet();
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

        makeCheckboxColumn();
//        routeTabSelectedRoute = new TableColumn<>("Select Route");
//        routeTabSelectedRoute.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Route,Route>, ObservableValue<Route>>() {
//            @Override
//            public ObservableValue<Route> call(TableColumn.CellDataFeatures<Route, Route> data) {
//                return new ReadOnlyObjectWrapper<>(data.getValue());
//            }
//        });
//        routeTabSelectedRoute.setCellFactory(new Callback<TableColumn<Route, Route>, TableCell<Route, Route>>() {
//            @Override
//            public TableCell<Route, Route> call(
//                    TableColumn<Route, Route> param) {
//                return new CheckBoxCell(selectedRoutes);
//            }
//        });
//        routeDataTable.getColumns().add(routeTabSelectedRoute);


        // Connect sliders to labels indicating their value
        routeStopsFilterSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> stopsLabel.textProperty().setValue(
                String.valueOf(newValue.intValue())));

        routeEmissionsFilterSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> emissionsLabel.textProperty().setValue(
                String.valueOf(newValue.intValue())));

        try {
            setDataSetComboBox();
            getMaxStops();
            setTable(); // Super class method which calls setTableData
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        routeTabDistanceBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Double distance;
                for (Route route : selectedRoutes) {
                    Integer index = routes.indexOf(route);
                    String sourceAirport = route.getSourceAirportCode();
                    String destAirport = route.getDestinationAirportCode();
                    try {
                        Connection con = DatabaseManager.connect();
                        distance = Calculations.calculateDistance(sourceAirport, destAirport, con);
                        route.setDistance(distance);
                        routes.get(index).setDistance(distance);
                        //System.out.println(routes.get(index).getDistance());
                        DatabaseManager.disconnect(con);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    System.out.println(route.getDistance());
                }
            }
        });


    }

    private void makeCheckboxColumn() {
        final TableColumn<Route, Boolean> routeTabSelectedRoute = new TableColumn<>("Select");
        routeDataTable.getColumns().addAll(routeTabSelectedRoute);
        routeTabSelectedRoute.setCellValueFactory(new PropertyValueFactory<>("select"));
        routeTabSelectedRoute.setCellFactory(CheckBoxTableCell.forTableColumn(routeTabSelectedRoute));
        routeTabSelectedRoute.setEditable(true);
        routeDataTable.setEditable(true);

        routeTabSelectedRoute.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            if (routes.get(param).isSelect()) {
                addToAirportsSelectedDatabase(routes.get(param));
            } else {
                removeFromAirportsSelectedDatabase(routes.get(param));
            }

            return routes.get(param).selectProperty();
        }));
    }

    private void addToAirportsSelectedDatabase(Route route) {
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        String between = "', '";

        Double distance = 0.0;
        String sourceAirport = route.getSourceAirportCode();
        String destAirport = route.getDestinationAirportCode();
        try {
            distance = Calculations.calculateDistance(sourceAirport, destAirport, con);
            route.setDistance(distance);
            //System.out.println(routes.get(index).getDistance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double carbonEmitted = Calculations.calculateEmissions(route);
        String query = "INSERT INTO RoutesSelected ('Airline', 'SourceAirport', 'DestinationAirport', 'Equipment', 'Distance', 'CarbonEmissions') "
                + "VALUES ('"
                + route.getAirlineCode().replaceAll("'", "''") + between
                + route.getSourceAirportCode().replaceAll("'", "''") + between
                + route.getDestinationAirportCode().replaceAll("'", "''") + between
                + route.getPlaneTypeCode().replaceAll("'", "''") + between
                + route.getDistance() + between
                + carbonEmitted
                + "');";
        try {
            stmt.executeUpdate(query);
            con.commit();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        DatabaseManager.disconnect(con);
    }

    private void removeFromAirportsSelectedDatabase(Route route) {
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        String between = "' and ";

        String query = "DELETE FROM RoutesSelected WHERE "
                + "Airline = '" + route.getAirlineCode().replaceAll("'", "''") + between
                + "SourceAirport = '" + route.getSourceAirportCode().replaceAll("'", "''") + between
                + "DestinationAirport = '" + route.getDestinationAirportCode().replaceAll("'", "''") + between
                + "Equipment = '" + route.getPlaneTypeCode().replaceAll("'", "''") + "'";
        try {
            stmt.executeUpdate(query);
            con.commit();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        DatabaseManager.disconnect(con);
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


}
