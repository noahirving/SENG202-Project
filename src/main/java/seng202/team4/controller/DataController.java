package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team4.Path;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class DataController {

    public abstract DataType getDataType();
    public abstract String getTableQuery();
    public abstract void setTableData(ResultSet rs) throws Exception;
    public abstract void initialiseComboBoxes();
    public abstract void filterData();
    public abstract String getNewRecordFXML();
    @FXML private ComboBox dataSetComboBox;
    public final static String ALL = "All";


    public void setDataSetListener() {
        dataSetComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                try {
                    setDataSet(newItem.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setDataSetComboBox() throws Exception{
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet rs = stmt.executeQuery("Select Name from " + getDataType().getSetName());
        ObservableList<String> dataSetNames = FXCollections.observableArrayList();
        dataSetNames.add(ALL);
        while (rs.next()) {
            dataSetNames.add(rs.getString("Name"));
        }
        dataSetComboBox.setItems(dataSetNames);
        rs.close();
        stmt.close();
        DatabaseManager.disconnect(c);
    }

    public void setDataSet(String dataSetName) throws Exception{
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        String query = "Select * from " + getDataType().getTypeName() + " ";
        if (dataSetName != ALL) {
            String idQuery = "Select ID from " + getDataType().getSetName() + " Where Name = '" + dataSetName + "';";
            ResultSet rs = stmt.executeQuery(idQuery);
            rs.next(); // Need to check no null

            query +=  "WHERE SetID = '" + rs.getInt("ID") + "'";
        }
        System.out.println(query);
        setTable(query);
        System.out.println("Table updated");
        stmt.close();
        DatabaseManager.disconnect(c);
    }

    public void setTable() throws Exception {
        setTable(getTableQuery());
    }

    public void setTable(String query) throws Exception {
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet rs = stmt.executeQuery(query);
        setTableData(rs);
        rs.close();
        stmt.close();
        DatabaseManager.disconnect(c);
        initialiseComboBoxes();
    }

    public void addToComboBoxList(ObservableList comboBoxList, String dataName) {
        if (!comboBoxList.contains(dataName)) {
            comboBoxList.add(dataName);
        }
    }

    public void uploadData() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Upload file");
        stage.setMinHeight(290);
        stage.setMinWidth(720);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Path.view + "/fileUpload.fxml"));
        stage.setScene(new Scene(loader.load(), 700, 250));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        FileUploadController controller = loader.getController();
        controller.setDataController(this);
        controller.setOwnerStage(stage);
    }

    public void newData(String name, File file) {
        DataLoader.uploadData(name, file, getDataType());
        try {
            setTable();
            setDataSetComboBox();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void newRecord() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("New Record");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(getNewRecordFXML()));
        stage.setScene(new Scene(loader.load(), 700, 250));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        NewRecord controller = loader.getController();
        controller.setUp(stage, this);
    }

    public ComboBox getDataSetComboBox() {
        return dataSetComboBox;
    }
}
