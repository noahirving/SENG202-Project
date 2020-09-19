package seng202.team4.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;
import seng202.team4.model.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

abstract class NewRecord {

    @FXML
    private ComboBox setComboBox;
    @FXML
    private Text errorText;
    private Stage stage;
    private DataController controller;

    abstract String[] getRecordData();

    void setUp(Stage stage, DataController controller) {
        this.controller = controller;
        this.stage = stage;
        try {
            setDataSetComboBox(setComboBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: Essentially the same as the function from DataController, a more efficient implementation would be preferred
    private void setDataSetComboBox(ComboBox comboBox) throws Exception{
        Connection c = DatabaseManager.connect();
        Statement stmt = DatabaseManager.getStatement(c);
        ResultSet rs = stmt.executeQuery("Select Name from " + controller.getDataType().getSetName());
        ObservableList<String> dataSetNames = FXCollections.observableArrayList();
        while (rs.next()) {
            dataSetNames.add(rs.getString("Name"));
        }
        comboBox.setItems(dataSetNames);
        comboBox.setValue(dataSetNames.get(0));
        rs.close();
        stmt.close();
        DatabaseManager.disconnect(c);
    }

    @FXML
    private void confirm(ActionEvent actionEvent) throws Exception {
        ArrayList<String> errorMessage = new ArrayList<String>();
        String[] recordData = getRecordData();
        DataType record = controller.getDataType().getValid(recordData, errorMessage);
        if (record == null) {
            if (errorMessage.size() > 0) {
                errorText.setText(errorMessage.get(0));
                errorText.setVisible(true);
            }
            for (String s: errorMessage) {
                System.out.println(s);
            }
        }
        else {
            String setName = setComboBox.getValue().toString();
            DataLoader.addNewRecord(record, setName);
            controller.setTable();
            stage.close();
        }
    }

    @FXML
    private void cancel(ActionEvent actionEvent) {
        stage.close();
    }
}
