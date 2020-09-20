package seng202.team4.model;

import java.util.ArrayList;
import java.util.Objects;

public class FlightPath extends DataType {

    private String type;
    private String id;
    private int altitude;
    private double latitude;
    private double longitude;

    public FlightPath() {}

    public FlightPath(String type, String id, int altitude, double latitude, double longitude) {
        this.type = type;
        this.id = id;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getInsertStatement(int setID) {
            return "INSERT INTO FlightPath ('TYPE', 'FLIGHTPATHID', 'ALTITUDE', 'LATITUDE', 'LONGITUDE', 'SETID') "
                + "VALUES ('"
                + getType().replaceAll("'", "''") + BETWEEN
                + getId() + BETWEEN
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

    public boolean equalsTest(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlightPath)) return false;
        FlightPath that = (FlightPath) o;
        return getAltitude() == that.getAltitude() &&
                Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                getType().equals(that.getType()) &&
                getId().equals(that.getId());
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
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
