package seng202.team4.model;

import seng202.team4.view.MainApplication;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws IOException {

        DatabaseManager.setUp();

        Main m = new Main();
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

        DataLoader.uploadData("Default", airline, new Airline());
        DataLoader.uploadData("Default", airport, new Airport());
        DataLoader.uploadData("Default", route, new Route());
        DataLoader.uploadData("Default", flightPath, new FlightPath());
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

    public File copyToFolder(String filename) throws IOException {

        InputStream initialStream = (this.getClass().getResourceAsStream(filename));
        File targetFile = new File(Path.DIRECTORY + filename);

        java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return targetFile;
    }


}