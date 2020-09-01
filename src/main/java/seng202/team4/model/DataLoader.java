package seng202.team4.model;

import seng202.team4.Path;

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
                //System.out.println(setInsertStatement);

                stmt.executeUpdate(setInsertStatement);
                String idQuery = "SELECT 'ID' FROM " + dataType.getSetName() + " WHERE 'Name = " + name + "';";
                ResultSet rs = stmt.executeQuery(idQuery);
                rs.next();
                int SetID = 1; //rs.getInt("ID");
                BufferedReader buffer = new BufferedReader(new FileReader(filePath));

                String line = buffer.readLine();
                while (line != null && line.trim().length() > 0) {
                    DataType data = dataType.newDataType(line);
                    stmt.addBatch(data.getInsertStatement(SetID));
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
    /*
    public static void makeSet(String name) {
        try (Connection c = DriverManager.getConnection(Path.databaseConnection);
             Statement stmt = c.createStatement()
        ) {
            System.out.println(c.toString());
            stmt.executeUpdate("INSERT INTO " + dataType.getSetName() + "AIRLINESET ('NAME') VALUES ('" + name + "');");
            stmt.executeBatch();
            stmt.close();
            DatabaseManager.disconnect(c);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
