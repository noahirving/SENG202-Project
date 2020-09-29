package seng202.team4.model;

import seng202.team4.controller.FileUploadController;

import java.io.File;
import java.sql.*;

/**
 * Performs basic interaction with the database that bypasses
 * the errors thrown by the database.
 */
public abstract class DatabaseManager {

    private static final String SET_TABLE =
            "(" +
            "\"ID\" INTEGER NOT NULL UNIQUE," +
            "\"Name\" STRING NOT NULL," +
            "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
            ")";

    private static final String AIRLINE_SET_TABLE = "CREATE TABLE \"AirlineSet\" " + SET_TABLE;
    private static final String AIRLINE_TABLE =
            "CREATE TABLE \"Airline\" (" +
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

    private static final String AIRPORT_SET_TABLE = "CREATE TABLE \"AirportSet\" " + SET_TABLE;
    private static final String AIRPORT_TABLE =
            "CREATE TABLE \"Airport\" (" +
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
            "\"DST\" STRING," +
            "\"TzDatabaseTime\" STRING," +
            "\"SetID\" INTEGER NOT NULL," +
            "FOREIGN KEY (SetID) REFERENCES AirportSet (SetID)," +
            "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
            ")";

    private static final String ROUTE_SET_TABLE = "CREATE TABLE \"RouteSet\" " + SET_TABLE;
    private static final String ROUTE_TABLE =
            "CREATE TABLE \"Route\" (" +
            "\"ID\" INTEGER NOT NULL UNIQUE," +
            "\"Airline\" STRING," +
            "\"SourceAirport\" STRING," +
            "\"DestinationAirport\" STRING," +
            "\"Codeshare\" STRING," +
            "\"Stops\" INTEGER," +
            "\"Equipment\" STRING," +
            "\"Distance\" INTEGER," +
            "\"SetID\" INTEGER NOT NULL," +
            "FOREIGN KEY (SetID) REFERENCES RouteSet (SetID)," +
            "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
            ")";

    private static final String FLIGHT_PATH_SET_TABLE = "CREATE TABLE \"FlightPathSet\" " + SET_TABLE;
    private static final String FLIGHT_PATH_TABLE =
            "CREATE TABLE \"FlightPath\" (" +
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

    private static final String ROUTES_SELECTED_TABLE =
            "CREATE TABLE \"RoutesSelected\" (" +
            "\"ID\"\tINTEGER NOT NULL UNIQUE," +
            "\"Airline\" STRING," +
            "\"SourceAirport\" STRING," +
            "\"DestinationAirport\" STRING," +
            "\"Equipment\" STRING," +
            "\"Distance\" INTEGER," +
            "\"CarbonEmissions\" INTEGER," +
            "PRIMARY KEY(\"ID\")" +
            ")";

    private static final String AIRPORTS_SELECTED_TABLE =
            "CREATE TABLE \"AirportsSelected\" (" +
            "\"ID\"\tINTEGER NOT NULL UNIQUE," +
            "\"Name\" STRING," +
            "\"Longitude\" DOUBLE," +
            "\"Latitude\" DOUBLE," +
            "PRIMARY KEY(\"ID\" AUTOINCREMENT)" +
            ")";

    /**
     * Gets a new {@link java.sql.Connection Connection}
     * @return a new {@link java.sql.Connection Connection} if connection does not throw an error, otherwise 'null'
     */
    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection(Path.DATABASE_CONNECTION);
            c.setAutoCommit(false);
            return c;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Disconnects from a given connection.
     * @param c the connection to disconnect from.
     * @return 'true' if disconnected successfully, 'false' otherwise.
     */
    public static boolean disconnect(Connection c) {
        try {
            c.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a statement for a given connection.
     * @param c the connection the statement is made for.
     * @return the new statement, 'null' if failed to make a statement.
     */
    public static Statement getStatement(Connection c) {
        try {
            Statement stmt = c.createStatement();
            return stmt;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Initial setup for the application. Creates a
     * new directory for the application's database
     * and a new database if they do not exist.
     */
    static void setUp() {
        // Creates a new directory and database if the directory doesn't exist.
        File directory = new File(Path.DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
            newDatabase();
//            new Main().loadDefaultData();
        }

        else {
            // If directory exists but database does not a new database is created.
            File database = new File(Path.DATABASE);
            if (!database.exists()) {
                newDatabase();
//                new Main().loadDefaultData();
            }
        }
    }

    /**
     * Creates a new database inserting all the tables
     * required for the database.
     */
    private static void newDatabase () {
        Connection c = DatabaseManager.connect();
        if (c != null) {
            try {
                Statement stmt = c.createStatement();
                stmt.addBatch(AIRLINE_SET_TABLE);
                stmt.addBatch(AIRPORT_SET_TABLE);
                stmt.addBatch(ROUTE_SET_TABLE);
                stmt.addBatch(FLIGHT_PATH_SET_TABLE);

                stmt.addBatch(AIRLINE_TABLE);
                stmt.addBatch(AIRPORT_TABLE);
                stmt.addBatch(ROUTE_TABLE);
                stmt.addBatch(FLIGHT_PATH_TABLE);

                stmt.addBatch(ROUTES_SELECTED_TABLE);
                stmt.addBatch(AIRPORTS_SELECTED_TABLE);
                stmt.executeBatch();
                stmt.close();
                c.commit();


            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DatabaseManager.disconnect(c);
            }
        }
    }
    /*
    public void updateDatabase() {
        try {
            stmt.executeBatch();

            stmt.close();
            c.commit();
            c.close();
        } catch (SQLException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

    }*/
}
