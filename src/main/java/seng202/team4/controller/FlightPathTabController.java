package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng202.team4.Path;
import seng202.team4.model.Airline;
import seng202.team4.model.DataType;
import seng202.team4.model.FlightPath;

import java.sql.ResultSet;

/**
 * Performs logic for the 'Flight Path' tab of the application
 * Responsible for connecting the flight path data to the JavaFX interface,
 * this includes displaying the coordinates and altitude in the JavaFX TableView with data
 * from the 'FlightPath' SQLite database table and also initialising/updating the
 * additive filtering and searching of said data.
 */
public class FlightPathTabController extends DataController {

    /**
     * TableView of the flight path raw data table.
     */
    @FXML private TableView<FlightPath> dataTable;
    /**
     * Type column of the raw data table.
     */
    @FXML private TableColumn<FlightPath, String> typeColumn;
    /**
     * ID column of the raw data table.
     */
    @FXML private TableColumn<FlightPath, String> idColumn;
    /**
     * Altitude column of the raw data table.
     */
    @FXML private TableColumn<FlightPath, Integer> altitudeColumn;
    /**
     * Latitude column of the raw data table.
     */
    @FXML private TableColumn<FlightPath, Double> latitudeColumn;
    /**
     * Longitude column of the raw data table.
     */
    @FXML private TableColumn<FlightPath, Double> longitudeColumn;
    /**
     * Text field used to search data table.
     */
    @FXML private TextField searchField;
    /**
     * Button that opens window to add new record.
     */
    @FXML private Button newRecordButton;
    /**
     * Mutable ObservableLst containing a list of types.
     */
    private ObservableList<Airline> types = FXCollections.observableArrayList();
    /**
     * Mutable ObservableLst containing a list of IDs.
     */
    private ObservableList<Airline> ids = FXCollections.observableArrayList();
    /**
     * Mutable ObservableLst containing a list of altitudes.
     */
    private ObservableList<Airline> altitudes = FXCollections.observableArrayList();
    /**
     * Mutable ObservableLst containing a list of latitudes.
     */
    private ObservableList<Airline> latitudes = FXCollections.observableArrayList();
    /**
     * Mutable ObservableLst containing a list of longitudes.
     */
    private ObservableList<Airline> longitudes = FXCollections.observableArrayList();
    /**
     * Mutable ObservableLst containing a list of flight paths.
     */
    private ObservableList<FlightPath> flightPaths = FXCollections.observableArrayList();

    /**
     * Holds the high level logic (set of instructions) for initialisation.
     * Initialisation order: Record Button, Table Columns, Set DataSet ComboBox, Set DataSet Listener,
     * Set Table
     */
    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        altitudeColumn.setCellValueFactory(new PropertyValueFactory<>("altitude"));
        latitudeColumn.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<>("longitude"));

        try {
            setNewRecordButton();
            setDataSetComboBox();
            setDataSetListener();
            setTable();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * Sets the shape of the new record button to circle and image to a green '+' icon.
     */
    private void setNewRecordButton() {
        Image addRecordImage = new Image(getClass().getResourceAsStream(Path.addRecordButtonPNG));
        newRecordButton.setGraphic(new ImageView(addRecordImage));
    }

    /**
     * Sets the JavaFX table with rows from the 'FlightPath' database table.
     * This is done using the table query and assigning each record to a row in the table
     * @param rs JDBC ResultSet obtained from querying the Database FlightPath table and is used to set the rows
     *           of the JavaFX data table by creating N FlightPath objects from the query that results in N tuples.
     * @throws Exception if the query fails, throws an exception
     */
    @Override
    public void setTableData(ResultSet rs) throws Exception{
        types = FXCollections.observableArrayList();
        ids = FXCollections.observableArrayList();
        altitudes = FXCollections.observableArrayList();
        latitudes = FXCollections.observableArrayList();
        longitudes = FXCollections.observableArrayList();
        while (rs.next()) {
            FlightPath flightPath = new FlightPath();
            String type = rs.getString("Type");
            String id = rs.getString("FlightPathID");
            int altitude = rs.getInt("Altitude");
            double latitude = rs.getDouble("Latitude");
            double longitude = rs.getDouble("Longitude");

            flightPath.setType(type);
            flightPath.setId(id);
            flightPath.setAltitude(altitude);
            flightPath.setLatitude(latitude);
            flightPath.setLongitude(longitude);

            flightPaths.add(flightPath);
        }
        dataTable.setItems(flightPaths);
    }

    /**
     * Override the parent's abstract class as to return the new record FXML file relating to the FlightPath class.
     * @return String the path to the newFlightPathFXML file.
     */
    @Override
    public String getNewRecordFXML() {
        return Path.newFlightPathFXML;
    }

    /**
     * Returns the 'FlightPath' datatype specifically used for this controller.
     * @return DataType a new FlightPath object.
     */
    @Override
    public DataType getDataType() { return new FlightPath(); }

    /**
     * Returns the JDBC/SQL query for selecting all rows from the 'FlightPath' table.
     * @return String for the  JDBC/SQL query for selecting all rows from the 'FlightPath' table.
     */
    @Override
    public String getTableQuery() {
        return "SELECT TYPE, FLIGHTPATHID, ALTITUDE, LATITUDE, LONGITUDE FROM FlightPath";
    }

    @Override
    public void initialiseComboBoxes() {

    }

    @Override
    public void filterData() {

    }
}
