package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import seng202.team4.model.DataType;

public abstract class Details {

    protected Stage stage;
    protected DataType data;
    public abstract void setData(DataType data);

    public void setUp(Stage stage, DataType data) {
        this.data = data;
        this.stage = stage;
        setData(data);
    }



}
