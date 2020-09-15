package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.team4.Path;
import seng202.team4.model.*;

import java.sql.*;

/**
 * Performs logic for the 'Routes' tab of the application.
 * Responsible for connecting the route data provided/added to the JavaFX interface, this includes
 * initialising/updating the JavaFX TableView with data from the SQLite database table 'Routes' and also
 * initialising/updating the additive filtering and searching of said data. Checkboxes in this table are used to add
 * to a separate 'RoutesSelected' table and are used by both the Maps tab and the Emissions tab.
 *
 * Authors: Swapnil Bhagat, Kye Oldham, Darryl Alang, Griffin Baxter, Noah Irving
 * SENG202 Team 4
 * Description written on 15/09/2020
 */
public class RouteTabController extends DataController{

    public Route dataType = new Route();
    @FXML private TableView<Route> routeDataTable;
    @FXML private TableColumn<Route, String> routeTabAirlineColumn;
    @FXML private TableColumn<Route, String> routeTabDepartureAirportColumn;
    @FXML private TableColumn<Route, String> routeTabDestinationAirportColumn;
    @FXML private TableColumn<Route, Integer> routeTabNumStopsColumn;
    @FXML private TableColumn<Route, String> routeTabPlaneTypeColumn;

    @FXML private ComboBox<String> routeAirlineFilterCombobox;
    @FXML private ComboBox<String> routeDepartureFilterCombobox;
    @FXML private ComboBox<String> routeDestinationFilterCombobox;
    @FXML private Slider routeStopsFilterSlider;
    @FXML private Label stopsLabel;
    @FXML private ComboBox<String> routePlaneTypeFilterCombobox;

    @FXML private TextField routeSearchField;

    private ObservableList<Route> routes = FXCollections.observableArrayList();
    private ObservableList<String> airlineCodes = FXCollections.observableArrayList();
    private ObservableList<String> departureCountries = FXCollections.observableArrayList();
    private ObservableList<String> destinationCountries = FXCollections.observableArrayList();
    private ObservableList<String> planeTypes = FXCollections.observableArrayList();

    /**
     * Holds the high level logic (set of instructions) for initialisation.
     * Initialisation order: Sliders, Table Columns, Dataset Chooser ComboBox, Set Table
     */
    public void initialize() {
        try {
            initialiseColumns();
            setDataSetComboBox();
            initialiseSliders();
            setDataSetListener();
            setTable(); // Super class method which calls setTableData
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * Connect table columns to their respective Airport class attribute.
     * Also makes the checkbox column for selecting specific rows.
     */
    private void initialiseColumns() {
        routeTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCode"));
        routeTabDepartureAirportColumn.setCellValueFactory(new PropertyValueFactory<>("sourceAirportCode"));
        routeTabDestinationAirportColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAirportCode"));
        routeTabNumStopsColumn.setCellValueFactory(new PropertyValueFactory<>("numStops"));
        routeTabPlaneTypeColumn.setCellValueFactory(new PropertyValueFactory<>("planeTypeCode"));
        routeDataTable.setEditable(true);

        makeCheckboxColumn();
    }

    /**
     * Connect sliders to their respective columns; sliders' maximum values are their corresponding column's max values
     */
    private void initialiseSliders() {
        routeStopsFilterSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> stopsLabel.textProperty().setValue(
                String.valueOf(newValue.intValue())));

        getMaxStops();

    }

    /**
     * Makes the checkbox column for selecting specific rows of the Airport table.
     * Selected rows are then used by the Map tab to display chosen airports.
     */
    private void makeCheckboxColumn() {
        final TableColumn<Route, Boolean> routeTabSelectedRoute = new TableColumn<>("Select");
        routeDataTable.getColumns().addAll(routeTabSelectedRoute);
        routeTabSelectedRoute.setCellValueFactory(new PropertyValueFactory<>("select"));
        routeTabSelectedRoute.setCellFactory(CheckBoxTableCell.forTableColumn(routeTabSelectedRoute));
        routeTabSelectedRoute.setEditable(true);
        routeDataTable.setEditable(true);

        routeTabSelectedRoute.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            if (routes.get(param).isSelect()) {
                DataLoader.addToRoutesSelectedDatabase(routes.get(param));
            } else {
                DataLoader.removeFromRoutesSelectedDatabase(routes.get(param));
            }

            return routes.get(param).selectProperty();
        }));
    }

    /**
     * Sets the JavaFX Airport table with rows from the 'Airports' table from the database.
     * @param rs SQL ResultSet obtained from querying the Database Airport table and is used to set the rows
     *           of the JavaFX airport table by creating N Route objects from the query that results in N tuples.
     */
    @Override
    public void setTableData(ResultSet rs) {
        routes = FXCollections.observableArrayList();
        airlineCodes = FXCollections.observableArrayList();
        departureCountries = FXCollections.observableArrayList();
        destinationCountries = FXCollections.observableArrayList();
        planeTypes = FXCollections.observableArrayList();
        routes = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
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
                routes.add(route);

                addToComboBoxList(airlineCodes, airline);
                addToComboBoxList(departureCountries, sourceAirport);
                addToComboBoxList(destinationCountries, destinationAirport);
                addToComboBoxList(planeTypes, planeType);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        routeDataTable.setItems(routes);
        initialiseSliders();

    }

    /**
     *
     */
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


        // Add search bar filter
        FilteredList<Route> searchFilter = addSearchBarFilter(planeFilter);
        SortedList<Route> sortedRoute = new SortedList<>(searchFilter);
        sortedRoute.comparatorProperty().bind(routeDataTable.comparatorProperty());

        routeDataTable.setItems(sortedRoute);

    }



    private FilteredList<Route> addSearchBarFilter(FilteredList<Route> emissionsSliderFilter) {
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

    /**
     * Add filter filtered list.
     *
     * @param filteredList the filtered list
     * @param comboBox     the combo box
     * @param filter       the filter
     * @return the filtered list
     */
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

    /**
     * Gets max stops.
     */
    public void getMaxStops() {
        String query = "SELECT max(STOPS) FROM Route";
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet result;
        try {
            result = stmt.executeQuery(query);
            Integer maxStop = result.getInt("max(STOPS)");
            routeStopsFilterSlider.setMax(maxStop);
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        DatabaseManager.disconnect(c);
    }

    @Override
    public String getNewRecordFXML() { return Path.newRouteFXML; }

    @Override
    public DataType getDataType() {
        return new Route();
    }

    @Override
    public String getTableQuery() {
        return "SELECT * FROM Route";
    }


}
