package seng202.team4.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;

/**
 * Route model class with attributes representing
 * the aspects of routes. These attributes were chosen
 * from the open flights sample data. Objects of this class
 * are created to display in the table on the route tab.
 */
public class Route extends DataType {

    /**
     * unique ID for route, as there can be multiple routes with the
     * same airline, source and destination airport.
     * This was required to satisfy the primary key constraint.
     */
    private int id;
    /**
     * Unique open flights Airline ID
     */
    private String airlineCode;
    /**
     * 3-letter IATA or 4-letter ICAO of source airport
     */
    private String sourceAirportCode;
    /**
     * 3-letter IATA or 4-letter ICAO of destination airport
     */
    private String destinationAirportCode;
    /**
     * 'Y' if this route is a code share. 'N' otherwise
     */
    private boolean codeshare;
    /**
     * Number of stops on this flight
     */
    private int numStops;
    /**
     * 3-letter code for plane type
     */
    private String planeTypeCode;
    /**
     * Distance between source and destination airport
     * Only calculated for selected routes to prevent
     * large load time.
     */
    private double distance;
    /**
     * Booleean property that is true if the airport has been selected with the checkbox and false otherwise
     */
    private BooleanProperty select = new SimpleBooleanProperty(false);
    /**
     * The carbon emissions per passenger in kg of C02 for the route.
     * Also only calculated for selected routes.
     */
    private double carbonEmissions;

    /**
     * Creates an empty object of route.
     */
    public Route() {}

    /**
     * Creates a full route object.
     * @param airline String Airline
     * @param srcAirport String Source Airport
     * @param dstAirport String Destination Airport
     * @param codeshare boolean Codeshare
     * @param stops int Stops
     * @param equipment String Equipment
     */
    public Route(String airline, String srcAirport, String dstAirport, boolean codeshare, int stops, String equipment) {
        this.airlineCode = airline;
        this.sourceAirportCode = srcAirport;
        this.destinationAirportCode = dstAirport;
        this.codeshare = codeshare;
        this.numStops = stops;
        this.planeTypeCode = equipment;
    }

    /**
     * Gets the route insert statement for the database.
     * @param setID int the ID of the set the that will be inserted into.
     * @return String The insert statement of a given ID.
     */
    @Override
    public String getInsertStatement(int setID) {
        return "INSERT INTO Route ('Airline', 'SourceAirport', 'DestinationAirport', 'Codeshare', 'Stops', 'Equipment', 'Distance', 'SetID') "
                + "VALUES ('"
                + getAirlineCode().replaceAll("'", "''") + BETWEEN
                + getSourceAirportCode().replaceAll("'", "''") + BETWEEN
                + getDestinationAirportCode().replaceAll("'", "''") + BETWEEN
                + isCodeshare() + BETWEEN
                + getNumStops() + BETWEEN
                + getPlaneTypeCode().replaceAll("'", "''") + BETWEEN
                + getDistance() + BETWEEN
                + setID
                + "');";
    }

    /**
     * Gets the datatype name.
     * @return String datatype name.
     */
    @Override
    public String getTypeName() {
        return "Route";
    }

    /**
     * Gets the datatype set name.
     * @return String the datatype set name.
     */
    @Override
    public String getSetName() {
        return "RouteSet";
    }

