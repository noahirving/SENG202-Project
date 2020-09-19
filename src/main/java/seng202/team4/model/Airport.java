package seng202.team4.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.util.ArrayList;
import java.util.Objects;

public class Airport extends DataType {

    private int id;
    private String name;
    private String city;
    private String country;
    private String iata;
    private String icao;
    private double latitude;
    private double longitude;
    private String coordinates;
    private double altitude;
    private float timezone;
    private char dst;
    private String tzDatabase;
    private int routeNum;
    private String type;
    private String source;
    private BooleanProperty select = new SimpleBooleanProperty(false);


    public Airport(String airportData) {
        String[] airportArr = airportData.replaceAll("\"", "").split(",");
        this.name = airportArr[1];
        int delta = 0;
        if (airportArr.length == 13) { // To deal with commas in city field
            this.city = (airportArr[2] + " " + airportArr[3]);
            delta = 1;
        }
        else {
            this.city = airportArr[2];
        }
        this.country = airportArr[3 + delta];
        this.iata = airportArr[4 + delta];
        this.icao = airportArr[5 + delta];
        this.latitude = Double.parseDouble(airportArr[6 + delta]);
        this.longitude = Double.parseDouble(airportArr[7 + delta]);
        setCoordinates(this.longitude, this.latitude);
        this.altitude = Double.parseDouble(airportArr[8 + delta]);
        this.timezone = Float.parseFloat(airportArr[9 + delta]);
        this.dst = airportArr[10 + delta].charAt(0);
        this.tzDatabase = airportArr[11 + delta];
        this.select = new SimpleBooleanProperty(false);

        // Below parameters are not in given data.
        //this.type = airportArr[12];
        //this.source = airportArr[13];
    }

    /**
     * Creates an empty object of airport.
     */
    public Airport() {
    }

