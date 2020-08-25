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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.team4.Path;
import seng202.team4.model.Airline;
import seng202.team4.model.DataLoader;

import java.io.File;
import java.io.IOException;
import java.sql.*;


public class airlineTabController {
    @FXML private TableView<Airline> airlineDataTable;
    @FXML private TableColumn<Airline, String> airlineTabAirlineColumn;
    @FXML private TableColumn<Airline, String> airlineTabCountryColumn;
    @FXML private ComboBox<String> airlineTabCountryCombobox;
    @FXML private TextField airlineSearchField;
    @FXML private Button airlineTabUploadDataBtn;

    private Connection conn;
    private ObservableList<Airline> airlines = FXCollections.observableArrayList();
    private ObservableList<String> countries = FXCollections.observableArrayList();
    private FilteredList<Airline> countryFilter = new FilteredList<>(airlines, p -> true);
    private FilteredList<Airline> searchFilter = new FilteredList<>(countryFilter, p -> true);

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
        airlineTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineName"));
        airlineTabCountryColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCountry"));

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(Path.database);
            getSQLData();
            conn.close();
        } catch (Exception ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            System.exit(0);
        }

        airlineDataTable.setItems(airlines);

        airlineTabCountryCombobox.getSelectionModel().select("---");
        countries.add("---");
        FXCollections.sort(countries);
        airlineTabCountryCombobox.setItems(countries);

    }

    public void getSQLData() throws Exception {
        ResultSet rs = conn.createStatement().executeQuery("SELECT Name, Country FROM AIRLINES");
        while (rs.next()) {
            Airline airline = new Airline();
            airline.setAirlineName(rs.getString("Name"));
            airline.setAirlineCountry(rs.getString("Country"));
            airlines.add(airline);
            if (!countries.contains(rs.getString("Country"))) {
                countries.add(rs.getString("Country"));
            }
        }
    }

    public void filterByCountry() {
        String newValue = airlineTabCountryCombobox.getValue();
        countryFilter.setPredicate(airline -> {
            if (newValue == null || newValue.equals("---")) {
                return true;
            }
            String lower = newValue.toLowerCase();
            return airline.getAirlineCountry().toLowerCase().contains(lower);
        });

        SortedList<Airline> sortedAirline = new SortedList<>(countryFilter);
        sortedAirline.comparatorProperty().bind(airlineDataTable.comparatorProperty());
        airlineDataTable.setItems(sortedAirline);
    }

    public void searchData() {
        String newValue = airlineSearchField.getText();
        searchFilter.setPredicate(airline -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lower = newValue.toLowerCase();
            if (airline.getAirlineName().toLowerCase().contains(lower)) {
                return true;
            } else {
                return airline.getAirlineCountry().toLowerCase().contains(lower);
            }
        });

        SortedList<Airline> sortedAirline = new SortedList<>(searchFilter);
        sortedAirline.comparatorProperty().bind(airlineDataTable.comparatorProperty());
        airlineDataTable.setItems(sortedAirline);
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
            loader.rawUserDataUploader(f, "AL");
        }
    }



}
