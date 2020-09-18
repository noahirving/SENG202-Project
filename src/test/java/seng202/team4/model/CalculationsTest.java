package seng202.team4.model;

import org.junit.*;
import seng202.team4.model.Calculations;
import seng202.team4.model.Main;
import seng202.team4.model.DatabaseManager;
import seng202.team4.model.Route;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalculationsTest {

    private static Connection con;
    private static ResultSet rs;

    String query1 = "SELECT Latitude,Longitude from Airport WHERE IATA = 'GKA'";
    String query2 = "SELECT Latitude,Longitude from Airport WHERE IATA = 'MAG'";

    public ResultSet getResultSet(String query) throws SQLException {
        rs = con.createStatement().executeQuery(query);
        return rs;
    }

    @BeforeClass
    public static void setup() throws Exception {
        Main m = new Main();
        m.createDirectory();
        m.deleteDB();
        m.newDB();
        m.loadTest();

        con = DatabaseManager.connect();
    }

    @AfterClass
    public static void teardown() throws Exception {
        rs.close();
        DatabaseManager.disconnect(con);
    }

    @Test
    public void testLat1() throws Exception {
        rs = getResultSet(query1);
        double lat1 = rs.getDouble("Latitude");
        Assert.assertEquals(-6.081689, lat1, 0);
    }

    @Test
    public void testLong1() throws Exception {
        rs = getResultSet(query1);
        double long1 = rs.getDouble("Longitude");
        Assert.assertEquals(145.391881, long1, 0);
    }

    @Test
    public void testLat2() throws Exception {
        rs = getResultSet(query2);
        double lat2 = rs.getDouble("Latitude");
        Assert.assertEquals(-5.207083, lat2, 0);
    }

    @Test
    public void testLong2() throws Exception {
        rs = getResultSet(query2);
        double long2 = rs.getDouble("Longitude");
        Assert.assertEquals(145.7887, long2, 0);
    }

    @Test
    public void testCalculateDistance() throws Exception {
        double distance = Calculations.calculateDistance("GKA", "MAG", con);
        Assert.assertEquals(106.70511125420559, distance, 0);
    }

    @Test
    public void testCalculateEmissions() {
        Route route = new Route();
        route.setDistance(1000.0);
        double emissions = Calculations.calculateEmissions(route);
        Assert.assertEquals(115.0, emissions, 0);
    }
}