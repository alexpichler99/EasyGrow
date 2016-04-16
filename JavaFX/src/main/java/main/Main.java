package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.setTitle("EasyGrow");
        String separator = System.getProperty("file.separator");
        primaryStage.getIcons().add(new Image("file:" +System.getProperty("user.dir") + separator + "src" + separator +
                "main" + separator + "resources" + separator + "images" + separator + "programmIcon.png"));

        primaryStage.setScene(new Scene(root, 675, 500));
        primaryStage.setResizable(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}