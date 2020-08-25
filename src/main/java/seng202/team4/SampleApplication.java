package seng202.team4;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SampleApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("Hello");
        Parent root = FXMLLoader.load(getClass().getResource(Path.mainSceneFXML));
        primaryStage.setTitle("Flight Companion");
        Scene mainScene = new Scene(root, 750, 500);
        mainScene.getStylesheets().add(Path.styleSheet);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
