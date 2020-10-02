package seng202.team4.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import seng202.team4.model.DataType;
import seng202.team4.model.Route;

public class contributionsGraphController {
    @FXML
    private BarChart<String, Double> contributionChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;
    
    private Stage stage;
    private DataType data;
    //public abstract void setData(DataType data);

    public void setUp(Stage stage, ObservableList<Route> selectedRoutes, Double dollarOffset, Double sumEmissions) {

        x.setLabel("Routes selected");
        y.setLabel("Carbon Emissions");
        XYChart.Series<String, Double> routeSeries = new XYChart.Series<>();
        routeSeries.setName("Selected Routes");
        for(Route route: selectedRoutes){
            String routeID = route.getAirlineCode() + ": " + route.getSourceAirportCode() + "-" + route.getDestinationAirportCode();
            routeSeries.getData().add(new XYChart.Data<>(routeID, route.getCarbonEmissions()));
        }
        contributionChart.getData().addAll(routeSeries);
        this.data = data;
        this.stage = stage;
    }


}
