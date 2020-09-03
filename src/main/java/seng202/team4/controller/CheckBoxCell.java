package seng202.team4.controller;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import seng202.team4.model.Route;

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
}