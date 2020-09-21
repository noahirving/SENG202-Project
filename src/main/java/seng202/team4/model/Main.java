package seng202.team4.model;

import seng202.team4.view.MainApplication;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws IOException {

        //deleteDatabase();
        DatabaseManager.setUp();

        //Main m = new Main();
        //m.loadDefaultData();

        MainApplication.main(args);

        //FlightPlanner fp = new FlightPlanner();
        //fp.searchFlight();
    }

    public void loadDefaultData() {
        // TODO: Check that default data is not already in database (temporary fix is deleting the database everytime).
        try {
            File airport = copyToFolder(Path.AIRPORT_RSC);
            File airline = copyToFolder(Path.AIRLINE_RSC);
            File route = copyToFolder(Path.ROUTE_RSC);
            File flightPath = copyToFolder(Path.FLIGHT_PATH_RSC);

            DataLoader.uploadData("Default", airline, new Airline());
            DataLoader.uploadData("Default", airport, new Airport());
            DataLoader.uploadData("Default", route, new Route());
            DataLoader.uploadData("Default", flightPath, new FlightPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteDatabase() {
        try
        {
            File file = new File(Path.DATABASE);
            if(file.delete()) {
                System.out.println("Database deleted");
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


}