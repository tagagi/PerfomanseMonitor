package gui.pMonitor;

import api.Global;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class pMonitor extends Application {
    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pmonitor.fxml"));
        Scene scene = new Scene(root);
        stage = primaryStage;

        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(Global.onTop);
        primaryStage.setScene(scene);
        primaryStage.setTitle("性能监测工具");
        primaryStage.getIcons().setAll(new Image("icon/icon.png"));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }
}
