package seng202.team4.model;

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
    private double carbonEmissions;

    public Route(String routeInfo, DataType dataType) {
        String[] routeArray = routeInfo.split(",");
        this.routeID = Integer.parseInt(routeArray[0]);
        this.airlineCode = routeArray[1];
        this.airlineID = tryReturnInt(routeArray[2]);
        this.sourceAirportCode = routeArray[3];
        this.sourceAirportID = tryReturnInt(routeArray[4]);
        this.destinationAirportCode = routeArray[5];
        this.destinationAirportID = tryReturnInt(routeArray[6]);
        this.codeshare = routeArray[7].equals("Y");
        try {
            this.numStops = Integer.parseInt(routeArray[8]);
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.planeTypeCode = routeArray[9];
        this.carbonEmissions = calculateCarbonEmissions();

        dataType.addToDatabase(this);
    }

    public Route() {

    }

    @Override
    public String getInsertStatement() {
        return "INSERT INTO ROUTES ('ROUTEID', 'AIRLINE', 'AIRLINEID', 'SourceAirport', 'SOURCEAIRPORTID', 'DESTINATIONAIRPORT', 'DESTINATIONAIRPORTID', 'CODESHARE', 'STOPS', 'EQUIPMENT', 'CARBONEMISSIONS') "
                + "VALUES ('"
                + route.getRouteID() + between
                + route.getAirlineCode().replaceAll("'", "''") + between
                + route.getAirlineID() + between
                + route.getSourceAirportCode().replaceAll("'", "''") + between
                + route.getSourceAirportID() + between
                + route.getDestinationAirportCode().replaceAll("'", "''") + between
                + route.getDestinationAirportID() + between
                + route.isCodeshare() + between
                + route.getNumStops() + between
                + route.getPlaneTypeCode().replaceAll("'", "''") + between
                + route.getCarbonEmissions()
                + "');";
    }

    public double calculateCarbonEmissions() {
        return 0; // To be implemented
    }

    public int tryReturnInt(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException e) {
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

    public double getCarbonEmissions() {
        return carbonEmissions;
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
