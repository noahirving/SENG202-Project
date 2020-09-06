package seng202.team4.controller;

import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import seng202.team4.Path;
import seng202.team4.model.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;

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

    private ObservableList<Airport> airports = FXCollections.observableArrayList();
    private ObservableList<String> countries = FXCollections.observableArrayList();
    private ObservableList<String> cities = FXCollections.observableArrayList();


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
            setTable(); // Super class method which calls setTableData
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    private void makeCheckboxColumn() {
        final TableColumn<Airport, Boolean> airportTabSelectColumn = new TableColumn<>("Select");
        airportDataTable.getColumns().addAll(airportTabSelectColumn);
        airportTabSelectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));
        airportTabSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(airportTabSelectColumn));
        airportTabSelectColumn.setEditable(true);
        airportDataTable.setEditable(true);

        airportTabSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
            if (airports.get(param).isSelect()) {
                addToAirportsSelectedDatabase(airports.get(param));
            } else {
                removeFromAirportsSelectedDatabase(airports.get(param));
            }

            return airports.get(param).selectProperty();
        }));
    }

    private void addToAirportsSelectedDatabase(Airport airport) {
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        String between = "', '";

        String query = "INSERT INTO AirportsSelected ('Name', 'Longitude', 'Latitude') "
                + "VALUES ('"
                + airport.getName().replaceAll("'", "''") + between
                + airport.getLongitude() + between
                + airport.getLatitude()
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

    private void removeFromAirportsSelectedDatabase(Airport airport) {
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        String between = "' and ";

        String query = "DELETE FROM AirportsSelected WHERE "
                + "Name = '" + airport.getName() + between
                + "Longitude = '" + airport.getLongitude() + between
                + "Latitude = '" + airport.getLatitude() + "'";
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
        airports = FXCollections.observableArrayList();

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

    @Override
    public void filterData() {
        // Connect combobox and slider filters to table
        FilteredList<Airport> countryFilter = addFilter(new FilteredList<>(airports, p -> true), airportTabCountryCombobox, "Country");
        FilteredList<Airport> cityFilter = addFilter(countryFilter, airportTabCityCombobox, "City");

        // Add search bar filter
        FilteredList<Airport> searchFilter = searchBarFilter(cityFilter);
        SortedList<Airport> sortedRoute = new SortedList<>(searchFilter);
        sortedRoute.comparatorProperty().bind(airportDataTable.comparatorProperty());
        airportDataTable.setItems(sortedRoute);

    }

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

    @Override
    public DataType getDataType() { return new Airport(); }

    @Override
    public String getTableQuery() { return "SELECT * FROM Airport"; }

}
