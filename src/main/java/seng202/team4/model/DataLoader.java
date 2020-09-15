package seng202.team4.model;

import seng202.team4.Path;
import seng202.team4.controller.Calculations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

/**
 * Initialises the DataLoader class by reading raw data files,
 * creating datatype classes, and adding them to a DataList class.
 */
public abstract class DataLoader {


    public static boolean uploadAirlineData(File filePath) {
        Airline airline = new Airline();
        return uploadData("default", filePath, airline);
    }

    public static boolean uploadAirportData(File filePath) {
        Airport airport = new Airport();
        return uploadData("default", filePath, airport);
    }

    public static boolean uploadRouteData(File filePath) {
        Route route = new Route();
        return uploadData("default", filePath, route);
    }

    public static Boolean uploadData(String name, File filePath, DataType dataType) {
        //makeSet(name);
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        if (c != null && stmt != null) {
            try {
                String setInsertStatement = "INSERT INTO " + dataType.getSetName() + " ('NAME') VALUES ('" + name + "');";
                String idQuery = "SELECT ID FROM " + dataType.getSetName() + " WHERE Name = '" + name + "';";
                stmt.executeUpdate(setInsertStatement);
                ResultSet rs = stmt.executeQuery(idQuery);
                rs.next();
                int setID = rs.getInt("ID");

                BufferedReader buffer = new BufferedReader(new FileReader(filePath));

                String line = buffer.readLine();
                while (line != null && line.trim().length() > 0) {
                    DataType data = dataType.newDataType(line);
                    stmt.addBatch(data.getInsertStatement(setID));
                    line = buffer.readLine();
                }
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

    public static void addNewRecord(DataType dataType, String setName){
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        try {
            String setIdQuery = "SELECT ID FROM " + dataType.getSetName() + " WHERE Name = '" + setName + "';";
            ResultSet rs = stmt.executeQuery(setIdQuery);
            rs.next();
            int setID = rs.getInt("ID");
            stmt.executeUpdate(dataType.getInsertStatement(setID));
            stmt.close();
            c.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseManager.disconnect(c);
        }
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
        String between = "' and ";

        String query = "DELETE FROM RoutesSelected WHERE "
                + "Airline = '" + route.getAirlineCode().replaceAll("'", "''") + between
                + "SourceAirport = '" + route.getSourceAirportCode().replaceAll("'", "''") + between
                + "DestinationAirport = '" + route.getDestinationAirportCode().replaceAll("'", "''") + between
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
        String between = "', '";

        String query = "INSERT INTO AirportsSelected ('Name', 'Longitude', 'Latitude') "
                + "VALUES ('"
                + airport.getName().replaceAll("'", "''") + between
                + airport.getLongitude() + between
                + airport.getLatitude()
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

    public static void removeFromAirportsSelectedDatabase(Airport airport) {
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        String between = "' and ";

        String query = "DELETE FROM AirportsSelected WHERE "
                + "Name = '" + airport.getName() + between
                + "Longitude = '" + airport.getLongitude() + between
                + "Latitude = '" + airport.getLatitude() + "'";
        try {
            stmt.executeUpdate(query);
            con.commit();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        DatabaseManager.disconnect(con);
    }
}
