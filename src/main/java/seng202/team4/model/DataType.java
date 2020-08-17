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
                sql = "INSERT INTO AIRLINES ('AIRLINE ID', 'NAME', 'ALIAS', 'IATA', 'ICAO', 'CALLSIGN', 'COUNTRY', 'RECENTLY ACTIVE') " +
                        "VALUES (";

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
                sql = "INSERT INTO AIRPORT ('AIRPORT ID', 'NAME', 'CITY', 'COUNTRY ', 'IATA', 'ICAO', 'LATITUDE', 'LONGITUDE', 'ALTITUDE', 'TIMEZONE', 'DST', 'TZ DATABASE TIME') " +
                        "VALUES (";

                sql += ((Airport) dataType).getAirportID() + ", '";
                sql += ((Airport) dataType).getName().replaceAll("'", "''") + between;
                sql += ((Airport) dataType).getCity().replaceAll("'", "''") + between;
                sql += ((Airport) dataType).getCountry().replaceAll("'", "''") + between;
                sql += ((Airport) dataType).getIata().replaceAll("'", "''") + between;
                sql += ((Airport) dataType).getIcao().replaceAll("'", "''") + "', ";
                sql += ((Airport) dataType).getLatitude() + ", ";
                sql += ((Airport) dataType).getLongitude() + ", ";
                sql += ((Airport) dataType).getAltitude() + ", ";
                sql += ((Airport) dataType).getTimezone() + ", '";
                sql += ((Airport) dataType).getDst() + between;
                sql += ((Airport) dataType).getTzDatabase().replaceAll("'", "''") + "');";
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
