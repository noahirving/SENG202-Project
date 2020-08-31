package seng202.team4;

import org.apache.commons.io.FileUtils;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DatabaseManager;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws IOException {

        //DataLoader loader = new DataLoader();

        Main m = new Main();
        m.createDirectory();
        m.deleteDB();
        m.newDB();
        m.loadTest();

        MainApplication.main(args);

        FlightPlanner fp = new FlightPlanner();
        fp.searchFlight();
    }

    public void loadTest() throws IOException {
        File airport = copyToFolder(Path.airportRsc);
        File airline = copyToFolder(Path.airlineRsc);
        File route = copyToFolder(Path.routeRsc);

        DataLoader.uploadAirportData(airport);
        DataLoader.uploadAirlineData(airline);
        DataLoader.uploadRouteData(route);
    }

    public void deleteDB() {
        try
        {
            File file = new File(Path.database);
            if(file.delete()) {
                System.out.println(file.getName() + " deleted");
            }
            else {
                System.out.println("Delete failed");
            }
        }
        catch(Exception e)
        {
            System.out.println("Invalid File to delete");
            e.printStackTrace();
        }
    }

    public void newDB () {
        Connection c = DatabaseManager.connect();
        if (c != null) {
            try {
                DatabaseMetaData metaData = c.getMetaData();
                System.out.println("The driver name is " + metaData.getDriverName());
                System.out.println("A new database has been created.");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            DatabaseManager.disconnect(c);

            String airlineTable = "CREATE TABLE \"Airline\" (" +
                    "\"AirlineID\" INTEGER NOT NULL UNIQUE," +
                    "\"Name\" STRING," +
                    "\"Alias\" STRING," +
                    "\"IATA\" STRING," +
                    "\"ICAO\" STRING," +
                    "\"Callsign\" STRING," +
                    "\"Country\" STRING," +
                    "\"RecentlyActive\" STRING," +
                    "PRIMARY KEY(\"AirlineID\" AUTOINCREMENT)" +
                    ")";
            String airportTable = "CREATE TABLE \"Airport\" (" +
                    "\"ID\" INTEGER NOT NULL UNIQUE," +
                    "\"Name\" STRING," +
                    "\"City\" STRING," +
                    "\"Country\" STRING," +
                    "\"IATA\" STRING," +
                    "\"ICAO\" STRING," +
                    "\"Latitude\" DOUBLE," +
                    "\"Longitude\" DOUBLE," +
                    "\"Altitude\" DOUBLE," +
                    "\"Timezone\" DOUBLE," +
                    "\"DST\"\tSTRING," +
                    "\"TzDatabaseTime\" STRING," +
                    "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
                    ")";
            String routeTable = "CREATE TABLE \"Route\" (" +
                    "\"ID\"\tINTEGER NOT NULL UNIQUE," +
                    "\"Airline\" STRING," +
                    "\"AirlineID\" INTEGER," +
                    "\"SourceAirport\" STRING," +
                    "\"SourceAirportID\" INTEGER," +
                    "\"DestinationAirport\" STRING," +
                    "\"DestinationAirportID\" INTEGER," +
                    "\"Codeshare\" STRING," +
                    "\"Stops\" INTEGER," +
                    "\"Equipment\" STRING," +
                    "\"CarbonEmissions\" INTEGER," +
                    "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
                    ")";


            createNewTable(airlineTable);
            createNewTable(airportTable);
            createNewTable(routeTable);
        }
    }

    public static void createNewTable(String table) {

        try (Connection c = DriverManager.getConnection(Path.databaseConnection);
             Statement stmt = c.createStatement()
        ) {
            stmt.execute(table);
            stmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public File copyToFolder(String filename) throws IOException {

        InputStream initialStream = (this.getClass().getResourceAsStream(filename));
        File targetFile = new File(Path.directory + filename);

        java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return targetFile;
    }

    public void createDirectory() {
        File folder = new File(Path.directory);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }
}