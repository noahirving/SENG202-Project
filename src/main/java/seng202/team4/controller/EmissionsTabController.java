package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team4.Path;
import seng202.team4.model.DataType;
import seng202.team4.model.Route;

import java.io.IOException;
import java.sql.ResultSet;

public class EmissionsTabController extends DataController {
    public Route dataType = new Route();

    private ObservableList<Route> selectedRoutes;
    private Double sumEmissions = 0.0;
    private Double dollarOffset = 0.0;
    private Double treeOffset = 0.0;

    @FXML TableView<Route> emissionsDataTable;
    @FXML private TableColumn<Route, String> emissionsTabAirlineColumn;
    @FXML private TableColumn<Route, String> emissionsTabSourceColumn;
    @FXML private TableColumn<Route, String> emissionsTabDestinationColumn;
    @FXML private TableColumn<Route, String> emissionsTabPlaneColumn;
    @FXML private TableColumn<Route, Integer> emissionsTabDistanceColumn;
    @FXML private TableColumn<Route, Integer> emissionsTabEmissionsColumn;
    @FXML private Button emissionsTabLoadRoutesBtn;
    @FXML private Label currentEmissionsValue;


    @FXML
    public void pressHomeButton(ActionEvent buttonPress) throws IOException {
        Parent homeView = FXMLLoader.load(getClass().getResource(Path.homeSceneFXML));

        Scene homeScene = new Scene(homeView);

        Stage window = (Stage)((Node) buttonPress.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }

    @FXML
    public void pressEnvironmentalDonationButton(ActionEvent buttonPress) {
        String donationDollarsCents = String.format("%.2f", dollarOffset);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Environmental Donation Equivalent");
        alert.setHeaderText("Donation Equivalent: $" + donationDollarsCents);
        alert.setContentText("Donations for offsetting carbon emissions can be made on the following sites...");
        alert.show();
    }

    @FXML
    public void pressTreesEquivalentButton(ActionEvent buttonPress) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Trees Equivalent");
        alert.setHeaderText("Trees Equivalent: " + treeOffset);
        alert.setContentText("Donations for planting trees can be made on the following sites...");
        alert.show();
    }

    @FXML
    public void pressAlternativeRoutesButton(ActionEvent buttonPress) {
        // To implement
    }

    @FXML
    public void pressAlternativeAircraftTypeButton(ActionEvent buttonPress) {
        // To implement
    }

    public void initialize() {
        emissionsTabAirlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCode"));
        emissionsTabSourceColumn.setCellValueFactory(new PropertyValueFactory<>("sourceAirportCode"));
        emissionsTabDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAirportCode"));
        emissionsTabPlaneColumn.setCellValueFactory(new PropertyValueFactory<>("planeTypeCode"));
        emissionsTabDistanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        emissionsTabEmissionsColumn.setCellValueFactory(new PropertyValueFactory<>("carbonEmissions"));

        try {
            setTable();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void setTotalEmissions() {
        for(Route route: selectedRoutes){
            sumEmissions += route.getCarbonEmissions();
        }
        dollarOffset = sumEmissions * 0.01;
        treeOffset = Math.ceil(dollarOffset);
        //String emissionsLabel = Double.toString();
        currentEmissionsValue.setText(String.format("%.2f", sumEmissions));
    }

    @FXML
    public void updateTable(ActionEvent buttonPress) throws IOException{
        try {
            //emissionsDataTable.setItems(selectedRoutes);
            setTable();
            sumEmissions = 0.0;
            setTotalEmissions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public DataType getDataType() {
        return new Route();
    }

    @Override
    public String getTableQuery() {
        return "Select distinct Airline, SourceAirport, DestinationAirport, Equipment, distance, carbonEmissions from RoutesSelected";
    }

    @Override
    public void setTableData(ResultSet rs) throws Exception {
        selectedRoutes = FXCollections.observableArrayList();
        while (rs.next()) {
            Route route = new Route();
            String airline = rs.getString("Airline");
            String sourceAirport = rs.getString("SourceAirport");
            String destinationAirport = rs.getString("DestinationAirport");
            String planeType = rs.getString("Equipment");

            route.setAirlineCode(airline);
            route.setSourceAirportCode(sourceAirport);
            route.setDestinationAirportCode(destinationAirport);
            route.setPlaneTypeCode(planeType);
            route.setDistance(rs.getDouble("distance"));
            route.setCarbonEmissions(rs.getDouble("carbonEmissions"));
            selectedRoutes.add(route);

        }
        emissionsDataTable.setItems(selectedRoutes);

    }

    @Override
    public void initialiseComboBoxes() {

    }

    @Override
    public void filterData() {

    }

}
