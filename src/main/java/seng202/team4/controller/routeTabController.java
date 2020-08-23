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
import javafx.stage.Stage;
import seng202.team4.model.Airline;
import seng202.team4.model.Route;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class routeTabController {

    @FXML
    private TableView<Route> routeDataTable;
    @FXML
    private TableColumn<Route, String> routeTabAirlineColumn;
    @FXML
    private TableColumn<Route, String> routeTabDepartureAirportColumn;
    @FXML
    private TableColumn<Route, String> routeTabDestinationAirportColumn;
    @FXML
    private TableColumn<Route, Integer> routeTabNumStopsColumn;
    @FXML
    private TableColumn<Route, String> routeTabPlaneTypeColumn;
    @FXML
    private TableColumn<Route, Integer> routeTabCarbonEmissionsColumn;

    @FXML
    private ComboBox<String> routeAirlineFilterCombobox;
    @FXML
    private ComboBox<String> routeDepartureFilterCombobox;
    @FXML
    private ComboBox<String> routeDestinationFilterCombobox;
    @FXML
    private Slider routeStopsFilterSlider;
    @FXML
    private ComboBox<String> routePlaneTypeFilterCombobox;
    @FXML
    private Slider routeEmissionsFilterSlider;

    @FXML
    private TextField routeSearchField;

    private ObservableList<Route> routes = FXCollections.observableArrayList();

    private Connection conn;


    @FXML
    public void pressHomeButton(ActionEvent buttonPress) throws IOException {
        Parent homeView = FXMLLoader.load(getClass().getResource("/seng202.team4/homePage.fxml"));

        Scene homeScene = new Scene(homeView);

        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }

    public void initialize() {
        routeTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCode"));
        routeTabDepartureAirportColumn.setCellValueFactory(new PropertyValueFactory<>("sourceAirportCode"));
        routeTabDestinationAirportColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAirportCode"));
        routeTabNumStopsColumn.setCellValueFactory(new PropertyValueFactory<>("numStops"));
        routeTabPlaneTypeColumn.setCellValueFactory(new PropertyValueFactory<>("planeTypeCode"));
        routeTabCarbonEmissionsColumn.setCellValueFactory(new PropertyValueFactory<>("carbonEmissions"));

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
            buildData();
            conn.close();
        } catch (Exception ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            System.exit(0);
        }


    }

    public void buildData() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT Airline, SourceAirport, DestinationAirport, Stops, Equipment, CarbonEmissions FROM Routes");
        while (rs.next()) {
            Route route = new Route();
            route.setAirlineCode(rs.getString("Airline"));
            route.setSourceAirportCode(rs.getString("SourceAirport"));
            route.setDestinationAirportCode(rs.getString("DestinationAirport"));
            route.setNumStops(rs.getString("Stops"));
            route.setPlaneTypeCode(rs.getString("Equipment"));
            route.setCarbonEmissions(rs.getDouble("CarbonEmissions"));
            routes.add(route);

        }
        routeDataTable.setItems(routes);
    }


}
