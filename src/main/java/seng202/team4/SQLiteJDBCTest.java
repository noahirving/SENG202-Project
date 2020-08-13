package seng202.team4;

import java.sql.*;

public class SQLiteJDBCTest {
    public static void main(String[] args) {
        Connection c;
        Statement stmt;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");

            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO AIRLINES ('AIRLINE ID', 'NAME', 'ALIAS', 'IATA', 'ICAO', 'CALLSIGN', 'COUNTRY', 'RECENTLY ACTIVE') " +
                    "VALUES (20000, 'Test Name', 'Test Alias', 'Test IATA', 'Test ICAO', 'Test Callsign', 'Test Country', 'Test Active');";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }
}
