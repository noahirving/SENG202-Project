package seng202.team4.model;

import java.sql.*;
import seng202.team4.model.Airline;

public abstract class DataType {

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
                sql = "INSERT INTO AIRLINES ('AIRLINE ID', 'NAME', 'ALIAS', 'IATA', 'ICAO', 'CALLSIGN', 'COUNTRY', 'RECENTLY ACTIVE') " +
                        "VALUES (";

                String between = "', '";

                sql += ((Airline) dataType).getAirlineID() + ", '";
                sql += ((Airline) dataType).getAirlineName().replaceAll("'", "''") + between;
                sql += ((Airline) dataType).getAirlineCode().replaceAll("'", "''") + between;
                sql += ((Airline) dataType).getAirlineIATA().replaceAll("'", "''") + between;
                sql += ((Airline) dataType).getAirlineICAO().replaceAll("'", "''") + between;
                sql += ((Airline) dataType).getAirlineCallSign().replaceAll("'", "''") + between;
                sql += ((Airline) dataType).getAirlineCountry().replaceAll("'", "''") + between;
                sql += ((Airline) dataType).isRecentlyActive() + "');";
            }

            else if (dataType instanceof Airport) {
                sql = "INSERT INTO AIRLINES ('AIRLINE ID', 'NAME', 'ALIAS', 'IATA', 'ICAO', 'CALLSIGN', 'COUNTRY', 'RECENTLY ACTIVE') " +
                        "VALUES (20000, 'Test Name', 'Test Alias', 'Test IATA', 'Test ICAO', 'Test Callsign', 'Test Country', 'Test Active');";
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
