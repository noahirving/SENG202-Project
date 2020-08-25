package seng202.team4.model;

import java.io.*;

/**
 * Initialises the DataLoader class by reading raw data files,
 * creating datatype classes, and adding them to a DataList class.
 */
public class DataLoader {

    private static String airlineRsc = "/airlines.txt";
    private static String airportRsc = "/airports.txt";
    private static String routeRsc = "/routes.txt";
    private DataList dataList;

    public DataLoader() {
        this.dataList = new DataList();

    }

    /**
     * Loads only airline data
     * @throws IOException Invalid input
     */
    public void loadAirlineData() throws IOException {
        rawDataLoader(airlineRsc, "AL");
    }

    /**
     * Loads only airport data
     * @throws IOException Invalid input
     */
    public void loadAirportData() throws IOException {
        rawDataLoader(airportRsc, "AP");
    }

    /**
     * Loads only route data
     * @throws IOException Invalid input
     */
    public void loadRouteData() throws IOException {
        rawDataLoader(routeRsc, "RT");
    }

    /**
     * Loads all data by calling individual data loading classes
     * @throws IOException Invalid input
     */
    public void loadAllData() throws IOException {
        loadAirlineData();
        loadAirportData();
        loadRouteData();
    }

    public void rawUserDataUploader(File data, String output) throws IOException {
        DataType dataType = new DataType();
        BufferedReader reader = new BufferedReader(new FileReader(data));
        dataLoaderLines(output, dataType, reader);

    }

    /**
     * Loads raw data from files to a class String variable
     * @param rsc resource to load data from
     * @param output output corresponding to either Airline (Int 1), Airport (Int 2), Route (Int 3)
     * @throws IOException Invalid input
     */
    private void rawDataLoader(String rsc, String output) throws IOException {
        DataType dataType = new DataType();
        InputStream inputStream = DataLoader.class.getResourceAsStream(rsc);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        dataLoaderLines(output, dataType, reader);

        inputStream.close();

    }

    private void dataLoaderLines(String output, DataType dataType, BufferedReader reader) throws IOException {
        String line;
        StringBuilder outputBuilder = new StringBuilder(output);
        int numEntries = 0;
        while((line = reader.readLine()) != null) {
            if (line.length() > 0) {
                outputBuilder.append(line);
                if (output.equals("AL")) { // Airline
                    this.dataList.getAirlineDataList().add(new Airline(line + "\n", dataType));
                } else if (output.equals("AP")) { // Airport
                    this.dataList.getAirportDataList().add(new Airport(line + "\n", dataType));
                } else if (output.equals("RT")) { // Route
                    this.dataList.getRouteDataList().add(new Route(numEntries + "," + line + "\n", dataType));
                }
                numEntries += 1;
            }
            if (numEntries >= 1000) {
                break;
            }
        }

        dataType.updateDatabase();
    }


}
