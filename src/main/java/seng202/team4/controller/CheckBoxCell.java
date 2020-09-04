package seng202.team4.controller;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import seng202.team4.model.DatabaseManager;
import seng202.team4.model.Route;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckBoxCell extends TableCell<Route, Route> {

    private final ObservableSet<Route> selectedRoute ;
    private final CheckBox checkBox ;

    public CheckBoxCell(ObservableSet<Route> selectedRoute) {
        this.selectedRoute = selectedRoute ;
        this.checkBox = new CheckBox() ;


        // listener to update the set of selected items when the
        // check box is checked or unchecked:
        checkBox.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (checkBox.isSelected()) {
                    selectedRoute.add(getItem());
                    try {
                        addToSelectedRoute(getItem());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    selectedRoute.remove(getItem());

                }
            }
        });

        // listener to update the check box when the collection of selected
        // items changes:
        selectedRoute.addListener(new SetChangeListener<Route>() {

            @Override
            public void onChanged(SetChangeListener.Change<? extends Route> change) {
                Route route = getItem();
                if (route != null) {
                    checkBox.setSelected(selectedRoute.contains(route));
                }
            }

        });
    }

    @Override
    public void updateItem(Route route, boolean empty) {
        super.updateItem(route, empty);
        if (empty) {
            setGraphic(null);
        } else {
            checkBox.setSelected(selectedRoute.contains(route));
            setGraphic(checkBox);
        }
    }

    private void addToSelectedRoute(Route route) throws SQLException {
        Connection con = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(con);
        String between = "', '";

        Double distance = 0.0;
        String sourceAirport = route.getSourceAirportCode();
        String destAirport = route.getDestinationAirportCode();
        try {
            distance = Calculations.calculateDistance(sourceAirport, destAirport);
            route.setDistance(distance);
            //System.out.println(routes.get(index).getDistance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Double carbonEmitted = Calculations.calculateEmissions(route);
        String query = "INSERT INTO RoutesSelected ('Airline', 'SourceAirport', 'DestinationAirport', 'Equipment', 'Distance', 'CarbonEmissions') "
                + "VALUES ('"
                + route.getAirlineCode().replaceAll("'", "''") + between
                + route.getSourceAirportCode().replaceAll("'", "''") + between
                + route.getDestinationAirportCode().replaceAll("'", "''") + between
                + route.getPlaneTypeCode().replaceAll("'", "''") + between
                + route.getDistance() + between
                + carbonEmitted
                + "');";
        stmt.executeUpdate(query);

        con.commit();
        stmt.close();
        DatabaseManager.disconnect(con);
    }
}