    /**
     * Creates a full airport object.
     * @param name
     * @param city
     * @param country
     * @param iata
     * @param icao
     * @param latitude
     * @param longitude
     * @param altitude
     * @param timeZone
     * @param dst
     * @param tzDatabase
     */
    public Airport (String name, String city, String country, String iata, String icao, double latitude, double longitude, double altitude, float timeZone, char dst, String tzDatabase) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.iata = iata;
        this.icao = icao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timezone = timeZone;
        this.dst = dst;
        this.tzDatabase = tzDatabase;
    }
    /**
     * Gets the airport insert statement for the database.
     * @param setID the ID of the set the that will be inserted into.
     * @return
     */
    @Override
    public String getInsertStatement(int setID) {
        return "INSERT INTO AIRPORT ('NAME', 'CITY', 'COUNTRY', 'IATA', 'ICAO', 'LATITUDE', 'LONGITUDE', 'ALTITUDE', 'TIMEZONE', 'DST', 'TZDATABASETIME', 'SETID') "
                + "VALUES ('"
                + getName().replaceAll("'", "''''") + BETWEEN
                + getCity().replaceAll("'", "''") + BETWEEN
                + getCountry().replaceAll("'", "''") + BETWEEN
                + getIata().replaceAll("'", "''") + BETWEEN
                + getIcao().replaceAll("'", "''") + BETWEEN
                + getLatitude() + BETWEEN
                + getLongitude() + BETWEEN
                + getAltitude() + BETWEEN
                + getTimezone() + BETWEEN
                + getDst() + BETWEEN
                + getTzDatabase().replaceAll("'", "''") + BETWEEN
                + setID
                + "');";
    }


    @Override
    public DataType newDataType(String line) {
        return new Airport(line);
    }

    /**
     * Gets the datatype name.
     * @return datatype name.
     */
    @Override
    public String getTypeName() {
        return "Airport";
    }

    /**
     * Gets the datatype set name.
     * @return the datatype set name.
     */
    @Override
    public String getSetName() {
        return "AirportSet";
    }

    /**
     * Gets a valid airline from the given strings. Fills
     * the error message list if any errors are encountered.
     * @param name
     * @param city
     * @param country
     * @param iata
     * @param icao
     * @param latitude
     * @param longitude
     * @param altitude
     * @param timeZone
     * @param dst
     * @param tzDatabase
     * @param errorMessage list of errors.
     * @return the airport if valid, otherwise null.
     */
    public static Airport getValid(String name, String city, String country, String iata, String icao, String latitude, String longitude, String altitude, String timeZone, String dst, String tzDatabase, ArrayList<String> errorMessage) {
        boolean valid = true;
        if (!Validate.isAlphaNumeric(name)) {
            errorMessage.add("Invalid name");
            valid = false;
        }
        if (!Validate.isAlpha(city)) {
            errorMessage.add("Invalid city");
            valid = false;
        }
        if (!Validate.isAlpha(country)) {
            errorMessage.add("Invalid country");
            valid = false;
        }
        if (!Validate.isValidIATA(iata)) {
            errorMessage.add("Invalid IATA");
            valid = false;
        }
        if (!Validate.isValidICAO(icao)) {
            errorMessage.add("Invalid ICAO");
            valid = false;
        }

        if (!Validate.isNumeric(latitude)) {
            errorMessage.add("Invalid latitude");
            valid = false;
        }
        if (!Validate.isNumeric(longitude)) {
            errorMessage.add("Invalid longitude");
            valid = false;
        }
        if (!Validate.isNumeric(altitude)) {
            errorMessage.add("Invalid altitude");
            valid = false;
        }
        if (!Validate.isValidTimeZone(timeZone)) {
            errorMessage.add("Invalid time zone");
            valid = false;
        }

        char dstChar = 'N';
        if (dst.length() == 1) {
            dstChar = dst.charAt(0);
            if (!(dstChar == 'E' || dstChar == 'A' || dstChar == 'S' || dstChar == 'O' || dstChar == 'Z' || dstChar == 'N' || dstChar == 'U')) {
                errorMessage.add("Invalid daylight savings time");
                valid = false;
            }
        }
        else {
            errorMessage.add("Invalid daylight savings time");
            valid = false;
        }

        if (!Validate.isValidTZDB(tzDatabase)) {
            errorMessage.add("Invalid tz database");
            valid = false;
        }

        if (valid) {
            return new Airport(name, city, country, iata, icao, Double.parseDouble(latitude), Double.parseDouble(longitude), Double.parseDouble(altitude), Float.parseFloat(timeZone), dstChar, tzDatabase);
        }
        else {
            return null;
        }

    }

    /**
     * Converts record array into individual strings and calls get valid.
     * @param record        array of strings constituting the record.
     * @param errorMessage  arrayList where the error messages will be stored.
     * @return the airport if valid, otherwise null.
     */
    @Override
    public DataType getValid(String[] record, ArrayList<String> errorMessage) {
        String name = record[0];
        String city = record[1];
        String country = record[2];
        String iata = record[3];
        String icao = record[4];
        String latitude = record[5];
        String longitude = record[6];
        String altitude = record[7];
        String timeZone = record[8];
        String dst = record[9];
        String tzDatabase = record[10];

        return getValid(name, city, country, iata, icao, latitude, longitude, altitude, timeZone, dst, tzDatabase, errorMessage);
    }

    public boolean equalsTest(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airport)) return false;
        Airport airport = (Airport) o;
        return getId() == airport.getId() &&
                Double.compare(airport.getLatitude(), getLatitude()) == 0 &&
                Double.compare(airport.getLongitude(), getLongitude()) == 0 &&
                Double.compare(airport.getAltitude(), getAltitude()) == 0 &&
                Float.compare(airport.getTimezone(), getTimezone()) == 0 &&
                getDst() == airport.getDst() &&
                getRouteNum() == airport.getRouteNum() &&
                getName().equals(airport.getName()) &&
                getCity().equals(airport.getCity()) &&
                getCountry().equals(airport.getCountry()) &&
                getIata().equals(airport.getIata()) &&
                getIcao().equals(airport.getIcao()) &&
                getTzDatabase().equals(airport.getTzDatabase());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getTimezone() {
        return timezone;
    }

    public void setTimezone(float timezone) {
        this.timezone = timezone;
    }

    public char getDst() {
        return dst;
    }

    public void setDst(char dst) {
        this.dst = dst;
    }

    public String getTzDatabase() {
        return tzDatabase;
    }

    public void setTzDatabase(String tzDatabase) {
        this.tzDatabase = tzDatabase;
    }

    public int getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(int routeNum) {
        this.routeNum = routeNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setCoordinates(double longitude, double latitude) {
        this.coordinates = String.format("%.4f, %.4f", latitude, longitude);
    }

    public String getCoordinates() {
        return coordinates;
    }

    public boolean isSelect() {
        return select.get();
    }

    public BooleanProperty selectProperty() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select.set(select);
    }
}
