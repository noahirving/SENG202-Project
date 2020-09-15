package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class FileUploadController {

    @FXML private TextField nameField;
    @FXML private Text filePath;
    @FXML private Label errorText;
    private String name;
    private File file;
    private DataController controller;
    private Stage ownerStage;

    public void setDataController(DataController controller) {
        this.controller = controller;
    }

    public void setOwnerStage(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    public void getName() {
        String name = nameField.getText().trim();
        if (!name.isBlank() && !name.toLowerCase().equals(DataController.ALL.toLowerCase())) { // Name doesn't equal keyword All
            this.name = name;
        }
        else {
            this.name = null;
        }
    }

    public void getFile() {


        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File f = fc.showOpenDialog(ownerStage);
        if (f != null) {
            file = f;
            filePath.setText(file.toString());
            errorText.setText("");
        }
    }

    public void cancel() {
        ownerStage.close();
    }

    public void confirm() {
        if (name == null) {
            errorText.setText("*Pick a name");
        } else if (file == null) {
            errorText.setText("*Pick a file");
        } else if (controller.getDataSetComboBox().getItems().contains(nameField.getText())) {
            errorText.setText("*Dataset name already chosen");
        } else {
            System.out.println("Confirmed");
            controller.newData(name, file);
            ownerStage.close();
        }

    }


}
