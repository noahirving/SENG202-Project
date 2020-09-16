package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.team4.Path;
import seng202.team4.model.Airport;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Controller for the airport tab, 'airportTab.fxml'
 * extends the abstract DataController class
 */
public class AirportTabController extends DataController {

    public Airport dataType = new Airport();
    @FXML private TableView<Airport> airportDataTable;
    @FXML private TableColumn<Airport, String> airportTabAirportColumn;
    @FXML private TableColumn<Airport, String> airportTabCityColumn;
    @FXML private TableColumn<Airport, String> airportTabCountryColumn;
    @FXML private TableColumn<Airport, String> airportTabCoordinatesColumn;

    @FXML private ComboBox<String> airportTabCityCombobox;
    @FXML private ComboBox<String> airportTabCountryCombobox;
    @FXML private TextField airportSearchField;

    private SortedList<Airport> sortedAirport;
    private ObservableList<Airport> airports = FXCollections.observableArrayList();
    private ObservableList<String> countries = FXCollections.observableArrayList();
    private ObservableList<String> cities = FXCollections.observableArrayList();


    /**
     * Initializes the airport tab
     */
    @FXML
    public void initialize() {
        // Initialise columns to data type attributes
        airportTabAirportColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        airportTabCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        airportTabCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        airportTabCoordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));

        // Make and connect checkbox column to AirportsSelected database table
        makeCheckboxColumn();

        try {
            setDataSetComboBox();
            setDataSetListener();
            setTable(); // Super class method which calls setTableData
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * Adds an extra column, with a checkbox for each airport record, to the table
     * Whenever this checkbox is toggled, the selected airport will be either added
     * or removed from the 'AirportsSelected' database table. This feature enables
     * selected airports to be displayed on the map, on the 'Map' tab
     */
    private void makeCheckboxColumn() {
        final TableColumn<Airport, Boolean> airportTabSelectColumn = new TableColumn<>("Select");
        airportDataTable.getColumns().addAll(airportTabSelectColumn);
        airportTabSelectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));
        airportTabSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(airportTabSelectColumn));
        airportTabSelectColumn.setEditable(true);
        airportDataTable.setEditable(true);

        airportTabSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            if (sortedAirport.get(param).isSelect()) {
                DataLoader.addToAirportsSelectedDatabase(sortedAirport.get(param));
            } else {
                DataLoader.removeFromAirportsSelectedDatabase(sortedAirport.get(param));
            }

            return sortedAirport.get(param).selectProperty();
        }));
    }


    /**
     * Sets the table data by displaying each airport in the 'airport' database table
     * in the table view. This is done using the table query and assigning each record
     * to a row in the table
     * @param rs result of the table query
     * @throws Exception if the query fails
     */
    @Override
    public void setTableData(ResultSet rs) throws Exception {
        airports = FXCollections.observableArrayList();
        countries = FXCollections.observableArrayList();
        cities = FXCollections.observableArrayList();
        countries.clear(); cities.clear();
        countries.add(""); cities.add("");
        while (rs.next()) {
            Airport airport = new Airport();
            String airportName = rs.getString("Name");
            String airportCountry = rs.getString("Country");
            String airportCity = rs.getString("City");
            double airportLongitude = rs.getDouble("Longitude");
            double airportLatitude = rs.getDouble("Latitude");

            airport.setName(airportName);
            airport.setCountry(airportCountry);
            airport.setCity(airportCity);
            airport.setLongitude(airportLongitude);
            airport.setLatitude(airportLatitude);
            airport.setCoordinates(airportLongitude, airportLatitude);

            airports.add(airport);

            addToComboBoxList(countries, airportCountry);
            addToComboBoxList(cities, airportCity);

        }
        airportDataTable.setItems(airports);
    }

    /**
     * Required method from the abstract DataController class
     * initializes the combo boxes with all the possible values
     * for each column
     */
    @Override
    public void initialiseComboBoxes() {
        // Sort and set combobox items
        FXCollections.sort(countries); airportTabCountryCombobox.setItems(countries);
        FXCollections.sort(cities); airportTabCityCombobox.setItems(cities);

        // Make combobox searching autocomplete
        new AutoCompleteComboBoxListener<>(airportTabCityCombobox);
        new AutoCompleteComboBoxListener<>(airportTabCountryCombobox);

        filterData();

    }

    /**
     * Required method from the abstract DataController class
     * Connects the combo boxes and slider filters to the table
     * Updates the table with values accepted by the filters
     */
    @Override
    public void filterData() {
        // Connect combobox and slider filters to table
        FilteredList<Airport> countryFilter = addFilter(new FilteredList<>(airports, p -> true), airportTabCountryCombobox, "Country");
        FilteredList<Airport> cityFilter = addFilter(countryFilter, airportTabCityCombobox, "City");

        // Add search bar filter
        FilteredList<Airport> searchFilter = searchBarFilter(cityFilter);
        sortedAirport = new SortedList<>(searchFilter);
        sortedAirport.comparatorProperty().bind(airportDataTable.comparatorProperty());
        airportDataTable.setItems(sortedAirport);

    }

    /**
     * Required method from the abstract DataController class
     * Gets the fxml file for adding a new airport record
     * @return the path to the fxml file
     */
    @Override
    public String getNewRecordFXML() {
        return Path.newAirportFXML;
    }

    /**
     *
     * @param filteredList
     * @param comboBox
     * @param filter
     * @return
     */
    public FilteredList<Airport> addFilter(FilteredList<Airport> filteredList, ComboBox<String> comboBox, String filter) {
        FilteredList<Airport> newFilter = new FilteredList<>(filteredList, p -> true);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                newFilter.setPredicate(airport -> {
                    if (newValue == null) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (filter.equals("Country")) {
                        return airport.getCountry().toLowerCase().contains(lower);
                    } else if (filter.equals("City")) {
                        return airport.getCity().toLowerCase().contains(lower);
                    }
                    return false;
                }));
        return newFilter;
    }

    /**
     *
     * @param countryFilter
     * @return
     */
    private FilteredList<Airport> searchBarFilter(FilteredList<Airport> countryFilter) {
        FilteredList<Airport> searchFilter = new FilteredList<>(countryFilter, p -> true);
        airportSearchField.textProperty().addListener((observable, oldValue, newValue) ->
                searchFilter.setPredicate(airport -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (airport.getName().toLowerCase().contains(lower)) {
                        return true;
                    } else if (airport.getCountry().toLowerCase().contains(lower)) {
                        return true;
                    } else if (airport.getCountry().toLowerCase().contains(lower)) {
                        return true;
                    } else if (airport.getCity().toLowerCase().contains(lower)) {
                        return true;
                    } else if (Integer.toString(airport.getRouteNum()).toLowerCase().contains(lower)) {
                        return true;
                    }
                    return false;
                }));
        return searchFilter;
    }

    /**
     * Required method from the abstract DataController class
     * @return the dataType, the model 'Airport' class in this case
     */
    @Override
    public DataType getDataType() { return new Airport(); }

    /**
     * Required method from the abstract DataController class
     * @return the query for generating a results set of all airports from the
     * database that will populate the table view
     */
    @Override
    public String getTableQuery() { return "SELECT * FROM Airport"; }

}
