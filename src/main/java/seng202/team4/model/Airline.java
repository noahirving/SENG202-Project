package seng202.team4.model;

import java.util.ArrayList;
import java.util.Objects;

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

    public Airline(String name, String code, String iata, String icao, String callSign, String country, String recentlyActive) {
        this.name = name;
        this.code = code;
        this.iata = iata;
        this.icao = icao;
        this.callSign = callSign;
        this.country = country;
        this.recentlyActive = recentlyActive.equals("Y");
        this.carbonEmissions = calculateCarbonEmissions();
    }

    @Override
    public String getInsertStatement(int setID){
        return "INSERT INTO Airline ('NAME', 'ALIAS', 'IATA', 'ICAO', 'CALLSIGN', 'COUNTRY', 'RECENTLYACTIVE', 'SETID') "
                + "VALUES ('"
                + getName().replaceAll("'", "''") + BETWEEN
                + getCode().replaceAll("'", "''") + BETWEEN
                + getIata().replaceAll("'", "''") + BETWEEN
                + getIcao().replaceAll("'", "''") + BETWEEN
                + getCallSign().replaceAll("'", "''") + BETWEEN
                + getCountry().replaceAll("'", "''") + BETWEEN
                + isRecentlyActive() + BETWEEN
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

    public static Airline getValid(String name, String code, String iata, String icao, String callSign, String country, String recentlyActive, ArrayList<String> errorMessage) {
        boolean valid = true;

        if (!Validate.isAlphaNumeric(name)) {
            errorMessage.add("Invalid name");
            valid = false;
        }
        if (!Validate.isAlphaNumeric(code)) {
            errorMessage.add("Invalid code");
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
        if (!Validate.isAlphaNumeric(callSign)) {
            errorMessage.add("Invalid call sign");
            valid = false;
        }
        if (!Validate.isAlpha(country)) {
            errorMessage.add("Invalid country");
            valid = false;
        }
        if (!recentlyActive.equals("Y") && !recentlyActive.equals("N")) {
            errorMessage.add("Invalid recently active");
            valid = false;
        }
        if (valid) {
            return new Airline(name, code, iata, icao, callSign, country, recentlyActive);
        }
        else {
            return null;
        }
    }
    @Override
    public Airline getValid(String[] record, ArrayList<String> errorMessage) {
        String name = record[0];
        String code = record[1];
        String iata = record[2];
        String icao = record[3];
        String callSign = record[4];
        String country = record[5];
        String recentlyActive = record[6];
        return getValid(name, code, iata, icao, callSign, country, recentlyActive, errorMessage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airline)) return false;
        Airline airline = (Airline) o;
        return getId() == airline.getId() &&
                isRecentlyActive() == airline.isRecentlyActive() &&
                getName().equals(airline.getName()) &&
                getCode().equals(airline.getCode()) &&
                getIata().equals(airline.getIata()) &&
                getIcao().equals(airline.getIcao()) &&
                getCallSign().equals(airline.getCallSign()) &&
                getCountry().equals(airline.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCode(), getIata(), getIcao(), getCallSign(), getCountry(), isRecentlyActive());
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
