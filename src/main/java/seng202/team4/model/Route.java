package seng202.team4.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.Objects;

public class Route extends DataType {

    private int id;
    private String airlineCode;
    private int airlineID;
    private String sourceAirportCode;
    private int sourceAirportID;
    private String destinationAirportCode;
    private int destinationAirportID;
    private boolean codeshare;
    private int numStops;
    private String planeTypeCode;
    private double distance;
    private BooleanProperty select = new SimpleBooleanProperty(false);
    private double carbonEmissions;

    public Route(String routeInfo) {
        String[] routeArray = routeInfo.split(",");
        this.airlineCode = routeArray[0];
        this.airlineID = tryReturnInt(routeArray[1]);
        this.sourceAirportCode = routeArray[2];
        this.sourceAirportID = tryReturnInt(routeArray[3]);
        this.destinationAirportCode = routeArray[4];
        this.destinationAirportID = tryReturnInt(routeArray[5]);
        this.codeshare = routeArray[6].equals("Y");
        try {
            this.numStops = Integer.parseInt(routeArray[7]);
        } catch(Exception e) {
            e.printStackTrace();
        }
        if (routeArray.length == 9) {
            this.planeTypeCode = routeArray[8];
        } else {
            this.planeTypeCode = "";
        }
        try {
            this.distance = Double.parseDouble(routeArray[10]);
        }
        catch (Exception e) {
            this.distance = 0.00;
        }
        this.select = new SimpleBooleanProperty(false);
    }

    /**
     * Creates an empty object of route.
     */
    public Route() {
    }

    /**
     * Creates a full route object.
     * @param airline
     * @param srcAirport
     * @param dstAirport
     * @param codeshare
     * @param stops
     * @param equipment
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
     * @param setID the ID of the set the that will be inserted into.
     * @return
     */
    @Override
    public String getInsertStatement(int setID) {
        return "INSERT INTO Route ('AIRLINE', 'AIRLINEID', 'SourceAirport', 'SOURCEAIRPORTID', 'DESTINATIONAIRPORT', 'DESTINATIONAIRPORTID', 'CODESHARE', 'STOPS', 'EQUIPMENT', 'DISTANCE', 'SETID') "
                + "VALUES ('"
                + getAirlineCode().replaceAll("'", "''") + BETWEEN
                + getAirlineID() + BETWEEN
                + getSourceAirportCode().replaceAll("'", "''") + BETWEEN
                + getSourceAirportID() + BETWEEN
                + getDestinationAirportCode().replaceAll("'", "''") + BETWEEN
                + getDestinationAirportID() + BETWEEN
                + isCodeshare() + BETWEEN
                + getNumStops() + BETWEEN
                + getPlaneTypeCode().replaceAll("'", "''") + BETWEEN
                + getDistance() + BETWEEN
                + setID
                + "');";
    }

    @Override
    public DataType newDataType(String line) {
        return new Route(line);
    }

    /**
     * Gets the datatype name.
     * @return datatype name.
     */
    @Override
    public String getTypeName() {
        return "Route";
    }

    /**
     * Gets the datatype set name.
     * @return the datatype set name.
     */
    @Override
    public String getSetName() {
        return "RouteSet";
    }

    /**
     * Gets a valid airline from the given strings. Fills
     * the error message list if any errors are encountered.
     * @param airline
     * @param srcAirport
     * @param dstAirport
     * @param codeshare
     * @param stops
     * @param equipment
     * @param errorMessage list of errors.
     * @return the airline if valid, otherwise null.
     */
    public static Route getValid(String airline, String srcAirport, String dstAirport, String codeshare, String stops, String equipment, ArrayList<String> errorMessage) {
        // TODO: Finish validating, get IDs
        boolean valid = true;
        if (!Validate.isAlphaNumeric(airline)) {
            errorMessage.add("Invalid airline");
            valid = false;
        }
        if (!Validate.isAlphaNumeric(srcAirport)) {
            errorMessage.add("Invalid source airport");
            valid = false;
        }
        if (!Validate.isAlphaNumeric(dstAirport)) {
            errorMessage.add("Invalid destination airport");
            valid = false;
        }
        if (!codeshare.equals("Y") && !codeshare.equals("")) {
            errorMessage.add("Invalid codeshare");
            valid = false;
        }
        if (!Validate.isNumeric(stops)) {
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
     * @param record        array of strings constituting the record.
     * @param errorMessage  arrayList where the error messages will be stored.
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

    public double getCarbonEmissions() {
        return carbonEmissions;
    }

    // TODO: remove
    public int tryReturnInt(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (Exception e) {
            return -1;
        }
    }

    public int getId() {
        return id;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public int getAirlineID() {
        return airlineID;
    }

    public String getSourceAirportCode() {
        return sourceAirportCode;
    }

    public int getSourceAirportID() {
        return sourceAirportID;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public int getDestinationAirportID() {
        return destinationAirportID;
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

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public void setAirlineID(int airlineID) {
        this.airlineID = airlineID;
    }

    public void setSourceAirportCode(String sourceAirportCode) {
        this.sourceAirportCode = sourceAirportCode;
    }

    public void setSourceAirportID(int sourceAirportID) {
        this.sourceAirportID = sourceAirportID;
    }

    public void setDestinationAirportCode(String destinationAirportCode) {
        this.destinationAirportCode = destinationAirportCode;
    }

    public void setDestinationAirportID(int destinationAirportID) {
        this.destinationAirportID = destinationAirportID;
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
