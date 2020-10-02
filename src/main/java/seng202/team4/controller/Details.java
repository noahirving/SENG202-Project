package seng202.team4.controller;

import javafx.stage.Stage;
import seng202.team4.model.DataType;

/**
 * Describes the required functionalities for all controllers
 * that displays all details of a selected row of data.
 */
public abstract class Details {

    protected Stage stage;
    protected DataType data;
    public abstract void setData(DataType data);

    /**
     * Initial set up and declaration of the stage and the data that will be displayed.
     * @param stage stage where data is displayed.
     * @param data  the data that is displayed.
     */
    public void setUp(Stage stage, DataType data) {
        this.data = data;
        this.stage = stage;
        setData(data);
    }

}
