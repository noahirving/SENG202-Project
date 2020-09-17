package seng202.team4;

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

        //FlightPlanner fp = new FlightPlanner();
        //fp.searchFlight();
    }

    public void loadTest() throws IOException {
        File airport = copyToFolder(Path.AIRPORT_RSC);
        File airline = copyToFolder(Path.AIRLINE_RSC);
        File route = copyToFolder(Path.ROUTE_RSC);
        File flightPath = copyToFolder(Path.FLIGHT_PATH_RSC);

        DataLoader.uploadAirportData(airport);
        DataLoader.uploadAirlineData(airline);
        DataLoader.uploadRouteData(route);
        DataLoader.uploadFlightPathData(flightPath);
    }

    public void deleteDB() {
        try
        {
            File file = new File(Path.DATABASE);
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

            String setTable =
                    "(" +
                    "\"ID\" INTEGER NOT NULL UNIQUE," +
                    "\"Name\" STRING NOT NULL," +
                    "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
                    ")";

            String airlineSetTable = "CREATE TABLE \"AirlineSet\" " + setTable;
            String airlineTable = "CREATE TABLE \"Airline\" (" +
                    "\"ID\" INTEGER NOT NULL UNIQUE," +
                    "\"Name\" STRING," +
                    "\"Alias\" STRING," +
                    "\"IATA\" STRING," +
                    "\"ICAO\" STRING," +
                    "\"Callsign\" STRING," +
                    "\"Country\" STRING," +
                    "\"RecentlyActive\" STRING," +
                    "\"SetID\" INTEGER NOT NULL," +
                    "FOREIGN KEY (SetID) REFERENCES AirlineSet (SetID)," +
                    "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
                    ")";

            String airportSetTable = "CREATE TABLE \"AirportSet\" " + setTable;
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
                    "\"SetID\" INTEGER NOT NULL," +
                    "FOREIGN KEY (SetID) REFERENCES AirportSet (SetID)," +
                    "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
                    ")";

            String routeSetTable = "CREATE TABLE \"RouteSet\" " + setTable;
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
                    "\"Distance\" INTEGER," +
                    "\"SetID\" INTEGER NOT NULL," +
                    "FOREIGN KEY (SetID) REFERENCES RouteSet (SetID)," +
                    "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
                    ")";

            String flightPathSetTable = "CREATE TABLE \"FlightPathSet\" " + setTable;
            String flightPathTable = "CREATE TABLE \"FlightPath\" (" +
                    "\"ID\"\tINTEGER NOT NULL UNIQUE," +
                    "\"Type\" STRING," +
                    "\"FlightPathID\" INTEGER," +
                    "\"Altitude\" INTEGER," +
                    "\"Latitude\" DOUBLE," +
                    "\"Longitude\" DOUBLE," +
                    "\"SetID\" INTEGER NOT NULL," +
                    "FOREIGN KEY (SetID) REFERENCES FlightPathSet (SetID)," +
                    "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
                    ")";

            String routesSelectedTable = "CREATE TABLE \"RoutesSelected\" (" +
                    "\"ID\"\tINTEGER NOT NULL UNIQUE," +
                    "\"Airline\" STRING," +
                    "\"SourceAirport\" STRING," +
                    "\"DestinationAirport\" STRING," +
                    "\"Equipment\" STRING," +
                    "\"Distance\" INTEGER," +
                    "\"CarbonEmissions\" INTEGER," +
                    "PRIMARY KEY(\"ID\")" +
                    ")";

            String airportsSelectedTable = "CREATE TABLE \"AirportsSelected\" (" +
                    "\"ID\"\tINTEGER NOT NULL UNIQUE," +
                    "\"Name\" STRING," +
                    "\"Longitude\" DOUBLE," +
                    "\"Latitude\" DOUBLE," +
                    "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
                    ")";

            createNewTable(airlineSetTable);
            createNewTable(airportSetTable);
            createNewTable(routeSetTable);
            createNewTable(flightPathSetTable);

            createNewTable(airlineTable);
            createNewTable(airportTable);
            createNewTable(routeTable);
            createNewTable(flightPathTable);

            createNewTable(routesSelectedTable);
            createNewTable(airportsSelectedTable);
        }
        DatabaseManager.disconnect(c);
    }

    public static void createNewTable(String table) {

        try (Connection c = DriverManager.getConnection(Path.DATABASE_CONNECTION);
             Statement stmt = c.createStatement()
        ) {
            stmt.execute(table);
            stmt.close();
            DatabaseManager.disconnect(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public File copyToFolder(String filename) throws IOException {

        InputStream initialStream = (this.getClass().getResourceAsStream(filename));
        File targetFile = new File(Path.DIRECTORY + filename);

        java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return targetFile;
    }

    public void createDirectory() {
        File folder = new File(Path.DIRECTORY);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }
}