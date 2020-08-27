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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.team4.Path;
import seng202.team4.model.Airport;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.Route;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class airportTabController extends DataController {

    public Airport dataType = new Airport();
    @FXML private TableView<Airport> airportDataTable;
    @FXML private TableColumn<Airport, String> airportTabAirportColumn;
    @FXML private TableColumn<Airport, String> airportTabCityColumn;
    @FXML private TableColumn<Airport, String> airportTabCountryColumn;
    @FXML private TableColumn<Airport, String> airportTabCoordinatesColumn;

    @FXML private ComboBox<String> airportTabCityCombobox;
    @FXML private ComboBox<String> airportTabCountryCombobox;
    @FXML private TextField airportSearchField;

    private Connection conn;
    private ObservableList<Airport> airports = FXCollections.observableArrayList();
    private ObservableList<String> countries = FXCollections.observableArrayList();
    private ObservableList<String> cities = FXCollections.observableArrayList();

    private FilteredList<Airport> countryFilter = new FilteredList<>(airports, p -> true);
    private FilteredList<Airport> cityFilter = new FilteredList<>(countryFilter, p -> true);
    private FilteredList<Airport> searchFilter = new FilteredList<>(cityFilter, p -> true);

    @FXML
    public void initialize() {
        airportTabAirportColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        airportTabCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        airportTabCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        airportTabCoordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));

        try {
            setTable();
        } catch (Exception ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            System.exit(0);
        }

        airportDataTable.setItems(airports);
        initCombobox();

    }

    public void initCombobox() {
        airportTabCityCombobox.getSelectionModel().select("---");
        cities.add("---");
        FXCollections.sort(cities);
        airportTabCityCombobox.setItems(cities);
        airportTabCountryCombobox.getSelectionModel().select("---");
        countries.add("---");
        FXCollections.sort(countries);
        airportTabCountryCombobox.setItems(countries);

        new AutoCompleteComboBoxListener<>(airportTabCityCombobox);
        new AutoCompleteComboBoxListener<>(airportTabCountryCombobox);

    }

    public void filterByCity() {
        String newValue = airportTabCityCombobox.getValue();
        cityFilter.setPredicate(airline -> {
            if (newValue == null || newValue.equals("---")) {
                return true;
            }
            String lower = newValue.toLowerCase();
            return airline.getCity().toLowerCase().contains(lower);
        });
        bindFilter(cityFilter);
    }

    public void filterCity() {
        ObservableList<String> cityInCountry = FXCollections.observableArrayList();
        cityInCountry.add("---");
        airportTabCityCombobox.getSelectionModel().select("---");
        for (Airport airport : airportDataTable.getItems()) {
            String newCity = airportTabCityColumn.getCellObservableValue(airport).getValue();
            if (!cityInCountry.contains(newCity)) {
                cityInCountry.add(newCity);
            }
        }
        FXCollections.sort(cityInCountry);
        airportTabCityCombobox.setItems(cityInCountry);
    }

    public void filterByCountry() {
        String newValue = airportTabCountryCombobox.getValue();
        countryFilter.setPredicate(airline -> {
            if (newValue == null || newValue.equals("---")) {
                return true;
            }
            String lower = newValue.toLowerCase();
            return airline.getCountry().toLowerCase().contains(lower);
        });
        bindFilter(countryFilter);
        filterCity();
    }

    public void searchData() {
        String newValue = airportSearchField.getText();
        searchFilter.setPredicate(airline -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lower = newValue.toLowerCase();
            if (airline.getName().toLowerCase().contains(lower)) {
                return true;
            } else if (airline.getCountry().toLowerCase().contains(lower)) {
                return true;
            } else {
                return airline.getCity().toLowerCase().contains(lower);
            }
        });
        bindFilter(searchFilter);
    }

    public void bindFilter(FilteredList<Airport> filteredList) {
        SortedList<Airport> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(airportDataTable.comparatorProperty());
        airportDataTable.setItems(sortedList);
    }


    @Override
    public DataType getDataType() {
        return new Airport();
    }

    @Override
    public String getTableQuery() {
        return "SELECT * FROM Airport";
    }

    @Override
    public void setTableData(ResultSet rs) throws Exception {
        while (rs.next()) {
            Airport airport = new Airport();
            airport.setName(rs.getString("Name"));
            airport.setCountry(rs.getString("Country"));
            airport.setCity(rs.getString("City"));
            airports.add(airport);
            if (!countries.contains(rs.getString("Country"))) {
                countries.add(rs.getString("Country"));
            }
            if (!cities.contains(rs.getString("City"))) {
                cities.add(rs.getString("City"));
            }
        }
    }
}
