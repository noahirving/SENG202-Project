package seng202.team4.model;

import java.util.ArrayList;

public class DataList {
    private ArrayList<Route> routeDataList;
    private ArrayList<Airport> airportDataList;
    private ArrayList<Airline> airlineDataList;

    public DataList(){
        this.routeDataList = new ArrayList<Route>();
        this.airportDataList = new ArrayList<Airport>();
        this.airlineDataList = new ArrayList<Airline>();
    }

    //Getters and setters for DataList class - kno42
    public ArrayList<Route> getRouteDataList() {
        return routeDataList;
    }

    public void setRouteDataList(ArrayList<Route> routeDataList) {
        this.routeDataList = routeDataList;
    }

    public ArrayList<Airport> getAirportDataList() {
        return airportDataList;
    }

    public void setAirportDataList(ArrayList<Airport> airportDataList) {
        this.airportDataList = airportDataList;
    }

    public ArrayList<Airline> getAirlineDataList() {
        return airlineDataList;
    }

    public void setAirlineDataList(ArrayList<Airline> airlineDataList) {
        this.airlineDataList = airlineDataList;
    }
    /* Skeleton code for updateData method, to be implemented later on
    public void updateData(newData DataType){

    }
     */

    /* Skeleton code for deleteData method, to be implemented later on
    public void deleteData(dataToDelete DataType){

    }
     */
}
