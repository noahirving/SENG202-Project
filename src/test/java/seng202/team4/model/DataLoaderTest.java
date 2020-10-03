package seng202.team4.model;

import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Performs the tests for Airline class.
 * Simple setters and getters are not tested.
 */

public class DataLoaderTest {

    private static Connection con;
    //private static ResultSet rs;


    /**
     * Gets result set from the given query.
     * @param query the query that requests the data.
     * @return      returns the values in the database that matches the query.
     * @throws SQLException exception to catch database access errors.
     */
    public ResultSet getResultSet(String query) throws SQLException {
        ResultSet rs = con.createStatement().executeQuery(query);
        return rs;
    }

    /**
     * Copies the provided file to a target file.
     *
     * @param fileName String name of source file
     * @return File the final file with the source files contents
     * @throws IOException if an exception occurs during the copy operation
     */
    public File copyToFolder(String fileName) throws IOException {

        InputStream initialStream = (this.getClass().getResourceAsStream(fileName));
        File targetFile = new File(Path.DIRECTORY + fileName);

        java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return targetFile;
    }
    /**
     * Set up the database to be used for the test.
     */
    @BeforeClass
    public static void setup() {
        DatabaseManager.setUp();
        Main m = new Main();

        con = DatabaseManager.connect();
    }

    /**
     * Close database connection after the test.
     * @throws Exception exception when database connection is not closed properly.
     */
    @AfterClass
    public static void teardown() throws Exception {
        //rs.close();
        Main.deleteDatabase();
        DatabaseManager.disconnect(con);
    }

    @Test
    public void uploadDataTestAirlineValid() throws IOException {
        ArrayList<String> invalidMsg;
        File testData = copyToFolder(Path.AIRLINE_TEST_RSC_VALID);
        invalidMsg = DataLoader.uploadData("Default", testData, new Airline());

        Assert.assertEquals(invalidMsg.size(), 0);
    }

    @Test
    public void uploadDataTestAirlineInvalid() throws IOException {
        ArrayList<String> invalidLines;
        File testDataWrong = copyToFolder(Path.AIRLINE_TEST_RSC_INVALID);
        invalidLines = DataLoader.uploadData("Default", testDataWrong, new Airline());
        Assert.assertEquals(invalidLines.size(), 1);
    }

    /*@Test
    public void addNewAirport() throws SQLException {
        Airport toInsert = new Airport("12","Egilsstadir","Iceland","EGS","BIEG",65.283333,-14.401389,Double.parseDouble("76"),Float.parseFloat("0"),"N".charAt(0),"Atlantic/Reykjavik");
        DataLoader.addNewRecord(toInsert, "TestSet");
        String countCheck = "SELECT count(*) from Airport where id = '" + toInsert.getId() + "';";
        ResultSet rs = getResultSet(countCheck);
        int count = rs.getInt("count(*)");
        Assert.assertEquals(count, 1);
        rs.close();
    }

    @Test
    public void addRouteToSelectedTest() throws SQLException {
        Route toAdd = new Route("CZ","AKL","CAN",false,0,"787");
        DataLoader.addToRoutesSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from RoutesSelected where Airline = '" + toAdd.getAirlineCode() +
                "' and SourceAirport = '" + toAdd.getSourceAirportCode() + "' and DestinationAirport = '" +
                toAdd.getDestinationAirportCode() + "';";
        ResultSet rs = getResultSet(countCheck);
        int count = rs.getInt("count(*)");
        Assert.assertEquals(count, 1);
        rs.close();
    }*/

    @Test
    public void deleteRouteFromSelectedTest() throws SQLException {
        Route toAdd = new Route("CZ","AKL","CAN",false,0,"787");
        DataLoader.addToRoutesSelectedDatabase(toAdd);
        DataLoader.removeFromRoutesSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from RoutesSelected where Airline = '" + toAdd.getAirlineCode() +
                "' and SourceAirport = '" + toAdd.getSourceAirportCode() + "' and DestinationAirport = '" +
                toAdd.getDestinationAirportCode() + "';";
        ResultSet rs = getResultSet(countCheck);
        int count = rs.getInt("count(*)");
        Assert.assertEquals(count, 0);
        rs.close();
    }

    /*@Test
    public void addAirportToSelectedTest() throws SQLException {
        Airport toAdd = new Airport("12","Egilsstadir","Iceland","EGS","BIEG",65.283333,-14.401389,Double.parseDouble("76"),Float.parseFloat("0"),"N".charAt(0),"Atlantic/Reykjavik");
        DataLoader.addToAirportsSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from AirportsSelected where id = '" + toAdd.getId() +
                ";'";
        ResultSet rs = getResultSet(countCheck);
        int count = rs.getInt("count(*)");
        Assert.assertEquals(count, 1);
        rs.close();
    }

    @Test
    public void deleteAirportFromSelectedTest() throws SQLException {
        Airport toAdd = new Airport("12","Egilsstadir","Iceland","EGS","BIEG",65.283333,-14.401389,Double.parseDouble("76"),Float.parseFloat("0"),"N".charAt(0),"Atlantic/Reykjavik");
        DataLoader.addToAirportsSelectedDatabase(toAdd);
        DataLoader.removeFromAirportsSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from AirportsSelected where id = '" + toAdd.getId() +
                ";'";
        ResultSet rs = getResultSet(countCheck);
        int count = rs.getInt("count(*)");
        Assert.assertEquals(count, 0);
        rs.close();
    }*/

//    @Test
//    public void deleteRowTest() throws SQLException {
//
//    }
}
