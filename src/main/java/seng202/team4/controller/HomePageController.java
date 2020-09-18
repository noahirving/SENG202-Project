package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
public class HomePageController extends DataController {

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
     * Calls method to load default database into the application from
     * stored files.
     *
     * @throws IOException caused by error in loading default database
     */
    @FXML
    public void loadDefaultDatabase() throws IOException {
        loadDefault();
    }

    /**
     * Loads default data into the application from
     * stored files. This means hobbyists can choose to
     * only view their own data.
     * @throws IOException caused by error in loading default database
     */
    public void loadDefault() throws IOException {
        File airport = copyToFolder(Path.AIRPORT_RSC);
        File airline = copyToFolder(Path.AIRLINE_RSC);
        File route = copyToFolder(Path.ROUTE_RSC);

        DataLoader.uploadAirportData(airport);
        DataLoader.uploadAirlineData(airline);
        DataLoader.uploadRouteData(route);

        loadDefaultDatabaseButton.disableProperty().setValue(true);

    }

    /**
     * Copies the provided file to a target file
     *
     * @param filename String name of source file
     * @return File the final file with the source files contents
     * @throws IOException if an exception occurs during the copy operation
     */
    public File copyToFolder(String filename) throws IOException {

        InputStream initialStream = (this.getClass().getResourceAsStream(filename));
        File targetFile = new File(Path.DIRECTORY + filename);

        java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return targetFile;
    }

    //DataControllers methods are redundant?
    @Override
    public DataType getDataType() {
        return null;
    }

    @Override
    public String getTableQuery() {
        return null;
    }

    @Override
    public void setTableData(ResultSet rs) throws Exception {

    }

    @Override
    public void initialiseComboBoxes() {

    }

    @Override
    public void filterData() {

    }

    @Override
    public String getNewRecordFXML() {
        return null;
    }
}
