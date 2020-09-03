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


public class AirlineTabController extends DataController {

    @FXML private TableView<Airline> airlineDataTable;
    @FXML private TableColumn<Airline, String> airlineTabAirlineColumn;
    @FXML private TableColumn<Airline, String> airlineTabCountryColumn;
    @FXML private ComboBox<String> airlineTabCountryCombobox;
    @FXML private TextField airlineSearchField;

    private ObservableList<Airline> airlines = FXCollections.observableArrayList();
    private ObservableList<String> countries = FXCollections.observableArrayList();

    private FilteredList<Airline> countryFilter = new FilteredList<>(airlines, p -> true);
    private FilteredList<Airline> searchFilter = new FilteredList<>(countryFilter, p -> true);

    @FXML
    public void initialize() {
        airlineTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineName"));
        airlineTabCountryColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCountry"));

        try {
            setDataSetComboBox();
            setTable();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    @Override
    public void setTableData(ResultSet rs) throws Exception{
        airlines = FXCollections.observableArrayList();
        while (rs.next()) {
            Airline airline = new Airline();
            String airlineName = rs.getString("Name");
            String airlineCountry = rs.getString("Country");

            airline.setAirlineName(airlineName);
            airline.setAirlineCountry(airlineCountry);
            airlines.add(airline);

            addToComboBoxList(countries, airlineCountry);
        }
        airlineDataTable.setItems(airlines);
    }
    @Override
    public void initialiseComboBoxes() {
        // Sort and set combobox items
        FXCollections.sort(countries);
        airlineTabCountryCombobox.setItems(countries);

        // Make combobox searching autocomplete
        new AutoCompleteComboBoxListener<>(airlineTabCountryCombobox);

        filterData();

    }
    @Override
    public void filterData() {
        // Connect combobox and slider filters to table
        FilteredList<Airline> countryFilter = addFilter(new FilteredList<>(airlines, p -> true), airlineTabCountryCombobox, "Country");

        // Add search bar filter
        FilteredList<Airline> searchFilter = searchBarFilter(countryFilter);
        SortedList<Airline> sortedAirline = new SortedList<>(searchFilter);
        sortedAirline.comparatorProperty().bind(airlineDataTable.comparatorProperty());

        airlineDataTable.setItems(sortedAirline);

    }

    public FilteredList<Airline> addFilter(FilteredList<Airline> filteredList, ComboBox<String> comboBox, String filter) {
        FilteredList<Airline> newFilter = new FilteredList<>(filteredList, p -> true);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                newFilter.setPredicate(airline -> {
                    if (newValue == null) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (filter.equals("Country")) {
                        return airline.getAirlineCountry().toLowerCase().contains(lower);
                    }
                    return false;
                }));
        return newFilter;
    }

    private FilteredList<Airline> searchBarFilter(FilteredList<Airline> countryFilter) {
        FilteredList<Airline> searchFilter = new FilteredList<>(countryFilter, p -> true);
        airlineSearchField.textProperty().addListener((observable, oldValue, newValue) ->
                searchFilter.setPredicate(airline -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (airline.getAirlineName().toLowerCase().contains(lower)) {
                        return true;
                    } else if (airline.getAirlineCountry().toLowerCase().contains(lower)) {
                        return true;
                    }
                    return false;
                }));
        return searchFilter;
    }

    @Override
    public DataType getDataType() { return new Airline(); }

    @Override
    public String getTableQuery() {
        return "SELECT Name, Country FROM Airline";
    }
}
