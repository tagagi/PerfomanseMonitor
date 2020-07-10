package gui.register;

import gui.dialog.DialogBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Register extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene scene = new Scene(root, 480, 330);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("注册");
        primaryStage.getIcons().setAll(new Image("icon/icon.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}