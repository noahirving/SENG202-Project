package seng202.team4.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import seng202.team4.model.Route;

public class ContributionsGraphController {
    @FXML
    private BarChart<String, Double> contributionChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    public void setUp(ObservableList<Route> selectedRoutes) {
        contributionChart.setLegendVisible(false);
        x.setLabel("Routes selected");
        y.setLabel("Carbon Emissions per passenger (Kg-C02)");
        XYChart.Series<String, Double> routeSeries = new XYChart.Series<>();
        routeSeries.setName("Selected Routes");
        for(Route route: selectedRoutes){
            String routeID = route.getAirlineCode() + ": " + route.getSourceAirportCode() + "-" + route.getDestinationAirportCode();
            routeSeries.getData().add(new XYChart.Data<>(routeID, route.getCarbonEmissions()));
        }
        contributionChart.getData().addAll(routeSeries);
    }


}
