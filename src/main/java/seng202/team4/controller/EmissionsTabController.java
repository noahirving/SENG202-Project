package seng202.team4.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.math3.util.Precision;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.Path;
import seng202.team4.model.Route;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Performs logic for the 'Emissions' tab of the application
 * Responsible for connecting the selected route data to the JavaFX interface,
 * this includes displaying the selected routes in the JavaFX TableView with data
 * from the 'RoutesSelected' SQLite database table and also using a search filter
 * on this data. Analysis is peformed on this data, specifically the distance and
 * carbon emissions for each route is calculated. The suggested donation amount to
 * offset a users carbon footprint is calculated, along with how many trees they
 * could plant with this derived figure.
 */
public class EmissionsTabController extends DataController {

    /**
     * Mutable ObservableList containing Route objects
     */
    private ObservableList<Route> selectedRoutes;
    /**
     * Initialization of users total emissions
     */
    private Double sumEmissions = 0.0;
    /**
     * Initialization of users suggested dollar offset amount
     */
    private Double dollarOffset = 0.0;
    /**
     * Initializaion of the trees the user could plant with dollarOffset
     */
    private Double treeOffset = 0.0;

    /**
     * TableView of the selected routes raw data table
     */
    @FXML private TableView<Route> dataTable;
    /**
     * Airline code column of the raw data table
     */
    @FXML private TableColumn<Route, String> airlineColumn;
    /**
     * Departure airport IATA column of the raw data table
     */
    @FXML private TableColumn<Route, String> sourceColumn;
    /**
     * Destination airport IATA column of the raw data table
     */
    @FXML private TableColumn<Route, String> destinationColumn;
    /**
     * Plane type column of the raw data table
     */
    @FXML private TableColumn<Route, String> planeColumn;
    /**
     * Distance column of the raw data table
     */
    @FXML private TableColumn<Route, Integer> distanceColumn;
    /**
     * Carbon emissions (C02) column of the raw data table
     */
    @FXML private TableColumn<Route, Integer> emissionsColumn;
    /**
     * Text field used to search the data table
     */
    @FXML private TextField searchField;
    /**
     * Current emissions label displaying the users total carbon footprint for
     * all routes in the data table
     */
    @FXML private Label currentEmissionsValue;

    @FXML private Button contributionsGraphButton;

    /**
     *  On Action method for the 'Environmental Donation' button
     *  Gets the calculated donation amount and displays this
     *  in a new Alert window
     *
     * @param buttonPress ActionEvent triggered when button is clicked
     */
    @FXML
    public void pressEnvironmentalDonationButton(ActionEvent buttonPress) {
        String donationDollarsCents = String.format("%.2f", dollarOffset);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Environmental Donation Equivalent");
        alert.setHeaderText("Donation required to offset your emissions: $" + donationDollarsCents + " NZD");

        FlowPane fp = new FlowPane();
        Label explainText = new Label("Donations can be made at the carbonfund.org site");
        fp.getChildren().addAll(explainText);
        alert.getDialogPane().contentProperty().set( fp );
        alert.show();
    }

    /**
     * On Action method for the 'Trees Equivalent' button
     * Gets how many trees the user could plant with the donation amount
     * and displays this in a new Alert window
     * @param buttonPress AcitonEvent triggered when button is clicked
     */
    @FXML
    public void pressTreesEquivalentButton(ActionEvent buttonPress) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Trees Equivalent");
        alert.setHeaderText("With the suggested donation amount you could plant " + treeOffset + " trees");

