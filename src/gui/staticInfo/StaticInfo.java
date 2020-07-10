package gui.staticInfo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class StaticInfo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("staticInfo.fxml"));
        Scene scene = new Scene(root,480,900);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("硬件信息");
        primaryStage.getIcons().setAll(new Image("icon/icon.png"));
        primaryStage.show();
    }
}
