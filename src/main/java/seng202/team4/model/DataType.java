package seng202.team4.model;

import java.sql.*;

public class DataType {

    private String between = "', '";

    private Statement stmt;
    private int dataCount = 0;

    Connection c;
    //Statement stmt = null;
    String sql = null;

    public DataType() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");

            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }


    }

    public void addToDatabase(Object dataType) {
        try {
            if (dataType instanceof Airline) {
                Airline airline = (Airline) dataType;
                sql = "INSERT INTO AIRLINES ('AIRLINEID', 'NAME', 'ALIAS', 'IATA', 'ICAO', 'CALLSIGN', 'COUNTRY', 'RECENTLYACTIVE') "
                        + "VALUES ("
                        + airline.getAirlineID() + ", '"
                        + airline.getAirlineName().replaceAll("'", "''") + between
                        + airline.getAirlineCode().replaceAll("'", "''") + between
                        + airline.getAirlineIATA().replaceAll("'", "''") + between
                        + airline.getAirlineICAO().replaceAll("'", "''") + between
                        + airline.getAirlineCallSign().replaceAll("'", "''") + between
                        + airline.getAirlineCountry().replaceAll("'", "''") + between
                        + airline.isRecentlyActive()
                        + "');";
            }

            else if (dataType instanceof Airport) {
                Airport airport = (Airport) dataType;
                sql = "INSERT INTO AIRPORT ('AIRPORTID', 'NAME', 'CITY', 'COUNTRY', 'IATA', 'ICAO', 'LATITUDE', 'LONGITUDE', 'ALTITUDE', 'TIMEZONE', 'DST', 'TZDATABASETIME') "
                        + "VALUES ("
                        + airport.getAirportID() + ", '"
                        + airport.getName().replaceAll("'", "''") + between
                        + airport.getCity().replaceAll("'", "''") + between
                        + airport.getCountry().replaceAll("'", "''") + between
                        + airport.getIata().replaceAll("'", "''") + between
                        + airport.getIcao().replaceAll("'", "''") + between
                        + airport.getLatitude() + between
                        + airport.getLongitude() + between
                        + airport.getAltitude() + between
                        + airport.getTimezone() + between
                        + airport.getDst() + between
                        + airport.getTzDatabase().replaceAll("'", "''")
                        + "');";
            }

            else if (dataType instanceof Route) {
                Route route = (Route) dataType;
                sql = "INSERT INTO ROUTES ('ROUTEID', 'AIRLINE', 'AIRLINEID', 'SourceAirport', 'SOURCEAIRPORTID', 'DESTINATIONAIRPORT', 'DESTINATIONAIRPORTID', 'CODESHARE', 'STOPS', 'EQUIPMENT', 'CARBONEMISSIONS') "
                        + "VALUES ("
                        + route.getRouteID() + ", '"
                        + route.getAirlineCode().replaceAll("'", "''") + between
                        + route.getAirlineID() + between
                        + route.getSourceAirportCode().replaceAll("'", "''") + between
                        + route.getSourceAirportID() + between
                        + route.getDestinationAirportCode().replaceAll("'", "''") + between
                        + route.getDestinationAirportID() + between
                        + route.isCodeshare() + between
                        + route.getNumStops() + between
                        + route.getPlaneTypeCode().replaceAll("'", "''") + between
                        + route.getCarbonEmissions()
                        + "');";
            }

            stmt.addBatch(sql);

            dataCount += 1;
            if (dataCount % 1000 == 0) {  // To be removed once we decide the max number of entries.
                stmt.executeBatch();
            }

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        //System.out.println("Records created successfully");
    }

    public void updateDatabase() {
        try {
            stmt.executeBatch();

            stmt.close();
            c.commit();
            c.close();
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

    }
}
