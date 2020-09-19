package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

/**
 * Controller used for uploading datasets
 * to the application.
 */
public class FileUploadController {

    @FXML private TextField nameField;
    @FXML private Text filePath;
    @FXML private Label errorText;
    private File file;
    private DataController controller;
    private Stage stage;

    /**
     * Initial setup of the controller, sets the stage
     * of the scene and the controller that called it.
     * @param controller the controller that opened the stage.
     * @param stage the stage.
     */
    public void setUp(DataController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    /**
     * Gets the name of the data set from the name field.
     * @return the name in the name field, otherwise null if invalid.
     */
    private String getName() {
        String name = nameField.getText().trim();
        if (!name.isBlank() && !name.toLowerCase().equals(DataController.ALL.toLowerCase())) { // Name doesn't equal keyword All
            return name;
        }
        else {
            return null;
        }
    }

    /**
     * Launches a file explorer to get the file from the user.
     */
    public void getFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".DAT, .CSV or .TXT files", "*.dat", "*.csv", "*.txt")
        );
        File f = fc.showOpenDialog(stage);
        if (f != null) {
            file = f;
            filePath.setText(file.toString());
            errorText.setText("");
        }
    }

    /**
     * Closes the stage.
     */
    public void cancel() {
        stage.close();
    }

    /**
     * If dataset name is valid and file is chosen attempts to
     * upload file and closes stage.
     */
    public void confirm() {
        String name = getName();
        if (name == null) {
            errorText.setText("*Pick a name");
        } else if (file == null) {
            errorText.setText("*Pick a file");
        } else if (controller.getDataSetComboBox().getItems().contains(nameField.getText())) {
            errorText.setText("*Dataset name already chosen");
        } else {
            System.out.println("Confirmed");
            controller.newData(name, file);
            stage.close();
        }
    }
}
