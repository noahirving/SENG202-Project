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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.team4.Path;
import seng202.team4.model.DataType;
import seng202.team4.model.Route;

import java.io.IOException;
import java.sql.ResultSet;

/**
 * Controller for the emissions tab, 'emissionsTab.fxml'
 * extends the abstract DataController class
 */
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
    @FXML private TextField emissionsSearchField;
    @FXML private Label currentEmissionsValue;


    /**
     *  On Action method for the 'Environmental Donation' button
     *  Gets the calculated donation amount and displays this
     *  in a new Alert window
     *
     * @param buttonPress
     */
    @FXML
    public void pressEnvironmentalDonationButton(ActionEvent buttonPress) {
        String donationDollarsCents = String.format("%.2f", dollarOffset);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Environmental Donation Equivalent");
        alert.setHeaderText("Donation Equivalent: $" + donationDollarsCents);
        alert.setContentText("Donations for offsetting carbon emissions can be made on the following sites...");
        alert.show();
    }

    /**
     * On Action method for the 'Trees Equivalent' button
     * Gets how many trees the user could plant with the donation amount
     * and displays this in a new Alert window
     * @param buttonPress
     */
    @FXML
    public void pressTreesEquivalentButton(ActionEvent buttonPress) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Trees Equivalent");
        alert.setHeaderText("Trees Equivalent: " + treeOffset);
        alert.setContentText("Donations for planting trees can be made on the following sites...");
        alert.show();
    }

    /**
     * On Action method for the 'Alternative Routes' button
     * This feature will be implemented in a later release.
     * This will display alternative routes that complete the same
     * route, but cause less damage to the environment
     * @param buttonPress
     */
    @FXML
    public void pressAlternativeRoutesButton(ActionEvent buttonPress) {
        // To implement
    }

    /**
     * On Action method for the 'Alternative Aircraft' button
     * This feature will be implemented in a later release.
     * This will display alternative aircrafts to the aircraft types used
     * by the selected routes. The displayed aircraft will be more economically
     * efficient and cause less damage to the environment
     * @param buttonPress
     */
    @FXML
    public void pressAlternativeAircraftTypeButton(ActionEvent buttonPress) {
        // To implement
    }

    /**
     * Initializes the emissions tab
     */
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

    /**
     * Sets the total carbon emissions for the selected routes
     * The recommended donation to offset the emissions
     * figure is calculated aswell
     */
    private void setTotalEmissions() {
        sumEmissions = 0.0;
        for(Route route: selectedRoutes){
            sumEmissions += route.getCarbonEmissions();
        }
        dollarOffset = sumEmissions * 0.01;
        treeOffset = Math.ceil(dollarOffset);
        //String emissionsLabel = Double.toString();
        currentEmissionsValue.setText(String.format("%.2f", sumEmissions));
    }

    /**
     * On action method for the 'Load Selected Routes' button
     * updates the table to display the routes selected by the user
     * on the 'Routes' tab, throws an IOException if this fails
     * @param buttonPress
     * @throws IOException
     */
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

    /**
     * Required method from the abstract DataController class
     * @return the dataType, in this case it is of type 'Route'
     */
    @Override
    public DataType getDataType() {
        return new Route();
    }

    /**
     * Required method from the abstract DataController class
     * @return the query for generating a results set that will
     * populate the table view
     */
    @Override
    public String getTableQuery() {
        return "Select distinct Airline, SourceAirport, DestinationAirport, Equipment, distance, carbonEmissions from RoutesSelected";
    }

    /**
     * Sets the table data by displaying each route in the 'RoutesSelected' database table
     * in the table view. This is done using the table query and assigning each record
     * to a row in the table
     * @param rs result of the table query
     * @throws Exception if the query fails
     */
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

    /**
     * Filtering functionality for the Search field.
     * Filters the routes in the table view that have a value in
     * either of the: airlineCode, sourceAirportCode, destinationAirportCode or planeTypeCode
     * columns that matches the users search input
     * @return the FilteredList of routes that match the input
     */
    private FilteredList<Route> searchBarFilter() {
        FilteredList<Route> searchFilter = new FilteredList<>(selectedRoutes, p -> true);
        emissionsSearchField.textProperty().addListener((observable, oldValue, newValue) ->
                searchFilter.setPredicate(route -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();
                    if (route.getAirlineCode().toLowerCase().contains(lower)) {
                        return true;
                    } else if (route.getSourceAirportCode().toLowerCase().contains(lower)) {
                        return true;
                    } else if (route.getDestinationAirportCode().toLowerCase().contains(lower)){
                        return true;
                    } else {
                        return (route.getPlaneTypeCode().toLowerCase().contains(lower));
                    }
                }));
        return searchFilter;
    }

    /**
     * Required method from the abstract DataController class
     * calls the filterData method, to apply the filters selected
     * by the user
     */
    @Override
    public void initialiseComboBoxes() {
        filterData();
    }
    /*
    public FilteredList<Route> addFilter(FilteredList<Route> filteredList, ComboBox<String> comboBox, String filter) {
        FilteredList<Route> newFilter = new FilteredList<>(filteredList, p -> true);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                newFilter.setPredicate(route -> {
                    if (newValue == null || newValue.equals("---")) {
                        return true;
                    }
                    String lower = newValue.toLowerCase();

                    if (filter.equals("Airline")) {
                        return route.getAirlineCode().toLowerCase().contains(lower);
                    } else if (filter.equals("Source")) {
                        return route.getSourceAirportCode().toLowerCase().contains(lower);
                    } else if (filter.equals("Destination")) {
                        return route.getDestinationAirportCode().toLowerCase().contains(lower);
                    } else {
                        return route.getPlaneTypeCode().toLowerCase().contains(lower);
                    }

                }));
        return newFilter;
    }*/

    /** Required method from the abstract DataController class
     * applies the search filter to the data table view
     */
    @Override
    public void filterData() {
        FilteredList<Route> searchFilter = searchBarFilter();
        SortedList<Route> sortedRoute = new SortedList<>(searchFilter);
        sortedRoute.comparatorProperty().bind(emissionsDataTable.comparatorProperty());

        emissionsDataTable.setItems(sortedRoute);

    }

    /**
     * Required method from the abstract DataController class
     * @return an empty record, which will be assigned values
     * and added to the table?
     */
    @Override
    public String getNewRecordFXML() {
        return null;
    }

}
