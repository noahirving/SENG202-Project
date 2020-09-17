package seng202.team4;

public abstract class Path {
    public static final String directory = System.getProperty("user.home") + "/flight_tracker";
    public static final String database = directory + "/database.db";
    public static final String databaseConnection = "jdbc:sqlite:" + database;

    public static final String view = "/seng202.team4";
    public static final String mainSceneFXML = view + "/mainFlightTrackerScene.fxml";
    public static final String homeSceneFXML = view + "/homePage.fxml";
    public static final String airlineSceneFXML = view + "/airlineTab.fxml";
    public static final String airportSceneFXML = view + "/airportTab.fxml";
    public static final String routeSceneFXML = view + "/routeTab.fxml";
    public static final String mapSceneFXML = view + "/mapTab.fxml";
    public static final String emissionsSceneFXML = view + "/emissionsTab.fxml";
    public static final String newAirlineFXML = view + "/newAirline.fxml";
    public static final String newAirportFXML = view + "/newAirport.fxml";
    public static final String newRouteFXML = view + "/newRoute.fxml";
    public static final String newFlightPathFXML = view + "/newFlightPath.fxml";

    public static final String refreshButtonPNG = view + "/images/refresh_icon.png";
    public static final String addRecordButtonPNG = view + "/images/add_record_image.png";


    public static final String styleSheet = view +  "/styles.css";

    public static final String airlineRsc = "/airlines.dat";
    public static final String airportRsc = "/airports.dat";
    public static final String routeRsc = "/routes.dat";
    public static final String flightPathRsc = "/NZCH-WSSS.csv";
    public static final String mapRsc = "/map.html";
}
