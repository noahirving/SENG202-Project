package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.stage.Stage;
import seng202.team4.model.Airline;
import seng202.team4.model.Airport;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class airportTabController {
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
    public void pressHomeButton(ActionEvent buttonPress) throws IOException {
        Parent homeView = FXMLLoader.load(getClass().getResource("/seng202.team4/homePage.fxml"));

        Scene homeScene = new Scene(homeView);

        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }

    @FXML
    public void initialize() {
        airportTabAirportColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        airportTabCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        airportTabCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
       // airportTabCoordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
            getSQLData();
            conn.close();
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
    }

    public void getSQLData() throws Exception {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Airport");
        while (rs.next()) {
            Airport airport = new Airport();
            airport.setName(rs.getString("Name"));
            airport.setCountry(rs.getString("Country "));
            airport.setCity(rs.getString("City"));
            airports.add(airport);
            if (!countries.contains(rs.getString("Country "))) {
                countries.add(rs.getString("Country "));
            }
            if (!cities.contains(rs.getString("City"))) {
                cities.add(rs.getString("City"));
            }
        }
    }

    public void filterCity() {

    }

    public void filterCountry() {

    }

    public void searchData() {

    }
}
