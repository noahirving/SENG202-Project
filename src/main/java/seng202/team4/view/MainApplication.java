package seng202.team4.view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng202.team4.model.Path;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(Path.MAN_SCENE_FXML));
        primaryStage.setTitle("Flight Tracker");
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1280);
        Scene mainScene = new Scene(root, 960, 540);
        mainScene.getStylesheets().add(Path.STYLE_SHEET);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
