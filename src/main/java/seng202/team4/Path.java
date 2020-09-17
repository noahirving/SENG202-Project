package seng202.team4;

public abstract class Path {
    public static final String DIRECTORY = System.getProperty("user.home") + "/flight_tracker";
    public static final String DATABASE = DIRECTORY + "/DATABASE.db";
    public static final String DATABASE_CONNECTION = "jdbc:sqlite:" + DATABASE;

    public static final String VIEW = "/seng202.team4";
    public static final String MAN_SCENE_FXML = VIEW + "/mainFlightTrackerScene.fxml";
    public static final String HOME_SCENE_FXML = VIEW + "/homePage.fxml";
    public static final String AIRLINE_SCENE_FXML = VIEW + "/airlineTab.fxml";
    public static final String AIRPORT_SCENE_FXML = VIEW + "/airportTab.fxml";
    public static final String ROUTE_SCENE_FXML = VIEW + "/routeTab.fxml";
    public static final String MAP_SCENE_FXML = VIEW + "/mapTab.fxml";
    public static final String EMISSIONS_SCENE_FXML = VIEW + "/emissionsTab.fxml";
    public static final String NEW_AIRLINE_FXML = VIEW + "/newAirline.fxml";
    public static final String NEW_AIRPORT_FXML = VIEW + "/newAirport.fxml";
    public static final String NEW_ROUTE_FXML = VIEW + "/newRoute.fxml";
    public static final String NEW_FLIGHT_PATH_FXML = VIEW + "/newFlightPath.fxml";

    public static final String REFRESH_BUTTON_PNG = VIEW + "/images/refresh_icon.png";
    public static final String ADD_RECORD_BUTTON_PNG = VIEW + "/images/add_record_image.png";


    public static final String STYLE_SHEET = VIEW +  "/styles.css";

    public static final String AIRLINE_RSC = "/airlines.dat";
    public static final String AIRPORT_RSC = "/airports.dat";
    public static final String ROUTE_RSC = "/routes.dat";
    public static final String FLIGHT_PATH_RSC = "/NZCH-WSSS.csv";
    public static final String MAP_RSC = "/map.html";
}
