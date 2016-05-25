package main;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/icon.png").toString()));
        Parent root = loader.load();
        primaryStage.setMinWidth(675);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
        ((Controller) loader.getController()).setStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}