package seng202.team4.model;

public class Airport extends DataType {

    private int airportID;
    private String name;
    private String city;
    private String country;
    private String iata;
    private String icao;
    private double latitude;
    private double longitude;
    private double altitude;
    private double timezone;
    private char dst;
    private String tzDatabase;
    private String type;
    private String source;


    public Airport(String airportData) {
        String[] airportArr = airportData.replaceAll("\"", "").split(",");
        this.airportID = Integer.parseInt(airportArr[0]);
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
        this.altitude = Double.parseDouble(airportArr[8 + delta]);
        this.timezone = Double.parseDouble(airportArr[9 + delta]);
        this.dst = airportArr[10 + delta].charAt(0);
        this.tzDatabase = airportArr[11 + delta];

        // Below parameters are not in given data.
        //this.type = airportArr[12];
        //this.source = airportArr[13];
    }

    public Airport() {

    }

    @Override
    public String getInsertStatement(int setID) {
        return "INSERT INTO AIRPORT ('NAME', 'CITY', 'COUNTRY', 'IATA', 'ICAO', 'LATITUDE', 'LONGITUDE', 'ALTITUDE', 'TIMEZONE', 'DST', 'TZDATABASETIME', 'SETID') "
                + "VALUES ('"
                + getName().replaceAll("'", "''") + between
                + getCity().replaceAll("'", "''") + between
                + getCountry().replaceAll("'", "''") + between
                + getIata().replaceAll("'", "''") + between
                + getIcao().replaceAll("'", "''") + between
                + getLatitude() + between
                + getLongitude() + between
                + getAltitude() + between
                + getTimezone() + between
                + getDst() + between
                + getTzDatabase().replaceAll("'", "''") + between
                + setID
                + "');";
    }

    @Override
    public DataType newDataType(String line) {
        return new Airport(line);
    }

    @Override
    public String getSetName() {
        return "AirportSet";
    }

    public int getAirportID() {
        return airportID;
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

    public double getTimezone() {
        return timezone;
    }

    public void setTimezone(double timezone) {
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

    public String getCoordinates() {
        return String.format("%.4f, %.4f", latitude, longitude);
    }
}
