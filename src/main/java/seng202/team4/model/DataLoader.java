package seng202.team4.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Initialises the DataLoader class by reading raw data files,
 * creating datatype classes, and adding them to a DataList class.
 */
public abstract class DataLoader {


    public static boolean uploadAirlineData(File filePath) {
        Airline airline = new Airline();
        return uploadData(filePath, airline);
    }

    public static boolean uploadAirportData(File filePath) {
        Airport airport = new Airport();
        return uploadData(filePath, airport);
    }

    public static boolean uploadRouteData(File filePath) {
        Route route = new Route();
        return uploadData(filePath, route);
    }

    public static Boolean uploadData(File filePath, DataType dataType) {
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        if (c != null && stmt != null) {
            try {
                BufferedReader buffer = new BufferedReader(new FileReader(filePath));

                String line = buffer.readLine();
                while (line != null && line.trim().length() > 0) {
                    DataType data = dataType.newDataType(line);
                    stmt.addBatch(data.getInsertStatement());
                    line = buffer.readLine();
                }
                stmt.executeBatch();
                stmt.close();
                c.commit();
                return true;
            } catch (Exception e) {
                System.out.println("Caught exception: " + e.toString());
                System.out.println("Stack trace: \n" + e.getMessage());
                return  false;
            } finally {
                DatabaseManager.disconnect(c);
            }
        }
        return false;
    }
}
