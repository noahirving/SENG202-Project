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

import java.io.File;
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
        Parent homeView = FXMLLoader.load(getClass().getResource(Path.homeSceneFXML));

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
        airportTabCoordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("coordinates"));

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(Path.database);
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

    //change country column in database
    public void getSQLData() throws Exception {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Airport");
        while (rs.next()) {
            Airport airport = new Airport();
            airport.setName(rs.getString("Name"));
            airport.setCountry(rs.getString("Country"));
            airport.setCity(rs.getString("City"));
            double latitude = Double.parseDouble(rs.getString("Latitude"));
            double longitude = Double.parseDouble(rs.getString("longitude"));
            airport.setCoordinates(latitude, longitude);
            airports.add(airport);
            if (!countries.contains(rs.getString("Country"))) {
                countries.add(rs.getString("Country"));
            }
            if (!cities.contains(rs.getString("City"))) {
                cities.add(rs.getString("City"));
            }
        }
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

    public void uploadData() throws IOException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
                ,new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File f = fc.showOpenDialog(null);
        if(f != null){
            /* Check data is valid format and then load into database */
            DataLoader loader = new DataLoader();
            loader.rawUserDataUploader(f, "AP");
        }
    }
}
