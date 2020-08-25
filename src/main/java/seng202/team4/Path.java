package seng202.team4;

public abstract class Path {
    public static final String database = "jdbc:sqlite:src/main/resources/database.db";

    public static final String view = "/seng202.team4";
    public static final String mainSceneFXML = view + "/mainFlightTrackerScene.fxml";
    public static final String homeSceneFXML = view + "/homePage.fxml";
    public static final String airlineSceneFXML = view + "/airlineTab.fxml";
    public static final String airportSceneFXML = view + "/airportTab.fxml";
    public static final String routeSceneFXML = view + "/routeTab.fxml";
    public static final String mapSceneFXML = view + "/mapTab.fxml";
    public static final String emissionsSceneFXML = view + "/emissionsTab.fxml";

    public static final String styleSheet = view +  "/styles.css";

    public static final String airlineRsc = "/airlines.txt";
    public static final String airportRsc = "/airports.txt";
    public static final String routeRsc = "/routes.txt";

}
