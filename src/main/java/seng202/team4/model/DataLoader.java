package seng202.team4.model;

import seng202.team4.Path;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Initialises the DataLoader class by reading raw data files,
 * creating datatype classes, and adding them to a DataList class.
 */
public class DataLoader {


    public boolean uploadAirlineData(String filePath) {
        Airline airline = new Airline();
        return uploadData(filePath, airline);
    }

    public boolean uploadAirportData(String filePath) {
        Airport airport = new Airport();
        return uploadData(filePath, airport);
    }

    public boolean uploadRouteData(String filePath) {
        Route route = new Route();
        return uploadData(filePath, route);
    }

    public Boolean uploadData(String filePath, DataType dataType) {
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        if (c != null && stmt != null) {
            try {
                BufferedReader buffer = new BufferedReader(new FileReader(filePath));
                String line = buffer.readLine();
                while (line != null) {
                    DataType data = dataType.newDataType(line);
                    stmt.addBatch(data.getInsertStatement());
                }
                stmt.executeBatch();
                stmt.close();
                c.commit();
                return true;
            } catch (Exception e) {
                return  false;
            } finally {
                DatabaseManager.disconnect(c);
            }
        }
        return false;
    }
}
