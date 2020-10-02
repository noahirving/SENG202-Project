package seng202.team4.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Performs calculations for the emissions tab of the application.
 * Responsible for calculating the distance and carbon emissions
 * of a route.
 */
public class Calculations {

    /**
     * Calculates the estimated carbon footprint of a passenger on the
     * provided route. This assumes each plane is a Boeing 737 at 65% capacity
     * Hence it is assumed 0.115kg of C02 is emitted per passenger per kilometre.
     * In the next release the fuel efficiency for different aircraft types will
     * be taken into account
     *
     * @param route Route to calculate carbon emissions for
     * @return double the calculated emissions figure for a single passenger
     */
    public static double calculateEmissions(Route route) {
        Double distance = route.getDistance();
        return distance * 0.115;
    }

    /**
     * Calculates the distance between two airports, a departure airport with
     * airportCodeOne and a destination airport with airportCodeTwo. Gets each
     * airports coordinates from the Airport database table and uses the
     * Haversine formula to calculate the distance between these coordinates
     *
     * @param airportCodeOne String representing code of the departure airport
     * @param airportCodeTwo String representing code of the destination airport
     * @param con connection to the SQLite database
     * @return double the calculated distance between two given airports
     * @throws SQLException SQL Exception
     */
    public static double calculateDistance(String airportCodeOne, String airportCodeTwo, Connection con) throws SQLException {
        Double lat1;
        Double lat2;
        Double long1;
        Double long2;

        //Sets up query required for accessing the provided airports in the Airport database table
        String query = "SELECT Latitude,Longitude from Airport WHERE IATA = '" + airportCodeOne + "' OR IATA = '" + airportCodeTwo + "'";
        Statement stmt = DatabaseManager.getStatement(con);
        //Execute query, store results in result set
        ResultSet result = stmt.executeQuery(query);
        ArrayList<Double> lats = new ArrayList<Double>();
        ArrayList<Double> longs = new ArrayList<Double>();
        //Store latitude and longitudes of airports
        while (result.next()){
            lats.add(result.getDouble("Latitude"));
            longs.add(result.getDouble("Longitude"));
        }
        stmt.close();
        //Convert latitude and longitude of airports to radians
        try{
            lat1 = Math.toRadians(lats.get(0));
            lat2 = Math.toRadians(lats.get(1));
            long1 = Math.toRadians(longs.get(0));
            long2 = Math.toRadians(longs.get(1));
        } catch(Exception e){
            return 0.0;
        }


        // Calculate distance between airports
        // Using the Haversine formula
        double dlon = long2 - long1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));
        // Radius of earth in kilometers.
        double r = 6371;
        //return distance to be stored in DB
        return(r * c);
    }

    /**
     * Calculates the amount of money required to offset the amount of carbon emissions
     * from the selected route.
     * @param route the route to calculate the equivalent dollar donations from
     * @return      the dollar donations required to offset flight carbon emissions
     */
    public static double calculateDollarOffset(Route route) {
        Double emission = route.getCarbonEmissions();
        return emission * 0.01479;
    }

    /**
     * Calculates the equivalent number of trees required to offset the flight carbon emission
     * of the selected route.
     * @param route the route to calculate the equivalent trees required from
     * @return      the number of trees to offset flight carbon emissions
     */
    public static int calculateTreesEquivalent(Route route) {
        Double dollars = route.getDollarOffset();
        return (int) Math.ceil(dollars);
    }
}
