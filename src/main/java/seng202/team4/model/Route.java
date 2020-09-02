package seng202.team4.model;

import javafx.scene.control.CheckBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Route extends DataType {

    private int routeID;
    private String airlineCode;
    private int airlineID;
    private String sourceAirportCode;
    private int sourceAirportID;
    private String destinationAirportCode;
    private int destinationAirportID;
    private boolean codeshare;
    private int numStops;
    private String planeTypeCode;
    private double distance;
    private BooleanProperty select;

    public Route(String routeInfo) {
        String[] routeArray = routeInfo.split(",");
        this.airlineCode = routeArray[0];
        this.airlineID = tryReturnInt(routeArray[1]);
        this.sourceAirportCode = routeArray[2];
        this.sourceAirportID = tryReturnInt(routeArray[3]);
        this.destinationAirportCode = routeArray[4];
        this.destinationAirportID = tryReturnInt(routeArray[5]);
        this.codeshare = routeArray[6].equals("Y");
        try {
            this.numStops = Integer.parseInt(routeArray[7]);
        } catch(Exception e) {
            e.printStackTrace();
        }
        if (routeArray.length == 9) {
            this.planeTypeCode = routeArray[8];
        } else {
            this.planeTypeCode = "";
        }
        try {
            this.distance = Double.parseDouble(routeArray[10]);
        }
        catch (Exception e) {
            this.distance = 0.00;
        }
        this.select = new SimpleBooleanProperty(false);
    }

    public Route() {

    }

    @Override
    public String getInsertStatement(int setID) {
        return "INSERT INTO Route ('AIRLINE', 'AIRLINEID', 'SourceAirport', 'SOURCEAIRPORTID', 'DESTINATIONAIRPORT', 'DESTINATIONAIRPORTID', 'CODESHARE', 'STOPS', 'EQUIPMENT', 'DISTANCE', 'SETID') "
                + "VALUES ('"
                + getAirlineCode().replaceAll("'", "''") + between
                + getAirlineID() + between
                + getSourceAirportCode().replaceAll("'", "''") + between
                + getSourceAirportID() + between
                + getDestinationAirportCode().replaceAll("'", "''") + between
                + getDestinationAirportID() + between
                + isCodeshare() + between
                + getNumStops() + between
                + getPlaneTypeCode().replaceAll("'", "''") + between
                + getDistance() + between
                + setID
                + "');";
    }


    @Override
    public DataType newDataType(String line) {
        return new Route(line);
    }

    @Override
    public String getSetName() {
        return "RouteSet";
    }

    public double calculateCarbonEmissions() {
        return 0; // To be implemented
    }

    public int tryReturnInt(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (Exception e) {
            return -1;
        }
    }

    public int getRouteID() {
        return routeID;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public int getAirlineID() {
        return airlineID;
    }

    public String getSourceAirportCode() {
        return sourceAirportCode;
    }

    public int getSourceAirportID() {
        return sourceAirportID;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public int getDestinationAirportID() {
        return destinationAirportID;
    }

    public boolean isCodeshare() {
        return codeshare;
    }

    public int getNumStops() {
        return numStops;
    }

    public String getPlaneTypeCode() {
        return planeTypeCode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public void setAirlineID(int airlineID) {
        this.airlineID = airlineID;
    }

    public void setSourceAirportCode(String sourceAirportCode) {
        this.sourceAirportCode = sourceAirportCode;
    }

    public void setSourceAirportID(int sourceAirportID) {
        this.sourceAirportID = sourceAirportID;
    }

    public void setDestinationAirportCode(String destinationAirportCode) {
        this.destinationAirportCode = destinationAirportCode;
    }

    public void setDestinationAirportID(int destinationAirportID) {
        this.destinationAirportID = destinationAirportID;
    }

    public void setCodeshare(boolean codeshare) {
        this.codeshare = codeshare;
    }

    public void setNumStops(int numStops) {
        this.numStops = numStops;
    }

    public void setPlaneTypeCode(String planeTypeCode) {
        this.planeTypeCode = planeTypeCode;
    }

    public boolean isSelected() {
        return this.select.get();
    }

    public BooleanProperty selectedProperty() {
        return select;
    }

    public void setSelected(boolean select) {
        this.select.set(select);
    }
}
