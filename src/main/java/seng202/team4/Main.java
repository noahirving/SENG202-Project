package seng202.team4;

import org.apache.commons.io.FileUtils;
import seng202.team4.model.DataLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {

        //DataLoader loader = new DataLoader();

        Main m = new Main();
        m.deleteDB();
        m.newDB();
        m.loadTest();

        SampleApplication.main(args);
    }

    public void loadTest() {
        File airport = new File(Path.airportRsc);
        File airline = new File(Path.airlineRsc);
        File route = new File(Path.routeRsc);
        DataLoader.uploadAirportData(airport);
        DataLoader.uploadAirlineData(airline);
        DataLoader.uploadRouteData(route);
    }

    public void deleteDB() {
        try
        {
            File file = new File("src/main/resources/database.db");
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
            File src = new File("src/main/resources/database_empty.db");
            File dst = new File("src/main/resources/database.db");
            FileUtils.copyFile(src, dst);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not copy DB");
        }
    }
}