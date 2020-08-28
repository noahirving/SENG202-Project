package seng202.team4;

import org.apache.commons.io.FileUtils;
import seng202.team4.model.DataLoader;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        //DataLoader loader = new DataLoader();

        Main m = new Main();
        m.deleteDB();
        //m.newDB();
        m.loadTest();

        MainApplication.main(args);
    }

    public void loadTest() {
        File airport = new File(getClass().getResource(Path.airportRsc).getPath());
        File airline = new File(getClass().getResource(Path.airlineRsc).getPath());
        File route = new File(getClass().getResource(Path.routeRsc).getPath());
        DataLoader.uploadAirportData(airport);
        DataLoader.uploadAirlineData(airline);
        DataLoader.uploadRouteData(route);
    }

    public void deleteDB() {
        try
        {
            File file = new File(getClass().getResource("/").getPath() + Path.database);
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
            File src = new File(getClass().getResource(Path.emptyDatabase).getPath());
            File dst = new File(getClass().getResource("/").getPath() + Path.database);
            System.out.println(dst.toString());
            System.out.println(src.toString());
            FileUtils.copyFile(src, dst);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not copy DB");
        }
    }
}