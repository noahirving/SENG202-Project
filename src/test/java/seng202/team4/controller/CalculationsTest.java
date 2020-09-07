package seng202.team4.controller;

import junit.framework.TestCase;
import org.junit.Test;
import seng202.team4.model.DatabaseManager;
import seng202.team4.model.Route;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CalculationsTest extends TestCase {

    private Connection con;
    private ResultSet rs;

    public void setUp() throws Exception {
        super.setUp();
        con = DatabaseManager.connect();
        String query = "SELECT Latitude,Longitude from Airport WHERE IATA = 'GKA' OR IATA = 'MAG'";
        Statement stmt = DatabaseManager.getStatement(con);
        rs = stmt.executeQuery(query);
    }

    public void tearDown() throws Exception {
        DatabaseManager.disconnect(con);
    }

    @Test
    public void testLat1() throws Exception {
        double lat1 = rs.getDouble("Latitude");
        assertEquals(-6.081689, lat1);
    }

    @Test
    public void testLong1() throws Exception {
        double long1 = rs.getDouble("Longitude");
        assertEquals(145.391881, long1);
    }

    @Test
    public void testLat2() throws Exception {
        rs.next(); rs.next();
        double lat2 = rs.getDouble("Latitude");
        assertEquals(-5.207083, lat2);
    }

    @Test
    public void testLong2() throws Exception {
        rs.next(); rs.next();
        double long2 = rs.getDouble("Longitude");
        assertEquals(145.7887, long2);
    }

    @Test
    public void testCalculateDistance() throws Exception {
        double distance = Calculations.calculateDistance("GKA", "MAG", con);
        assertEquals(106.70511125420559, distance);
    }

    @Test
    public void testCalculateEmissions() throws Exception {
        Route route = new Route();
        route.setDistance(1000.0);
        double emissions = Calculations.calculateEmissions(route);
        assertEquals(115.0, emissions);
    }
}