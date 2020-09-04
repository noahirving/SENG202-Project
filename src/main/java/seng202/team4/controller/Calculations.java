package seng202.team4.controller;

import seng202.team4.model.DatabaseManager;
import seng202.team4.model.Route;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Calculations {

    static double calculateEmissions(Route route) {
        Double distance = route.getDistance();
        return distance * 20;
    }

    public static double calculateDistance(String airportCodeOne, String airportCodeTwo) throws SQLException {
        Double lat1;
        Double lat2;
        Double long1;
        Double long2;

        String query = "SELECT Latitude,Longitude from Airport WHERE IATA = '" + airportCodeOne + "' OR IATA = '" + airportCodeTwo + "'";
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        ResultSet result = stmt.executeQuery(query);
        ArrayList<Double> lats = new ArrayList<Double>();
        ArrayList<Double> longs = new ArrayList<Double>();
        while (result.next()){
            lats.add(result.getDouble("Latitude"));
            longs.add(result.getDouble("Longitude"));
        }
        stmt.close();
        //Get latitude and longitude of airports in route
        try{
            lat1 = Math.toRadians(lats.get(0));
            lat2 = Math.toRadians(lats.get(1));
            long1 = Math.toRadians(longs.get(0));
            long2 = Math.toRadians(longs.get(1));
        } catch(Exception e){
            return 0.0;
        }


        //Calculate distance between airports
        // Haversine formula
        double dlon = long2 - long1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));
        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;
        //return distance to be stored in DB
        DatabaseManager.disconnect(con);
        return(r * c);
    }
}
