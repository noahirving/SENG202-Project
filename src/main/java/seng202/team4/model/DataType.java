package seng202.team4.model;

import java.sql.*;

public abstract class DataType {

    private String between = "', '";

    public void addToDatabase(Object dataType) {
        Connection c;
        Statement stmt;
        String sql = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");

            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();

            if (dataType instanceof Airline) {
                Airline airline = (Airline) dataType;
                sql = "INSERT INTO AIRLINES ('AIRLINE ID', 'NAME', 'ALIAS', 'IATA', 'ICAO', 'CALLSIGN', 'COUNTRY', 'RECENTLY ACTIVE') "
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
                sql = "INSERT INTO AIRPORT ('AIRPORT ID', 'NAME', 'CITY', 'COUNTRY ', 'IATA', 'ICAO', 'LATITUDE', 'LONGITUDE', 'ALTITUDE', 'TIMEZONE', 'DST', 'TZ DATABASE TIME') "
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


            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        //System.out.println("Records created successfully");
    }
}
