package seng202.team4;

import org.apache.commons.io.FileUtils;
import seng202.team4.model.DataLoader;

import java.io.*;
import java.nio.file.StandardCopyOption;

public class Main {

    public static void main(String[] args) throws IOException {

        //DataLoader loader = new DataLoader();

        Main m = new Main();
        m.createDirectory();
        m.deleteDB();
        m.newDB();
        m.loadTest();

        MainApplication.main(args);

        FlightPlanner fp = new FlightPlanner();
        fp.searchFlight();
    }

    public void loadTest() throws IOException {
        copyToFolder(Path.airportRsc);
        copyToFolder(Path.airlineRsc);
        copyToFolder(Path.routeRsc);

        File airport = new File(Path.directory + "\\airports.txt");
        File airline = new File(Path.directory + "\\airlines.txt");
        File route = new File(Path.directory + "\\routes.txt");

        DataLoader.uploadAirportData(airport);
        DataLoader.uploadAirlineData(airline);
        DataLoader.uploadRouteData(route);
    }

    public void deleteDB() {
        try
        {
            File file = new File(Path.database);
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

    public void newDB() {

        try {
            copyToFolder(Path.emptyDatabase);
            File src = new File(Path.directory + "\\database_empty.db");
            File dst = new File(Path.database);
            FileUtils.copyFile(src, dst);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not copy DB");
        }
    }

    public void copyToFolder(String filename) throws IOException {

        InputStream initialStream = (this.getClass().getResourceAsStream("/" + filename));
        File targetFile = new File(Path.directory + "\\" + filename);

        java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public void createDirectory() {
        File folder = new File(Path.directory);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }
}