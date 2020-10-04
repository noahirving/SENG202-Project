package seng202.team4.model;

import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public ResultSet getResultSet(String query) {
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
                return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
    public static void setup() throws SQLException {
        DatabaseManager.setUp();
        Main m = new Main();
        //con = DatabaseManager.connect();
    }

    /**
     * Close database connection after the test.
     * @throws Exception exception when database connection is not closed properly.
     */
    @AfterClass
    public static void teardown() throws Exception {
        //rs.close();
        //con.close();
        Main.deleteDatabase();
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

    @Test
    public void addNewAirline() throws IOException {
        File testData = copyToFolder(Path.AIRLINE_TEST_RSC_VALID);
        DataLoader.uploadData("Default", testData, new Airline());
        Airline toInsert = new Airline("213 Flight Unit","test","","TFU","","Russia",false);
        boolean insertResult = DataLoader.addNewRecord(toInsert, "Default");
        Assert.assertEquals(insertResult, true);
    }

    @Test
    public void addRouteToSelectedTest() throws SQLException {
        Route toAdd = new Route("CZ","AKL","CAN",false,0,"787");
        DataLoader.addToRoutesSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from RoutesSelected where Airline = '" + toAdd.getAirlineCode() +
                "' and SourceAirport = '" + toAdd.getSourceAirportCode() + "' and DestinationAirport = '" +
                toAdd.getDestinationAirportCode() + "';";
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countCheck)) {
            int count = rs.getInt("count(*)");
            Assert.assertEquals(count, 1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(false);
        }
    }

    @Test
    public void deleteRouteFromSelectedTest() throws SQLException {
        Route toAdd = new Route("CZ","AKL","CAN",false,0,"787");
        DataLoader.addToRoutesSelectedDatabase(toAdd);
        DataLoader.removeFromRoutesSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from RoutesSelected where Airline = '" + toAdd.getAirlineCode() +
                "' and SourceAirport = '" + toAdd.getSourceAirportCode() + "' and DestinationAirport = '" +
                toAdd.getDestinationAirportCode() + "';";
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countCheck)) {
                int count = rs.getInt("count(*)");
                Assert.assertEquals(count, 0);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(false);
        }
    }

    @Test
    public void addAirportToSelectedTest() throws SQLException {
        Airport toAdd = new Airport("Egilsstadir","Egilsstadir","Iceland","EGS","BIEG",65.283333,-14.401389,Double.parseDouble("76"),Float.parseFloat("0"),"N".charAt(0),"Atlantic/Reykjavik");
        DataLoader.addToAirportsSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from AirportsSelected where name = '" + toAdd.getName() +
                "';";
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countCheck)) {
            int count = rs.getInt("count(*)");
            Assert.assertEquals(count, 1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(false);
        }
    }

    @Test
    public void deleteAirportFromSelectedTest() throws SQLException {
        Airport toAdd = new Airport("Egilsstadir","Egilsstadir","Iceland","EGS","BIEG",65.283333,-14.401389,Double.parseDouble("76"),Float.parseFloat("0"),"N".charAt(0),"Atlantic/Reykjavik");
        DataLoader.addToAirportsSelectedDatabase(toAdd);
        DataLoader.removeFromAirportsSelectedDatabase(toAdd);
        String countCheck = "SELECT count(*) from AirportsSelected where name = '" + toAdd.getName() +
                "';";
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countCheck)) {
            int count = rs.getInt("count(*)");
            Assert.assertEquals(count, 0);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(false);
        }
    }
    @Test
   public void deleteRecordTest() throws IOException {
        File testData = copyToFolder(Path.AIRLINE_TEST_RSC_VALID);
        DataLoader.uploadData("Default", testData, new Airline());
        boolean deleteResult = DataLoader.deleteRecord(10, "Airline");
        Assert.assertEquals(deleteResult, true);
   }

    @Test
    public void updateRecordTest() throws IOException {
        File testData = copyToFolder(Path.AIRLINE_TEST_RSC_VALID);
        DataLoader.uploadData("Default", testData, new Airline());
        Airline toUpdate = new Airline("213 Flight Unit","test","","TFU","","Russia",false);
        boolean updateResult = DataLoader.updateRecord(toUpdate, "Default");
        Assert.assertEquals(updateResult, true);
    }
}
