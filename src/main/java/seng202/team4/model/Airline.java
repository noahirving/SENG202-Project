package seng202.team4.model;

import java.util.ArrayList;
import java.util.Arrays;
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
    }

    /**
     * Creates an empty object of airline.
     */
    public Airline() {

    }

    /**
     * Creates a full airline object.
     * @param name
     * @param code
     * @param iata
     * @param icao
     * @param callSign
     * @param country
     * @param recentlyActive
     */
    public Airline(String name, String code, String iata, String icao, String callSign, String country, boolean recentlyActive) {
        this.name = name;
        this.code = code;
        this.iata = iata;
        this.icao = icao;
        this.callSign = callSign;
        this.country = country;
        this.recentlyActive = recentlyActive;
    }

    /**
     * Gets the airline insert statement for the database.
     * @param setID the ID of the set the that will be inserted into.
     * @return
     */
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

    /**
     * Gets the datatype name.
     * @return datatype name.
     */
    @Override
    public String getTypeName() {
        return "Airline";
    }

    /**
     * Gets the datatype set name.
     * @return the datatype set name.
     */
    @Override
    public String getSetName() {
        return "AirlineSet";
    }

    /**
     * Gets a valid airline from the given strings. Fills
     * the error message list if any errors are encountered.
     * @param name
     * @param code
     * @param iata
     * @param icao
     * @param callSign
     * @param country
     * @param recentlyActive
     * @param errorMessage list of errors.
     * @return the airline if valid, otherwise null.
     */
    public static Airline getValid(String name, String code, String iata, String icao, String callSign, String country, String recentlyActive, ArrayList<String> errorMessage) {
        boolean valid = true;

        if (!Validate.isAlphaNumeric(name)) {
            errorMessage.add("Invalid name");
            valid = false;
        }
        if (!Validate.isAlphaNumeric(code) && !code.equals("\\N")) {
            errorMessage.add("Invalid code");
            valid = false;
        }
        if (!Validate.isAirlineIATA(iata)) {
            errorMessage.add("Invalid IATA");
            valid = false;
        }
        if (!Validate.isAirlineICAO(icao)) {
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
            return new Airline(name, code, iata, icao, callSign, country, recentlyActive.equals("Y"));
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

    public DataType getValid(String record, ArrayList<String> errorMessage) {
        String[] recordList = record.replaceAll("\"", "").split(",");
        if (recordList.length != 8) {
            errorMessage.add("Invalid number of attributes");
            return null;
        }
        recordList = Arrays.copyOfRange(recordList, 1, 8);
        return getValid(recordList, errorMessage);
    }

    public boolean equalsTest(Object o) {
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

}