    /**
     * Gets a valid airline from the given strings. Fills
     * the error message list if any errors are encountered.
     * @param airline String Airline
     * @param srcAirport String Source Airport
     * @param dstAirport String Destination Airport
     * @param codeshare String Codeshare
     * @param stops String Stops
     * @param equipment String Equipment
     * @param errorMessage ArrayList<String> list of errors.
     * @return the airline if valid, otherwise null.
     */
    public static Route getValid(String airline, String srcAirport, String dstAirport, String codeshare, String stops, String equipment, ArrayList<String> errorMessage) {
        // TODO: Finish validating, get IDs
        boolean valid = true;
        if (!Validate.isAirlineIATA(airline) && !Validate.isAirlineICAO(airline)) {
            System.out.println("test: " + airline);
            errorMessage.add("Invalid airline");
            valid = false;
        }
        if (!Validate.isAirportIATA(srcAirport) && !Validate.isAirportICAO(srcAirport)) {
            errorMessage.add("Invalid source airport");
            valid = false;
        }
        if (!Validate.isAirportIATA(dstAirport) && !Validate.isAirportICAO(dstAirport)) {
            errorMessage.add("Invalid destination airport");
            valid = false;
        }
        if (!codeshare.equals("Y") && !codeshare.equals("")) {
            errorMessage.add("Invalid codeshare");
            valid = false;
        }
        if (!Validate.isInteger(stops)) {
            errorMessage.add("Invalid stops");
            valid = false;
        }
        if (!Validate.isAlphaNumeric(equipment)) {
            errorMessage.add("Invalid equipment");
            valid = false;
        }
        if (valid) {
            return new Route(airline, srcAirport, dstAirport, codeshare.equals("Y"), Integer.parseInt(stops), equipment);
        }
        else {
            return null;
        }
    }

    /**
     * Converts record array into individual strings and calls get valid.
     * @param record String[] array of strings constituting the record.
     * @param errorMessage ArrayList<String> arrayList where the error messages will be stored.
     * @return the airline if valid, otherwise null.
     */
    @Override
    public DataType getValid(String[] record, ArrayList<String> errorMessage) {
        String airline = record[0];
        String srcAirport = record[1];
        String dstAirport = record[2];
        String codeshare = record[3];
        String stops = record[4];
        String equipment = record[5];
        return getValid(airline, srcAirport, dstAirport, codeshare, stops, equipment, errorMessage);
    }

    /**
     * Converts a string record into individual strings and calls get valid.
     * @param record String constituting the record.
     * @param errorMessage ArrayList<String> where the error messages will be stored.
     * @return DataType the airline if valid, otherwise null.
     */
    @Override
    public DataType getValid(String record, ArrayList<String> errorMessage) {
        String[] recordList = record.replaceAll("\"", "").split(",");
        if (recordList.length == 9) {
            recordList = new String[] {recordList[0], recordList[2], recordList[4], recordList[6], recordList[7], recordList[8]};
            return getValid(recordList, errorMessage);
        }
        else if (recordList.length == 8) {
            recordList = new String[]{recordList[0], recordList[2], recordList[4], recordList[6], recordList[7], ""};
            return getValid(recordList, errorMessage);
        }
        else {
            errorMessage.add("Invalid number of attributes");
            return null;
        }
    }

    /**
     * A method for checking if a given object is equal the current object of the class.
     * @param o Object
     * @return Returns true if equal to current object.
     */
    @Override
    public boolean equalsTest(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return isCodeshare() == route.isCodeshare() &&
                getNumStops() == route.getNumStops() &&
                getAirlineCode().equals(route.getAirlineCode()) &&
                getSourceAirportCode().equals(route.getSourceAirportCode()) &&
                getDestinationAirportCode().equals(route.getDestinationAirportCode()) &&
                getPlaneTypeCode().equals(route.getPlaneTypeCode());
    }

    public void setCarbonEmissions(double emissions) {
        this.carbonEmissions = emissions;
    }

    public int getId() {
        return id;
    }

    public double getCarbonEmissions() {
        return carbonEmissions;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public String getSourceAirportCode() {
        return sourceAirportCode;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public boolean isCodeshare() {
        return codeshare;
    }

    public int getNumStops() {
        return numStops;
    }

    public String getPlaneTypeCode() {
        return planeTypeCode;
    }

    public double getDistance() {
        return distance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public void setSourceAirportCode(String sourceAirportCode) {
        this.sourceAirportCode = sourceAirportCode;
    }

    public void setDestinationAirportCode(String destinationAirportCode) {
        this.destinationAirportCode = destinationAirportCode;
    }

    public void setCodeshare(boolean codeshare) {
        this.codeshare = codeshare;
    }

    public void setNumStops(int numStops) {
        this.numStops = numStops;
    }

    public void setPlaneTypeCode(String planeTypeCode) {
        this.planeTypeCode = planeTypeCode;
    }

    public boolean isSelect() {
        return this.select.get();
    }

    public BooleanProperty selectProperty() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select.set(select);
    }
}
