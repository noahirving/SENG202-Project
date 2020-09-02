package seng202.team4.controller;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.team4.Path;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DataController {
    public abstract DataType getDataType();
    public abstract String getTableQuery();
    public abstract void setTableData(ResultSet rs) throws Exception;
    public abstract void initialiseComboBoxes();
    public abstract void filterData();

    public void setTable() throws Exception {
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet rs = stmt.executeQuery(getTableQuery());
        setTableData(rs);
        rs.close();
        stmt.close();
        DatabaseManager.disconnect(c);
    }

    public void addToComboBoxList(ObservableList comboBoxList, String dataName) {
        if (!comboBoxList.contains(dataName)) {
            comboBoxList.add(dataName);
        }
    }

    public void uploadData() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Upload file");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Path.view + "/fileUpload.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
        FileUploadController controller = loader.getController();
        controller.setDataController(this);
        controller.setStage(stage);
    }

    public void newData(String name, File file) {
        DataLoader.uploadData(name, file, getDataType());
        try {
            setTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
