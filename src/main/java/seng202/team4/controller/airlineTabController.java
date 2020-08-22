package seng202.team4.controller;

import javafx.application.Platform;
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
import javafx.stage.Stage;
import seng202.team4.model.Airline;

import java.io.IOException;
import java.sql.*;


public class airlineTabController {
    @FXML
    private TableView<Airline> airlineDataTable;
    @FXML
    private TableColumn<Airline, String> airlineTabAirlineColumn;
    @FXML
    private TableColumn<Airline, String> airlineTabCountryColumn;
    @FXML
    private ComboBox<String> airlineTabCountryCombobox;
    @FXML
    private TextField airlineSearchField;

    private ObservableList<Airline> airlines = FXCollections.observableArrayList();
    private ObservableList<String> countries = FXCollections.observableArrayList();

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
        airlineTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineName"));
        airlineTabCountryColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCountry"));

        airlineTabCountryCombobox.getSelectionModel().select("---");

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
            ResultSet rs = conn.createStatement().executeQuery("SELECT NAME, COUNTRY FROM AIRLINES");
            while (rs.next()) {
                Airline airline = new Airline();
                airline.setAirlineName(rs.getString("NAME"));
                airline.setAirlineCountry(rs.getString("COUNTRY"));
                airlines.add(airline);
                if (!countries.contains(rs.getString("COUNTRY"))) {
                    countries.add(rs.getString("COUNTRY"));
                }
            }
            conn.close();
        } catch (Exception ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            System.exit(0);
        }

        airlineDataTable.setItems(airlines);
        countries.add("---");
        FXCollections.sort(countries);
        airlineTabCountryCombobox.setItems(countries);

        FilteredList<Airline> countryFilter = new FilteredList<>(airlines, p -> true);
        airlineTabCountryCombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                countryFilter.setPredicate(airline -> {
                    if (newValue == null || newValue.equals("---")) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    return airline.getAirlineCountry().toLowerCase().contains(lower);
                }));

        FilteredList<Airline> searchFilter = new FilteredList<>(countryFilter, p -> true);
        airlineSearchField.textProperty().addListener((observable, oldValue, newValue) ->
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
            }));

        SortedList<Airline> sortedAirline = new SortedList<>(searchFilter);
        sortedAirline.comparatorProperty().bind(airlineDataTable.comparatorProperty());

        airlineDataTable.setItems(sortedAirline);
    }




}
