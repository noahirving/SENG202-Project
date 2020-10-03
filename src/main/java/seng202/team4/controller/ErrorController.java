package seng202.team4.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.team4.model.Path;

public class ErrorController {

    @FXML
    public JFXTextArea messageField;
    public boolean exitSystem;
    private Stage stage;

    public static void createErrorMessage(String message, boolean exitSystem) {
        Stage stage = new Stage();
        stage.setTitle("Error");
        FXMLLoader loader = new FXMLLoader(ErrorController.class.getResource(Path.ERROR_FXML));
        try {
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            ErrorController controller = loader.getController();
            controller.setUp(stage, message, exitSystem);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0); // TODO
        }
    }

    private void setUp(Stage stage, String message, boolean exitSystem) {
        this.stage = stage;
        messageField.setText(message);
        this.exitSystem = exitSystem;
    }

    @FXML
    public void onExit(ActionEvent actionEvent) {
        if (exitSystem) {
            System.exit(0);
        }
        else {
            stage.close();
        }
    }
}
