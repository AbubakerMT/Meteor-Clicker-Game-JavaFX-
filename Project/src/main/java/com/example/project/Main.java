package com.example.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    public static Stage stg;

    @Override
    public void start(Stage stage) throws IOException {
        stage.getIcons().add(new Image("https://cdn.discordapp.com/attachments/1008487453331574817/1105475585288962088/GameIcon.png"));
        stg = stage;
        stage.setResizable(false);
        FXMLLoader endPane = new FXMLLoader(Main.class.getResource("startPane.fxml"));
        Scene scene = new Scene(endPane.load(), 1280, 800);
        scene.setFill(Color.BLANCHEDALMOND);
        stage.setTitle("Team 69 Project");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}