package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import seng202.team4.model.Airline;

import java.sql.*;


public class airlineTabController {
    @FXML
    private TableView<Airline> airlineDataTable;
    @FXML
    private TableColumn<Airline, String> airlineTabAirlineColumn;
    @FXML
    private TableColumn<Airline, String> airlineTabCountryColumn;

    private static Connection conn;

    @FXML
    public void initialize() {
        airlineTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<Airline, String>("airlineName"));
        airlineTabCountryColumn.setCellValueFactory(new PropertyValueFactory<Airline, String>("airlineCountry"));
        airlineDataTable.setItems(airlines);

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM AIRLINES");
            while (rs.next()) {
                Airline airline = new Airline();
                airline.setAirlineName(rs.getString("NAME"));
                airline.setAirlineCountry(rs.getString("COUNTRY"));
                airlines.add(airline);
            }
        } catch (Exception ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            System.exit(0);
        }

    }

    private ObservableList<Airline> airlines = FXCollections.observableArrayList(
            //new Airline("2,135 Airways,N,,GNL,GENERAL,United States,N"),
            //new Airline("1,Private flight,N,-,N/A,,,Y")
    );



}
