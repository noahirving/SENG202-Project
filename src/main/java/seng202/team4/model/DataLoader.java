package seng202.team4.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Handles loading data into the database. The data can be loaded from either:
 * the default data, the user uploading a file or the user adding a new record
 */
public abstract class DataLoader {

    /**
     * Uploads data to the database from a file of a specific dataType with a name for the new set of data.
     * @param setName String the name of the new set of data.
     * @param file File the file that's being uploaded to the database.
     * @param dataType DataType the data type of new data that's being uploaded.
     * @return ArrayList<String> an ArrayList of erroneous lines
     */
    public static ArrayList<String> uploadData(String setName, File file, DataType dataType) {
        Connection c = DatabaseManager.connect(); // TODO: throw connection error
        if (c != null) {
            Statement stmt = DatabaseManager.getStatement(c);
            try {
                // Inserts a new set into the related dataType's set table
                String setInsertStatement = "INSERT INTO " + dataType.getSetName() + " ('NAME') VALUES ('" + setName + "');";
                stmt.executeUpdate(setInsertStatement);

                int setID = getSetID(setName, dataType, stmt);

                // Stores the invalid lines in the file
                ArrayList<String> invalidLines = new ArrayList<>();

                // Reads lines from file and adds valid lines to statement batch
                BufferedReader buffer = new BufferedReader(new FileReader(file));
                String line = buffer.readLine();
                while (line != null && line.trim().length() > 0) {
                    ArrayList<String> errorMessage = new ArrayList<>();
                    DataType data = dataType.getValid(line, errorMessage);
                    if (data != null) {
                        stmt.addBatch(data.getInsertStatement(setID)); // Add to database
                    }
                    else {
                        if (errorMessage.size() > 0) {
                            invalidLines.add(line + " (" + errorMessage.get(0) + ")");
                            System.out.println(dataType.getTypeName());
                            System.out.println(line);
                            System.out.println(errorMessage.get(0) + "\n\n");
                        }
                    }
                    line = buffer.readLine();
                }

                // Commits changes to database
                stmt.executeBatch();
                stmt.close();
                c.commit();
                return invalidLines;

            } catch (Exception e) {
                e.printStackTrace();
                return null;

            } finally {
                DatabaseManager.disconnect(c);
            }
        }
        return null;
    }

    /**
     * Adds a new record to the database.
     * @param dataType DataType the type of data the new record is.
     * @param setName String the name of the set the data will be added to.
     * @return 'true' if record was successfully inserted into the database, 'false' otherwise
     */
    public static boolean addNewRecord(DataType dataType, String setName){
        Connection c = DatabaseManager.connect(); // TODO: throw connection error
        if (c != null) {
            Statement stmt = DatabaseManager.getStatement(c);
            try {
                int setID = getSetID(setName, dataType, stmt);

                // Inserts the new record into the database
                stmt.executeUpdate(dataType.getInsertStatement(setID));
                stmt.close();
                c.commit();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;

            } finally {
                DatabaseManager.disconnect(c);
            }
        }
        return false;
    }

    /**
     * Gets the ID of a set given the sets name and what data type it is.
     * @param setName String the name of the set.
     * @param dataType DataType the data type of the set.
     * @param stmt Statement the statement used to to execute the query.
     * @return int the ID of the set.
     * @throws SQLException SQL Exception
     */
    private static int getSetID(String setName, DataType dataType, Statement stmt) throws SQLException{
        String setIdQuery = "SELECT ID FROM " + dataType.getSetName() + " WHERE Name = '" + setName + "';";
        ResultSet rs = stmt.executeQuery(setIdQuery);
        rs.next();
        return rs.getInt("ID");
    }

    /**
     * Adds a route to the RoutesSelected database table
     * when the user clicks on its corresponding checkbox
     * The distance is also calculated via the Calculations
     * class before the route is inserted.
     *
     * @param route Route the route that has been selected
     *              with a checkbox
     */
    public static void addToRoutesSelectedDatabase(Route route) {
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        String between = "', '";

        Double distance = 0.0;
        String sourceAirport = route.getSourceAirportCode();
        String destAirport = route.getDestinationAirportCode();
        try {
            distance = Calculations.calculateDistance(sourceAirport, destAirport, con);
            route.setDistance(distance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double carbonEmitted = Calculations.calculateEmissions(route);
        String query = "INSERT INTO RoutesSelected ('Airline', 'SourceAirport', 'DestinationAirport', 'Equipment', 'Distance', 'CarbonEmissions') "
                + "VALUES ('"
                + route.getAirlineCode().replaceAll("'", "''") + between
                + route.getSourceAirportCode().replaceAll("'", "''") + between
                + route.getDestinationAirportCode().replaceAll("'", "''") + between
                + route.getPlaneTypeCode().replaceAll("'", "''") + between
                + route.getDistance() + between
                + carbonEmitted
                + "');";
        try {
            stmt.executeUpdate(query);
            con.commit();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        DatabaseManager.disconnect(con);
    }

    /**
     *  Removes a route from the RoutesSelected database table
     *  when its corresponding checkbox is unselected
     *
     * @param route Route the route to be removed
     */
    public static void removeFromRoutesSelectedDatabase(Route route) {
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        final String AND = "' and ";

        String query = "DELETE FROM RoutesSelected WHERE "
                + "Airline = '" + route.getAirlineCode().replaceAll("'", "''") + AND
                + "SourceAirport = '" + route.getSourceAirportCode().replaceAll("'", "''") + AND
                + "DestinationAirport = '" + route.getDestinationAirportCode().replaceAll("'", "''") + AND
                + "Equipment = '" + route.getPlaneTypeCode().replaceAll("'", "''") + "'";
        try {
            stmt.executeUpdate(query);
            con.commit();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        DatabaseManager.disconnect(con);
    }

    /**
     * Adds a route to the AirportsSelected database table
     * when the user clicks on its corresponding checkbox
     *
     * @param airport Airport the airport that has been selected
     *              with a checkbox
     */
    public static void addToAirportsSelectedDatabase(Airport airport) {
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        final String BETWEEN = "', '";

        String query = "INSERT INTO AirportsSelected ('Name', 'Longitude', 'Latitude') "
                + "VALUES ('"
                + airport.getName().replaceAll("'", "''") + BETWEEN
                + airport.getLongitude() + BETWEEN
                + airport.getLatitude()
                + "');";
        try {
            stmt.executeUpdate(query);
            con.commit();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseManager.disconnect(con);

    }

    /**
     *  Removes an airport from the AirportsSelected database table
     *  when its corresponding checkbox is unselected
     *
     * @param airport Airport the airport to be removed
     */
    public static void removeFromAirportsSelectedDatabase(Airport airport) {
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        final String AND = "' and ";

        String query = "DELETE FROM AirportsSelected WHERE "
                + "Name = '" + airport.getName() + AND
                + "Longitude = '" + airport.getLongitude() + AND
                + "Latitude = '" + airport.getLatitude() + "'";
        try {
            stmt.executeUpdate(query);
            con.commit();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        DatabaseManager.disconnect(con);
    }

    /**
     * Deletes a selected row from its corresponding database table
     * and the table view when the corresponding 'Delete' button
     * is clicked
     *
     * @param id int id of the object and record to be removed
     *           from the database table
     * @param table String the name of the database table where the record
     *              will be removed from
     * @return boolean true if delete successful, false otherwise
     */
    public static boolean deleteRecord(int id, String table) {
        String query = "Delete from " + table + " Where ID = " + id;

        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        try {
            stmt.executeUpdate(query);
            con.commit();
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            DatabaseManager.disconnect(con);
        }
    }
}
