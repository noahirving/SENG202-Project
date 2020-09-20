package seng202.team4.model;

import org.junit.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Test whether the calculations class gives the expected result
 * based on the carbon emissions and distance formula.
 */

public class CalculationsTest {

    private static Connection con;
    private static ResultSet rs;

    String query1 = "SELECT Latitude,Longitude from Airport WHERE IATA = 'GKA'";
    String query2 = "SELECT Latitude,Longitude from Airport WHERE IATA = 'MAG'";

    /**
     * Gets result set from the given query.
     * @param query the query that requests the data.
     * @return      returns the values in the database that matches the query.
     * @throws SQLException exception to catch database access errors.
     */
    public ResultSet getResultSet(String query) throws SQLException {
        rs = con.createStatement().executeQuery(query);
        return rs;
    }

    /**
     * Set up the database to be used for the test.
     * @throws Exception exception when database connection is not established properly.
     */
    @BeforeClass
    public static void setup() throws Exception {
        DatabaseManager.setUp();
        Main m = new Main();
        m.loadDefaultData();

        con = DatabaseManager.connect();
    }

    /**
     * Close database connection after the test.
     * @throws Exception exception when database connection is not closed properly.
     */
    @AfterClass
    public static void teardown() throws Exception {
        rs.close();
        Main.deleteDatabase();
        DatabaseManager.disconnect(con);
    }

    /**
     * Checks whether the correct latitude is taken from the database
     * based from the given query.
     * @throws Exception exception to catch database access errors.
     */
    @Test
    public void testLat1() throws Exception {
        rs = getResultSet(query1);
        double lat1 = rs.getDouble("Latitude");
        Assert.assertEquals(-6.081689, lat1, 0);
    }

    /**
     * Checks whether the correct longitude is taken from the database
     * based from the given query.
     * @throws Exception exception to catch database access errors.
     */
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

    /**
     * Tests whether the distance calculation is accurate up to 3 decimal places.
     * @throws Exception exception to catch database access errors.
     */
    @Test
    public void testCalculateDistance() throws Exception {
        double distance = Calculations.calculateDistance("GKA", "MAG", con);
        Assert.assertEquals(106.705, distance, 0.001);
    }

    /**
     * Test whether the calculated amount of flight carbon emission
     * is accurate up to 3 decimal places.
     */
    @Test
    public void testCalculateEmissions() {
        Route route = new Route();
        route.setDistance(1000.0);
        double emissions = Calculations.calculateEmissions(route);
        Assert.assertEquals(115.0, emissions, 0.001);
    }
}