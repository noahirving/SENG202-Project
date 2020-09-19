package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import seng202.team4.model.Main;
import seng202.team4.model.Path;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;

/**
 * Performs logic for the 'Home' tab of the application
 * Responsible for giving instructions to the user and in
 * a later release will be responsible for global searching
 * functionality. Allows user to load the default database
 */
public class HomePageController {

    /**
     * Button to load default data into the application
     */
    @FXML private Button loadDefaultDatabaseButton;

    /**
     * Called when the user clicks the search button
     */
    @FXML
    public void searchButtonPressed() {
    }

    /**
     * Loads default data into the application from
     * stored files.
     *
     * @throws IOException caused by error in loading default database
     */
    @FXML
    public void loadDefaultDatabase() throws IOException {
        Main m = new Main();
        m.loadDefaultData();
        // TODO: Reload all tables and data set combo boxes, (temporary solution is to click all in data set combo box).
        loadDefaultDatabaseButton.disableProperty().setValue(true);
    }
}
