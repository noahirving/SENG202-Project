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
import seng202.team4.model.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;


public class airlineTabController extends DataController {
    public Airline dataType = new Airline();
    @FXML private TableView<Airline> airlineDataTable;
    @FXML private TableColumn<Airline, String> airlineTabAirlineColumn;
    @FXML private TableColumn<Airline, String> airlineTabCountryColumn;
    @FXML private ComboBox<String> airlineTabCountryCombobox;
    @FXML private TextField airlineSearchField;

    private Connection conn;
    private ObservableList<Airline> airlines = FXCollections.observableArrayList();
    private ObservableList<String> countries = FXCollections.observableArrayList();
    private FilteredList<Airline> countryFilter = new FilteredList<>(airlines, p -> true);
    private FilteredList<Airline> searchFilter = new FilteredList<>(countryFilter, p -> true);

    @FXML
    public void initialize() {
        airlineTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineName"));
        airlineTabCountryColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCountry"));

        try {
            setTable();
        } catch (Exception ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            System.exit(0);
        }

        airlineDataTable.setItems(airlines);

        airlineTabCountryCombobox.getSelectionModel().select("---");
        countries.add("---");
        FXCollections.sort(countries);
        airlineTabCountryCombobox.setItems(countries);
        new AutoCompleteComboBoxListener<>(airlineTabCountryCombobox);


    }

    @Override
    public String getTableQuery() {
        return "SELECT Name, Country FROM AIRLINES";
    }

    @Override
    public void setTableData(ResultSet rs) throws Exception{
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


    @Override
    public DataType getDataType() {
        return new Airline();
    }
}
