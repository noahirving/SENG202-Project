package seng202.team4.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Airline extends DataType {

    private int id;
    private String name;
    private String code;
    private String iata;
    private String icao;
    private String callSign;
    private String country;
    private boolean recentlyActive;
    private double carbonEmissions;

    public Airline(String airlineData) {
        String[] airlines = airlineData.replaceAll("\"", "").split(",");
        this.id = Integer.parseInt(airlines[0]);
        this.name = airlines[1];
        this.code = airlines[2];
        this.iata = airlines[3];
        this.icao = airlines[4];
        this.callSign = airlines[5];
        this.country = airlines[6];
        this.recentlyActive = airlines[7].equals("Y");
        this.carbonEmissions = calculateCarbonEmissions();
    }

    public Airline() {

    }

    @Override
    public String getInsertStatement(int setID){
        return "INSERT INTO Airline ('NAME', 'ALIAS', 'IATA', 'ICAO', 'CALLSIGN', 'COUNTRY', 'RECENTLYACTIVE', 'SETID') "
                + "VALUES ('"
                + getName().replaceAll("'", "''") + between
                + getCode().replaceAll("'", "''") + between
                + getIata().replaceAll("'", "''") + between
                + getIcao().replaceAll("'", "''") + between
                + getCallSign().replaceAll("'", "''") + between
                + getCountry().replaceAll("'", "''") + between
                + isRecentlyActive() + between
                + setID
                + "');";
    }

    @Override
    public DataType newDataType(String line) {
        return new Airline(line);
    }

    @Override
    public String getTypeName() {
        return "Airline";
    }

    @Override
    public String getSetName() {
        return "AirlineSet";
    }

    public double calculateCarbonEmissions() {
        return 0; // To be implemented
    }

    // getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getIata() {
        return iata;
    }

    public String getIcao() {
        return icao;
    }

    public String getCallSign() {
        return callSign;
    }

    public String getCountry() {
        return country;
    }

    public boolean isRecentlyActive() {
        return recentlyActive;
    }

    public double getCarbonEmissions() {
        return carbonEmissions;
    }


    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRecentlyActive(boolean recentlyActive) {
        this.recentlyActive = recentlyActive;
    }

    public void setCarbonEmissions(double carbonEmissions) {
        this.carbonEmissions = carbonEmissions;
    }
}
