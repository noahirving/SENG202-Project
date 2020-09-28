package seng202.team4.controller;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.util.ArrayList;

public class InvalidLinesPopUp {
    @FXML
    public JFXListView<String> listView;
    public Stage stage;
    public Stage parentStage;

    public void setUp(Stage stage, Stage parentStage) {
        this.stage = stage;
        this.parentStage = parentStage;
    }

    public void addErrorLines(ArrayList<String> errorLines) {
        listView.setEditable(true);
        listView.getItems().addAll(errorLines);
    }

    public void exit() {
        stage.close();
        parentStage.close();
    }
}