        FlowPane treesFp = new FlowPane();
        Label explainText = new Label("Plant trees at the teamtrees.org site");
        treesFp.getChildren().addAll(explainText);
        alert.getDialogPane().contentProperty().set( treesFp );
        alert.show();
    }

    @FXML
    public void pressContributionsGraphButton(ActionEvent buttonPress) {
        showChart("Contributions");
    }

    /**
     * On Action method for the 'Alternative Routes' button
     * This feature will be implemented in a later release.
     * This will display alternative routes that complete the same
     * route, but cause less damage to the environment
     * @param buttonPress ActionEvent triggered when button is clicked
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
     * @param buttonPress ActionEvent triggered when the button is clicked
     */
    @FXML
    public void pressAlternativeAircraftTypeButton(ActionEvent buttonPress) {
        // To implement
    }

    /**
     * Holds the high level logic (set of instructions) for initialisation.
     * Initialisation order: Table Columns, Set Table
     */
    public void initialize() {
        //Connects table columns to their respective Route class attribute
        airlineColumn.setCellValueFactory(new PropertyValueFactory<>("airlineCode"));
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("sourceAirportCode"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationAirportCode"));
        planeColumn.setCellValueFactory(new PropertyValueFactory<>("planeTypeCode"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        emissionsColumn.setCellValueFactory(new PropertyValueFactory<>("carbonEmissions"));

        // Multiple rows can be selected
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            initialiseButtons();
            setTable();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Calculates the total carbon emissions for the selected routes in
     * the data table. The recommended donation to offset the emissions
     * figure is calculated using this figure.
     */
    private void setTotalEmissions() {
        sumEmissions = 0.0;
        for(Route route: selectedRoutes){
            sumEmissions += route.getCarbonEmissions();
        }
        dollarOffset = sumEmissions * 0.01479;
        treeOffset = Math.ceil(dollarOffset);
        //String emissionsLabel = Double.toString();
        currentEmissionsValue.setText(String.format("%.2f", sumEmissions) + "kg C02");
    }

    /**
     * On action method for the 'Load Selected Routes' button
     * updates the table to display the routes selected by the user
     * on the 'Routes' tab, throws an IOException if this fails
     * @param buttonPress ActionEvent triggered when the user clicks the buton
     * @throws IOException if the setTable() method call results in an exception
     */
    @FXML
    public void updateTable(ActionEvent buttonPress) throws IOException{
        try {
            //emissionsDataTable.setItems(selectedRoutes);
            setTable();
            setTotalEmissions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Required method from the abstract DataController class
     * @return DataType in this case it is of type 'Route'
     */
    @Override
    public DataType getDataType() {
        return new Route();
    }

    /**
     * Returns the JDBC/SQL query for selecting all distinct/unique rows from the 'RoutesSelected' table.
     * @return String for the  JDBC/SQL query for selecting all rows from the 'RoutesSelected' table.
     */
    @Override
    public String getTableQuery() {
        //Old query Select distinct Airline, SourceAirport, DestinationAirport, Equipment, distance, carbonEmissions from RoutesSelected
        return "Select distinct Airline, SourceAirport, DestinationAirport, Equipment, distance, carbonEmissions from RoutesSelected";
    }

    /**
     * Sets the JavaFX table with rows from the 'RoutesSelected' database table.
     * This is done using the table query and assigning each record to a row in the table
     * @param rs JDBC ResultSet obtained from querying the Database RoutesSelected table and is used to set the rows
     *           of the JavaFX data table by creating N Route objects from the query that results in N tuples.
     * @throws Exception if the query fails, throws an exception
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
            route.setDistance(Precision.round(rs.getDouble("distance"), 2));
            route.setCarbonEmissions(Precision.round(rs.getDouble("carbonEmissions"), 2));
            selectedRoutes.add(route);

        }
        dataTable.setItems(selectedRoutes);

    }

    /**
     * Filtering functionality for the Search field.
     * Filters the routes in the table view that have a value in
     * either of the: airlineCode, sourceAirportCode, destinationAirportCode or planeTypeCode
     * columns that matches the users search input
     * @return FilteredList of routes that match the input
     */
    private FilteredList<Route> searchBarFilter() {
        FilteredList<Route> searchFilter = new FilteredList<>(selectedRoutes, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
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

    /**
     * Required method from the abstract DataController class
     * applies the search filter to the data table view
     */
    @Override
    public void filterData() {
        FilteredList<Route> searchFilter = searchBarFilter();
        SortedList<Route> sortedRoute = new SortedList<>(searchFilter);
        sortedRoute.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedRoute);

    }

    /**
     * Required method from the abstract DataController class
     * @return String an empty record as no new records are added
     * to the emissions table
     */
    @Override
    public String getNewRecordFXML() {
        return null;
    }

    /**
     * Sets the images of buttons
     */
    void initialiseButtons() {
        javafx.scene.image.Image deleteRecordImage = new Image(getClass().getResourceAsStream(Path.DELETE_RECORD_BUTTON_PNG));
        deleteRecordButton.setGraphic(new ImageView(deleteRecordImage));
    }

    /**
     * Delete each row selected in the table view
     */
    @FXML
    @Override
    public void deleteRow() {
        ObservableList<Route> routes = dataTable.getSelectionModel().getSelectedItems();
        ArrayList<Route> rows = new ArrayList<>(routes);
        rows.forEach(row -> {
                    row.setSelect(false);
                    selectedRoutes.remove(row);
                    DataLoader.removeFromRoutesSelectedDatabase(row);
                }
        );
        setTotalEmissions();

    }

    @Override
    public void clearFilters() {

    }

    @Override
    public String getDetailsFXML() {
        return Path.EMISSIONS_DETAILS;
    }


    public void showChart(String chartType) {
        Stage stage = new Stage();
        stage.setTitle(chartType);
        stage.setMinHeight(440);
        stage.setMinWidth(720);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Path.CONTRIBUTIONS_GRAPH));
        try {
            stage.setScene(new Scene(loader.load(), 700, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        contributionsGraphController contributions = loader.getController();
        contributions.setUp(stage, selectedRoutes, dollarOffset, sumEmissions);
    }
}
