package seng202.team4.model;

public class Airport {
    private final int airportID;
    private String name;
    private String city;
    private String country;
    private String iata;
    private String icao;
    private long latitude;
    private long longitude;
    private long altitude;
    private float timezone;
    private char dst;
    private String tzDatabase;
    private String type;
    private String source;


    public Airport(String airportData) {
        String[] airportArr = airportData.split(",");
        this.airportID = Integer.parseInt(airportArr[0]);
        this.name = airportArr[1];
        this.city = airportArr[2];
        this.country = airportArr[3];
        this.iata = airportArr[4];
        this.icao = airportArr[5];
        this.latitude = Long.parseLong(airportArr[6]);
        this.longitude = Long.parseLong(airportArr[7]);
        this.altitude = Long.parseLong(airportArr[8]);
        this.timezone = Float.parseFloat(airportArr[9]);
        this.dst = airportArr[10].charAt(0);
        this.tzDatabase = airportArr[11];
        this.type = airportArr[12];
        this.source = airportArr[13];
    }


    public Airport(int airportID, String name, String city, String country, String iata, String icao, long latitude, long longitude, long altitude, float timezone, char dst, String tzDatabase, String type, String source) {
        this.airportID = airportID;
        this.name = name;
        this.city = city;
        this.country = country;
        this.iata = iata;
        this.icao = icao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timezone = timezone;
        this.dst = dst;
        this.tzDatabase = tzDatabase;
        this.type = type;
        this.source = source;
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

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getAltitude() {
        return altitude;
    }

    public void setAltitude(long altitude) {
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

}
