package seng202.team4;

import seng202.team4.model.DataLoader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        SampleApplication.main(args);
        DataLoader loader = new DataLoader();

        // Test
        loader.loadAirportData();
        loader.loadAirlineData();
        loader.loadRouteData();

    }
}
