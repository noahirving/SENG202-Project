package seng202.team4.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

/**
 * Handles loading data into the database.
 */
public abstract class DataLoader {

    /**
     * Uploads data to the database from a file of a specific dataType with a name for the new set of data.
     * @param setName   the name of the new set of data.
     * @param file      the file that's being uploaded to the database.
     * @param dataType  the data type of new data that's being uploaded.
     * @return 'true' if data was successfully uploaded, 'false' otherwise.
     */
    public static Boolean uploadData(String setName, File file, DataType dataType) {
        Connection c = DatabaseManager.connect(); // TODO: throw connection error
        if (c != null) {
            Statement stmt = DatabaseManager.getStatement(c);
            try {
                // Inserts a new set into the related dataType's set table
                String setInsertStatement = "INSERT INTO " + dataType.getSetName() + " ('NAME') VALUES ('" + setName + "');";
                stmt.executeUpdate(setInsertStatement);

                int setID = getSetID(setName, dataType, stmt);

                // Reads lines from file and adds valid lines to statement batch
                BufferedReader buffer = new BufferedReader(new FileReader(file));
                String line = buffer.readLine();
                while (line != null && line.trim().length() > 0) {
                    DataType data = dataType.newDataType(line); // TODO: Replace with getValid
                    stmt.addBatch(data.getInsertStatement(setID));
                    line = buffer.readLine();
                }

                // Commits changes to database
                stmt.executeBatch();
                stmt.close();
                c.commit();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return  false;

            } finally {
                DatabaseManager.disconnect(c);
            }
        }
        return false;
    }

    /**
     * Adds a new record to the database.
     * @param dataType  the type of data the new record is.
     * @param setName   the name of the set the data will be added to.
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
     * @param setName the name of the set.
     * @param dataType the data type of the set.
     * @param stmt the statement used to to execute the query.
     * @return the ID of the set.
     * @throws SQLException
     */
    private static int getSetID(String setName, DataType dataType, Statement stmt) throws SQLException{
        String setIdQuery = "SELECT ID FROM " + dataType.getSetName() + " WHERE Name = '" + setName + "';";
        ResultSet rs = stmt.executeQuery(setIdQuery);
        rs.next();
        return rs.getInt("ID");
    }

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
            //System.out.println(routes.get(index).getDistance());
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
}
