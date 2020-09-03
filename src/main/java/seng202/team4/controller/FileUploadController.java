package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileUploadController {

    @FXML private TextField nameField;
    @FXML private Text filePath;
    private String name;
    private File file;
    private DataController controller;
    private Stage stage;

    public void setDataController(DataController controller) {
        this.controller = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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
        File f = fc.showOpenDialog(null);
        if (f != null) {
            file = f;
            filePath.setText(file.toString());
        }
    }

    public void cancel() {
        stage.close();
    }

    public void confirm() {
        if (name != null && file != null) {
            System.out.println("Confirmed");
            controller.newData(name, file);
            stage.close();
        }
        else {
            System.out.println("Invalid");
        }
    }


}
