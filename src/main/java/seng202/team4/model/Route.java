package seng202.team4.model;

public class Route {

    private String airlineCode;
    private int airlineID;
    private String sourceAirportCode;
    private int sourceAirportID;
    private String destinationAirportCode;
    private int destinationAirportID;
    private boolean codeshare;
    private int numStops;
    private String planeTypeCode;
    private double carbonEmissions;

    public Route(String routeInfo) {
        String[] routeArray = routeInfo.split("\n");
        this.airlineCode = routeArray[0];
        this.airlineID = Integer.parseInt(routeArray[1]);
        this.sourceAirportCode = routeArray[2];
        this.sourceAirportID = Integer.parseInt(routeArray[3]);
        this.destinationAirportCode = routeArray[4];
        this.destinationAirportID = Integer.parseInt(routeArray[5]);

        if (routeArray[6].equals("Y")) {
            this.codeshare = true;
        } else if (routeArray[6].equals("N")) {
            this.codeshare = false;
        }

        this.numStops = Integer.parseInt(routeArray[7]);
        this.planeTypeCode = routeArray[8];
        this.carbonEmissions = calculateCarbonEmissions();
    }

    public double calculateCarbonEmissions() {
        return 0; // To be implemented
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

    public void setCarbonEmissions(double carbonEmissions) {
        this.carbonEmissions = carbonEmissions;
    }
}
