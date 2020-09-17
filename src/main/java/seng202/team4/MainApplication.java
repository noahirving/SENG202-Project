package seng202.team4;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("Hello");
        Parent root = FXMLLoader.load(getClass().getResource(Path.MAN_SCENE_FXML));
        primaryStage.setTitle("Flight Companion");
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(780);
        Scene mainScene = new Scene(root, 760, 460);
        mainScene.getStylesheets().add(Path.STYLE_SHEET);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
