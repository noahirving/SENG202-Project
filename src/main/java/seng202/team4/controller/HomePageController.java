package seng202.team4.controller;

import javafx.fxml.FXML;
import seng202.team4.Path;
import seng202.team4.model.DataLoader;
import seng202.team4.model.DataType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;

public class HomePageController extends DataController {


    @FXML
    public void searchButtonPressed() {
    }

    @FXML
    public void loadDefaultDatabase() throws IOException {
        loadDefault();
    }

    public void loadDefault() throws IOException {
        File airport = copyToFolder(Path.airportRsc);
        File airline = copyToFolder(Path.airlineRsc);
        File route = copyToFolder(Path.routeRsc);

        DataLoader.uploadAirportData(airport);
        DataLoader.uploadAirlineData(airline);
        DataLoader.uploadRouteData(route);
    }

    public File copyToFolder(String filename) throws IOException {

        InputStream initialStream = (this.getClass().getResourceAsStream(filename));
        File targetFile = new File(Path.directory + filename);

        java.nio.file.Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return targetFile;
    }

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
}
