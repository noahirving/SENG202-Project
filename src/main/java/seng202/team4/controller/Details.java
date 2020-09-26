package seng202.team4.controller;

import javafx.stage.Stage;
import seng202.team4.model.DataType;

public abstract class Details {

    private Stage stage;
    private DataType data;
    public abstract void setData(DataType data);

    public void setUp(Stage stage, DataType data) {
        this.data = data;
        this.stage = stage;
        setData(data);
    }

}
