package seng202.team4.model;

import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;


public class FlightPath extends DataType {

    private int id;
    private String type;
    private String flightPathId;
    private int altitude;
    private double latitude;
    private double longitude;

    public FlightPath() {}

    public FlightPath(String type, String flightPathId, int altitude, double latitude, double longitude) {
        this.type = type;
        this.flightPathId = flightPathId;
        this.altitude = altitude;
        this.latitude = Precision.round(latitude, 2);
        this.longitude = Precision.round(longitude, 2);
    }

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

    @Override
    public String getTypeName() {
        return "FlightPath";
    }


    @Override
    public String getSetName() {
        return "FlightPathSet";
    }



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

    @Override
    public DataType getValid(String[] record, ArrayList<String> errorMessage) {
        String type = record[0];
        String id = record[1];
        String altitude = record[2];
        String latitude = record[3];
        String longitude = record[4];
        return getValid(type, id, altitude, latitude, longitude, errorMessage);
    }

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
     * @return Returns true if equal to current object.
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
