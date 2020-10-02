package seng202.team4.model;

import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;

/**
 * FlightPath model class with attributes representing
 * the aspects of the flightPaths. These attributes were chosen
 * from the flight plan database. Flight path files are created
 * by the user on flightplandatabaes.com and uploaded. Objects of this class
 * are created to display in the table on the Flight Path tab.
 */
public class FlightPath extends DataType {

    /**
     * unique ID for flightpath, as there can be multiple flightPaths with the
     * same flightPathId this was required to satisfy the primary key constraint.
     */
    private int id;
    /**
     * Type of flight path.
     */
    private String type;
    /**
     * flightPath ID, not unique.
     */
    private String flightPathId;
    /**
     * Altitude at waypoint or airport in feet.
     */
    private int altitude;
    /**
     * latitude at waypoint or airport in decimal degrees.
     */
    private double latitude;
    /**
     * longitude at waypoint or airport in decimal degrees.
     */
    private double longitude;

    /**
     * Creates an empty object of flightpath.
     */
    public FlightPath() {}

    /**
     *
     * @param type String Type
     * @param flightPathId String FlightPathId
     * @param altitude int Altitude
     * @param latitude double Latitude
     * @param longitude double Longitude
     */
    public FlightPath(String type, String flightPathId, int altitude, double latitude, double longitude) {
        this.type = type;
        this.flightPathId = flightPathId;
        this.altitude = altitude;
        this.latitude = Precision.round(latitude, 2);
        this.longitude = Precision.round(longitude, 2);
    }

    /**
     * Gets the flightpath insert statement for the database.
     * @param setID int the ID of the set the that will be inserted into.
     * @return String The insert statement of a given ID.
     */
    @Override
    public String getInsertStatement(int setID) {
            return "INSERT INTO FlightPath ('TYPE', 'FLIGHTPATHID', 'ALTITUDE', 'LATITUDE', 'LONGITUDE', 'SETID') "
                + "VALUES ('"
                + getType().replaceAll("'", "''") + BETWEEN
                + getFlightPathId() + BETWEEN
                + getAltitude() + BETWEEN
                + getLatitude() + BETWEEN
                + getLongitude() + BETWEEN
                + setID
                + "');";
    }

    /**
     * Gets the datatype name.
     * @return String the datatype name
     */
    @Override
    public String getTypeName() {
        return "FlightPath";
    }

    /**
     * Gets the set name
     * @return String the set name.
     */
    @Override
    public String getSetName() {
        return "FlightPathSet";
    }


    /**
     *
     * @param type String type
     * @param id String id
     * @param altitude String altitude
     * @param latitude String latitude
     * @param longitude String longitude
     * @param errorMessage ArrayList<String> errorMessage list of errors
     * @return
     */
    public static DataType getValid(String type, String id, String altitude, String latitude, String longitude, ArrayList<String> errorMessage) {
        boolean valid = true;
        if (!Validate.isAlpha(type)) {
            errorMessage.add("Invalid type");
            valid = false;
        }
        if (!Validate.isAlpha(id)) {
            errorMessage.add("Invalid ID");
            valid = false;
        }
        if (!Validate.isInteger(altitude)) {
            errorMessage.add("Invalid altitude");
            valid = false;
        }
        if (!Validate.isFloat(latitude)) {
            errorMessage.add("Invalid latitude");
            valid = false;
        }
        if (!Validate.isFloat(longitude)) {
            errorMessage.add("Invalid longitude");
            valid = false;
        }
        if (valid) {
            return new FlightPath(type, id, Integer.parseInt(altitude), Double.parseDouble(latitude), Double.parseDouble(longitude));
        }
        else {
            return null;
        }
    }

    /**
     *
     * @param record String[] array of strings constituting the record.
     * @param errorMessage ArrayList<String> arrayList where the error messages will be stored.
     * @return
     */
    @Override
    public DataType getValid(String[] record, ArrayList<String> errorMessage) {
        String type = record[0];
        String id = record[1];
        String altitude = record[2];
        String latitude = record[3];
        String longitude = record[4];
        return getValid(type, id, altitude, latitude, longitude, errorMessage);
    }

    /**
     *
     * @param record String constituting the record.
     * @param errorMessage ArrayList<String> where the error messages will be stored.
     * @return
     */
    @Override
    public DataType getValid(String record, ArrayList<String> errorMessage) {
        String[] recordList = record.replaceAll("\"", "").split(",");
        if (recordList.length != 5) {
            errorMessage.add("Invalid number of attributes");
            return null;
        }
        return getValid(recordList, errorMessage);
    }

    /**
     * A method for checking if a given object is equal the current object of the class.
     * @param o Object
     * @return boolean Returns true if equal to current object.
     */
    @Override
    public boolean equalsTest(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlightPath)) return false;
        FlightPath that = (FlightPath) o;
        return getAltitude() == that.getAltitude() &&
                Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                getType().equals(that.getType()) &&
                getFlightPathId().equals(that.getFlightPathId());
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getFlightPathId() {
        return flightPathId;
    }

    public int getAltitude() {
        return altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFlightPathId(String flightPathId) {
        this.flightPathId = flightPathId;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
